package dev.migwel.tournify.chesscom.client;

import dev.migwel.chesscomjava.api.data.tournament.TournamentPlayer;
import dev.migwel.chesscomjava.api.services.TournamentService;
import dev.migwel.tournify.chesscom.util.ChesscomUtil;
import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.exception.FetchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component("ChesscomTournamentClient")
public class ChesscomTournamentClient implements TournamentClient {

    private static final Logger log = LoggerFactory.getLogger(ChesscomTournamentClient.class);

    private final TournamentService tournamentService;

    public ChesscomTournamentClient(@Qualifier("ChessTournamentService") TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @Override
    @Nonnull
    public Tournament fetchTournament(String url) throws FetchException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Nonnull
    public Set<Player> getParticipants(String formattedUrl) throws FetchException {
        log.info("Fetching tournament at url: " + formattedUrl);
        String tournamentSlug = ChesscomUtil.findTournamentSlug(formattedUrl);
        dev.migwel.chesscomjava.api.data.tournament.Tournament tournament = tournamentService.getTournament(tournamentSlug);
        return tournament.players().stream().map(this::convertPlayer).collect(Collectors.toSet());
    }

    private Player convertPlayer(TournamentPlayer p) {
        return new Player(p.username());
    }
}
