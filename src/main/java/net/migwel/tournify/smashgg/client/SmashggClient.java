package net.migwel.tournify.smashgg.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.migwel.tournify.client.TournamentClient;
import net.migwel.tournify.data.Address;
import net.migwel.tournify.data.GameType;
import net.migwel.tournify.data.Phase;
import net.migwel.tournify.data.Player;
import net.migwel.tournify.data.Set;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.smashgg.config.SmashggConfiguration;
import net.migwel.tournify.smashgg.response.SmashggEntrant;
import net.migwel.tournify.smashgg.response.SmashggEvent;
import net.migwel.tournify.smashgg.response.SmashggEventResponse;
import net.migwel.tournify.smashgg.response.SmashggNode;
import net.migwel.tournify.smashgg.response.SmashggPhase;
import net.migwel.tournify.smashgg.response.SmashggPhaseGroup;
import net.migwel.tournify.smashgg.response.SmashggPhaseGroupResponse;
import net.migwel.tournify.smashgg.response.SmashggResponse;
import net.migwel.tournify.smashgg.response.SmashggSlot;
import net.migwel.tournify.smashgg.response.SmashggTournament;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component("SmashggClient")
@Immutable
public class SmashggClient implements TournamentClient {

    private static final Logger log = LoggerFactory.getLogger(SmashggClient.class);

    private final SmashggConfiguration configuration;
    private final ObjectMapper objectMapper;

    public SmashggClient(SmashggConfiguration configuration, ObjectMapper objectMapper) {
        this.configuration = configuration;
        this.objectMapper = objectMapper;
    }

    @Override
    @Nullable
    public Tournament fetchTournament(@Nullable Tournament oldTournament, String eventUrl) {
        log.info("Fetching tournament at url: " + eventUrl);

        String eventSlug = findEventSlug(eventUrl);
        SmashggEvent smashggEvent = fetchEvent(eventSlug);

        if (smashggEvent == null) {
            log.info("Could not retrieve tournament for url: "+ eventUrl);
            return null;
        }

        Collection<Phase> existingPhases = Collections.emptyList();
        if(oldTournament != null) {
            existingPhases = oldTournament.getPhases();
        }
        Map<Long, Collection<SmashggPhaseGroup>> phaseGroupsPerPhase = getPhaseGroupsPerPhase(smashggEvent.getPhaseGroups());
        List<Phase> tournamentPhases = getPhases(existingPhases, smashggEvent.getPhases(), phaseGroupsPerPhase);
        boolean tournamentDone = true;
        if(!tournamentPhases.stream().allMatch(Phase::isDone)) {
            tournamentDone = false;
        }

        Address address = buildAddress(smashggEvent.getTournament());

        log.info("Done with fetching tournament at url: "+ eventUrl);


        return new Tournament(String.valueOf(smashggEvent.getId()),
                tournamentPhases,
                smashggEvent.getTournament().getName(),
                new GameType(smashggEvent.getVideogame().getDisplayName()),
                address,
                eventUrl,
                new Date(smashggEvent.getStartAt()*1000),
                tournamentDone);
    }

    @SuppressWarnings("unchecked")
    private <T> T fetch(String request, Class<? extends SmashggResponse> responseClass) {
        String eventJsonStr = postRequest(request);
        SmashggResponse<T> response;
        try {
            response = responseClass.cast(objectMapper.readValue(eventJsonStr, responseClass));
        } catch (IOException e) {
            log.warn("Could not convert JSON response to SmashggEventResponse");
            return null;
        }

        if(response == null || response.getData() == null) {
            return null;
        }

        return response.getData().getObject();
    }

    @CheckForNull
    private SmashggEvent fetchEvent(String eventSlug) {
        String request = buildEventRequest(eventSlug);
        return fetch(request, SmashggEventResponse.class);
    }

