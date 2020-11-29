package dev.migwel.tournify.chesscom.client;

import dev.migwel.chesscomjava.api.data.games.Participant;
import dev.migwel.chesscomjava.api.data.tournament.GroupGame;
import dev.migwel.chesscomjava.api.data.tournament.RoundGroup;
import dev.migwel.chesscomjava.api.data.tournament.TournamentPlayer;
import dev.migwel.chesscomjava.api.data.tournament.TournamentRound;
import dev.migwel.chesscomjava.api.services.TournamentService;
import dev.migwel.tournify.chesscom.util.ChesscomUtil;
import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.data.GameType;
import dev.migwel.tournify.core.data.Phase;
import dev.migwel.tournify.core.data.Set;
import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.exception.FetchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
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
    public Tournament fetchTournament(String formattedUrl) throws FetchException {
        String tournamentSlug = ChesscomUtil.findTournamentSlug(formattedUrl);
        dev.migwel.chesscomjava.api.data.tournament.Tournament chesscomTournament = tournamentService.getTournament(tournamentSlug);
        java.util.Set<Phase> phases = new HashSet<>();
        Collection<Player> players = new HashSet<>();
        for (String roundStr: chesscomTournament.rounds()) {
            phases.add(fetchPhase(tournamentSlug, ChesscomUtil.findRoundSlug(roundStr), players));
        }
        boolean isDone = chesscomTournament.finishTime() != null;
        if (isDone) {
            phases.forEach(e -> e.setDone(true));
        }
        return new Tournament(null,
                phases,
                chesscomTournament.name(),
                new GameType("Chess"),
                null,
                chesscomTournament.url(),
                null,
                players,
                isDone
                );
    }

    private Phase fetchPhase(String tournamentSlug, String round, Collection<Player> tournamentPlayers) {
        TournamentRound chesscomRound = tournamentService.getRound(tournamentSlug, round);
        java.util.Set<Set> sets = new HashSet<>();
        for (String groupStr : chesscomRound.groups()) {
            sets.addAll(fetchSets(tournamentSlug, round, ChesscomUtil.findGroupSlug(groupStr), tournamentPlayers));
        }

        //We always set done to false because chess.com doesn't give us any indication whether a phase is over or not and may add games to phase that look to be done
        return new Phase(null, sets, "Round "+ round, false);
    }

    private Collection<Set> fetchSets(String tournamentSlug, String round, String groupStr, Collection<Player> tournamentPlayers) {
        java.util.Set<Set> sets = new HashSet<>();
        RoundGroup group = tournamentService.getGroup(tournamentSlug, round, groupStr);
        for (GroupGame game : group.games()) {
            java.util.Set<Player> setPlayers = java.util.Set.of(buildPlayer(game.white(), tournamentPlayers), buildPlayer(game.black(), tournamentPlayers));
            GameResult gameResult = buildGameResult(game, tournamentPlayers);
            java.util.Set<Player> winners = gameResult.winner != null ? java.util.Set.of(gameResult.winner) : Collections.emptySet();
            sets.add(new Set(game.url(), setPlayers, winners, game.url(), gameResult.isDone));
        }
        return sets;
    }

    private GameResult buildGameResult(GroupGame game, Collection<Player> tournamentPlayers) {
        boolean isDone = game.endTime() != null;
        if ("win".equals(game.white().result())) {
            return new GameResult(buildPlayer(game.white(), tournamentPlayers), isDone);
        } else if ("win".equals(game.black().result())) {
            return new GameResult(buildPlayer(game.black(), tournamentPlayers), isDone);
        }
        return new GameResult(null, isDone);
    }

    private Player buildPlayer(Participant participant, Collection<Player> tournamentPlayers) {
        Player player = new Player(null, participant.username(), participant.id());
        if (tournamentPlayers.contains(player)) {
            return tournamentPlayers.stream().filter(e -> e.equals(player)).findAny().orElseThrow(() -> new NoSuchElementException(player.toString()));
        }
        return player;
    }

    @Override
    @Nonnull
    public java.util.Set<Player> getParticipants(String formattedUrl) throws FetchException {
        log.info("Fetching tournament at url: " + formattedUrl);
        String tournamentSlug = ChesscomUtil.findTournamentSlug(formattedUrl);
        dev.migwel.chesscomjava.api.data.tournament.Tournament tournament = tournamentService.getTournament(tournamentSlug);
        return tournament.players().stream().map(this::convertPlayer).collect(Collectors.toSet());
    }

    private Player convertPlayer(TournamentPlayer p) {
        return new Player(p.username());
    }

    private static record GameResult(Player winner, boolean isDone) {}
}
