package dev.migwel.tournify.smashgg.client;

import dev.migwel.tournify.communication.commons.Player;
import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.smashgg.config.SmashggConfiguration;
import dev.migwel.tournify.smashgg.response.SmashggEvent;
import dev.migwel.tournify.smashgg.response.SmashggEventResponse;
import dev.migwel.tournify.smashgg.response.SmashggNode;
import dev.migwel.tournify.smashgg.response.SmashggParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.TreeSet;

public class SmashggParticipantsFetcher {
    private static final Logger log = LoggerFactory.getLogger(SmashggParticipantsFetcher.class);
    private final SmashggConfiguration configuration;
    private final SmashggFetcher smashggFetcher;

    public SmashggParticipantsFetcher(SmashggConfiguration configuration, SmashggFetcher smashggFetcher) {
        this.configuration = configuration;
        this.smashggFetcher = smashggFetcher;
    }

    @Nonnull
    Set<Player> fetchParticipants(String eventSlug) throws FetchException {
        var participants = new TreeSet<Player>();
        SmashggEvent event;
        long page = 0;
        do {
            page++;
            event = fetchParticipants(eventSlug, page);
            if (event.getEntrants() == null ||
                    event.getEntrants().getPageInfo() == null ||
                    event.getEntrants().getPageInfo().getTotalPages() == 0) {
                break;
            }

            for (SmashggNode node : event.getEntrants().getNodes()) {
                for (SmashggParticipant entrant : node.getParticipants()) {
                    participants.add(new Player(entrant.getPrefix(), entrant.getGamerTag()));
                }
            }

        } while (event.getEntrants().getPageInfo().getTotalPages() != page);

        return participants;
    }

    @Nonnull
    private SmashggEvent fetchParticipants(String eventSlug, long page) throws FetchException {
        String request = buildParticipantsRequest(eventSlug, page, configuration.getSetsPerPage());
        log.info("Fetching participants at "+ eventSlug);
        return smashggFetcher.fetchWithRetries(request, SmashggEventResponse.class);
    }

    private String buildParticipantsRequest(String eventSlug, long page, long perPage) {
        return String.format("{\"query\":\"query event($slug: String!, $page:Int!, $perPage:Int!){"+
                " event(slug: $slug) {"+
                " entrants(query:{page:$page, perPage:$perPage}){ " +
                " pageInfo {total totalPages} "+
                " nodes { "+
                " participants{gamerTag prefix} "+
                " }}}}\", " +
                "\"variables\":{\"slug\":\"%s\", \"page\":\"%d\", \"perPage\":\"%d\"}}", eventSlug, page, perPage);
    }
}