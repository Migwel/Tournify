package net.migwel.tournify.core.service;

import net.migwel.tournify.communication.commons.Update;
import net.migwel.tournify.communication.commons.Updates;
import net.migwel.tournify.core.client.TournamentClient;
import net.migwel.tournify.core.data.Phase;
import net.migwel.tournify.core.data.Player;
import net.migwel.tournify.core.data.Set;
import net.migwel.tournify.core.data.Tournament;
import net.migwel.tournify.core.store.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractTournamentService implements TournamentService {

    private static final Logger log = LoggerFactory.getLogger(AbstractTournamentService.class);

    private final TournamentRepository tournamentRepository;

    private final TrackingService trackingService;

    public AbstractTournamentService(TournamentRepository tournamentRepository, TrackingService trackingService) {
        this.tournamentRepository = tournamentRepository;
        this.trackingService = trackingService;
    }

    @Nullable
    protected Tournament getTournament(TournamentClient tournamentClient,
                                       String formattedUrl) {
        Tournament tournament = tournamentRepository.findByUrl(formattedUrl);
        if (tournament != null) {
            log.info("Tournament was found in database for url: "+ formattedUrl);
            return tournament;
        }

        log.info("No tournament was found, let's fetch it. url = "+ formattedUrl);
        tournament = fetchTournament(null, tournamentClient, formattedUrl);
        if(tournament == null) {
            log.info("Could not fetch tournament at url = "+ formattedUrl);
            return null;
        }
        tournamentRepository.save(tournament);
        trackingService.trackTournament(tournament);
        return tournament;
    }

    @Nullable
    protected abstract Tournament fetchTournament(Tournament oldTournament, String url);

    @Nullable
    protected Tournament fetchTournament(Tournament oldTournament,
                                         TournamentClient tournamentClient,
                                         String formattedUrl) {
        if(oldTournament != null && oldTournament.isDone()) {
            return oldTournament;
        }

        return tournamentClient.fetchTournament(oldTournament, formattedUrl);
    }

    @Override
    @Nonnull
    public Updates updateTournament(String url) {
        Tournament oldTournament = tournamentRepository.findByUrl(url);
        if(oldTournament.isDone()) {
            return Updates.nothingNew();
        }

        Tournament newTournament = fetchTournament(oldTournament, url);
        //We could/should probably use some comparison framework, like JaVers
        boolean firstFetch = oldTournament.getExternalId() == null;
        Collection<Update> updateList = new ArrayList<>();
        if(!compareTournaments(oldTournament, newTournament, firstFetch, updateList)) {
            log.info("Tournament has changed: saving modifications");
            tournamentRepository.save(oldTournament);
        }
        Updates updates = new Updates(updateList, oldTournament.isDone());
        return firstFetch ? Updates.nothingNew() : updates;
    }

    //Returns true if tournaments are the same
    private boolean compareTournaments(Tournament oldTournament, Tournament newTournament, boolean firstFetch, Collection<Update> updates) {
        boolean areSame = true;
        if(firstFetch) {
            oldTournament.setExternalId(newTournament.getExternalId());
            oldTournament.setName(newTournament.getName());
            oldTournament.setAddress(newTournament.getAddress());
            oldTournament.setDate(newTournament.getDate());
            areSame = false;
        }
        Collection<Phase> oldPhases = oldTournament.getPhases();
        Collection<Phase> newPhases = newTournament.getPhases();

        Map<String, Phase> oldPhasesMap = phasesToMap(oldPhases);
        for(Phase newPhase : newPhases) {
            Phase oldPhase = oldPhasesMap.get(newPhase.getExternalId());
            if(oldPhase == null) {
                oldPhases.add(newPhase);
                updates.addAll(getNewSets(newPhase.getSets()));
                continue;
            }
            areSame = comparePhases(oldPhase, newPhase, updates, oldTournament.getName()) && areSame;
        }

        if(newTournament.isDone()) {
            oldTournament.setDone(true);
            updates.add(new Update(null, "["+ oldTournament.getName() +"] Tournament is over"));
        }

        return areSame;
    }

    private Collection<Update> getNewSets(Collection<Set> sets) {
        Collection<Update> updates = new LinkedList<>();
        for(Set set : sets) {
            net.migwel.tournify.communication.commons.Set setSO = buildSetSO(set);
            updates.add(new Update(setSO, "New set found")); //TODO: Change description
        }
        return updates;
    }

    private net.migwel.tournify.communication.commons.Set buildSetSO(Set set) {
        return new net.migwel.tournify.communication.commons.Set(
                set.getExternalId(),
                buildPlayersSO(set.getPlayers()),
                buildPlayerSO(set.getWinner()),
                set.getRound(),
                set.isDone()
        );
    }

    private List<net.migwel.tournify.communication.commons.Player> buildPlayersSO(Collection<Player> players) {
        List<net.migwel.tournify.communication.commons.Player> playersSO = new ArrayList<>();
        for(Player player : players) {
            playersSO.add(buildPlayerSO(player));
        }
        return playersSO;
    }

    private net.migwel.tournify.communication.commons.Player buildPlayerSO(Player player) {
        return new net.migwel.tournify.communication.commons.Player(player.getPrefix(), player.getUsername());
    }

    //Returns true if phases are the same
    private boolean comparePhases(@Nonnull Phase oldPhase, Phase newPhase, Collection<Update> updates, String tournamentName) {
        if(newPhase == null) {
            return true; //Is this correct?
        }

        boolean areSame = true;
        if(!oldPhase.getPhaseName().equals(newPhase.getPhaseName())) {
            oldPhase.setPhaseName(newPhase.getPhaseName());
            areSame = false;
        }

        Collection<Set> oldSets = oldPhase.getSets();
        Collection<Set> newSets = newPhase.getSets();

        Map<String, Set> oldSetsMap = setsToMap(oldSets);
        for(Set newSet : newSets) {
            Set oldSet = oldSetsMap.get(newSet.getExternalId());
            if(oldSet == null) {
                oldSets.add(newSet);
                if(newSet.isDone()) {
                    String description = buildSetUpdateDescription(tournamentName, oldPhase.getPhaseName(), newSet.getRound(), newSet.getPlayers(), newSet.getWinner());
                    updates.add(new Update(buildSetSO(newSet), description));
                }
                areSame = false;
                continue;
            }

            areSame = compareSets(oldSet, newSet, updates, tournamentName, oldPhase.getPhaseName()) && areSame;
        }

        if(newPhase.isDone()) {
            oldPhase.setDone(true);
            updates.add(new Update(null, "["+ newPhase.getPhaseName() +"] Phase is over"));
        }
        return areSame;
    }

    //Returns true if sets are the same
    private boolean compareSets(@Nonnull Set oldSet, Set newSet, Collection<Update> updates, String tournamentName, String phaseName) {
        if(newSet == null || newSet.getWinner() == null) {
            return true;
        }

        Collection<Player> oldPlayers = oldSet.getPlayers();
        for (Player player : newSet.getPlayers()) {
            if(!oldPlayers.contains(player)) {
                oldPlayers.add(player);
            }
        }

        if(!newSet.getWinner().equals(oldSet.getWinner())) {
            oldSet.setWinner(newSet.getWinner());
            oldSet.setDone(true);
            String description = buildSetUpdateDescription(tournamentName, phaseName, oldSet.getRound(), oldSet.getPlayers(), newSet.getWinner());
            updates.add(new Update(buildSetSO(oldSet), description));
            return false;
        }

        return true;
    }

    private String buildSetUpdateDescription(String tournamentName, String phaseName, String round, Collection<Player> players, Player winner) {
        return "Tournament [" +
                tournamentName +
                "] - Phase [" +
                phaseName +
                "] - Set [" +
                round +
                "] - " +
                players.stream().map(Player::getDisplayUsername).collect(Collectors.joining(" vs ")) +
                " - Winner is " +
                winner.getDisplayUsername();
    }

    private Map<String,Set> setsToMap(Collection<Set> sets) {
        Map<String, Set> setsMap = new HashMap<>();
        for(Set set : sets) {
            setsMap.put(set.getExternalId(), set);
        }
        return setsMap;
    }

    private Map<String,Phase> phasesToMap(Collection<Phase> phases) {
        Map<String, Phase> phasesMap = new HashMap<>();
        for(Phase phase : phases) {
            phasesMap.put(phase.getExternalId(), phase); //TODO: Probably smarter to use hashcode
        }

        return phasesMap;
    }

}
