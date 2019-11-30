package dev.migwel.tournify.challonge.client;

import dev.migwel.tournify.challonge.impl.ChallongeUrlService;
import dev.migwel.tournify.challonge.response.ParticipantsResponse;
import dev.migwel.tournify.communication.commons.Player;
import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.util.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class ChallongeClient implements TournamentClient {

    private static final Logger log = LoggerFactory.getLogger(ChallongeClient.class);

    private final ChallongeFetcher challongeFetcher;
    private final ChallongeUrlService challongeUrlService;

    public ChallongeClient(ChallongeFetcher challongeFetcher, ChallongeUrlService challongeUrlService) {
        this.challongeFetcher = challongeFetcher;
        this.challongeUrlService = challongeUrlService;
    }

    @Override
    @Nonnull
    public Tournament fetchTournament(@Nonnull String formattedUrl) {
        log.info("Fetching tournament at url: " + formattedUrl);
        return new Tournament();
    }
    @Nonnull
    @Override
    public java.util.Set<dev.migwel.tournify.communication.commons.Player> getParticipants(@Nonnull String formattedUrl) throws FetchException {
        log.info("Fetching tournament at url: " + formattedUrl);
        String getParticipantsUrl = challongeUrlService.buildParticipantsUrl(formattedUrl);
        ParticipantsResponse[] participantResponses = challongeFetcher.fetchWithRetries(getParticipantsUrl, ParticipantsResponse[].class);
        return convertChallongeParticipantResponse(participantResponses);
    }

    private Set<Player> convertChallongeParticipantResponse(@Nonnull ParticipantsResponse[] participantResponses) {
        final Set<Player> players = new HashSet<>();
        for(ParticipantsResponse participantsResponse : participantResponses) {
            Optional<String> participantUserName = getParticipantUserName(participantsResponse);
            participantUserName.ifPresent(e -> players.add(new Player(e)));
        }
        return players;
    }

    private Optional<String> getParticipantUserName(ParticipantsResponse participantsResponse) {
        if (participantsResponse == null) {
            return Optional.empty();
        }
        if (participantsResponse.getParticipant() == null) {
            return Optional.empty();
        }
        if (TextUtil.isEmptyOrNull(participantsResponse.getParticipant().getDisplayName())) {
            return Optional.empty();
        }

        return Optional.of(participantsResponse.getParticipant().getDisplayName());
    }
}
