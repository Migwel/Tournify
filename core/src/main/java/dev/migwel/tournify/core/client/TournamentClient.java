package dev.migwel.tournify.core.client;

import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.exception.FetchException;

import javax.annotation.Nonnull;
import java.util.Set;

public interface TournamentClient {

    @Nonnull
    Tournament fetchTournament(String url) throws FetchException;

    @Nonnull
    Set<Player> getParticipants(String url) throws FetchException;

    /*
     * Method to quickly verify that a tournament exists.
     * This may be redundant with fetchTournament but the idea is to have a fast method to check if a tournament exists
     * without the need to fully fetch it if it's the case
     */
    boolean tournamentExists(String url);
}
