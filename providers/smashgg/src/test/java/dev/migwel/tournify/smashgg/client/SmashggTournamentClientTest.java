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
import dev.migwel.tournify.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
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

        String participantsJson = FileUtil.loadFile("ParticipantsResponse.json");
        String eventJson = FileUtil.loadFile("EventResponse.json");
        String phaseGroupJson = FileUtil.loadFile("PhaseGroupResponse.json");
        String eventDoesntExistJson = FileUtil.loadFile("EventDoesntExistResponse.json");
        when(httpClient.postRequest(contains("entrants("), anyString(), anyCollection())).thenReturn(participantsJson);
        when(httpClient.postRequest(contains("tournament {"), anyString(), anyCollection())).thenReturn(eventJson);
        when(httpClient.postRequest(contains("query phaseGroup("), anyString(), anyCollection())).thenReturn(phaseGroupJson);
        when(httpClient.postRequest(contains("invalid"), anyString(), anyCollection())).thenReturn(eventDoesntExistJson);
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

    @Test
    void exceptionWhenFetchingTournamentThatDoesntExist() {
        String url = "https://api.smash.gg/tournament/invalid/event/invalid";
        assertThrows(FetchException.class, () -> tournamentClient.fetchTournament(url));
    }

    @Test
    void tournamentExistsMethodWhenTournamentExists() {
        String url = "https://api.smash.gg/tournament/jackpot-4/event/smash-ultimate-1vs1";
        assertTrue(tournamentClient.tournamentExists(url));
    }

    @Test
    void tournamentExistsMethodWhenTournamentDoesntExist() {
        String url = "https://api.smash.gg/tournament/invalid/event/invalid";
        assertFalse(tournamentClient.tournamentExists(url));
    }

}
