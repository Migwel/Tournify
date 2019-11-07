package dev.migwel.tournify.smashgg.impl;

import dev.migwel.tournify.core.service.AbstractTournamentService;
import dev.migwel.tournify.core.service.TrackingService;
import dev.migwel.tournify.core.store.TournamentRepository;
import dev.migwel.tournify.smashgg.client.SmashggClient;
import org.springframework.stereotype.Service;

import javax.annotation.concurrent.Immutable;

@Service
@Immutable
public class SmashggTournamentService extends AbstractTournamentService {

    public SmashggTournamentService(TournamentRepository tournamentRepository,
                                    TrackingService trackingService,
                                    SmashggUrlService urlService,
                                    SmashggClient tournamentClient) {
        super(tournamentRepository, trackingService, urlService, tournamentClient);
    }
}
