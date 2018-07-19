package net.migwel.tournify.service;

import net.migwel.tournify.consumer.TournamentConsumer;
import net.migwel.tournify.data.Tournament;
import org.springframework.stereotype.Service;

@Service
public interface TournamentService {

    Tournament getTournament(String url);
    UrlService getUrlService();

    default Tournament getTournament(TournamentConsumer tournamentConsumer, String formattedUrl) {
        return tournamentConsumer.getTournament(formattedUrl);
    }
}
