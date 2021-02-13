package dev.migwel.tournify.challonge.client;

import dev.migwel.tournify.challonge.impl.ChallongeUrlService;
import dev.migwel.tournify.challonge.response.ChallongeParticipant;
import dev.migwel.tournify.challonge.response.ChallongeTournament;
import dev.migwel.tournify.challonge.response.ParticipantsResponse;
import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.data.GameType;
import dev.migwel.tournify.core.data.Phase;
import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.data.Set;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.util.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.TreeSet;

@Component
public class ChallongeTournamentClient implements TournamentClient {

    private static final Logger log = LoggerFactory.getLogger(ChallongeTournamentClient.class);

    private final ChallongeFetcher challongeFetcher;
    private final ChallongeUrlService challongeUrlService;
    private final ChallongeTournamentFetcher challongeTournamentFetcher;
    private final ChallongeMatchesFetcher challongeMatchesFetcher;

    public ChallongeTournamentClient(ChallongeFetcher challongeFetcher, ChallongeUrlService challongeUrlService, ChallongeTournamentFetcher challongeTournamentFetcher, ChallongeMatchesFetcher challongeMatchesFetcher) {
        this.challongeFetcher = challongeFetcher;
        this.challongeUrlService = challongeUrlService;
        this.challongeTournamentFetcher = challongeTournamentFetcher;
        this.challongeMatchesFetcher = challongeMatchesFetcher;
    }

    @Override
    @Nonnull
    public Tournament fetchTournament(@Nonnull String formattedUrl) throws FetchException {
        ChallongeTournamentClient.log.info("Fetching tournament at url: " + formattedUrl);
        ChallongeTournament challongeTournament = challongeTournamentFetcher.fetchTournament(formattedUrl);

        if(challongeTournament == null) {
            throw new FetchException("Could not fetch tournament for url "+ formattedUrl, false);
        }

        Collection<Player> players = getParticipants(formattedUrl);
        Collection<Set> sets = challongeMatchesFetcher.fetchMatches(formattedUrl, players);

        Collection<Phase> phases = buildPhases(sets);
        boolean isDone = phases.stream().allMatch(Phase::isDone);
        return new Tournament(
                challongeTournament.getId(),
                phases,
                challongeTournament.getName(),
                new GameType(challongeTournament.getGameName()),
                null,
                formattedUrl,
                challongeTournament.getStartDate(),
                players,
                isDone);
    }

    private Collection<Phase> buildPhases(Collection<Set> sets) {
        boolean isDone = sets.stream().allMatch(Set::isDone);
        Phase phase = new Phase(null, sets, null, isDone);
        return Collections.singleton(phase);
    }

    @Nonnull
    @Override
    public java.util.Set<Player> getParticipants(@Nonnull String formattedUrl) throws FetchException {
        log.info("Fetching participants at url: " + formattedUrl);
        String getParticipantsUrl = challongeUrlService.buildParticipantsUrl(formattedUrl);
        ParticipantsResponse[] participantResponses = challongeFetcher.fetchWithRetries(getParticipantsUrl, ParticipantsResponse[].class);
        return convertChallongeParticipantResponse(participantResponses);
    }

    @Override
    public boolean tournamentExists(String formattedUrl) {
        try {
            challongeTournamentFetcher.fetchTournament(formattedUrl);
            return true;
        } catch (FetchException e) {
            log.warn("Could not fetch event at url "+ formattedUrl, e);
            return false;
        }
    }

    private java.util.Set<Player> convertChallongeParticipantResponse(@Nonnull ParticipantsResponse[] participantsResponses) {
        final java.util.Set<Player> players = new TreeSet<>();
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
