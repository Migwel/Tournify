package net.migwel.tournify.service;

import net.migwel.tournify.client.TournamentClient;
import net.migwel.tournify.data.Phase;
import net.migwel.tournify.data.Player;
import net.migwel.tournify.data.Set;
import net.migwel.tournify.data.SetUpdate;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.store.NotificationRepository;
import net.migwel.tournify.store.TournamentRepository;
import net.migwel.tournify.store.TrackingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
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
    private NotificationRepository notificationRepository;

    @Autowired
    private TrackingService trackingService;

    public abstract String normalizeUrl(String tournamentUrl);

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
        trackingService.trackTournament(tournament);
        return tournament;
    }

    protected Tournament fetchTournament(TournamentClient tournamentClient,
                                       String formattedUrl) {
        return tournamentClient.fetchTournament(formattedUrl);
    }

    @Nonnull
    public List<SetUpdate> updateTournament(String url) {
        Tournament oldTournament = tournamentRepository.findByUrl(url);
        Tournament newTournament = fetchTournament(url);
        //We could/should probably use some comparison framework, like JaVers
        boolean firstFetch = oldTournament.getExternalId() == null;
        List<SetUpdate> setUpdates = compareTournaments(oldTournament, newTournament, firstFetch);
        if(!setUpdates.isEmpty()) {
            log.info("Tournament has changed: saving modifications");
            tournamentRepository.save(oldTournament);
        }
        return firstFetch ? Collections.emptyList() : setUpdates;
    }

    //Returns true if tournaments are the same
    @Nonnull
    private List<SetUpdate> compareTournaments(Tournament oldTournament, Tournament newTournament, boolean firstFetch) {
        if(firstFetch) {
            oldTournament.setExternalId(newTournament.getExternalId());
            oldTournament.setName(newTournament.getName());
            oldTournament.setAddress(newTournament.getAddress());
            oldTournament.setDate(newTournament.getDate());
        }
        List<SetUpdate> setUpdates = new LinkedList<>();
        List<Phase> oldPhases = oldTournament.getPhases();
        List<Phase> newPhases = newTournament.getPhases();

        Map<String, Phase> oldPhasesMap = phasesToMap(oldPhases);
        for(Phase newPhase : newPhases) {
            Phase oldPhase = oldPhasesMap.get(newPhase.getPhaseName());
            if(oldPhase == null) {
                oldPhases.add(newPhase);
                setUpdates.addAll(getNewSets(newPhase.getSets()));
                continue;
            }
            comparePhases(oldPhase, newPhase, setUpdates, oldTournament.getName());
        }

        return setUpdates;
    }

    private Collection<SetUpdate> getNewSets(Collection<Set> sets) {
        List<SetUpdate> setUpdates = new LinkedList<>();
        for(Set set : sets) {
            setUpdates.add(new SetUpdate(set, "New set found")); //TODO: Change description
        }
        return setUpdates;
    }

    //Returns true if phases are the same
    private void comparePhases(@Nonnull Phase oldPhase, Phase newPhase, List<SetUpdate> setUpdates, String tournamentName) {
        if(newPhase == null) {
            return;
        }

        List<Set> oldSets = oldPhase.getSets();
        List<Set> newSets = newPhase.getSets();

        Map<String, Set> oldSetsMap = setsToMap(oldSets);
        boolean phaseDone = true;
        for(Set newSet : newSets) {
            Set oldSet = oldSetsMap.get(newSet.getExternalId());
            if(oldSet == null) {
                oldSets.add(newSet);
                continue;
            }

            compareSets(oldSet, newSet, setUpdates, tournamentName, oldPhase.getPhaseName());
            if(!oldSet.isDone()) {
                phaseDone = false;
            }
        }

        oldPhase.setDone(phaseDone);
    }

    //Returns true if sets are the same
    private void compareSets(@Nonnull Set oldSet, Set newSet, List<SetUpdate> setUpdates, String tournamentName, String phaseName) {
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
            oldSet.setDone(true);
            String description = buildSetUpdateDescription(tournamentName, phaseName, oldSet.getRound(), oldSet.getPlayers(), newSet.getWinner());
            setUpdates.add(new SetUpdate(oldSet, description));
        }
    }

    private String buildSetUpdateDescription(String tournamentName, String phaseName, String round, Collection<Player> players, Player winner) {
        return "Tournament [" +
                tournamentName +
                "] - Phase [" +
                phaseName +
                "] - Set [" +
                round +
                "] - Players [" +
                players.stream().map(Player::getDisplayUsername).collect(Collectors.joining(" vs ")) +
                "] - Winner is " +
                winner.getDisplayUsername();
    }

    private Map<String,Set> setsToMap(List<Set> sets) {
        Map<String, Set> setsMap = new HashMap<>();
        for(Set set : sets) {
            setsMap.put(set.getExternalId(), set);
        }
        return setsMap;
    }

    private Map<String,Phase> phasesToMap(List<Phase> phases) {
        Map<String, Phase> phasesMap = new HashMap<>();
        for(Phase phase : phases) {
            phasesMap.put(phase.getPhaseName(), phase); //TODO: Probably smarter to use hashcode
        }

        return phasesMap;
    }

    public abstract Tournament getTournament(String url);
    protected abstract Tournament fetchTournament(String url);
}
