package net.migwel.tournify.consumer;

import net.migwel.tournify.data.Event;
import net.migwel.tournify.data.*;
import net.migwel.tournify.data.Phase;
import net.migwel.tournify.data.Set;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.data.consumer.smashgg.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class SmashggConsumer {

    private static final Logger log = LoggerFactory.getLogger(SmashggConsumer.class);

    private static String EXPAND_TOURNAMENT = "?expand[]=event&expand[]=phase&expand[]=groups";
    private static String PHASE_GROUP_URL = "https://api.smash.gg/phase_group/";
    private static String EXPAND_PHASE_GROUP = "?expand[]=sets&expand[]=seeds";

    @Autowired
    private RestTemplate restTemplate;

    public SmashggConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Tournament getTournament(String url) {
        String tournamentWithEventsUrl = url + EXPAND_TOURNAMENT;
        GetTournamentResponse tournamentResponse = restTemplate.getForObject(tournamentWithEventsUrl, GetTournamentResponse.class);

        if(tournamentResponse == null ||
           tournamentResponse.getEntities() == null ||
           tournamentResponse.getEntities().getTournament() == null) {
            log.info("Could not retrieve tournament for url: "+ url);
            return null;
        }

        Map<Long, GameType> videoGames = new HashMap<>();
        for(VideoGame videoGame : tournamentResponse.getEntities().getVideogame()) {
            videoGames.put(videoGame.getId(), new GameType(videoGame.getName()));
        }

        Map<Long, List<PhaseGroup>> groups = new HashMap<>();
        for(net.migwel.tournify.data.consumer.smashgg.Group group : tournamentResponse.getEntities().getGroups()) {
            groups.computeIfAbsent(group.getPhaseId(), k -> new ArrayList<>()).add(new PhaseGroup(group.getId(), group.getDisplayIdentifier(), null));
        }

        Map<Long, List<Phase>> phases = new HashMap<>();
        for(net.migwel.tournify.data.consumer.smashgg.Phase phase : tournamentResponse.getEntities().getPhase()) {
            phases.computeIfAbsent(phase.getEventId(), k -> new ArrayList<>()).add(new Phase(groups.get(phase.getId()), phase.getName()));
        }

        List<Event> events = new ArrayList<>();
        for(net.migwel.tournify.data.consumer.smashgg.Event event : tournamentResponse.getEntities().getEvent()) {
            events.add(new Event(phases.get(event.getId()), videoGames.get(event.getVideogameId()), event.getName(), event.getDescription()));
        }

        for(Event event : events) {
            for(Phase phase : event.getPhases()) {
                for(PhaseGroup phaseGroup : phase.getPhaseGroups()) {
                    String phaseGroupUrl = PHASE_GROUP_URL + phaseGroup.getId() + EXPAND_PHASE_GROUP;

                    GetPhaseGroupResponse phaseGroupResponse = restTemplate.getForObject(phaseGroupUrl, GetPhaseGroupResponse.class);

                    if(phaseGroupResponse.getEntities() == null ||
                       phaseGroupResponse.getEntities().getSeeds() == null ||
                       phaseGroupResponse.getEntities().getSets() == null) {
                        continue;
                    }

                    Map<Long, Player> participants = new HashMap<>();
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
                    for(net.migwel.tournify.data.consumer.smashgg.Set set : phaseGroupResponse.getEntities().getSets()) {
                        List<Player> listParticipants = getParticipants(set, participants);
                        sets.add(new Set(listParticipants, set.getWinnerId()));
                    }
                    phaseGroup.setSets(sets);
                }
            }
        }

        Address address = buildAddress(tournamentResponse.getEntities().getTournament());

        return new Tournament(events,
                tournamentResponse.getEntities().getTournament().getName(),
                address,
                tournamentWithEventsUrl,
                new Date(tournamentResponse.getEntities().getTournament().getStartAt()*1000));
    }

    private Address buildAddress(net.migwel.tournify.data.consumer.smashgg.Tournament tournament) {
        return new Address(tournament.getCity(), tournament.getAddrState(), tournament.getVenueAddress(), null, tournament.getCountryCode());
    }

    private List<Player> getParticipants(net.migwel.tournify.data.consumer.smashgg.Set set, Map<Long, Player> participants) {
        List<Player> listParticipants = new ArrayList<>();
        listParticipants.add(participants.get(set.getEntrant1Id()));
        listParticipants.add(participants.get(set.getEntrant2Id()));

        return listParticipants;
    }
}
