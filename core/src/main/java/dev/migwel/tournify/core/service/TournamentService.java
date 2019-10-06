package dev.migwel.tournify.core.service;

import dev.migwel.tournify.communication.commons.Updates;
import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.data.Tournament;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public interface TournamentService {

    @Nonnull
    String normalizeUrl(String tournamentUrl);

    @Nonnull
    Updates updateTournament(String url);

    @Nullable
    Tournament getTournament(String url);

    @Nonnull
    Collection<Player> getParticipants(String url);
}
