package dev.migwel.tournify.challonge.impl;

import dev.migwel.tournify.challonge.client.ChallongeClient;
import dev.migwel.tournify.core.service.AbstractTournamentService;
import dev.migwel.tournify.core.service.TrackingService;
import dev.migwel.tournify.core.store.TournamentRepository;
import org.springframework.stereotype.Service;

@Service("ChallongeTournamentService")
public class ChallongeTournamentService extends AbstractTournamentService {

    public ChallongeTournamentService(TournamentRepository tournamentRepository,
                                      TrackingService trackingService,
                                      ChallongeUrlService urlService,
                                      ChallongeClient tournamentClient) {
        super(tournamentRepository, trackingService, urlService, tournamentClient);
    }
}
