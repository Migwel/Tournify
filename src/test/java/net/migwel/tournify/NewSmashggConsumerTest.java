package net.migwel.tournify;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.migwel.tournify.smashgg.client.NewSmashggClient;
import org.junit.Test;

public class NewSmashggConsumerTest {

    @Test
    public void testGetTournament() {
        NewSmashggClient smashggClient = new NewSmashggClient(null, new ObjectMapper());
        System.out.println(smashggClient.fetchTournament("https://api.smash.gg/tournament/salty-arena-bi-weekly-2/event/ultimate-1v1"));
    }
}
