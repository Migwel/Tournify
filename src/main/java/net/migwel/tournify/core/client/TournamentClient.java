package net.migwel.tournify.core.client;

import net.migwel.tournify.core.data.Player;
import net.migwel.tournify.core.data.Tournament;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface TournamentClient {

    @Nullable
    Tournament fetchTournament(Tournament oldTournament, String url);

    @Nonnull
    List<Player> getParticipants(String url);
}
