package net.migwel.tournify.core.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.migwel.tournify.core.data.Address;
import net.migwel.tournify.core.data.GameType;
import net.migwel.tournify.core.data.Phase;
import net.migwel.tournify.core.data.Player;
import net.migwel.tournify.core.data.Set;
import net.migwel.tournify.core.data.Tournament;
import net.migwel.tournify.core.exception.TimeoutException;
import net.migwel.tournify.core.http.HttpClient;
import net.migwel.tournify.smashgg.config.SmashggConfiguration;
import net.migwel.tournify.smashgg.response.SmashggEntrant;
import net.migwel.tournify.smashgg.response.SmashggEvent;
import net.migwel.tournify.smashgg.response.SmashggEventResponse;
import net.migwel.tournify.smashgg.response.SmashggNode;
import net.migwel.tournify.smashgg.response.SmashggParticipant;
import net.migwel.tournify.smashgg.response.SmashggPhase;
import net.migwel.tournify.smashgg.response.SmashggPhaseGroup;
import net.migwel.tournify.smashgg.response.SmashggPhaseGroupResponse;
import net.migwel.tournify.smashgg.response.SmashggResponse;
import net.migwel.tournify.smashgg.response.SmashggSlot;
import net.migwel.tournify.smashgg.response.SmashggTournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component("SmashggClient")
@Immutable
public class SmashggClient implements TournamentClient {

    private static final Logger log = LoggerFactory.getLogger(SmashggClient.class);

    private final SmashggConfiguration configuration;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public SmashggClient(SmashggConfiguration configuration, ObjectMapper objectMapper, HttpClient httpClient) {
        this.configuration = configuration;
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
    }

    @Override
    @Nullable
    public Tournament fetchTournament(@Nullable Tournament oldTournament, @Nonnull String eventUrl) {
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
        Collection<Phase> tournamentPhases = getPhases(existingPhases, smashggEvent.getPhases(), phaseGroupsPerPhase);
        boolean tournamentDone = true;
        if(!tournamentPhases.stream().allMatch(Phase::isDone)) {
            tournamentDone = false;
        }

        Address address = buildAddress(smashggEvent.getTournament());

        log.info("Done with fetching tournament at url: "+ eventUrl);


        return new Tournament(String.valueOf(smashggEvent.getId()),
                tournamentPhases,
                smashggEvent.getTournament().getName() + " - "+ smashggEvent.getName(),
                new GameType(smashggEvent.getVideogame().getDisplayName()),
                address,
                eventUrl,
                new Date(smashggEvent.getStartAt()*1000),
                tournamentDone);
    }

    @Nonnull
    @Override
    public Collection<Player> getParticipants(@Nonnull String eventUrl) {
        log.info("Fetching tournament at url: " + eventUrl);
        String eventSlug = findEventSlug(eventUrl);
        return fetchParticipants(eventSlug);
    }

    @Nullable
    private <T> T fetch(String request, Class<? extends SmashggResponse> responseClass) throws TimeoutException {
        Collection<Pair<String, String>> headers = Collections.singleton(Pair.of("Authorization", "Bearer " + configuration.getApiToken()));
        String responseStr = httpClient.postRequest(request, configuration.getApiUrl(), headers);
        try {
            @SuppressWarnings("unchecked")
            SmashggResponse<T> response = responseClass.cast(objectMapper.readValue(responseStr, responseClass));

            if(response == null || response.getData() == null) {
                throw new TimeoutException();
            }

            return response.getData().getObject();
        } catch (IOException e) {
            log.warn("Could not convert JSON response "+ responseStr +" to "+ responseClass, e);
            return null;
        }
    }

    @CheckForNull
    private SmashggEvent fetchEvent(String eventSlug) {
        String request = buildEventRequest(eventSlug);
        log.info("Fetching event at "+ eventSlug);
        return fetchWithRetries(request, SmashggEventResponse.class);
    }