    private String buildEventRequest(String eventSlug) {
        return String.format("{\"query\":\"query event($slug: String!){  event(slug: $slug) { " +
                "id slug startAt " +
                " tournament {id name city addrState venueAddress countryCode} " +
                " phaseGroups {id state phaseId displayIdentifier} "+
                " phases {id name} "+
                " videogame {displayName}" +
                " }}\", " +
                "\"variables\":{\"slug\":\"%s\"}}", eventSlug);
    }

    @CheckForNull
    private SmashggPhaseGroup fetchPhaseGroup(long phaseGroupId, long page) {
        String request = buildPhaseGroupRequest(phaseGroupId, page, configuration.getSetsPerPage());
        return fetch(request, SmashggPhaseGroupResponse.class);
    }

    @Nonnull
    private List<Set> fetchSets(long phaseGroupId) {
        List<Set> sets = new ArrayList<>();
        SmashggPhaseGroup phaseGroup;
        long page = 0;
        do {
            page++;
            phaseGroup = fetchPhaseGroup(phaseGroupId, page);
            if(phaseGroup == null ||
               phaseGroup.getPaginatedSets() == null ||
               phaseGroup.getPaginatedSets().getPageInfo() == null ||
               phaseGroup.getPaginatedSets().getPageInfo().getTotalPages() == 0) {
                break;
            }

            sets.addAll(getSets(phaseGroup.getPaginatedSets().getNodes()));

        } while (phaseGroup.getPaginatedSets().getPageInfo().getTotalPages() != page);

        return sets;
    }

    @Nonnull
    private List<Set> getSets(Collection<SmashggNode> nodes) {
        List<Set> sets = new ArrayList<>();
        for(SmashggNode node : nodes) {
            if(node == null) {
                continue;
            }
            long winnerId = node.getWinnerId();
            Player winner = null;
            List<Player> players = new ArrayList<>();
            for(SmashggSlot slot : node.getSlots()) {
                if(slot == null) {
                    continue;
                }
                SmashggEntrant entrant = slot.getEntrant();
                if(entrant == null) {
                    continue;
                }

                Player player = new Player(entrant.getName());
                players.add(player);
                if(entrant.getId() == winnerId) {
                    winner = player;
                }
            }
            sets.add(new Set(node.getId(), players, winner, node.getFullRoundText(), winner != null));
        }

        return sets;
    }

    private String buildPhaseGroupRequest(long phaseGroupId, long page, long perPage) {
        return String.format("{\"query\":\"query phaseGroup($id: Int!, $page:Int!, $perPage:Int!) {"+
                " phaseGroup(id: $id) {id displayIdentifier "+
                " paginatedSets(page:$page, perPage:$perPage) { " +
                " pageInfo {total totalPages page perPage} "+
                " nodes {id fullRoundText winnerId "+
                " slots(includeByes: false) { "+
                " entrant{id name}}" +
                " }}}}\", " +
                "\"variables\":{\"id\":\"%d\", \"page\":\"%d\", \"perPage\":\"%d\"}}", phaseGroupId, page, perPage);
    }

    @CheckForNull
    private String postRequest(String request) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(configuration.getApiUrl());

