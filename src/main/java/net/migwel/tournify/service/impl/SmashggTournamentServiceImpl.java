package net.migwel.tournify.service.impl;

import net.migwel.tournify.consumer.SmashggConsumer;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.service.TournamentService;
import net.migwel.tournify.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SmashggTournamentService")
public class SmashggTournamentServiceImpl implements TournamentService {

    @Autowired
    private UrlService urlService;

    @Autowired
    private SmashggConsumer smashggConsumer;

    @Override
    public Tournament getTournament(String url) {

        String formattedUrl = urlService.formatUrl(url);
        return smashggConsumer.getTournament(formattedUrl);
    }
}
