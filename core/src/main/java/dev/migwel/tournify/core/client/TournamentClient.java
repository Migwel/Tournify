package dev.migwel.tournify.core.client;

import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.data.Tournament;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public interface TournamentClient {

    @Nullable
    Tournament fetchTournament(Tournament oldTournament, String url);

    @Nonnull
    Collection<Player> getParticipants(String url);
}
