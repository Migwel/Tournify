package net.migwel.tournify.consumer;

import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.data.consumer.smashgg.GetTournamentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Component
public class SmashggConsumer {

    @Autowired
    private RestTemplate restTemplate;

    public SmashggConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Tournament getTournament(String url) {
        GetTournamentResponse tournamentResponse = restTemplate.getForObject(url, GetTournamentResponse.class);
        return new Tournament(null,
                tournamentResponse.getEntities().getTournament().getName(),
                "Amsterdam",
                url,
                new Date(tournamentResponse.getEntities().getTournament().getStartAt()*1000));
    }
}
