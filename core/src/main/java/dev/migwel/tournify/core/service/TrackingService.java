package dev.migwel.tournify.core.service;

import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.data.TournamentTracking;
import dev.migwel.tournify.core.store.TrackingRepository;
import org.springframework.stereotype.Service;

import javax.annotation.concurrent.Immutable;
import java.util.Date;

@Service
@Immutable
public class TrackingService {

    private final TrackingRepository trackingRepository;

    public TrackingService(TrackingRepository trackingRepository) {
        this.trackingRepository = trackingRepository;
    }

    public void trackTournament(Tournament tournament) {
        TournamentTracking tournamentTracking = new TournamentTracking(tournament, new Date(), new Date());
        trackingRepository.save(tournamentTracking);
    }
}
