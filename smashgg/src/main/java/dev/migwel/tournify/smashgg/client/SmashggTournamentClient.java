package dev.migwel.tournify.smashgg.client;

import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.data.Address;
import dev.migwel.tournify.core.data.GameType;
import dev.migwel.tournify.core.data.Phase;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.smashgg.response.SmashggEvent;
import dev.migwel.tournify.smashgg.response.SmashggTournament;
import dev.migwel.tournify.smashgg.util.SmashggUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collection;
import java.util.Date;


@Component("SmashggClient")
@Immutable
public class SmashggTournamentClient implements TournamentClient {

    private static final Logger log = LoggerFactory.getLogger(SmashggTournamentClient.class);

    private final SmashggEventFetcher smashggEventFetcher;
    private final SmashggPhaseFetcher smashggPhaseFetcher;
    private final SmashggParticipantsFetcher smashggParticipantsFetcher;

    public SmashggTournamentClient(SmashggEventFetcher smashggEventFetcher, SmashggPhaseFetcher smashggPhaseFetcher, SmashggParticipantsFetcher smashggParticipantsFetcher) {
        this.smashggEventFetcher = smashggEventFetcher;
        this.smashggPhaseFetcher = smashggPhaseFetcher;
        this.smashggParticipantsFetcher = smashggParticipantsFetcher;
    }

    @Override
    @Nonnull
    public Tournament fetchTournament(@Nonnull String formattedUrl) throws FetchException {
        log.info("Fetching tournament at url: " + formattedUrl);
        SmashggEvent smashggEvent = smashggEventFetcher.fetchEvent(formattedUrl);
        Collection<Phase> tournamentPhases = smashggPhaseFetcher.fetchPhases(formattedUrl, smashggEvent);
        log.info("Done with fetching tournament at url: "+ formattedUrl);


        return new Tournament(String.valueOf(smashggEvent.getId()),
                tournamentPhases,
                smashggEvent.getTournament().getName() + " - "+ smashggEvent.getName(),
                new GameType(smashggEvent.getVideogame().getDisplayName()),
                buildAddress(smashggEvent.getTournament()),
                formattedUrl,
                new Date(smashggEvent.getStartAt()*1000),
                isTournamentDone(tournamentPhases));
    }

    private boolean isTournamentDone(Collection<Phase> tournamentPhases) {
        boolean tournamentDone = true;
        if(!tournamentPhases.stream().allMatch(Phase::isDone)) {
            tournamentDone = false;
        }
        return tournamentDone;
    }

    @Nonnull
    @Override
    public java.util.Set<dev.migwel.tournify.communication.commons.Player> getParticipants(@Nonnull String formattedUrl) throws FetchException {
        log.info("Fetching tournament at url: " + formattedUrl);
        String eventSlug = SmashggUtil.findEventSlug(formattedUrl);
        return smashggParticipantsFetcher.fetchParticipants(eventSlug);
    }

    private Address buildAddress(SmashggTournament tournament) {
        return new Address(tournament.getCity(), tournament.getAddrState(), tournament.getVenueAddress(), null, tournament.getCountryCode());
    }

}