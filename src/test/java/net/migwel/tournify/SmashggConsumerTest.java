package net.migwel.tournify;

import net.migwel.tournify.smashgg.client.SmashggClient;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

public class SmashggConsumerTest {

    @Test
    public void testGetTournament() {
        RestTemplateBuilder templateBuilder = new RestTemplateBuilder();
        SmashggClient consumer = new SmashggClient(templateBuilder.build());
        System.out.println(consumer.fetchTournament("https://api.smash.gg/tournament/nhl18-community-tournament-series-at-square-one"));
    }
}
