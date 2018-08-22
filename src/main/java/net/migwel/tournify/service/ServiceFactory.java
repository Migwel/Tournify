package net.migwel.tournify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;


@Component
public class ServiceFactory {

    @Autowired
    @Qualifier("SmashggTournamentService")
    private TournamentService smashggTournamentService;

    @Autowired
    @Qualifier("SmashggUrlService")
    private UrlService smashggUrlService;

    @Autowired
    private UrlService urlService;

    public ServiceFactory(TournamentService smashggTournamentService, UrlService urlService) {
        this.smashggTournamentService = smashggTournamentService;
        this.urlService = urlService;
    }

    @Nonnull
    public TournamentService getTournamentService(String url) {
        switch(urlService.parseUrl(url)) {
            case Smashgg:
                return smashggTournamentService;
            default:
                throw new IllegalArgumentException("Couldn't find any service for url: "+ url );
        }
    }

    @Nonnull
    public UrlService getUrlService(String url) {
        switch(urlService.parseUrl(url)) {
            case Smashgg:
                return smashggUrlService;
            default:
                throw new IllegalArgumentException("Couldn't find any service for url: "+ url );
        }
    }



}
