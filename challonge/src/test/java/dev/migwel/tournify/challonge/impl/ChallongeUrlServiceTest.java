package dev.migwel.tournify.challonge.impl;

import dev.migwel.tournify.core.service.UrlService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ChallongeUrlServiceTest {

    private final UrlService challongeUrlService = new ChallongeUrlService();
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
}
