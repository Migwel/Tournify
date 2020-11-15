package dev.migwel.tournify.chesscom.impl;

import dev.migwel.tournify.chesscom.client.ChesscomTournamentClient;
import dev.migwel.tournify.core.service.AbstractTournamentService;
import dev.migwel.tournify.core.service.TrackingService;
import dev.migwel.tournify.core.service.UrlService;
import dev.migwel.tournify.core.store.TournamentRepository;
import org.springframework.stereotype.Service;

@Service("ChesscomTournamentService")
public class ChesscomTournamentService extends AbstractTournamentService {

    public ChesscomTournamentService(TournamentRepository tournamentRepository, TrackingService trackingService, UrlService urlService, ChesscomTournamentClient tournamentClient) {
        super(tournamentRepository, trackingService, urlService, tournamentClient);
    }
}
