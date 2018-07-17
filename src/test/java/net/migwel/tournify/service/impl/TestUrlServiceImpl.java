package net.migwel.tournify.service.impl;

import net.migwel.tournify.data.Source;
import net.migwel.tournify.service.UrlService;
import net.migwel.tournify.smashgg.service.impl.SmashggUrlService;
import org.junit.Assert;
import org.junit.Test;

public class TestUrlServiceImpl {

    private UrlService urlService = new SmashggUrlService();

    private String[] validSmashggUrls = {"https://api.smash.gg/tournament/genesis-3",
            "www.smash.gg/tournament/genesis-3",
            "smash.gg/tournament/genesis-3",
            "https://www.smash.gg/tournament/abcdeflkjfDDDD1235EEDDS5",
            "https://www.smash.gg/tournament/ab$%^", // This one is valid but will be formatted to tournament/ab
    };

    private String[] expectedSmashggUrls = {"https://api.smash.gg/tournament/genesis-3",
            "https://api.smash.gg/tournament/genesis-3",
            "https://api.smash.gg/tournament/genesis-3",
            "https://api.smash.gg/tournament/abcdeflkjfDDDD1235EEDDS5",
            "https://api.smash.gg/tournament/ab", // This one is valid but will be formatted to tournament/ab
    };

    private String[] invalidUrls = {"www.google.fr",
            "www.smash.gg/tournament/",
            "www.smash.gg/tournament/$",
    };

    @Test
    public void parseUrlTest_validSmashggUrls() {
        for(String validSmashggUrl : validSmashggUrls) {
            Assert.assertEquals("Parse valid Smashgg URL "+ validSmashggUrl, Source.Smashgg, urlService.parseUrl(validSmashggUrl));
        }
    }

    @Test
    public void parseUrlTest_invalidUrls() {
        for(String invalidUrl : invalidUrls) {
            Assert.assertEquals("Parse invalid URL "+ invalidUrl, Source.Unknown, urlService.parseUrl(invalidUrl));
        }
    }

    @Test
    public void formatUrl_smashggUrls() {
        for(int i = 0; i < validSmashggUrls.length; i++) {
            Assert.assertEquals("Format Smashgg URL "+ validSmashggUrls[i],
                                expectedSmashggUrls[i],
                                urlService.formatUrl(validSmashggUrls[i]));
        }
    }
}
