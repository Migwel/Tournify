package dev.migwel.tournify.core.converter;

import dev.migwel.tournify.communication.commons.Address;
import dev.migwel.tournify.communication.commons.Phase;
import dev.migwel.tournify.communication.commons.Player;
import dev.migwel.tournify.communication.commons.Set;
import dev.migwel.tournify.communication.commons.Tournament;
import dev.migwel.tournify.util.CollectionsUtil;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public final class DataToServiceConverter {
    public DataToServiceConverter() {
        //util
    }

    public static Tournament convertTournament(@Nonnull dev.migwel.tournify.core.data.Tournament tournamentData) {
        Collection<Phase> phases = new HashSet<>();
        if (CollectionsUtil.hasItems(tournamentData.getPhases())) {
            tournamentData.getPhases()
                          .stream()
                          .filter(Objects::nonNull)
                          .forEach(e -> phases.add(convertPhase(e)));
        }
        return new Tournament(tournamentData.getExternalId(),
                              phases,
                              tournamentData.getName(),
                              tournamentData.getGameType().getName(),
                              convertAddress(tournamentData.getAddress()),
                              tournamentData.getUrl(),
                              tournamentData.getDate(),
                              tournamentData.isDone());
    }

    public static Phase convertPhase(@Nonnull dev.migwel.tournify.core.data.Phase phaseData) {
        Collection<Set> sets = new HashSet<>();
        if (CollectionsUtil.hasItems(phaseData.getSets())) {
            phaseData.getSets()
                     .stream()
                     .filter(Objects::nonNull)
                     .forEach(e -> sets.add(convertSet(null, null, e)));
        }
        return new Phase(phaseData.getExternalId(),
                         sets,
                         phaseData.getName(),
                         phaseData.isDone());

    }

    public static Set convertSet(String tournamentName, String phaseName, @Nonnull dev.migwel.tournify.core.data.Set setData) {
        Collection<Player> players = convertPlayers(setData.getPlayers());
        Collection<Player> winners = convertPlayers(setData.getWinners());
        return new Set(setData.getExternalId(),
                       tournamentName,
                       phaseName,
                       players,
                       winners,
                       setData.getName(),
                       setData.isDone());
    }

    private static Collection<Player> convertPlayers(@CheckForNull Collection<dev.migwel.tournify.core.data.Player> playersData) {
        Collection<Player> players = new HashSet<>();
        if (CollectionsUtil.hasItems(playersData)) {
            playersData.stream()
                       .filter(Objects::nonNull)
                       .forEach(e -> players.add(convertPlayer(e)));
        }
        return players;
    }

    public static Player convertPlayer(@Nonnull dev.migwel.tournify.core.data.Player playerData) {
        return new Player(playerData.getPrefix(),
                          playerData.getUsername(),
                          playerData.getExternalId());
    }

    public static Address convertAddress(@Nonnull dev.migwel.tournify.core.data.Address address) {
        return new Address(address.getCity(),
                           address.getState(),
                           address.getStreet(),
                           address.getHouseNumber(),
                           address.getCountry());
    }
}
