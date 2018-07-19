package net.migwel.tournify.controller;

import net.migwel.tournify.SubscriptionResponse;
import net.migwel.tournify.data.Subscription;
import net.migwel.tournify.request.SubscriptionRequest;
import net.migwel.tournify.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscribe")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @RequestMapping(method= RequestMethod.PUT)
    public SubscriptionResponse addSubscription(@RequestBody SubscriptionRequest request) {
        Subscription subscription = subscriptionService.addSubscription(request.getTournamentUrl(), request.getCallbackUrl());
        SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
        subscriptionResponse.setTournamentUrl(subscription.getTournament().getUrl());
        subscriptionResponse.setCallbackUrl(subscription.getCallbackUrl());

        return subscriptionResponse;

    }
}
