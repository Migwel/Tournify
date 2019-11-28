package dev.migwel.tournify.challonge.client;

import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.data.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Collections;

@Component
public class ChallongeClient implements TournamentClient {

    private static final Logger log = LoggerFactory.getLogger(ChallongeClient.class);

    @Override
    @Nonnull
    public Tournament fetchTournament(@Nonnull String formattedUrl) {
        log.info("Fetching tournament at url: " + formattedUrl);
        return new Tournament();
    }
    @Nonnull
    @Override
    public java.util.Set<dev.migwel.tournify.communication.commons.Player> getParticipants(@Nonnull String formattedUrl) {
        log.info("Fetching tournament at url: " + formattedUrl);
        return Collections.emptySet();
    }
}
