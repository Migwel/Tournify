package net.migwel.tournify.service;

import net.migwel.tournify.data.Subscription;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.store.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    private final static Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ServiceFactory serviceFactory;

    public Subscription addSubscription(String tournamentUrl, String callbackUrl) {
        TournamentService tournamentService = serviceFactory.getTournamentService(tournamentUrl);
        String normalizedTournamentUrl = tournamentService.normalizeUrl(tournamentUrl);
        Subscription subscription = subscriptionRepository.findByCallbackUrlAndTournamentUrl(callbackUrl, normalizedTournamentUrl);
        if(subscription != null) {
            log.info("Subscription already exists for callBackUrl: "+ callbackUrl +" and tournamentUrl: "+ normalizedTournamentUrl);
            return  subscription;
        }

        Tournament tournament = tournamentService.getTournament(normalizedTournamentUrl);

        subscription = new Subscription(tournament, callbackUrl, true);
        subscriptionRepository.save(subscription);

        return subscription;
    }

    public void deleteSubscription(String tournamentUrl, String callbackUrl) {
        TournamentService tournamentService = serviceFactory.getTournamentService(tournamentUrl);
        String normalizedTournamentUrl = tournamentService.normalizeUrl(tournamentUrl);
        Subscription subscription = subscriptionRepository.findByCallbackUrlAndTournamentUrl(callbackUrl, normalizedTournamentUrl);
        if(subscription == null) {
            log.info("Subscription doesn't exist for callBackUrl: "+ callbackUrl +" and tournamentUrl: "+ normalizedTournamentUrl);
            return;
        }

        subscriptionRepository.delete(subscription);
    }
}
