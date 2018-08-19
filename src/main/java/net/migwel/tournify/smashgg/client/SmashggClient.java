package net.migwel.tournify.smashgg.client;

import net.migwel.tournify.client.TournamentClient;
import net.migwel.tournify.data.Address;
import net.migwel.tournify.data.GameType;
import net.migwel.tournify.data.Phase;
import net.migwel.tournify.data.Player;
import net.migwel.tournify.data.Set;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.service.UrlService;
import net.migwel.tournify.smashgg.data.GetEventResponse;
import net.migwel.tournify.smashgg.data.GetPhaseGroupResponse;
import net.migwel.tournify.smashgg.data.GetTournamentResponse;
import net.migwel.tournify.smashgg.data.Group;
import net.migwel.tournify.smashgg.data.Participant;
import net.migwel.tournify.smashgg.data.Seed;
import net.migwel.tournify.smashgg.data.VideoGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component("SmashggClient")
public class SmashggClient implements TournamentClient {

    private static final Logger log = LoggerFactory.getLogger(SmashggClient.class);

    private static final String EXPAND_TOURNAMENT = "?expand[]=event";
    private static final String EXPAND_EVENT = "?expand[]=phase&expand[]=groups";
    private static final String PHASE_GROUP_URL = "https://api.smash.gg/phase_group/";
    private static final String EXPAND_PHASE_GROUP = "?expand[]=sets&expand[]=seeds";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("SmashggUrlService")
    private UrlService urlService;

    public SmashggClient(RestTemplate restTemplate, UrlService urlService) {
        this.restTemplate = restTemplate;
        this.urlService = urlService;
    }

    @Override
    public Tournament fetchTournament(String eventUrl) {
        log.info("Fetching tournament at url: "+ eventUrl);
        String tournamentWithEventsUrl = buildTournamentUrlFromEventUrl(eventUrl);
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

        Map<Long, List<Group>> groups = new HashMap<>(); //Map PhaseId - PhaseGroup
        for(Group smashGgGroup : eventResponse.getEntities().getGroups()) {
            groups.computeIfAbsent(smashGgGroup.getPhaseId(), k -> new ArrayList<>()).add(smashGgGroup);
        }

        List<Phase> tournamentPhases = new ArrayList<>();
        for(net.migwel.tournify.smashgg.data.Phase smashGgPhase : eventResponse.getEntities().getPhases()) {
            List<Set> phaseSets = new ArrayList<>();
            for(Group smashGgGroup : groups.get(smashGgPhase.getId())) {
                String phaseGroupUrl = PHASE_GROUP_URL + smashGgGroup.getId() + EXPAND_PHASE_GROUP;

                GetPhaseGroupResponse phaseGroupResponse = restTemplate.getForObject(phaseGroupUrl, GetPhaseGroupResponse.class);

                if(phaseGroupResponse.getEntities() == null ||
                   phaseGroupResponse.getEntities().getSeeds() == null ||
                   phaseGroupResponse.getEntities().getSets() == null) {
                    continue;
                }

                Map<String, Player> participants = new HashMap<>();
                for(Seed seed : phaseGroupResponse.getEntities().getSeeds()) {
                    if(seed.getMutations() == null || seed.getMutations().getParticipants() == null) {
                        continue;
                    }

                    Map<String, Participant> participantsMap = seed.getMutations().getParticipants();
                    for(Participant participant : participantsMap.values()) {
                        Player player = new Player(participant.getPrefix(), participant.getGamerTag());
                        participants.put(seed.getEntrantId(), player);
                    }
                }

                List<Set> sets = new ArrayList<>();
                for(net.migwel.tournify.smashgg.data.Set set : phaseGroupResponse.getEntities().getSets()) {
                    Map<String, Player> participantsMap = getParticipants(set, participants);
                    List<Player> listParticipants = new ArrayList<>(participantsMap.values());
                    Player winner = participantsMap.get(set.getWinnerId());
                    sets.add(new Set(set.getId(), listParticipants, winner, set.getFullRoundText()));
                }
                phaseSets.addAll(sets);
            }
            tournamentPhases.add(new Phase(phaseSets, smashGgPhase.getName()));
        }

        Address address = buildAddress(tournamentResponse.getEntities().getTournament());

        log.info("Done with fetching tournament at url: "+ eventUrl);


        return new Tournament(tournamentPhases,
                tournamentResponse.getEntities().getTournament().getName(),
                videoGames.get(eventResponse.getEntities().getEvent().getVideogameId()),
                address,
                eventUrl,
                new Date(tournamentResponse.getEntities().getTournament().getStartAt()*1000));
    }

    private String buildTournamentUrlFromEventUrl(String eventUrl) {
        return urlService.normalizeUrl(eventUrl) + EXPAND_TOURNAMENT;
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
