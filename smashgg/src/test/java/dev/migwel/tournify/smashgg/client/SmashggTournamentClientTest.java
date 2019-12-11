package dev.migwel.tournify.smashgg.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.data.Phase;
import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.core.http.HttpClient;
import dev.migwel.tournify.core.store.TournamentRepository;
import dev.migwel.tournify.smashgg.config.SmashggConfiguration;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SmashggTournamentClientTest {

    private SmashggConfiguration smashggConfiguration = new SmashggConfiguration();
    private HttpClient httpClient = mock(HttpClient.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    private SmashggFetcher smashggFetcher = new SmashggFetcher(smashggConfiguration, httpClient, objectMapper);
    private TournamentRepository tournamentRepository = mock(TournamentRepository.class);
    private SmashggPhaseGroupFetcher smashggPhaseGroupFetcher = new SmashggPhaseGroupFetcher(smashggConfiguration, smashggFetcher);
    private SmashggPhaseFetcher smashggPhaseFetcher = new SmashggPhaseFetcher(tournamentRepository, smashggPhaseGroupFetcher);
    private SmashggEventFetcher smashggEventFetcher = new SmashggEventFetcher(smashggFetcher);
    private SmashggParticipantsFetcher smashggParticipantsFetcher = new SmashggParticipantsFetcher(smashggConfiguration, smashggFetcher);
    private TournamentClient tournamentClient = new SmashggTournamentClient(smashggEventFetcher, smashggPhaseFetcher, smashggParticipantsFetcher);

    @BeforeEach
    void before() throws URISyntaxException, IOException {
        smashggConfiguration.setApiToken("token");
        smashggConfiguration.setSetsPerPage(100);
        smashggConfiguration.setParticipantsPerPage(100);
        smashggConfiguration.setRetryNumber(8);
        smashggConfiguration.setApiUrl("https://api.smash.gg/gql/alpha");

        when(tournamentRepository.findByUrl(anyString())).thenReturn(null);

        String participantsJson = loadJson("ParticipantsResponse.json");
        String eventJson = loadJson("EventResponse.json");
        String phaseGroupJson = loadJson("PhaseGroupResponse.json");
        when(httpClient.postRequest(contains("entrants("), anyString(), anyCollection())).thenReturn(participantsJson);
        when(httpClient.postRequest(contains("tournament {"), anyString(), anyCollection())).thenReturn(eventJson);
        when(httpClient.postRequest(contains("query phaseGroup("), anyString(), anyCollection())).thenReturn(phaseGroupJson);
    }

    @Test
    void testGetParticipants() throws FetchException {
        String url = "https://api.smash.gg/tournament/jackpot-4/event/smash-ultimate-1vs1";
        Set<Player> players = tournamentClient.getParticipants(url);
        assertTrue(players.contains(new Player("DF", "AcidBath")));
    }

    @Test
    void testFetchTournament() throws FetchException {
        String url = "https://api.smash.gg/tournament/jackpot-4/event/smash-ultimate-1vs1";
        Tournament tournament = tournamentClient.fetchTournament(url);
        assertEquals(1, tournament.getPhases().size());
        Phase phase = tournament.getPhases().iterator().next();
        assertEquals(45, phase.getSets().size());
    }


    private String loadJson(String filename) throws URISyntaxException, IOException {
        Path path = Paths.get(this.getClass().getClassLoader().getResource(filename).toURI());
        Stream<String> lines = Files.lines(path);
        String json = lines.collect(Collectors.joining("\n"));
        lines.close();
        return json;
    }

}
