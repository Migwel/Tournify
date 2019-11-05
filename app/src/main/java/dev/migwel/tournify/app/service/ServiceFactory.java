package dev.migwel.tournify.app.service;

import dev.migwel.tournify.core.data.Source;
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

    public ServiceFactory(@Qualifier("SmashggTournamentService") TournamentService smashggTournamentService, @Qualifier("SmashggUrlService") UrlService smashggUrlService) {
        this.smashggTournamentService = smashggTournamentService;
        this.smashggUrlService = smashggUrlService;
    }

    @Nonnull
    public TournamentService getTournamentService(String url) {
        switch(Source.getSource(url)) {
            case Smashgg:
                return smashggTournamentService;
            default:
                throw new IllegalArgumentException("Couldn't find any service for url: "+ url );
        }
    }

    @Nonnull
    public UrlService getUrlService(String url) {
        switch(Source.getSource(url)) {
            case Smashgg:
                return smashggUrlService;
            default:
                throw new IllegalArgumentException("Couldn't find any service for url: "+ url );
        }
    }



}
