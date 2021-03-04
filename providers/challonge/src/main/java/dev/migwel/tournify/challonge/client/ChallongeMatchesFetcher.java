package dev.migwel.tournify.challonge.client;

import dev.migwel.tournify.challonge.impl.ChallongeUrlService;
import dev.migwel.tournify.challonge.response.ChallongeMatch;
import dev.migwel.tournify.challonge.response.MatchResponse;
import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.data.Set;
import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.util.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class ChallongeMatchesFetcher {
    private static final Logger log = LoggerFactory.getLogger(ChallongeMatchesFetcher.class);

    private final ChallongeUrlService challongeUrlService;
    private final ChallongeFetcher challongeFetcher;

    public ChallongeMatchesFetcher(ChallongeUrlService challongeUrlService, ChallongeFetcher challongeFetcher) {
        this.challongeUrlService = challongeUrlService;
        this.challongeFetcher = challongeFetcher;
    }

    @Nonnull
    public Collection<Set> fetchMatches(@Nonnull String formattedUrl, @Nonnull Collection<Player> players) throws FetchException {
        String getMatchesUrl = challongeUrlService.buildMatchesUrl(formattedUrl);
        MatchResponse[] matchesResponse = challongeFetcher.fetchWithRetries(getMatchesUrl, MatchResponse[].class);
        Collection<ChallongeMatch> matches = new HashSet<>();
        for(MatchResponse matchResponse : matchesResponse) {
            matches.add(matchResponse.getMatch());
        }
        return buildSets(matches, players);
    }

    private Collection<dev.migwel.tournify.core.data.Set> buildSets(@Nonnull Collection<ChallongeMatch> matches, @Nonnull Collection<Player> players) {
        Map<String, Player> idToPlayer = new HashMap<>();
        players.forEach(e -> idToPlayer.put(e.getExternalId(), e));

        Collection<dev.migwel.tournify.core.data.Set> sets = new HashSet<>();
        for (ChallongeMatch match : matches) {
            dev.migwel.tournify.core.data.Set set = new dev.migwel.tournify.core.data.Set(match.getId(),
                                                                                          java.util.Set.of(idToPlayer.get(match.getPlayer1Id()), idToPlayer.get(match.getPlayer2Id())),
                                                                                          java.util.Set.of(idToPlayer.get(match.getWinnerId())),
                                                                                          null,
                                                                                          TextUtil.hasText(match.getWinnerId())
            );
            sets.add(set);
        }
        return sets;
    }
}