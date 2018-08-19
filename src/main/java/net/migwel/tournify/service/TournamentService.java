package net.migwel.tournify.service;

import net.migwel.tournify.client.TournamentClient;
import net.migwel.tournify.data.Event;
import net.migwel.tournify.data.Notification;
import net.migwel.tournify.data.Phase;
import net.migwel.tournify.data.PhaseGroup;
import net.migwel.tournify.data.Player;
import net.migwel.tournify.data.Set;
import net.migwel.tournify.data.SetUpdate;
import net.migwel.tournify.data.Subscription;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.data.TournamentTracking;
import net.migwel.tournify.store.NotificationRepository;
import net.migwel.tournify.store.SubscriptionRepository;
import net.migwel.tournify.store.TournamentRepository;
import net.migwel.tournify.store.TrackingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TODO: I'm not sure I'm happy with an abstract class. I'd rather have an interface but not sure how to do that...
public abstract class TournamentService {

    private static final Logger log = LoggerFactory.getLogger(TournamentService.class);

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TrackingRepository trackingRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    protected Tournament getTournament(TournamentClient tournamentClient,
                                       String formattedUrl) {
        Tournament tournament = tournamentRepository.findByUrl(formattedUrl);
        if (tournament != null) {
            log.info("Tournament was found in database for url: "+ formattedUrl);
            return tournament;
        }

        log.info("No tournament was found, let's fetch it. url = "+ formattedUrl);
        tournament = fetchTournament(tournamentClient, formattedUrl);
        tournamentRepository.save(tournament);
        trackTournament(tournament);
        return tournament;
    }

    protected Tournament fetchTournament(TournamentClient tournamentClient,
                                       String formattedUrl) {
        return tournamentClient.fetchTournament(formattedUrl);
    }

    private void trackTournament(Tournament tournament) {
        TournamentTracking tournamentTracking = new TournamentTracking(tournament, new Date(), new Date());
        trackingRepository.save(tournamentTracking);
    }

    //Returns true if tournament has changed
    public boolean updateTournament(String url) {
        Tournament oldTournament = tournamentRepository.findByUrl(url);
        Tournament newTournament = fetchTournament(url);
        //We could/should probably use some comparison framework, like JaVers
        List<SetUpdate> setUpdates = compareTournaments(oldTournament, newTournament);
        boolean hasChanged = !setUpdates.isEmpty();
        if(hasChanged) {
            log.info("Tournament has changed: saving modifications");
            tournamentRepository.save(oldTournament);
            addNotification(oldTournament.getUrl(), setUpdates); //This shouldn't be here
        }
        return hasChanged;
    }

    private void addNotification(String tournamentUrl, List<SetUpdate> setUpdates) {
        List<Subscription> subscriptionList = subscriptionRepository.findByTournamentUrlAndActive(tournamentUrl, true);
        String setUpdatesStr = setUpdates.stream().map(e -> e.toString()).collect(Collectors.joining(","));
        for(Subscription subscription : subscriptionList) {
            Notification notification = new Notification(subscription, setUpdatesStr, new Date(), new Date());
            notificationRepository.save(notification);
        }
    }

    //Returns true if tournaments are the same
    @Nonnull
    private List<SetUpdate> compareTournaments(Tournament oldTournament, Tournament newTournament) {
        List<SetUpdate> setUpdates = new LinkedList<>();
        List<Event> oldEvents = oldTournament.getEvents();
        List<Event> newEvents = newTournament.getEvents();

        Map<String, Event> oldEventsMap = eventsToMap(oldEvents);
        for(Event newEvent : newEvents) {
            Event oldEvent = oldEventsMap.get(newEvent.getName());
            if(oldEvent == null) {
                oldEvents.add(newEvent);
                continue;
            }
            compareEvents(oldEvent, newEvent, setUpdates);
        }

        return setUpdates;
    }

    //Returns true if events are the same
    private void compareEvents(@Nonnull Event oldEvent, Event newEvent, List<SetUpdate> setUpdates) {
        if(newEvent == null) {
            return;
        }

        List<Phase> oldPhases = oldEvent.getPhases();
        List<Phase> newPhases = newEvent.getPhases();

        Map<String, Phase> oldPhasesMap = phasesToMap(oldPhases);
        for(Phase newPhase : newPhases) {
            Phase oldPhase = oldPhasesMap.get(newPhase.getPhaseName());
            if(oldPhase == null) {
                oldPhases.add(newPhase);
                continue;
            }
            comparePhases(oldPhase, newPhase, setUpdates);
        }
    }

    //Returns true if phases are the same
    private void comparePhases(@Nonnull Phase oldPhase, Phase newPhase, List<SetUpdate> setUpdates) {
        if(newPhase == null) {
            return;
        }

        List<PhaseGroup> oldPhaseGroups = oldPhase.getPhaseGroups();
        List<PhaseGroup> newPhaseGroups = newPhase.getPhaseGroups();

        Map<Long, PhaseGroup> oldPhaseGroupsMap = phaseGroupsToMap(oldPhaseGroups);
        for(PhaseGroup newPhaseGroup : newPhaseGroups) {
            PhaseGroup oldPhaseGroup = oldPhaseGroupsMap.get(newPhaseGroup.getExternalId());
            if(oldPhaseGroup == null) {
                oldPhaseGroups.add(newPhaseGroup);
                continue;
            }
            comparePhaseGroups(oldPhaseGroup, newPhaseGroup, setUpdates);
        }
    }

    //Returns true if phase groups are the same
    private void comparePhaseGroups(@Nonnull PhaseGroup oldPhaseGroup, PhaseGroup newPhaseGroup, List<SetUpdate> setUpdates) {
        if(newPhaseGroup == null) {
            return;
        }

        List<Set> oldSets = oldPhaseGroup.getSets();
        List<Set> newSets = newPhaseGroup.getSets();

        Map<String, Set> oldSetsMap = setsToMap(oldSets);
        for(Set newSet : newSets) {
            Set oldSet = oldSetsMap.get(newSet.getExternalId());
            if(oldSet == null) {
                oldSets.add(newSet);
                continue;
            }

            compareSets(oldSet, newSet, setUpdates);
        }
    }

    //Returns true if sets are the same
    private void compareSets(@Nonnull Set oldSet, Set newSet, List<SetUpdate> setUpdates) {
        if(newSet == null || newSet.getWinner() == null) {
            return;
        }

        List<Player> oldPlayers = oldSet.getPlayers();
        for (Player player : newSet.getPlayers()) {
            if(!oldPlayers.contains(player)) {
                oldPlayers.add(player);
            }
        }

        if(!newSet.getWinner().equals(oldSet.getWinner())) {
            oldSet.setWinner(newSet.getWinner());
            setUpdates.add(new SetUpdate(oldSet, "Set "+ oldSet.getExternalId() +" has been updated: winner is "+ newSet.getWinner()));
        }
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

    @Nonnull
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
