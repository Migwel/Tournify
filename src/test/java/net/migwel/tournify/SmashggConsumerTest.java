package net.migwel.tournify;

import net.migwel.tournify.consumer.SmashggConsumer;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

public class SmashggConsumerTest {

    @Test
    public void testGetTournament() {
        RestTemplateBuilder templateBuilder = new RestTemplateBuilder();
        SmashggConsumer consumer = new SmashggConsumer(templateBuilder.build());
        System.out.println(consumer.getTournament("https://api.smash.gg/tournament/pulsar-premier-league"));
    }
}
