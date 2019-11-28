package dev.migwel.tournify.app.service;

import dev.migwel.tournify.core.data.Source;
import dev.migwel.tournify.core.service.TournamentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;


@Component
@Immutable
public class TournamentServiceFactory {

    private final TournamentService smashggTournamentService;
    private final TournamentService challongeTournamentService;

    public TournamentServiceFactory(@Qualifier("SmashggTournamentService") TournamentService smashggTournamentService,
                                    @Qualifier("ChallongeTournamentService") TournamentService challongeTournamentService) {
        this.smashggTournamentService = smashggTournamentService;
        this.challongeTournamentService = challongeTournamentService;
    }

    @Nonnull
    public TournamentService getTournamentService(String url) {
        switch(Source.getSource(url)) {
            case Smashgg:
                return smashggTournamentService;
            case Challonge:
                return challongeTournamentService;
        }

        throw new IllegalArgumentException("The provided url is not supported: "+ url);
    }
}