    private String buildEventRequest(String eventSlug) {
        return String.format("{\"query\":\"query event($slug: String!){  event(slug: $slug) { " +
                "id slug startAt name" +
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
        log.info("Fetching phaseGroup "+ phaseGroupId);
        return fetchWithRetries(request, SmashggPhaseGroupResponse.class);
    }

    private String buildPhaseGroupRequest(long phaseGroupId, long page, long perPage) {
        return String.format("{\"query\":\"query phaseGroup($id: ID!, $page:Int!, $perPage:Int!) {"+
                " phaseGroup(id: $id) {id displayIdentifier "+
                " paginatedSets(page:$page, perPage:$perPage) { " +
                " pageInfo {total totalPages} "+
                " nodes {id fullRoundText winnerId "+
                " slots(includeByes: false) { "+
                " entrant{id name}}" +
                " }}}}\", " +
                "\"variables\":{\"id\":\"%d\", \"page\":\"%d\", \"perPage\":\"%d\"}}", phaseGroupId, page, perPage);
    }

    @Nonnull
    private Collection<Player> fetchParticipants(String eventSlug) {
        Collection<Player> participants = new ArrayList<>();
        SmashggEvent event;
        long page = 0;
        do {
            page++;
            event = fetchParticipants(eventSlug, page);
            if(event == null ||
               event.getEntrants() == null ||
               event.getEntrants().getPageInfo() == null ||
               event.getEntrants().getPageInfo().getTotalPages() == 0) {
                break;
            }

            for(SmashggNode node : event.getEntrants().getNodes()) {
                for(SmashggParticipant entrant : node.getParticipants()) {
                    participants.add(new Player(entrant.getPrefix(), entrant.getGamerTag()));
                }
            }

        } while (event.getEntrants().getPageInfo().getTotalPages() != page);

        return participants;
    }

    @CheckForNull
    private SmashggEvent fetchParticipants(String eventSlug, long page) {
        String request = buildParticipantsRequest(eventSlug, page, configuration.getSetsPerPage());
        log.info("Fetching participants at "+ eventSlug);
        return fetchWithRetries(request, SmashggEventResponse.class);
    }

    private <T> T fetchWithRetries(String request, Class<? extends SmashggResponse> responseClass) {
        for (int i = 0; i < configuration.getRetryNumber(); i++) {
            try {
                return fetch(request, responseClass);
            } catch (TimeoutException e) {
                log.info("A timeout happened", e);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e1) {
                    //
                }
            }
        }
        log.warn("Could not execute the fetch, no retries left");
        return null;
    }

    private String buildParticipantsRequest(String eventSlug, long page, long perPage) {
        return String.format("{\"query\":\"query event($slug: String!, $page:Int!, $perPage:Int!){"+
                " event(slug: $slug) {"+
                " entrants(query:{page:$page, perPage:$perPage}){ " +
                " pageInfo {total totalPages} "+
                " nodes { "+
                " participants{gamerTag prefix} "+
                " }}}}\", " +
                "\"variables\":{\"slug\":\"%s\", \"page\":\"%d\", \"perPage\":\"%d\"}}", eventSlug, page, perPage);
    }

    @Nonnull
    private Collection<Set> fetchSets(long phaseGroupId) {
        Collection<Set> sets = new ArrayList<>();
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

            sets.addAll(getSets(phaseGroup.getDisplayIdentifier(), phaseGroup.getPaginatedSets().getNodes()));

        } while (phaseGroup.getPaginatedSets().getPageInfo().getTotalPages() != page);

        return sets;
    }

    @Nonnull
    private Collection<Set> getSets(String phaseGroupName, Collection<SmashggNode> nodes) {
        Collection<Set> sets = new ArrayList<>();
        for(SmashggNode node : nodes) {
            if(node == null || node.getId().startsWith("preview")) {
                continue;
            }
            long winnerId = node.getWinnerId();
            Player winner = null;
            Collection<Player> players = new ArrayList<>();
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
            sets.add(new Set(node.getId(), players, winner, phaseGroupName+ " - "+ node.getFullRoundText(), winner != null));
        }

        return sets;
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

    private Collection<Phase> getPhases(Collection<Phase> existingPhases,
                                  Collection<SmashggPhase> smashggPhases,
                                  Map<Long, Collection<SmashggPhaseGroup>> smashggGroups) {
        Collection<Phase> tournamentPhases = new ArrayList<>();
        for(SmashggPhase smashGgPhase : smashggPhases) {
            Collection<Set> phaseSets = new ArrayList<>();
            if(phaseIsDone(existingPhases, smashGgPhase)) {
                continue;
            }
            boolean phaseDone = true;
            for(SmashggPhaseGroup smashGgGroup : smashggGroups.get(smashGgPhase.getId())) {
                Collection<Set> sets = fetchSets(smashGgGroup.getId());
                if(sets.isEmpty() || !sets.stream().allMatch(Set::isDone)) {
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

    private Address buildAddress(SmashggTournament tournament) {
        return new Address(tournament.getCity(), tournament.getAddrState(), tournament.getVenueAddress(), null, tournament.getCountryCode());
    }

}
