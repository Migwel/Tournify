package dev.migwel.tournify.core.service;

import dev.migwel.tournify.communication.commons.Update;
import dev.migwel.tournify.communication.commons.Updates;
import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.data.Phase;
import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.data.Set;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.core.store.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractTournamentService implements TournamentService {

    private static final Logger log = LoggerFactory.getLogger(AbstractTournamentService.class);

    private final TournamentRepository tournamentRepository;

    private final TrackingService trackingService;

    private final UrlService urlService;

    private final TournamentClient tournamentClient;

    public AbstractTournamentService(TournamentRepository tournamentRepository, TrackingService trackingService, UrlService urlService, TournamentClient tournamentClient) {
        this.tournamentRepository = tournamentRepository;
        this.trackingService = trackingService;
        this.urlService = urlService;
        this.tournamentClient = tournamentClient;
    }

    @Override
    @Nonnull
    public String normalizeUrl(String tournamentUrl) {
        return urlService.normalizeUrl(tournamentUrl);
    }

    @Nonnull
    @Override
    public Collection<Player> getParticipants(String url) {
        String formattedUrl = normalizeUrl(url);
        try {
            return tournamentClient.getParticipants(formattedUrl);
        } catch (FetchException e) {
            log.warn("Could not get participants from url: "+ formattedUrl, e);
            return Collections.emptyList();
        }
    }

    @Nullable
    public Tournament getTournament(String url) {
        String formattedUrl = urlService.normalizeUrl(url);
        Tournament tournament = tournamentRepository.findByUrl(formattedUrl);
        if (tournament != null) {
            log.info("Tournament was found in database for url: "+ formattedUrl);
            return tournament;
        }

        log.info("No tournament was found, let's fetch it. url = "+ formattedUrl);
        try {
            tournament = fetchTournament(null, formattedUrl);
        } catch (FetchException e) {
            log.info("Could not fetch tournament at url = "+ formattedUrl, e);
            return null;
        }
        tournamentRepository.save(tournament);
        trackingService.trackTournament(tournament);
        return tournament;
    }

    @Nonnull
    protected Tournament fetchTournament(Tournament oldTournament, String formattedUrl) throws FetchException {
        if(oldTournament != null && oldTournament.isDone()) {
            return oldTournament;
        }

        return tournamentClient.fetchTournament(formattedUrl);
    }

    @Override
    @Nonnull
    public Updates updateTournament(String formattedUrl) {
        Tournament oldTournament = tournamentRepository.findByUrl(formattedUrl);
        if(oldTournament.isDone()) {
            return Updates.nothingNew(true);
        }

        Tournament newTournament;
        try {
            newTournament = fetchTournament(oldTournament, formattedUrl);
        } catch (FetchException e) {
            log.warn("Could not fetch tournament at url: "+ formattedUrl, e);
            return Updates.nothingNew();
        }

        //We could/should probably use some comparison framework, like JaVers
        TournamentChanges tournamentChanges = getTournamentChanges(oldTournament, newTournament);
        if(tournamentChanges.hasChanged) {
            log.info("Tournament has changed: saving modifications");
            tournamentRepository.save(oldTournament);
        }

        return new Updates(tournamentChanges.updates, newTournament.isDone());
    }

    private TournamentChanges getTournamentChanges(Tournament oldTournament, Tournament newTournament) {
        boolean firstFetch = oldTournament.getExternalId() == null;
        if(firstFetch) {
            compareTournamentsFirstTime(oldTournament, newTournament);
            return TournamentChanges.firstUpdate();
        }
        return compareTournaments(oldTournament, newTournament);
    }

    private TournamentChanges compareTournamentsFirstTime(Tournament oldTournament, Tournament newTournament) {
        fillGeneralTournamentInfo(oldTournament, newTournament);
        return compareTournaments(oldTournament, newTournament);
    }

    private void fillGeneralTournamentInfo(Tournament oldTournament, Tournament newTournament) {
        oldTournament.setExternalId(newTournament.getExternalId());
        oldTournament.setName(newTournament.getName());
        oldTournament.setAddress(newTournament.getAddress());
        oldTournament.setDate(newTournament.getDate());
    }

    //Returns true if tournaments are the same
    private TournamentChanges compareTournaments(Tournament oldTournament, Tournament newTournament) {
        boolean areSame = true;
        List<Update> updates = new ArrayList<>();

        Collection<Phase> oldPhases = oldTournament.getPhases();
        Collection<Phase> newPhases = newTournament.getPhases();

        Map<String, Phase> oldPhasesMap = phasesToMap(oldPhases);
        for(Phase newPhase : newPhases) {
            Phase oldPhase = oldPhasesMap.get(newPhase.getExternalId());
            if(oldPhase == null) {
                oldPhases.add(newPhase);
                updates.addAll(getNewSets(newPhase.getSets(), newTournament.getName(), newPhase.getName()));
                continue;
            }
            areSame = comparePhases(oldPhase, newPhase, updates, oldTournament.getName()) && areSame;
        }

        if(newTournament.isDone()) {
            oldTournament.setDone(true);
            updates.add(new Update(null, "["+ oldTournament.getName() +"] Tournament is over"));
        }

        return new TournamentChanges(!areSame, updates);
    }

    private Collection<Update> getNewSets(Collection<Set> sets, String tournamentName, String phaseName) {
        Collection<Update> updates = new LinkedList<>();
        for(Set set : sets) {
            dev.migwel.tournify.communication.commons.Set setSO = buildSetSO(tournamentName, phaseName, set);
            updates.add(new Update(setSO, "New set found")); //TODO: Change description
        }
        return updates;
    }

    private dev.migwel.tournify.communication.commons.Set buildSetSO(String tournamentName, String phaseName, Set set) {
        return new dev.migwel.tournify.communication.commons.Set(
                set.getExternalId(),
                tournamentName,
                phaseName,
                buildPlayersSO(set.getPlayers()),
                buildPlayersSO(set.getWinners()),
                set.getName(),
                set.isDone()
        );
    }

    private List<dev.migwel.tournify.communication.commons.Player> buildPlayersSO(Collection<Player> players) {
        List<dev.migwel.tournify.communication.commons.Player> playersSO = new ArrayList<>();
        for(Player player : players) {
            playersSO.add(buildPlayerSO(player));
        }
        return playersSO;
    }

    private dev.migwel.tournify.communication.commons.Player buildPlayerSO(Player player) {
        if(player == null) {
            return null;
        }
        return new dev.migwel.tournify.communication.commons.Player(player.getPrefix(), player.getUsername());
    }

    //Returns true if phases are the same
    private boolean comparePhases(@Nonnull Phase oldPhase, Phase newPhase, Collection<Update> updates, String tournamentName) {
        if(newPhase == null) {
            return true; //Is this correct?
        }

        boolean areSame = true;
        if(!oldPhase.getName().equals(newPhase.getName())) {
            oldPhase.setName(newPhase.getName());
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
                    String description = buildSetUpdateDescription(tournamentName, oldPhase.getName(), newSet.getName(), newSet.getPlayers(), newSet.getWinners());
                    updates.add(new Update(buildSetSO(tournamentName, oldPhase.getName(), newSet), description));
                }
                areSame = false;
                continue;
            }

            areSame = compareSets(oldSet, newSet, updates, tournamentName, oldPhase.getName()) && areSame;
        }

        if(newPhase.isDone()) {
            oldPhase.setDone(true);
            updates.add(new Update(null, "["+ newPhase.getName() +"] Phase is over"));
        }
        return areSame;
    }

    //Returns true if sets are the same
    private boolean compareSets(@Nonnull Set oldSet, Set newSet, Collection<Update> updates, String tournamentName, String phaseName) {
        if(newSet == null || newSet.getWinners() == null) {
            return true;
        }

        Collection<Player> oldPlayers = oldSet.getPlayers();
        for (Player player : newSet.getPlayers()) {
            if(!oldPlayers.contains(player)) {
                oldPlayers.add(player);
            }
        }

        if(!newSet.getWinners().equals(oldSet.getWinners())) {
            oldSet.setWinners(newSet.getWinners());
            oldSet.setDone(true);
            String description = buildSetUpdateDescription(tournamentName, phaseName, oldSet.getName(), oldSet.getPlayers(), newSet.getWinners());
            updates.add(new Update(buildSetSO(tournamentName, phaseName, oldSet), description));
            return false;
        }

        return true;
    }

    private String buildSetUpdateDescription(String tournamentName, String phaseName, String round, Collection<Player> players, Collection<Player> winners) {
        return "Tournament [" +
                tournamentName +
                "] - Phase [" +
                phaseName +
                "] - Set [" +
                round +
                "] - " +
                players.stream().map(Player::getDisplayUsername).collect(Collectors.joining(" vs ")) +
                " - Winner is " +
                winners.stream().map(Player::getDisplayUsername).collect(Collectors.joining(" vs "));
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

    static class TournamentChanges {
        private boolean hasChanged;
        private List<Update> updates;

        public TournamentChanges(boolean hasChanged, List<Update> updates) {
            this.hasChanged = hasChanged;
            this.updates = updates;
        }

        public static TournamentChanges firstUpdate() {
            return new TournamentChanges(true, Collections.emptyList());
        }
    }

}
