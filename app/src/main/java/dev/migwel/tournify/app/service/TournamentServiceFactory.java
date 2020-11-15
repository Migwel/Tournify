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
    private final TournamentService chesscomTournamentService;

    public TournamentServiceFactory(@Qualifier("SmashggTournamentService") TournamentService smashggTournamentService,
                                    @Qualifier("ChallongeTournamentService") TournamentService challongeTournamentService,
                                    @Qualifier("ChesscomTournamentService") TournamentService chesscomTournamentService) {
        this.smashggTournamentService = smashggTournamentService;
        this.challongeTournamentService = challongeTournamentService;
        this.chesscomTournamentService = chesscomTournamentService;
    }

    @Nonnull
    public TournamentService getTournamentService(String url) {
        return switch (Source.getSource(url)) {
            case Smashgg -> smashggTournamentService;
            case Challonge -> challongeTournamentService;
            case Chesscom -> chesscomTournamentService;
        };
    }
}
