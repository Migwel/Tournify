package net.migwel.tournify.core.service.impl;

import net.migwel.tournify.core.data.Source;
import net.migwel.tournify.core.service.UrlService;
import net.migwel.tournify.smashgg.service.impl.SmashggUrlService;
import org.junit.Assert;
import org.junit.Test;

public class TestUrlServiceImpl {

    private UrlService urlService = new SmashggUrlService();

    private String[] validSmashggUrls = {"https://api.smash.gg/tournament/genesis-3/event/brawl-1v1-singles",
            "www.smash.gg/tournament/genesis-3/event/brawl-1v1-singles",
            "smash.gg/tournament/genesis-3/event/brawl-1v1-singles",
            "smash.gg/tournament/genesis-3/events/brawl-1v1-singles",
            "https://www.smash.gg/tournament/test/event/abcdeflkjfDDDD1235EEDDS5",
            "https://smash.gg/tournament/super-smash-con-2018/event/brawl-1v1-singles/overview", // This one is valid but will be formatted to tournament/ab
    };

    private String[] expectedSmashggUrls = {"https://api.smash.gg/tournament/genesis-3/event/brawl-1v1-singles",
            "https://api.smash.gg/tournament/genesis-3/event/brawl-1v1-singles",
            "https://api.smash.gg/tournament/genesis-3/event/brawl-1v1-singles",
            "https://api.smash.gg/tournament/genesis-3/event/brawl-1v1-singles",
            "https://api.smash.gg/tournament/test/event/abcdeflkjfDDDD1235EEDDS5",
            "https://api.smash.gg/tournament/super-smash-con-2018/event/brawl-1v1-singles",
    };

    private String[] invalidUrls = {"www.google.fr",
            "www.smash.gg/tournament/",
            "www.smash.gg/tournament/$",
            "https://smash.gg/tournament/super-smash-con-2018",
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
                                urlService.normalizeUrl(validSmashggUrls[i]));
        }
    }
}
