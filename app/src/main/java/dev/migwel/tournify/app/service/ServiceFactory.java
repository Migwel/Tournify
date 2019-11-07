package dev.migwel.tournify.app.service;

import dev.migwel.tournify.core.data.Source;
import dev.migwel.tournify.core.service.TournamentService;
import dev.migwel.tournify.smashgg.impl.SmashggTournamentService;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;


@Component
@Immutable
public class ServiceFactory {

    private final ApplicationContext applicationContext;

    public ServiceFactory(ApplicationContext applicationContext) {
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
