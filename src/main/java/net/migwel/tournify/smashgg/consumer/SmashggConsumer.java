package net.migwel.tournify.smashgg.consumer;

import net.migwel.tournify.consumer.TournamentConsumer;
import net.migwel.tournify.data.Address;
import net.migwel.tournify.data.Event;
import net.migwel.tournify.data.GameType;
import net.migwel.tournify.data.Phase;
import net.migwel.tournify.data.PhaseGroup;
import net.migwel.tournify.data.Player;
import net.migwel.tournify.data.Set;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.smashgg.data.GetPhaseGroupResponse;
import net.migwel.tournify.smashgg.data.GetTournamentResponse;
import net.migwel.tournify.smashgg.data.Participant;
import net.migwel.tournify.smashgg.data.Seed;
import net.migwel.tournify.smashgg.data.VideoGame;
import net.migwel.tournify.smashgg.data.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component("SmashggConsumer")
public class SmashggConsumer implements TournamentConsumer {

    private static final Logger log = LoggerFactory.getLogger(SmashggConsumer.class);

    private static final String EXPAND_TOURNAMENT = "?expand[]=event&expand[]=phase&expand[]=groups";
    private static final String PHASE_GROUP_URL = "https://api.smash.gg/phase_group/";
    private static final String EXPAND_PHASE_GROUP = "?expand[]=sets&expand[]=seeds";

    @Autowired
    private RestTemplate restTemplate;

    public SmashggConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
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
        for(Group group : tournamentResponse.getEntities().getGroups()) {
            groups.computeIfAbsent(group.getPhaseId(), k -> new ArrayList<>()).add(new PhaseGroup(group.getId(), group.getDisplayIdentifier(), null));
        }

        Map<Long, List<Phase>> phases = new HashMap<>();
        for(net.migwel.tournify.smashgg.data.Phase phase : tournamentResponse.getEntities().getPhase()) {
            phases.computeIfAbsent(phase.getEventId(), k -> new ArrayList<>()).add(new Phase(groups.get(phase.getId()), phase.getName()));
        }

        List<Event> events = new ArrayList<>();
        for(net.migwel.tournify.smashgg.data.Event event : tournamentResponse.getEntities().getEvent()) {
            events.add(new Event(phases.get(event.getId()), videoGames.get(event.getVideogameId()), event.getName(), event.getDescription()));
        }

        for(Event event : events) {
            for(Phase phase : event.getPhases()) {
                for(PhaseGroup phaseGroup : phase.getPhaseGroups()) {
                    String phaseGroupUrl = PHASE_GROUP_URL + phaseGroup.getExternalId() + EXPAND_PHASE_GROUP;

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
                    for(net.migwel.tournify.smashgg.data.Set set : phaseGroupResponse.getEntities().getSets()) {
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
                url,
                new Date(tournamentResponse.getEntities().getTournament().getStartAt()*1000));
    }

    private Address buildAddress(net.migwel.tournify.smashgg.data.Tournament tournament) {
        return new Address(tournament.getCity(), tournament.getAddrState(), tournament.getVenueAddress(), null, tournament.getCountryCode());
    }

    private List<Player> getParticipants(net.migwel.tournify.smashgg.data.Set set, Map<Long, Player> participants) {
        List<Player> listParticipants = new ArrayList<>();
        listParticipants.add(participants.get(set.getEntrant1Id()));
        listParticipants.add(participants.get(set.getEntrant2Id()));

        return listParticipants;
    }
}
