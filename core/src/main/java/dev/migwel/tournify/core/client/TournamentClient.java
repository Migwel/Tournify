package dev.migwel.tournify.core.client;

import dev.migwel.tournify.communication.commons.Player;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.exception.FetchException;

import javax.annotation.Nonnull;
import java.util.Set;

public interface TournamentClient {

    @Nonnull
    Tournament fetchTournament(String url) throws FetchException;

    @Nonnull
    Set<Player> getParticipants(String url) throws FetchException;
}
