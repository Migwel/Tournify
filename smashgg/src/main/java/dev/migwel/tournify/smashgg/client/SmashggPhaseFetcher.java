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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SmashggPhaseFetcher {
    private final TournamentRepository tournamentRepository;
    private final SmashggPhaseGroupFetcher smashggPhaseGroupFetcher;

    public SmashggPhaseFetcher(TournamentRepository tournamentRepository, SmashggPhaseGroupFetcher smashggPhaseGroupFetcher) {
        this.tournamentRepository = tournamentRepository;
        this.smashggPhaseGroupFetcher = smashggPhaseGroupFetcher;
    }

    Collection<Phase> fetchPhases(@Nonnull String formattedUrl, SmashggEvent smashggEvent) throws FetchException {
        Collection<Phase> existingPhases = getExistingPhases(formattedUrl);
        Map<Long, Collection<SmashggPhaseGroup>> phaseGroupsPerPhase = getPhaseGroupsPerPhase(smashggEvent.getPhaseGroups());
        return getPhases(existingPhases, smashggEvent.getPhases(), phaseGroupsPerPhase);
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
                                        Map<Long, Collection<SmashggPhaseGroup>> smashggGroups) throws FetchException {
        Collection<Phase> tournamentPhases = new ArrayList<>();
        for(SmashggPhase smashGgPhase : smashggPhases) {
            Collection<Set> phaseSets = new ArrayList<>();
            if(phaseIsDone(existingPhases, smashGgPhase)) {
                continue;
            }
            boolean phaseDone = true;
            for(SmashggPhaseGroup smashGgGroup : smashggGroups.get(smashGgPhase.getId())) {
                Collection<Set> sets = fetchSets(smashGgGroup.getId());
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
    private Collection<Set> fetchSets(long phaseGroupId) throws FetchException {
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

            sets.addAll(getSets(phaseGroup.getDisplayIdentifier(), phaseGroup.getPaginatedSets().getNodes()));

        } while (phaseGroup.getPaginatedSets().getPageInfo().getTotalPages() != page);

        return sets;
    }



    @Nonnull
    private Collection<Set> getSets(String phaseGroupName, Collection<SmashggNode> nodes) {
        Collection<Set> sets = new ArrayList<>();
        for(SmashggNode node : nodes) {
            if(node == null || node.getId().startsWith("preview")) {
                continue;
            }
            long winnerId = node.getWinnerId();
            java.util.Set<Player> winners = null;
            java.util.Set<Player> players = new HashSet<>();
            for(SmashggSlot slot : node.getSlots()) {
                if(slot == null) {
                    continue;
                }
                SmashggEntrant entrant = slot.getEntrant();
                if(entrant == null) {
                    continue;
                }

                java.util.Set<Player> slotPlayers = buildPlayerList(entrant);
                players.addAll(slotPlayers);
                if(entrant.getId() == winnerId) {
                    winners = slotPlayers;
                }
            }
            sets.add(new Set(node.getId(), players, winners, phaseGroupName+ " - "+ node.getFullRoundText(), winners != null && !winners.isEmpty()));
        }

        return sets;
    }

    private java.util.Set<Player> buildPlayerList(SmashggEntrant entrant) {
        java.util.Set<Player> players = new HashSet<>();
        for(Participant participant : entrant.getParticipants()) {
            players.add(new Player(participant.getPrefix(), participant.getGamerTag()));
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