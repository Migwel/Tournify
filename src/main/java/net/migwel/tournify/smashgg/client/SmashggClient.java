package net.migwel.tournify.smashgg.client;

import net.migwel.tournify.client.TournamentClient;
import net.migwel.tournify.data.Address;
import net.migwel.tournify.data.GameType;
import net.migwel.tournify.data.Phase;
import net.migwel.tournify.data.Player;
import net.migwel.tournify.data.Set;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.smashgg.data.GetEventResponse;
import net.migwel.tournify.smashgg.data.GetPhaseGroupResponse;
import net.migwel.tournify.smashgg.data.GetTournamentResponse;
import net.migwel.tournify.smashgg.data.Group;
import net.migwel.tournify.smashgg.data.Participant;
import net.migwel.tournify.smashgg.data.Seed;
import net.migwel.tournify.smashgg.data.VideoGame;
import net.migwel.tournify.store.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.CheckForNull;
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
public class SmashggClient implements TournamentClient {

    private static final Logger log = LoggerFactory.getLogger(SmashggClient.class);

    private static final String EXPAND_TOURNAMENT = "?expand[]=event";
    private static final String EXPAND_EVENT = "?expand[]=phase&expand[]=groups";
    private static final String PHASE_GROUP_URL = "https://api.smash.gg/phase_group/";
    private static final String EXPAND_PHASE_GROUP = "?expand[]=sets&expand[]=seeds";

    private final RestTemplate restTemplate;

    private final TournamentRepository tournamentRepository;

