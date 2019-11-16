package dev.migwel.tournify.app.service;

import dev.migwel.tournify.core.data.Subscription;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.service.TournamentService;
import dev.migwel.tournify.core.service.TrackingService;
import dev.migwel.tournify.core.store.SubscriptionRepository;
import dev.migwel.tournify.core.store.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.UUID;

@Service
@Immutable
public class SubscriptionService {

    private final static Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;

    private final TournamentRepository tournamentRepository;

    private final TrackingService trackingService;

    private final TournamentServiceFactory tournamentServiceFactory;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               TournamentRepository tournamentRepository,
                               TrackingService trackingService,
                               TournamentServiceFactory tournamentServiceFactory) {
        this.subscriptionRepository = subscriptionRepository;
        this.tournamentRepository = tournamentRepository;
        this.trackingService = trackingService;
        this.tournamentServiceFactory = tournamentServiceFactory;
    }

    public Subscription addSubscription(String tournamentUrl, String callbackUrl, List<String> players) {
        TournamentService tournamentService = tournamentServiceFactory.getTournamentService(tournamentUrl);
        String normalizedTournamentUrl = tournamentService.normalizeUrl(tournamentUrl);
        Subscription subscription = subscriptionRepository.findByCallbackUrlAndTournamentUrl(callbackUrl, normalizedTournamentUrl);
        if(subscription != null) {
            return updateSubscription(subscription, players);
        }

        Tournament tournament = tournamentRepository.findByUrl(normalizedTournamentUrl);
        if(tournament == null) {
            tournament = createAndTrackTournament(normalizedTournamentUrl);
        }

        subscription = new Subscription(tournament, callbackUrl, players, true);
        subscriptionRepository.save(subscription);

        return subscription;
    }

    private Subscription updateSubscription(Subscription subscription, List<String> players) {
        if(!subscription.isActive()) {
            reactivateSubscription(subscription, players);
            return subscription;
        }
        List<String> followedPlayers = subscription.getPlayers();
        if(followedPlayers.isEmpty()) {
            return subscription;
        }

        if(players.isEmpty()) {
            subscription.setPlayers(null);
            subscriptionRepository.save(subscription);
            return subscription;
        }
        else {
            followedPlayers.addAll(players);
        }
        subscription.setPlayers(followedPlayers);
        subscriptionRepository.save(subscription);
        return subscription;
    }

    private void reactivateSubscription(Subscription subscription, List<String> players) {
        subscription.setPlayers(players);
        subscription.setActive(true);
        subscriptionRepository.save(subscription);
    }

    private Tournament createAndTrackTournament(String normalizedTournamentUrl) {
        Tournament tournament = new Tournament(normalizedTournamentUrl);
        tournamentRepository.save(tournament);
        trackingService.trackTournament(tournament);
        return tournament;
    }

    public void inactivateSubscription(UUID id) {
        Subscription subscription = subscriptionRepository.findById(id);
        if(subscription == null) {
            log.info("Subscription with id : "+ id +" doesn't exist");
            return;
        }

        subscription.setActive(false);
        subscriptionRepository.save(subscription);
    }
}
