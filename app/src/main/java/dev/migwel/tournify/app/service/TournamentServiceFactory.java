package dev.migwel.tournify.app.service;

import dev.migwel.tournify.core.data.Source;
import dev.migwel.tournify.core.service.TournamentService;
import dev.migwel.tournify.smashgg.impl.SmashggTournamentService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;


@Component
@Immutable
public class TournamentServiceFactory {

    private final ApplicationContext applicationContext;

    public TournamentServiceFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Nonnull
    public TournamentService getTournamentService(String url) {
        switch(Source.getSource(url)) {
            case Smashgg:
                return applicationContext.getBean(SmashggTournamentService.class);
        }

        throw new IllegalArgumentException("The provided url is not supported: "+ url);
    }
}