    public SmashggClient(RestTemplate restTemplate, TournamentRepository tournamentRepository) {
        this.restTemplate = restTemplate;
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public Tournament fetchTournament(String eventUrl) {
        log.info("Fetching tournament at url: "+ eventUrl);
        String tournamentWithEventsUrl = buildTournamentUrlFromEventUrl(eventUrl);
        if(tournamentWithEventsUrl == null) {
            return null;
        }
        GetTournamentResponse tournamentResponse = restTemplate.getForObject(tournamentWithEventsUrl, GetTournamentResponse.class);

        if(tournamentResponse == null ||
           tournamentResponse.getEntities() == null ||
           tournamentResponse.getEntities().getTournament() == null) {
            log.info("Could not retrieve tournament for url: "+ eventUrl);
            return null;
        }

        Map<Long, GameType> videoGames = new HashMap<>();
        for(VideoGame videoGame : tournamentResponse.getEntities().getVideogame()) {
            videoGames.put(videoGame.getId(), new GameType(videoGame.getName()));
        }

        GetEventResponse eventResponse = restTemplate.getForObject(eventUrl + EXPAND_EVENT, GetEventResponse.class);
        if(eventResponse == null ||
                eventResponse.getEntities() == null ||
                eventResponse.getEntities().getEvent() == null) {
            log.info("Could not retrieve event for url: "+ eventUrl);
            return null;
        }

        Map<Long, Collection<Group>> groups = getGroups(eventResponse.getEntities().getGroups());

        Tournament tournament = tournamentRepository.findByUrl(eventUrl);
        Collection<Phase> existingPhases = Collections.emptyList();
        if(tournament != null) {
            existingPhases = tournament.getPhases();
        }
        List<Phase> tournamentPhases = getPhases(existingPhases, eventResponse.getEntities().getPhases(), groups);

        Address address = buildAddress(tournamentResponse.getEntities().getTournament());

        log.info("Done with fetching tournament at url: "+ eventUrl);


        return new Tournament(String.valueOf(eventResponse.getEntities().getEvent().getId()),
                tournamentPhases,
                tournamentResponse.getEntities().getTournament().getName(),
                videoGames.get(eventResponse.getEntities().getEvent().getVideogameId()),
                address,
                eventUrl,
                new Date(tournamentResponse.getEntities().getTournament().getStartAt()*1000));
    }

    private Map<Long, Collection<Group>> getGroups(Collection<Group> smashggGroups) {
        Map<Long, Collection<Group>> groups = new HashMap<>(); //Map PhaseId - PhaseGroup
        for(Group smashGgGroup : smashggGroups) {
            groups.computeIfAbsent(smashGgGroup.getPhaseId(), k -> new ArrayList<>()).add(smashGgGroup);
        }
        return groups;
    }

    private List<Phase> getPhases(Collection<Phase> existingPhases,
                                  Collection<net.migwel.tournify.smashgg.data.Phase> smashggPhases,
                                  Map<Long, Collection<Group>> smashggGroups) {
        List<Phase> tournamentPhases = new ArrayList<>();
        for(net.migwel.tournify.smashgg.data.Phase smashGgPhase : smashggPhases) {
            List<Set> phaseSets = new ArrayList<>();
            if(phaseIsDone(existingPhases, smashGgPhase)) {
                continue;
            }
            boolean phaseDone = true;
            for(Group smashGgGroup : smashggGroups.get(smashGgPhase.getId())) {
                String phaseGroupUrl = PHASE_GROUP_URL + smashGgGroup.getId() + EXPAND_PHASE_GROUP;

                GetPhaseGroupResponse phaseGroupResponse = restTemplate.getForObject(phaseGroupUrl, GetPhaseGroupResponse.class);

                if(phaseGroupResponse.getEntities() == null ||
                   phaseGroupResponse.getEntities().getSeeds() == null ||
                   phaseGroupResponse.getEntities().getSets() == null) {
                    continue;
                }

                Map<String, Player> participants = getParticipants(phaseGroupResponse.getEntities().getSeeds());
                Collection<Set> sets = getSets(phaseGroupResponse.getEntities().getSets(), participants);
                 if(!sets.stream().allMatch(Set::isDone)) {
                     phaseDone = false;
                 }
                phaseSets.addAll(sets);
            }
            tournamentPhases.add(new Phase(String.valueOf(smashGgPhase.getId()), phaseSets, smashGgPhase.getName(), phaseDone));
        }
        return tournamentPhases;
    }

    private boolean phaseIsDone(Collection<Phase> existingPhases, net.migwel.tournify.smashgg.data.Phase smashGgPhase) {
        for(Phase existingPhase : existingPhases) {
            if(existingPhase.getExternalId() != null && existingPhase.getExternalId().equals(String.valueOf(smashGgPhase.getId()))) {
                return existingPhase.isDone();
            }
        }

        return false;
    }

    private Collection<Set> getSets(Collection<net.migwel.tournify.smashgg.data.Set> smashggSets, Map<String, Player> participants) {
        List<Set> sets = new ArrayList<>();
        for(net.migwel.tournify.smashgg.data.Set smashggSet : smashggSets) {
            if(smashggSet.isUnreachable() || smashggSet.getDisplayRound() == -1) {
                continue;
            }
            Map<String, Player> participantsMap = getParticipants(smashggSet, participants);
            List<Player> listParticipants = new ArrayList<>(participantsMap.values());
            Player winner = participantsMap.get(smashggSet.getWinnerId());
            sets.add(new Set(smashggSet.getId(), listParticipants, winner, smashggSet.getFullRoundText(), winner != null));
        }
        return sets;
    }

    private Map<String, Player> getParticipants(Collection<Seed> smashGgSeeds) {
        Map<String, Player> participants = new HashMap<>();
        for(Seed seed : smashGgSeeds) {
            if(seed.getMutations() == null || seed.getMutations().getParticipants() == null) {
                continue;
            }

            Map<String, Participant> participantsMap = seed.getMutations().getParticipants();
            for(Participant participant : participantsMap.values()) {
                Player player = new Player(participant.getPrefix(), participant.getGamerTag());
                participants.put(seed.getEntrantId(), player);
            }
        }
        return participants;
    }

    @CheckForNull
    private String buildTournamentUrlFromEventUrl(String eventUrl) {
        String smashggTournamentURLPattern = "^https:\\/\\/api.smash.gg\\/tournament\\/[A-Za-z0-9-]+";
        Pattern p = Pattern.compile(smashggTournamentURLPattern);
        Matcher m = p.matcher(eventUrl);

        if (m.find()) {
            return m.group(0) + EXPAND_TOURNAMENT;
        }

        return null;
    }

    private Address buildAddress(net.migwel.tournify.smashgg.data.Tournament tournament) {
        return new Address(tournament.getCity(), tournament.getAddrState(), tournament.getVenueAddress(), null, tournament.getCountryCode());
    }

    private Map<String, Player> getParticipants(net.migwel.tournify.smashgg.data.Set set, Map<String, Player> participants) {
        Map<String, Player> listParticipants = new HashMap<>();
        String entrant1Id = set.getEntrant1Id();
        if(entrant1Id != null) {
            listParticipants.put(entrant1Id, participants.get(entrant1Id));
        }
        String entrant2Id = set.getEntrant2Id();
        if(entrant2Id != null) {
            listParticipants.put(entrant2Id, participants.get(entrant2Id));
        }
        return listParticipants;
    }
}
