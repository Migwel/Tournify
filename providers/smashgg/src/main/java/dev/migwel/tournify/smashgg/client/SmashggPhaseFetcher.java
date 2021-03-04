package dev.migwel.tournify.smashgg.client;

import dev.migwel.tournify.core.data.Phase;
import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.data.Set;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.core.store.TournamentRepository;
import dev.migwel.tournify.smashgg.data.Participant;
import dev.migwel.tournify.smashgg.response.SmashggEntrant;
import dev.migwel.tournify.smashgg.response.SmashggEvent;
import dev.migwel.tournify.smashgg.response.SmashggNode;
import dev.migwel.tournify.smashgg.response.SmashggPhase;
import dev.migwel.tournify.smashgg.response.SmashggPhaseGroup;
import dev.migwel.tournify.smashgg.response.SmashggSlot;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class SmashggPhaseFetcher {
    private final TournamentRepository tournamentRepository;
    private final SmashggPhaseGroupFetcher smashggPhaseGroupFetcher;

    public SmashggPhaseFetcher(TournamentRepository tournamentRepository, SmashggPhaseGroupFetcher smashggPhaseGroupFetcher) {
        this.tournamentRepository = tournamentRepository;
        this.smashggPhaseGroupFetcher = smashggPhaseGroupFetcher;
    }

    Collection<Phase> fetchPhases(@Nonnull String formattedUrl, SmashggEvent smashggEvent, Collection<Player> players) throws FetchException {
        Collection<Phase> existingPhases = getExistingPhases(formattedUrl);
        Map<Long, Collection<SmashggPhaseGroup>> phaseGroupsPerPhase = getPhaseGroupsPerPhase(smashggEvent.getPhaseGroups());
        return getPhases(existingPhases, smashggEvent.getPhases(), phaseGroupsPerPhase, players);
    }

    private Collection<Phase> getExistingPhases(String formattedUrl) {
        Tournament oldTournament = tournamentRepository.findByUrl(formattedUrl);
        if (oldTournament == null) {
            return Collections.emptyList();
        }
        return oldTournament.getPhases();
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
                                        Map<Long, Collection<SmashggPhaseGroup>> smashggGroups,
                                        Collection<Player> players) throws FetchException {
        Collection<Phase> tournamentPhases = new ArrayList<>();
        for(SmashggPhase smashGgPhase : smashggPhases) {
            Collection<Set> phaseSets = new ArrayList<>();
            if(phaseIsDone(existingPhases, smashGgPhase)) {
                continue;
            }
            boolean phaseDone = true;
            for(SmashggPhaseGroup smashGgGroup : smashggGroups.get(smashGgPhase.getId())) {
                Collection<Set> sets = fetchSets(smashGgGroup.getId(), players);
                if(sets.isEmpty() || !sets.stream().allMatch(Set::isDone)) {
                    phaseDone = false;
                }
                phaseSets.addAll(sets);
            }
            tournamentPhases.add(new Phase(String.valueOf(smashGgPhase.getId()), phaseSets, smashGgPhase.getName(), phaseDone));
        }
        return tournamentPhases;
    }

    @Nonnull
    private Collection<Set> fetchSets(long phaseGroupId, Collection<Player> players) throws FetchException {
        Collection<Set> sets = new ArrayList<>();
        SmashggPhaseGroup phaseGroup;
        long page = 0;
        do {
            page++;
            phaseGroup = smashggPhaseGroupFetcher.fetchPhaseGroup(phaseGroupId, page);
            if(phaseGroup.getPaginatedSets() == null ||
               phaseGroup.getPaginatedSets().getPageInfo() == null ||
               phaseGroup.getPaginatedSets().getPageInfo().getTotalPages() == 0) {
                break;
            }

            sets.addAll(getSets(phaseGroup.getDisplayIdentifier(), phaseGroup.getPaginatedSets().getNodes(), players));

        } while (phaseGroup.getPaginatedSets().getPageInfo().getTotalPages() != page);

        return sets;
    }



    @Nonnull
    private Collection<Set> getSets(String phaseGroupName, Collection<SmashggNode> nodes, Collection<Player> players) {
        Collection<Set> sets = new ArrayList<>();
        for(SmashggNode node : nodes) {
            if(node == null || node.getId().startsWith("preview")) {
                continue;
            }
            long winnerId = node.getWinnerId();
            java.util.Set<Player> setWinners = null;
            java.util.Set<Player> setPlayers = new HashSet<>();
            for(SmashggSlot slot : node.getSlots()) {
                if(slot == null) {
                    continue;
                }
                SmashggEntrant entrant = slot.getEntrant();
                if(entrant == null) {
                    continue;
                }

                java.util.Set<Player> slotPlayers = buildPlayerList(entrant, players);
                setPlayers.addAll(slotPlayers);
                if(entrant.getId() == winnerId) {
                    setWinners = slotPlayers;
                }
            }
            sets.add(new Set(node.getId(), setPlayers, setWinners, phaseGroupName+ " - "+ node.getFullRoundText(), setWinners != null && !setWinners.isEmpty()));
        }

        return sets;
    }

    private java.util.Set<Player> buildPlayerList(SmashggEntrant entrant, Collection<Player> tournamentPlayers) {
        java.util.Set<Player> players = new HashSet<>();
        for(Participant participant : entrant.getParticipants()) {
            final Player tmpPlayer = new Player(participant.getPrefix(), participant.getGamerTag());
            Player setPlayer;
            if (tournamentPlayers.contains(tmpPlayer)) {
                setPlayer = tournamentPlayers.stream().filter(p -> p.equals(tmpPlayer)).findAny().orElseThrow(() -> new NoSuchElementException(tmpPlayer.toString()));
            }
            else {
                setPlayer = tmpPlayer;
                tournamentPlayers.add(tmpPlayer);
            }
            players.add(setPlayer);
        }
        return players;
    }

    private boolean phaseIsDone(Collection<Phase> existingPhases, SmashggPhase smashGgPhase) {
        for(Phase existingPhase : existingPhases) {
            if(existingPhase.getExternalId() != null && existingPhase.getExternalId().equals(String.valueOf(smashGgPhase.getId()))) {
                return existingPhase.isDone();
            }
        }
        return false;
    }
}