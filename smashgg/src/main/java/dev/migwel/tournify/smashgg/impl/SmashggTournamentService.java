package dev.migwel.tournify.smashgg.impl;

import dev.migwel.tournify.core.service.AbstractTournamentService;
import dev.migwel.tournify.core.service.TrackingService;
import dev.migwel.tournify.core.store.TournamentRepository;
import dev.migwel.tournify.smashgg.client.SmashggTournamentClient;
import org.springframework.stereotype.Service;

import javax.annotation.concurrent.Immutable;

@Service("SmashggTournamentService")
@Immutable
public class SmashggTournamentService extends AbstractTournamentService {

    public SmashggTournamentService(TournamentRepository tournamentRepository,
                                    TrackingService trackingService,
                                    SmashggUrlService urlService,
                                    SmashggTournamentClient tournamentClient) {
        super(tournamentRepository, trackingService, urlService, tournamentClient);
    }
}
