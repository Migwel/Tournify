package net.migwel.tournify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.CheckForNull;


@Component
public class ServiceFactory {

    @Autowired
    @Qualifier("SmashggTournamentService")
    private TournamentService smashggTournamentService;

    @Autowired
    private UrlService urlService;

    public ServiceFactory(TournamentService smashggTournamentService, UrlService urlService) {
        this.smashggTournamentService = smashggTournamentService;
        this.urlService = urlService;
    }

    @CheckForNull
    public TournamentService getTournamentService(String url) {

        switch(urlService.parseUrl(url)) {
            case Smashgg:
                return smashggTournamentService;
            default:
                return null;
        }
    }



}
