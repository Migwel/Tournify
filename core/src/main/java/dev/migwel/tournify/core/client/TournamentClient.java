package dev.migwel.tournify.core.client;

import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.exception.FetchException;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface TournamentClient {

    @Nonnull
    Tournament fetchTournament(String url) throws FetchException;

    @Nonnull
    Collection<Player> getParticipants(String url) throws FetchException;
}
