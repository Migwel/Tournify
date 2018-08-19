package net.migwel.tournify;

import net.migwel.tournify.smashgg.client.SmashggClient;
import net.migwel.tournify.smashgg.service.impl.SmashggUrlService;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

public class SmashggConsumerTest {

    //https://smash.gg/tournament/super-smash-con-2018/events/melee-1v1-singles/overview

    @Test
    public void testGetTournament() {
        RestTemplateBuilder templateBuilder = new RestTemplateBuilder();
        SmashggClient consumer = new SmashggClient(templateBuilder.build(), new SmashggUrlService());
        System.out.println(consumer.fetchTournament("https://api.smash.gg/tournament/nhl18-community-tournament-series-at-square-one/event/nhl-10-5"));
    }
}
