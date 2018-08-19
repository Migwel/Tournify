package net.migwel.tournify.service;

import net.migwel.tournify.data.Subscription;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.store.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ServiceFactory serviceFactory;

    public Subscription addSubscription(String tournamentUrl, String callbackUrl) {
        Subscription subscription = subscriptionRepository.findByTournamentUrlAndCallbackUrl(tournamentUrl, callbackUrl);
        if(subscription != null) {
            return  subscription;
        }

        TournamentService tournamentService = serviceFactory.getTournamentService(tournamentUrl);
        Tournament tournament = tournamentService.getTournament(tournamentUrl);

        subscription = new Subscription(tournament, callbackUrl, true);
        subscriptionRepository.save(subscription);

        return subscription;
    }
}
