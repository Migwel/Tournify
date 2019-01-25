package net.migwel.tournify.smashgg.service.impl;

import net.migwel.tournify.client.TournamentClient;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.service.AbstractTournamentService;
import net.migwel.tournify.service.TrackingService;
import net.migwel.tournify.service.UrlService;
import net.migwel.tournify.store.TournamentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Service("SmashggTournamentService")
@Immutable
public class SmashggTournamentService extends AbstractTournamentService {

    private final UrlService urlService;
    private final TournamentClient tournamentClient;

    public SmashggTournamentService(TournamentRepository tournamentRepository,
                                    TrackingService trackingService,
                                    @Qualifier("SmashggUrlService") UrlService urlService,
                                    @Qualifier("NewSmashggClient") TournamentClient tournamentClient) {
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

    @Override
    @Nullable
    protected Tournament fetchTournament(String url) {
        String formattedUrl = normalizeUrl(url);
        return fetchTournament(tournamentClient, formattedUrl);
    }
}
