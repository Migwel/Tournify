package dev.migwel.tournify.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.migwel.tournify.app.service.ServiceFactory;
import dev.migwel.tournify.communication.commons.Player;
import dev.migwel.tournify.communication.commons.Update;
import dev.migwel.tournify.communication.commons.Updates;
import dev.migwel.tournify.core.data.Notification;
import dev.migwel.tournify.core.data.Subscription;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.data.TournamentTracking;
import dev.migwel.tournify.core.service.TournamentService;
import dev.migwel.tournify.core.store.NotificationRepository;
import dev.migwel.tournify.core.store.SubscriptionRepository;
import dev.migwel.tournify.core.store.TrackingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.CheckForNull;
import javax.annotation.concurrent.Immutable;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

@Component
@Immutable
public class Tracker { //TODO: Tracking should be more fine-grained (events or sets)

    private static final Logger log = LoggerFactory.getLogger(Tracker.class);

    private final static long SEC = 1000;
    private final static long MIN = 60 * SEC;
    private final static long HOUR = 60 * MIN;
    private final static long DAY = 24 * HOUR;
    private final static long WEEK = 7 * DAY;
    private final static long TRACKING_WAIT_MS = 5 * SEC;
    private final static long[] NO_UPDATE_WAIT_MS = {
            MIN, MIN, MIN, MIN, MIN,
            2 * MIN, 2 * MIN, 2 * MIN, 2 * MIN, 2 * MIN, 2 * MIN,
            5 * MIN, 5 * MIN, 5 * MIN, 5 * MIN, 5 * MIN, 5 * MIN, 5 * MIN,
            15 * MIN, 15 * MIN, 15 * MIN, 15 * MIN, 15 * MIN,
            30 * MIN, 30 * MIN, 30 * MIN, 30 * MIN, 30 * MIN,
            HOUR, HOUR, HOUR, HOUR, HOUR, HOUR, HOUR, HOUR, HOUR, HOUR, HOUR, HOUR, HOUR, HOUR, HOUR, HOUR, HOUR, HOUR,
            HOUR, HOUR, HOUR, HOUR, HOUR, HOUR,
            DAY, DAY, DAY,
            WEEK
    };

    private final TrackingRepository trackingRepository;

    private final SubscriptionRepository subscriptionRepository;

    private final NotificationRepository notificationRepository;

    private final ServiceFactory serviceFactory;

    private final ObjectMapper objectMapper;

    public Tracker(TrackingRepository trackingRepository, SubscriptionRepository subscriptionRepository, NotificationRepository notificationRepository, ServiceFactory serviceFactory, ObjectMapper objectMapper) {
        this.trackingRepository = trackingRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.notificationRepository = notificationRepository;
        this.serviceFactory = serviceFactory;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = TRACKING_WAIT_MS)
    private void startTracking() {
        Collection<TournamentTracking> trackingList = trackingRepository.findByNextDateBeforeAndDone(new Date(), false);
        log.info("Tracking list size: "+ trackingList.size());
        for(TournamentTracking tracking : trackingList) {
            Tournament tournament = tracking.getTournament();
            trackTournament(tracking, tournament);
            trackingRepository.save(tracking);
        }
    }

    private void trackTournament(TournamentTracking tracking, Tournament tournament) {
        if(tournament.getDate() != null && tournament.getDate().after(new Date())) {
            tracking.setNextDate(tournament.getDate());
            return;
        }

        TournamentService tournamentService = serviceFactory.getTournamentService(tournament.getUrl());
        Updates updates = tournamentService.updateTournament(tournament.getUrl()); //TODO: This should be done in a separate thread

        if(updates.getUpdateList().isEmpty()) {
            tracking.setNextDate(computeNextDate(tracking.getNoUpdateRetries()));
            tracking.setNoUpdateRetries(tracking.getNoUpdateRetries() + 1);
            if(tracking.getNoUpdateRetries() > NO_UPDATE_WAIT_MS.length - 1) {
                tracking.setDone(true);
            }
            return;
        }

        addNotification(tournament.getUrl(), updates);
        if(updates.isTournamentDone()) {
            tracking.setDone(true);
            return;
        }

        tracking.setNextDate(computeNextDate(0));
        tracking.setNoUpdateRetries(0);
    }

    private void addNotification(String tournamentUrl, Updates updates) {
        Collection<Subscription> subscriptionList = subscriptionRepository.findByTournamentUrlAndActive(tournamentUrl, true);
        if(updates == null) {
            return;
        }


        for(Update update : updates.getUpdateList()) {
            for (Subscription subscription : subscriptionList) {
                if(!relevantUpdate(update, subscription)) {
                    continue;
                }

                String updateStr = updateToString(update);
                if(updateStr == null) {
                    continue;
                }
                Notification notification = new Notification(subscription, Base64.getEncoder().encodeToString(updateStr.getBytes()), new Date(), new Date());
                notificationRepository.save(notification);
            }
        }
    }

    private boolean relevantUpdate(Update update, Subscription subscription) {
        if(update.getSet() == null || update.getSet().getPlayers().isEmpty()) {
            return true;
        }
        if(subscription.getPlayers().isEmpty()) {
            return true;
        }

        Collection<String> followedPlayers = subscription.getPlayers();
        for(Player setPlayer : update.getSet().getPlayers()) {
            if(followedPlayers.contains(setPlayer.getDisplayUsername())) {
                return true;
            }
        }

        return false;
    }

    @CheckForNull
    private String updateToString(Update update) {
        String updateStr = null;
        try {
            updateStr = objectMapper.writeValueAsString(update);
        } catch (JsonProcessingException e) {
            log.warn("Could not serialize update "+ update +": "+ e.getMessage());
        }
        return updateStr;
    }

    private Date computeNextDate(int noUpdateRetries) {
        return new Date(System.currentTimeMillis() + computeTimeToAdd(noUpdateRetries));
    }

    private long computeTimeToAdd(int noUpdateRetries) {
        if(noUpdateRetries >= NO_UPDATE_WAIT_MS.length) {
            return NO_UPDATE_WAIT_MS[NO_UPDATE_WAIT_MS.length - 1];
        }

        return NO_UPDATE_WAIT_MS[noUpdateRetries];
    }
}
