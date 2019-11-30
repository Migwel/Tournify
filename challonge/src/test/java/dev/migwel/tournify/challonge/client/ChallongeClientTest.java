package dev.migwel.tournify.challonge.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.migwel.tournify.challonge.config.ChallongeConfiguration;
import dev.migwel.tournify.challonge.impl.ChallongeUrlService;
import dev.migwel.tournify.communication.commons.Player;
import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.core.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChallongeClientTest {


    private ChallongeConfiguration challongeConfiguration = new ChallongeConfiguration();
    private ChallongeUrlService challongeUrlService = new ChallongeUrlService();
    private HttpClient httpClient =  mock(HttpClient.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    private ChallongeFetcher challongeFetcher = new ChallongeFetcher(challongeConfiguration, challongeUrlService, httpClient, objectMapper);
    private TournamentClient tournamentClient = new ChallongeClient(challongeFetcher, challongeUrlService);

    @BeforeEach
    void before() throws URISyntaxException, IOException {
        challongeConfiguration.setUsername("username");
        challongeConfiguration.setApiToken("password");
        challongeConfiguration.setRetryNumber(3);

        Path path = Paths.get(this.getClass().getClassLoader().getResource("ParticipantResponse.json").toURI());
        Stream<String> lines = Files.lines(path);
        String json = lines.collect(Collectors.joining("\n"));
        lines.close();
        when(httpClient.get(anyString(), anyCollection())).thenReturn(json);
    }

    @Test
    void testGetParticipants() throws FetchException {
        String url = "https://api.challonge.com/v1/tournaments/xgt2019nov";
        Set<Player> players = tournamentClient.getParticipants(url);
        assertTrue(players.contains(new Player("Adrien")));

    }
}
