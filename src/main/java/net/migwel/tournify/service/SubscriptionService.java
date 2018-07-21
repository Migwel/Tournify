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

    public Subscription addSubscription(String tournamentUrl, String callbackUrl) throws Exception {
        Subscription subscription = subscriptionRepository.findByTournamentUrlAndCallbackUrl(tournamentUrl, callbackUrl);
        if(subscription != null) {
            return  subscription;
        }

        TournamentService tournamentService = serviceFactory.getTournamentService(tournamentUrl);
        if(tournamentService == null) {
            throw new Exception("Couldn't find any service for url: "+ tournamentUrl); //TODO: Send more specific exception (and send it inside serviceFactory?)
        }
        Tournament tournament = tournamentService.getTournament(tournamentUrl);

        subscription = new Subscription(tournament, callbackUrl);
        subscriptionRepository.save(subscription);

        return subscription;
    }
}
