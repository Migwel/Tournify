package dev.migwel.tournify.challonge.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ChallongeUrlServiceTest {

    private final ChallongeUrlService challongeUrlService = new ChallongeUrlService();
    private static final Map<String, String> urlsToNormalizedUrls = new HashMap<>();
    static {
        urlsToNormalizedUrls.put("https://challonge.com/ycmopwda", "https://api.challonge.com/v1/tournaments/ycmopwda");
    }

    @Test
    public void testNormalizeUrl() {
        for(var urlToNormalizedURl : urlsToNormalizedUrls.entrySet()) {
            Assertions.assertEquals(urlToNormalizedURl.getValue(), challongeUrlService.normalizeUrl(urlToNormalizedURl.getKey()));
        }
    }

    @Test
    public void testAddUsernamePasswordToUrl() {
        Assertions.assertEquals("https://username:apiToken@api.challonge.com/v1/tournaments/ycmopwda",
                challongeUrlService.addUsernamePasswordToUrl("https://api.challonge.com/v1/tournaments/ycmopwda", "username", "apiToken"));
    }

    @Test
    public void testBuildParticipantsUrl() {
        Assertions.assertEquals("https://api.challonge.com/v1/tournaments/ycmopwda/participants.json",
                challongeUrlService.buildParticipantsUrl("https://api.challonge.com/v1/tournaments/ycmopwda"));
    }
}
