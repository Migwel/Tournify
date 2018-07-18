package net.migwel.tournify.service;

import net.migwel.tournify.consumer.TournamentConsumer;
import net.migwel.tournify.data.Tournament;
import org.springframework.stereotype.Service;

@Service
public interface TournamentService {

    Tournament getTournament(String url);

    default Tournament getTournament(UrlService urlService, TournamentConsumer tournamentConsumer, String url) {
        String formattedUrl = urlService.normalizeUrl(url);
        return tournamentConsumer.getTournament(formattedUrl);
    }
}
