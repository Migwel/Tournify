package dev.migwel.tournify.challonge.client;

import dev.migwel.tournify.challonge.impl.ChallongeUrlService;
import dev.migwel.tournify.challonge.response.ChallongeTournament;
import dev.migwel.tournify.challonge.response.TournamentResponse;
import dev.migwel.tournify.core.exception.FetchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

@Component
public class ChallongeTournamentFetcher {
    private static final Logger log = LoggerFactory.getLogger(ChallongeTournamentFetcher.class);

    private final ChallongeUrlService challongeUrlService;
    private final ChallongeFetcher challongeFetcher;

    public ChallongeTournamentFetcher(ChallongeUrlService challongeUrlService, ChallongeFetcher challongeFetcher) {
        this.challongeUrlService = challongeUrlService;
        this.challongeFetcher = challongeFetcher;
    }

    @Nonnull
    public ChallongeTournament fetchTournament(@Nonnull String formattedUrl) throws FetchException {
        String getTournamentUrl = challongeUrlService.buildTournamentUrl(formattedUrl);
        TournamentResponse tournamentResponse = challongeFetcher.fetchWithRetries(getTournamentUrl, TournamentResponse.class);
        if (tournamentResponse.getTournament() == null) {
            throw new FetchException("Could not fetch tournament for url: "+ formattedUrl, false);
        }
        return tournamentResponse.getTournament();
    }
}