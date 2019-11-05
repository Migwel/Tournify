package dev.migwel.tournify.smashgg.impl;

import org.junit.Assert;
import org.junit.Test;

public class SmasggUrlServiceTest {

    private SmashggUrlService smashggUrlService = new SmashggUrlService();
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

    @Test
    public void formatUrl_smashggUrls() {
        for(int i = 0; i < validSmashggUrls.length; i++) {
            Assert.assertEquals("Format Smashgg URL "+ validSmashggUrls[i],
                                expectedSmashggUrls[i],
                                smashggUrlService.normalizeUrl(validSmashggUrls[i]));
        }
    }
}
