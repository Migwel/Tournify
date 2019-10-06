package dev.migwel.tournify.app.service;

import dev.migwel.tournify.core.service.TournamentService;
import dev.migwel.tournify.core.service.UrlService;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;


@Component
@Immutable
public class ServiceFactory {

    private final TournamentService smashggTournamentService;

    private final UrlService smashggUrlService;

    private final UrlService urlService;

    public ServiceFactory(@Qualifier("SmashggTournamentService") TournamentService smashggTournamentService, @Qualifier("SmashggUrlService") UrlService smashggUrlService, UrlService urlService) {
        this.smashggTournamentService = smashggTournamentService;
        this.smashggUrlService = smashggUrlService;
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
