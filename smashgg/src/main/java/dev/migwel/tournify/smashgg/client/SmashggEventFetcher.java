package dev.migwel.tournify.smashgg.client;

import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.smashgg.response.SmashggEvent;
import dev.migwel.tournify.smashgg.response.SmashggEventResponse;
import dev.migwel.tournify.smashgg.util.SmashggUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class SmashggEventFetcher {
    private static final Logger log = LoggerFactory.getLogger(SmashggEventFetcher.class);
    private final SmashggFetcher smashggFetcher;

    public SmashggEventFetcher(SmashggFetcher smashggFetcher) {
        this.smashggFetcher = smashggFetcher;
    }

    SmashggEvent fetchEvent(@Nonnull String formattedUrl) throws FetchException {
        String eventSlug = SmashggUtil.findEventSlug(formattedUrl);
        String request = buildEventRequest(eventSlug);
        log.info("Fetching event at " + eventSlug);
        return smashggFetcher.fetchWithRetries(request, SmashggEventResponse.class);
    }

    private String buildEventRequest(String eventSlug) {
        return String.format("{\"query\":\"query event($slug: String!){  event(slug: $slug) { " +
                "id slug startAt name" +
                " tournament {id name city addrState venueAddress countryCode} " +
                " phaseGroups {id state phaseId displayIdentifier} "+
                " phases {id name} "+
                " videogame {displayName}" +
                " }}\", " +
                "\"variables\":{\"slug\":\"%s\"}}", eventSlug);
    }
}