package dev.migwel.tournify.challonge.client;

import dev.migwel.tournify.challonge.impl.ChallongeUrlService;
import dev.migwel.tournify.challonge.response.ChallongeMatch;
import dev.migwel.tournify.challonge.response.ChallongeParticipant;
import dev.migwel.tournify.challonge.response.ChallongeTournament;
import dev.migwel.tournify.challonge.response.ParticipantsResponse;
import dev.migwel.tournify.communication.commons.Player;
import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.data.GameType;
import dev.migwel.tournify.core.data.Phase;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.util.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ChallongeClient implements TournamentClient {

    private static final Logger log = LoggerFactory.getLogger(ChallongeClient.class);

    private final ChallongeFetcher challongeFetcher;
    private final ChallongeUrlService challongeUrlService;
    private final ChallongeTournamentFetcher challongeTournamentFetcher;
    private final ChallongeMatchesFetcher challongeMatchesFetcher;

    public ChallongeClient(ChallongeFetcher challongeFetcher, ChallongeUrlService challongeUrlService, ChallongeTournamentFetcher challongeTournamentFetcher, ChallongeMatchesFetcher challongeMatchesFetcher) {
        this.challongeFetcher = challongeFetcher;
        this.challongeUrlService = challongeUrlService;
        this.challongeTournamentFetcher = challongeTournamentFetcher;
        this.challongeMatchesFetcher = challongeMatchesFetcher;
    }

    @Override
    @Nonnull
    public Tournament fetchTournament(@Nonnull String formattedUrl) throws FetchException {
        ChallongeClient.log.info("Fetching tournament at url: " + formattedUrl);
        ChallongeTournament challongeTournament = challongeTournamentFetcher.fetchTournament(formattedUrl);

        if(challongeTournament == null) {
            throw new FetchException("Could not fetch tournalement for url "+ formattedUrl);
        }

        Set<ChallongeMatch> matches = challongeMatchesFetcher.fetchMatches(formattedUrl);
        Set<Player> players = getParticipants(formattedUrl);
        // This is stupid. We should use communication objects everywhere or data but not sometimes ones sometimes the others
        Set<dev.migwel.tournify.core.data.Player> playersData = players
                .stream()
                .map(p -> new dev.migwel.tournify.core.data.Player(p.getPrefix(), p.getUsername(), p.getExternalId()))
                .collect(Collectors.toSet());

        Collection<Phase> phases = buildPhases(matches, playersData);
        boolean isDone = phases.stream().allMatch(Phase::isDone);
        return new Tournament(
                challongeTournament.getId(),
                phases,
                challongeTournament.getName(),
                new GameType(challongeTournament.getGameName()),
                null,
                formattedUrl,
                challongeTournament.getStartDate(),
                isDone);
    }

    private Collection<Phase> buildPhases(Set<ChallongeMatch> matches, Set<dev.migwel.tournify.core.data.Player> players) {
        Collection<dev.migwel.tournify.core.data.Set> sets = buildSets(matches, players);
        boolean isDone = sets.stream().allMatch(dev.migwel.tournify.core.data.Set::isDone);
        Phase phase = new Phase(null, sets, null, isDone);
        return Collections.singleton(phase);
    }

    private Collection<dev.migwel.tournify.core.data.Set> buildSets(Set<ChallongeMatch> matches, Set<dev.migwel.tournify.core.data.Player> players) {
        Map<String, dev.migwel.tournify.core.data.Player> idToPlayer = new HashMap<>();
        players.forEach(e -> idToPlayer.put(e.getExternalId(), e));

        Collection<dev.migwel.tournify.core.data.Set> sets = new HashSet<>();
        for (ChallongeMatch match : matches) {
            dev.migwel.tournify.core.data.Set set = new dev.migwel.tournify.core.data.Set(match.getId(),
                    Set.of(idToPlayer.get(match.getPlayer1Id()), idToPlayer.get(match.getPlayer2Id())),
                    Set.of(idToPlayer.get(match.getWinnerId())),
                    null,
                    TextUtil.hasText(match.getWinnerId())
                    );
            sets.add(set);
        }
        return sets;
    }

    @Nonnull
    @Override
    public java.util.Set<Player> getParticipants(@Nonnull String formattedUrl) throws FetchException {
        log.info("Fetching participants at url: " + formattedUrl);
        String getParticipantsUrl = challongeUrlService.buildParticipantsUrl(formattedUrl);
        ParticipantsResponse[] participantResponses = challongeFetcher.fetchWithRetries(getParticipantsUrl, ParticipantsResponse[].class);
        return convertChallongeParticipantResponse(participantResponses);
    }

    private Set<Player> convertChallongeParticipantResponse(@Nonnull ParticipantsResponse[] participantsResponses) {
        final Set<Player> players = new HashSet<>();
        for(ParticipantsResponse participantsResponse : participantsResponses) {
            if(participantsResponse == null ||
               participantsResponse.getParticipant() == null ||
               TextUtil.isEmptyOrNull(participantsResponse.getParticipant().getDisplayName())) {
                continue;
            }
            ChallongeParticipant participant = participantsResponse.getParticipant();
            players.add(new Player(null, participant.getDisplayName(), participant.getId()));
        }
        return players;
    }
}
