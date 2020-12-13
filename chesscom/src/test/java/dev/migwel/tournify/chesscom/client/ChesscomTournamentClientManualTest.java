package dev.migwel.tournify.chesscom.client;

import dev.migwel.chesscomjava.api.services.FactoryCreator;
import dev.migwel.chesscomjava.api.services.TournamentService;
import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.data.Phase;
import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.exception.FetchException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChesscomTournamentClientManualTest {


    private final TournamentService tournamentService = FactoryCreator.getServiceFactory().getTournamentService();
    private final TournamentClient tournamentClient = new ChesscomTournamentClient(tournamentService);

    @Test
    void testGetParticipants() throws FetchException {
        String url = "https://api.chess.com/pub/tournament/-titled-tuesday-blitz-1692727";
        Set<Player> players = tournamentClient.getParticipants(url);
        assertTrue(players.contains(new Player( "lyonbeast")));
    }

    @Test
    void testFetchTournament() throws FetchException {
        String url = "https://api.chess.com/pub/tournament/-titled-tuesday-blitz-1692727";
        dev.migwel.tournify.core.data.Tournament tournament = tournamentClient.fetchTournament(url);
        assertEquals(1, tournament.getPhases().size());
        Phase phase = tournament.getPhases().iterator().next();
        assertEquals(3055, phase.getSets().size());
    }

    @Test
    void tournamentDoesntExist() {
        String url = "https://api.chess.com/pub/tournament/tournamentdoesntexists";
        assertThrows(FetchException.class, () -> tournamentClient.fetchTournament(url));
    }
}