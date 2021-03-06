package dev.migwel.tournify.app.controller;

import dev.migwel.tournify.app.exception.SubscriptionException;
import dev.migwel.tournify.app.service.SubscriptionService;
import dev.migwel.tournify.communication.request.SubscriptionRequest;
import dev.migwel.tournify.communication.response.SubscriptionResponse;
import dev.migwel.tournify.core.data.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/subscribe")
public class SubscriptionController {

    private final static Logger log = LoggerFactory.getLogger(SubscriptionController.class);
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @RequestMapping(method= RequestMethod.POST)
    public SubscriptionResponse addSubscription(@RequestBody SubscriptionRequest request) {
        Subscription subscription;
        try {
            subscription = subscriptionService.addSubscription(request.getTournamentUrl(), request.getCallbackUrl(), request.getPlayers(), request.getUsername(), request.getPassword());
        } catch (SubscriptionException e) {
            log.warn("Could not subscribe to "+ request.getTournamentUrl(), e);
            return SubscriptionResponse.error();
        }
        return new SubscriptionResponse(subscription.getId().toString(),
                                        subscription.getTournament().getUrl(),
                                        subscription.getCallbackUrl());
    }

    @RequestMapping(value = "/{id}", method= RequestMethod.DELETE)
    public void deleteSubscription(@PathVariable String id) {
        subscriptionService.inactivateSubscription(UUID.fromString(id));
    }
}
