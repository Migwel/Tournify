package net.migwel.tournify.smashgg.service.impl;

import net.migwel.tournify.consumer.TournamentConsumer;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.service.TournamentService;
import net.migwel.tournify.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("SmashggTournamentService")
public class SmashggTournamentService extends TournamentService {

    @Autowired
    @Qualifier("SmashggUrlService")
    private UrlService urlService;

    @Autowired
    @Qualifier("SmashggConsumer")
    private TournamentConsumer tournamentConsumer;

    @Override
    public Tournament getTournament(String url) {
        String formattedUrl = urlService.normalizeUrl(url);
        return getTournament(tournamentConsumer, formattedUrl);
    }

    @Override
    protected Tournament fetchTournament(String url) {
        String formattedUrl = urlService.normalizeUrl(url);
        return fetchTournament(tournamentConsumer, formattedUrl);
    }
}
