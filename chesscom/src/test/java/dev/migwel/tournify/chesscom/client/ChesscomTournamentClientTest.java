package dev.migwel.tournify.chesscom.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.migwel.chesscomjava.api.data.tournament.Tournament;
import dev.migwel.chesscomjava.api.services.FactoryCreator;
import dev.migwel.chesscomjava.api.services.TournamentService;
import dev.migwel.chesscomjava.implementation.TournamentServiceImpl;
import dev.migwel.tournify.chesscom.configuration.ChesscomConfiguration;
import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChesscomTournamentClientTest {


    private final TournamentService tournamentService = mock(TournamentService.class);
    private final TournamentClient tournamentClient = new ChesscomTournamentClient(tournamentService);

    @BeforeEach
    void before() throws URISyntaxException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String tournamentJson = FileUtil.loadFile("Tournament.json");
        Tournament tournament = objectMapper.readValue(tournamentJson, Tournament.class);
        when(tournamentService.getTournament(contains("-titled-tuesday-blitz-1692727"))).thenReturn(tournament);
    }


    @Test
    void testGetParticipants() throws FetchException {
        String url = "https://api.chess.com/pub/tournament/-titled-tuesday-blitz-1692727";
        Set<Player> players = tournamentClient.getParticipants(url);
        assertTrue(players.contains(new Player( "lyonbeast")));
    }
}