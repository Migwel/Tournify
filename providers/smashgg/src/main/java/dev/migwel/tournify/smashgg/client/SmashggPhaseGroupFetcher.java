package dev.migwel.tournify.smashgg.client;

import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.smashgg.config.SmashggConfiguration;
import dev.migwel.tournify.smashgg.response.SmashggPhaseGroup;
import dev.migwel.tournify.smashgg.response.SmashggPhaseGroupResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class SmashggPhaseGroupFetcher {
    private static final Logger log = LoggerFactory.getLogger(SmashggFetcher.class);
    private final SmashggConfiguration configuration;
    private final SmashggFetcher smashggFetcher;

    public SmashggPhaseGroupFetcher(SmashggConfiguration configuration, SmashggFetcher smashggFetcher) {
        this.configuration = configuration;
        this.smashggFetcher = smashggFetcher;
    }

    @Nonnull
    public SmashggPhaseGroup fetchPhaseGroup(long phaseGroupId, long page) throws FetchException {
        String request = buildPhaseGroupRequest(phaseGroupId, page, configuration.getSetsPerPage());
        log.info("Fetching phaseGroup " + phaseGroupId);
        return smashggFetcher.fetchWithRetries(request, SmashggPhaseGroupResponse.class);
    }

    private String buildPhaseGroupRequest(long phaseGroupId, long page, long perPage) {
        return String.format("{\"query\":\"query phaseGroup($id: ID!, $page:Int!, $perPage:Int!) {"+
                " phaseGroup(id: $id) {id displayIdentifier "+
                " paginatedSets(page:$page, perPage:$perPage) { " +
                " pageInfo {total totalPages} "+
                " nodes {id fullRoundText winnerId "+
                " slots(includeByes: false) { "+
                " entrant{id name" +
                " participants{gamerTag prefix}}}" +
                " }}}}\", " +
                "\"variables\":{\"id\":\"%d\", \"page\":\"%d\", \"perPage\":\"%d\"}}", phaseGroupId, page, perPage);
    }
}