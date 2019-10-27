package dev.migwel.tournify.smashgg.impl;

import dev.migwel.tournify.core.client.TournamentClient;
import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.service.AbstractTournamentService;
import dev.migwel.tournify.core.service.TrackingService;
import dev.migwel.tournify.core.service.UrlService;
import dev.migwel.tournify.core.store.TournamentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Collection;

@Service("SmashggTournamentService")
@Immutable
public class SmashggTournamentService extends AbstractTournamentService {

    private final UrlService urlService;
    private final TournamentClient tournamentClient;

    public SmashggTournamentService(TournamentRepository tournamentRepository,
                                    TrackingService trackingService,
                                    @Qualifier("SmashggUrlService") UrlService urlService,
                                    @Qualifier("SmashggClient") TournamentClient tournamentClient) {
        super(tournamentRepository, trackingService);
        this.urlService = urlService;
        this.tournamentClient = tournamentClient;
    }

    @Override
    @Nonnull
    public String normalizeUrl(String tournamentUrl) {
        return urlService.normalizeUrl(tournamentUrl);
    }

    @Override
    @Nullable
    public Tournament getTournament(String url) {
        String formattedUrl = normalizeUrl(url);
        return getTournament(tournamentClient, formattedUrl);
    }

    @Nonnull
    @Override
    public Collection<Player> getParticipants(String url) {
        String formattedUrl = normalizeUrl(url);
        return tournamentClient.getParticipants(formattedUrl);
    }

    @Override
    @Nullable
    protected Tournament fetchTournament(Tournament oldTournament, String url) {
        String formattedUrl = normalizeUrl(url);
        return fetchTournament(oldTournament, tournamentClient, formattedUrl);
    }
}