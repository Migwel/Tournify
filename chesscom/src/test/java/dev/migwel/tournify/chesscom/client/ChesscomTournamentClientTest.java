package dev.migwel.tournify.chesscom.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.migwel.chesscomjava.api.data.tournament.RoundGroup;
import dev.migwel.chesscomjava.api.data.tournament.Tournament;
import dev.migwel.chesscomjava.api.data.tournament.TournamentRound;
import dev.migwel.chesscomjava.api.services.FactoryCreator;
import dev.migwel.chesscomjava.api.services.TournamentService;
import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.data.Phase;
import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

class ChesscomTournamentClientTest {


    private final TournamentService tournamentService = mock(FactoryCreator.getServiceFactory().getTournamentService().getClass());
    private final TournamentClient tournamentClient = new ChesscomTournamentClient(tournamentService);

    @BeforeEach
    void before() throws URISyntaxException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String tournamentJson = FileUtil.loadFile("Tournament.json");
        String roundJson = FileUtil.loadFile("TournamentRound.json");
        String groupJson = FileUtil.loadFile("RoundGroup.json");
        Tournament tournament = objectMapper.readValue(tournamentJson, Tournament.class);
        TournamentRound round = objectMapper.readValue(roundJson, TournamentRound.class);
        RoundGroup group = objectMapper.readValue(groupJson, RoundGroup.class);
        when(tournamentService.getTournament(contains("-titled-tuesday-blitz-1692727"))).thenReturn(tournament);
        when(tournamentService.getRound(contains("-titled-tuesday-blitz-1692727"), contains("11"))).thenReturn(round);
        when(tournamentService.getGroup(contains("-titled-tuesday-blitz-1692727"), contains("11"), contains("1"))).thenReturn(group);
        when(tournamentService.getTournament(contains("tournamentdoesntexists"))).thenReturn(null);
    }


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