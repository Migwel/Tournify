package net.migwel.tournify.controller;

import net.migwel.tournify.data.Subscription;
import net.migwel.tournify.request.SubscriptionRequest;
import net.migwel.tournify.response.SubscriptionResponse;
import net.migwel.tournify.service.SubscriptionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/subscribe")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @RequestMapping(method= RequestMethod.POST)
    public SubscriptionResponse addSubscription(@RequestBody SubscriptionRequest request) throws Exception {
        Subscription subscription = subscriptionService.addSubscription(request.getTournamentUrl(), request.getCallbackUrl());
        SubscriptionResponse subscriptionResponse = new SubscriptionResponse(subscription.getId().toString(),
                                                                             subscription.getTournament().getUrl(),
                                                                             subscription.getCallbackUrl());

        return subscriptionResponse;
    }

    @RequestMapping(value = "/{id}", method= RequestMethod.DELETE)
    public void deleteSubscription(@PathVariable String id) throws Exception {
        subscriptionService.inactivateSubscription(UUID.fromString(id));
    }
}
