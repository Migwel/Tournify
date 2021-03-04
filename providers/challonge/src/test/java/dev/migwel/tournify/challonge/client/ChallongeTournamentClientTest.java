package dev.migwel.tournify.challonge.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.migwel.tournify.challonge.config.ChallongeConfiguration;
import dev.migwel.tournify.challonge.impl.ChallongeUrlService;
import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.data.Phase;
import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.core.http.HttpClient;
import dev.migwel.tournify.util.FileUtil;
import org.junit.jupiter.api.Assertions;
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

public class ChallongeTournamentClientTest {


    private ChallongeConfiguration challongeConfiguration = new ChallongeConfiguration();
    private ChallongeUrlService challongeUrlService = new ChallongeUrlService();
    private HttpClient httpClient =  mock(HttpClient.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    private ChallongeFetcher challongeFetcher = new ChallongeFetcher(challongeConfiguration, challongeUrlService, httpClient, objectMapper);
    private ChallongeTournamentFetcher challongeTournamentFetcher = new ChallongeTournamentFetcher(challongeUrlService, challongeFetcher);
    private ChallongeMatchesFetcher challongeMatchesFetcher = new ChallongeMatchesFetcher(challongeUrlService, challongeFetcher);
    private TournamentClient tournamentClient = new ChallongeTournamentClient(challongeFetcher, challongeUrlService, challongeTournamentFetcher, challongeMatchesFetcher);

    @BeforeEach
    void before() throws URISyntaxException, IOException {
        challongeConfiguration.setUsername("username");
        challongeConfiguration.setApiToken("password");
        challongeConfiguration.setRetryNumber(3);

        String participantsJson = FileUtil.loadFile("ParticipantResponse.json");
        String tournamentJson = FileUtil.loadFile("TournamentResponse.json");
        String matchesJson = FileUtil.loadFile("MatchesResponse.json");
        String tournamentDoesntExistJson = FileUtil.loadFile("TournamentDoesntExistResponse.json");
        when(httpClient.get(contains("participants"), anyCollection())).thenReturn(participantsJson);
        when(httpClient.get(contains("xgt2019nov.json"), anyCollection())).thenReturn(tournamentJson);
        when(httpClient.get(contains("matches.json"), anyCollection())).thenReturn(matchesJson);
        when(httpClient.get(contains("tournamentdoesntexist"), anyCollection())).thenReturn(tournamentDoesntExistJson);
    }

    @Test
    void testGetParticipants() throws FetchException {
        String url = "https://api.challonge.com/v1/tournaments/xgt2019nov";
        Set<Player> players = tournamentClient.getParticipants(url);
        assertTrue(players.contains(new Player("Adrien")));
    }

    @Test
    void testParticipantsAreOrdered() throws FetchException {
        String url = "https://api.challonge.com/v1/tournaments/xgt2019nov";
        Set<Player> players = tournamentClient.getParticipants(url);
        Player previousPlayer = null;
        for (Player player : players) {
            if (previousPlayer == null) {
                previousPlayer = player;
                continue;
            }
            assertTrue(player.compareTo(previousPlayer) > 0);
            previousPlayer = player;
        }
    }

    @Test
    void testGetTournament() throws FetchException {
        String url = "https://api.challonge.com/v1/tournaments/xgt2019nov";
        Tournament tournament = tournamentClient.fetchTournament(url);
        Assertions.assertEquals("Super Smash Bros. Ultimate", tournament.getGameType().getName());
        Assertions.assertEquals(1, tournament.getPhases().size());
        Phase phase = tournament.getPhases().iterator().next();
        Assertions.assertEquals(57, phase.getSets().size());
    }

    @Test
    void exceptionWhenFetchingTournamentThatDoesntExist() {
        String url = "https://api.challonge.com/v1/tournaments/tournamentdoesntexist";
        assertThrows(FetchException.class, () -> tournamentClient.fetchTournament(url));
    }

    @Test
    void tournamentExistsMethodWhenTournamentExists() {
        String url = "https://api.challonge.com/v1/tournaments/xgt2019nov";
        assertTrue(tournamentClient.tournamentExists(url));
    }

    @Test
    void tournamentExistsMethodWhenTournamentDoesntExist() {
        String url = "https://api.challonge.com/v1/tournaments/tournamentdoesntexist";
        assertFalse(tournamentClient.tournamentExists(url));
    }
}
