package net.migwel.tournify.smashgg.service.impl;

import net.migwel.tournify.client.TournamentClient;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.service.TournamentService;
import net.migwel.tournify.service.TrackingService;
import net.migwel.tournify.service.UrlService;
import net.migwel.tournify.store.TournamentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("SmashggTournamentService")
public class SmashggTournamentService extends TournamentService {

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
    public String normalizeUrl(String tournamentUrl) {
        return urlService.normalizeUrl(tournamentUrl);
    }

    @Override
    public Tournament getTournament(String url) {
        String formattedUrl = normalizeUrl(url);
        return getTournament(tournamentClient, formattedUrl);
    }

    @Override
    protected Tournament fetchTournament(String url) {
        String formattedUrl = normalizeUrl(url);
        return fetchTournament(tournamentClient, formattedUrl);
    }
}