        StringEntity requestEntity = null;
        try {
            requestEntity = new StringEntity(request);
        } catch (UnsupportedEncodingException e) {
            log.warn("An error occurred while creating StringEntity", e);
            return null;
        }
        httpPost.setEntity(requestEntity);
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + configuration.getApiToken());
        CloseableHttpResponse response;
        try {
            response = client.execute(httpPost);
        } catch (IOException e) {
            log.warn("An error occurred while executing the POST request", e);
            return null;
        }

        HttpEntity responseEntity = response.getEntity();
        if (responseEntity == null) {
            log.warn("Response entity was null");
            return null;
        }

        try {
            return EntityUtils.toString(responseEntity);
        } catch (IOException e) {
            log.warn("Could not get content from response entity", e);
            return null;
        }

    }

    private String findEventSlug(String eventUrl) {
        String smashggTournamentURLPattern = "^https:\\/\\/api.smash.gg\\/(tournament\\/[A-Za-z0-9-]+\\/event\\/[A-Za-z0-9-]+)";
        Pattern p = Pattern.compile(smashggTournamentURLPattern);
        Matcher m = p.matcher(eventUrl);

        if (m.find()) {
            return m.group(1);
        }

        return null;
    }

    private Map<Long, Collection<SmashggPhaseGroup>> getPhaseGroupsPerPhase(Collection<SmashggPhaseGroup> smashggGroups) {
        Map<Long, Collection<SmashggPhaseGroup>> phaseGroupsPerPhase = new HashMap<>();
        for(SmashggPhaseGroup smashGgGroup : smashggGroups) {
            phaseGroupsPerPhase.computeIfAbsent(smashGgGroup.getPhaseId(), k -> new ArrayList<>()).add(smashGgGroup);
        }
        return phaseGroupsPerPhase;
    }

    private List<Phase> getPhases(Collection<Phase> existingPhases,
                                  Collection<SmashggPhase> smashggPhases,
                                  Map<Long, Collection<SmashggPhaseGroup>> smashggGroups) {
        List<Phase> tournamentPhases = new ArrayList<>();
        for(SmashggPhase smashGgPhase : smashggPhases) {
            List<Set> phaseSets = new ArrayList<>();
            if(phaseIsDone(existingPhases, smashGgPhase)) {
                continue;
            }
            boolean phaseDone = true;
            for(SmashggPhaseGroup smashGgGroup : smashggGroups.get(smashGgPhase.getId())) {
                List<Set> sets = fetchSets(smashGgGroup.getId());
                if(!sets.stream().allMatch(Set::isDone)) {
                    phaseDone = false;
                }
                phaseSets.addAll(sets);
            }
            tournamentPhases.add(new Phase(String.valueOf(smashGgPhase.getId()), phaseSets, smashGgPhase.getName(), phaseDone));
        }
        return tournamentPhases;
    }

    private boolean phaseIsDone(Collection<Phase> existingPhases, SmashggPhase smashGgPhase) {
        for(Phase existingPhase : existingPhases) {
            if(existingPhase.getExternalId() != null && existingPhase.getExternalId().equals(String.valueOf(smashGgPhase.getId()))) {
                return existingPhase.isDone();
            }
        }

        return false;
    }

//
//    private Map<String, Player> getParticipants(Collection<Seed> smashGgSeeds) {
//        Map<String, Player> participants = new HashMap<>();
//        for(Seed seed : smashGgSeeds) {
//            if(seed.getMutations() == null || seed.getMutations().getParticipants() == null) {
//                continue;
//            }
//
//            Map<String, Participant> participantsMap = seed.getMutations().getParticipants();
//            for(Participant participant : participantsMap.values()) {
//                Player player = new Player(participant.getPrefix(), participant.getGamerTag());
//                participants.put(seed.getEntrantId(), player);
//            }
//        }
//        return participants;
//    }
//
    private Address buildAddress(SmashggTournament tournament) {
        return new Address(tournament.getCity(), tournament.getAddrState(), tournament.getVenueAddress(), null, tournament.getCountryCode());
    }
//
//    private Map<String, Player> getParticipants(net.migwel.tournify.smashgg.data.Set set, Map<String, Player> participants) {
//        Map<String, Player> listParticipants = new HashMap<>();
//        String entrant1Id = set.getEntrant1Id();
//        if(entrant1Id != null) {
//            listParticipants.put(entrant1Id, participants.get(entrant1Id));
//        }
//        String entrant2Id = set.getEntrant2Id();
//        if(entrant2Id != null) {
//            listParticipants.put(entrant2Id, participants.get(entrant2Id));
//        }
//        return listParticipants;
//    }


}
