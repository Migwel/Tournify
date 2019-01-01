package net.migwel.tournify.service;

import net.migwel.tournify.data.Subscription;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.store.SubscriptionRepository;
import net.migwel.tournify.store.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SubscriptionService {

    private final static Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;

    private final TournamentRepository tournamentRepository;

    private final TrackingService trackingService;

    private final ServiceFactory serviceFactory;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, TournamentRepository tournamentRepository, TrackingService trackingService, ServiceFactory serviceFactory) {
        this.subscriptionRepository = subscriptionRepository;
        this.tournamentRepository = tournamentRepository;
        this.trackingService = trackingService;
        this.serviceFactory = serviceFactory;
    }

    public Subscription addSubscription(String tournamentUrl, String callbackUrl) {
        TournamentService tournamentService = serviceFactory.getTournamentService(tournamentUrl);
        String normalizedTournamentUrl = tournamentService.normalizeUrl(tournamentUrl);
        Subscription subscription = subscriptionRepository.findByCallbackUrlAndTournamentUrl(callbackUrl, normalizedTournamentUrl);
        if(subscription != null) {
            log.info("Subscription already exists for callBackUrl: "+ callbackUrl +" and tournamentUrl: "+ normalizedTournamentUrl);
            return  subscription;
        }

        Tournament tournament = tournamentRepository.findByUrl(normalizedTournamentUrl);
        if(tournament == null) {
            tournament = createAndTrackTournament(normalizedTournamentUrl);
        }

        subscription = new Subscription(tournament, callbackUrl, true);
        subscriptionRepository.save(subscription);

        return subscription;
    }

    private Tournament createAndTrackTournament(String normalizedTournamentUrl) {
        Tournament tournament = new Tournament(normalizedTournamentUrl);
        tournamentRepository.save(tournament);
        trackingService.trackTournament(tournament);
        return tournament;
    }

    public void deleteSubscription(UUID id) {
        Subscription subscription = subscriptionRepository.findById(id);
        if(subscription == null) {
            log.info("Subscription with id : "+ id +" doesn't exist");
            return;
        }

        subscriptionRepository.delete(subscription);
    }
}
