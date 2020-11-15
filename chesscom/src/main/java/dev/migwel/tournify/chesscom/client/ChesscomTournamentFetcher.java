package dev.migwel.tournify.chesscom.client;

import dev.migwel.tournify.core.data.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ChesscomTournamentFetcher {

    private static final Logger log = LoggerFactory.getLogger(ChesscomTournamentFetcher.class);

    public static Set<Player> fetchParticipants(String formattedUrl) {
        return null;
    }
}
