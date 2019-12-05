package dev.migwel.tournify.challonge.client;

import dev.migwel.tournify.challonge.impl.ChallongeUrlService;
import dev.migwel.tournify.challonge.response.ChallongeMatch;
import dev.migwel.tournify.challonge.response.MatchResponse;
import dev.migwel.tournify.core.exception.FetchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

@Component
public class ChallongeMatchesFetcher {
    private static final Logger log = LoggerFactory.getLogger(ChallongeMatchesFetcher.class);

    private final ChallongeUrlService challongeUrlService;
    private final ChallongeFetcher challongeFetcher;

    public ChallongeMatchesFetcher(ChallongeUrlService challongeUrlService, ChallongeFetcher challongeFetcher) {
        this.challongeUrlService = challongeUrlService;
        this.challongeFetcher = challongeFetcher;
    }

    @CheckForNull
    public Set<ChallongeMatch> fetchMatches(@Nonnull String formattedUrl) throws FetchException {
        String getMatchesUrl = challongeUrlService.buildMatchesUrl(formattedUrl);
        MatchResponse[] matchesResponse = challongeFetcher.fetchWithRetries(getMatchesUrl, MatchResponse[].class);
        Set<ChallongeMatch> matches = new HashSet<>();
        for(MatchResponse matchResponse : matchesResponse) {
            matches.add(matchResponse.getMatch());
        }
        return matches;
    }
}