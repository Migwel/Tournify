package dev.migwel.tournify.chesscom.configuration;

import dev.migwel.chesscomjava.api.services.FactoryCreator;
import dev.migwel.chesscomjava.api.services.TournamentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChesscomConfiguration {

    @Bean("ChessTournamentService")
    public TournamentService tournamentService() {
        return FactoryCreator.getServiceFactory().getTournamentService();
    }
}
