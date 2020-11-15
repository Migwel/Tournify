package dev.migwel.tournify.chesscom.client;

import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.exception.FetchException;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Set;

@Component("ChesscomTournamentClient")
public class ChesscomTournamentClient implements TournamentClient {

    @Override
    @Nonnull
    public Tournament fetchTournament(String url) throws FetchException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Nonnull
    public Set<Player> getParticipants(String url) throws FetchException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
