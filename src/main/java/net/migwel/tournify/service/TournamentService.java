package net.migwel.tournify.service;

import net.migwel.tournify.consumer.TournamentConsumer;
import net.migwel.tournify.data.Event;
import net.migwel.tournify.data.Phase;
import net.migwel.tournify.data.PhaseGroup;
import net.migwel.tournify.data.Set;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.data.TournamentTracking;
import net.migwel.tournify.store.TournamentRepository;
import net.migwel.tournify.store.TrackingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: I'm not sure I'm happy with an abstract class. I'd rather have an interface but not sure how to do that...
public abstract class TournamentService {

    private static final Logger log = LoggerFactory.getLogger(TournamentService.class);

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TrackingRepository trackingRepository;

    protected Tournament getTournament(TournamentConsumer tournamentConsumer,
                                       String formattedUrl) {
        Tournament tournament = tournamentRepository.findByUrl(formattedUrl);
        if (tournament != null) {
            log.info("Tournament was found in database for url: "+ formattedUrl);
            return tournament;
        }

        log.info("No tournament was found, let's fetch it. url = "+ formattedUrl);
        tournament = fetchTournament(tournamentConsumer, formattedUrl);
        tournamentRepository.save(tournament);
        trackTournament(tournament);
        return tournament;
    }

    protected Tournament fetchTournament(TournamentConsumer tournamentConsumer,
                                       String formattedUrl) {
        return tournamentConsumer.fetchTournament(formattedUrl);
    }

    private void trackTournament(Tournament tournament) {
        TournamentTracking tournamentTracking = new TournamentTracking(tournament, new Date(), new Date());
        trackingRepository.save(tournamentTracking);
    }

    //Returns true if tournament has changed
    public boolean updateTournament(String url) {
        Tournament newTournament = fetchTournament(url);
        Tournament oldTournament = tournamentRepository.findByUrl(url);
        return !compareTournaments(oldTournament, newTournament);
    }

    //Returns true if tournaments are the same
    private boolean compareTournaments(Tournament oldTournament, Tournament newTournament) {
        List<Event> oldEvents = oldTournament.getEvents();
        List<Event> newEvents = newTournament.getEvents();

        Map<String, Event> oldEventsMap = eventsToMap(oldEvents);
        for(Event newEvent : newEvents) {
            if(!compareEvents(oldEventsMap.get(newEvent.getName()), newEvent)) {
                log.info("Event "+ newEvent.getName() +" was updated");
                return false;
            }
        }

        return true;
    }

    //Returns true if events are the same
    private boolean compareEvents(Event oldEvent, Event newEvent) {
        if(oldEvent == null) {
            return false;
        }

        List<Phase> oldPhases = oldEvent.getPhases();
        List<Phase> newPhases = newEvent.getPhases();

        Map<String, Phase> oldPhasesMap = phasesToMap(oldPhases);
        for(Phase newPhase : newPhases) {
            if(!comparePhases(oldPhasesMap.get(newPhase.getPhaseName()), newPhase)) {
                log.info("Phase "+ newPhase.getPhaseName() +" was updated");
                return false;
            }
        }

        return true;
    }

    //Returns true if phases are the same
    private boolean comparePhases(Phase oldPhase, Phase newPhase) {
        if(oldPhase == null) {
            return false;
        }

        List<PhaseGroup> oldPhaseGroups = oldPhase.getPhaseGroups();
        List<PhaseGroup> newPhaseGroups = newPhase.getPhaseGroups();

        Map<Long, PhaseGroup> oldPhaseGroupsMap = phaseGroupsToMap(oldPhaseGroups);
        for(PhaseGroup newPhaseGroup : newPhaseGroups) {
            if(!comparePhaseGroups(oldPhaseGroupsMap.get(newPhaseGroup.getExternalId()), newPhaseGroup)) {
                log.info("Phase group "+ newPhaseGroup.getExternalId() +" was updated");
                return false;
            }
        }

        return true;
    }

    //Returns true if phase groups are the same
    private boolean comparePhaseGroups(PhaseGroup oldPhaseGroup, PhaseGroup newPhaseGroup) {
        if(oldPhaseGroup == null) {
            return false;
        }

        if(newPhaseGroup.getSets() == null) {
            return true;
        }

        List<Set> oldSets = oldPhaseGroup.getSets();
        List<Set> newSets = newPhaseGroup.getSets();

        Map<String, Set> oldSetsMap = setsToMap(oldSets);
        for(Set newSet : newSets) {
            if(!compareSets(oldSetsMap.get(newSet.getExternalId()), newSet)) {
                log.info("Set "+ newSet.getRound() +" was updated");
                return false;
            }
        }
        return true;
    }

    //Returns true if sets are the same
    private boolean compareSets(Set oldSet, Set newSet) {
        if(newSet == null || newSet.getWinner() == null) {
            return true;
        }

        if(oldSet == null) {
            return false;
        }

        if(!newSet.getWinner().equals(oldSet.getWinner())) {
            return false;
        }

        return true;

    }

    private Map<String,Set> setsToMap(List<Set> sets) {
        Map<String, Set> setsMap = new HashMap<>();
        for(Set set : sets) {
            setsMap.put(set.getExternalId(), set);
        }
        return setsMap;
    }

    private Map<Long,PhaseGroup> phaseGroupsToMap(List<PhaseGroup> phaseGroups) {
        Map<Long, PhaseGroup> phaseGroupsMap = new HashMap<>();
        for(PhaseGroup phaseGroup : phaseGroups) {
            phaseGroupsMap.put(phaseGroup.getExternalId(), phaseGroup);
        }
        return phaseGroupsMap;
    }

    private Map<String,Phase> phasesToMap(List<Phase> phases) {
        Map<String, Phase> phasesMap = new HashMap<>();
        for(Phase phase : phases) {
            phasesMap.put(phase.getPhaseName(), phase); //TODO: Probably smarter to use hashcode
        }

        return phasesMap;
    }

    @NotNull
    private Map<String, Event> eventsToMap(List<Event> events) {
        Map<String, Event> eventsMap = new HashMap<>();
        for(Event event : events) {
            eventsMap.put(event.getName(), event); //TODO: Probably smarter to use hashcode
        }

        return eventsMap;
    }

    public abstract Tournament getTournament(String url);
    protected abstract Tournament fetchTournament(String url);
}
