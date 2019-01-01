package net.migwel.tournify.service;

import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.data.TournamentTracking;
import net.migwel.tournify.store.TrackingRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
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
