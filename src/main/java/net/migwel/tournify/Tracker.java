package net.migwel.tournify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.migwel.tournify.data.Notification;
import net.migwel.tournify.data.Subscription;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.data.TournamentTracking;
import net.migwel.tournify.service.ServiceFactory;
import net.migwel.tournify.service.TournamentService;
import net.migwel.tournify.store.NotificationRepository;
import net.migwel.tournify.store.SubscriptionRepository;
import net.migwel.tournify.store.TrackingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.concurrent.Immutable;
import java.util.Date;
import java.util.List;

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
        List<TournamentTracking> trackingList = trackingRepository.findByNextDateBeforeAndDone(new Date(), false);
        log.info("Tracking list size: "+ trackingList.size());
        for(TournamentTracking tracking : trackingList) {
            Tournament tournament = tracking.getTournament();
            TournamentService tournamentService;
            try {
                tournamentService = serviceFactory.getTournamentService(tournament.getUrl());
            }
            catch(IllegalArgumentException e) {
                continue;
            }

            Updates updates = tournamentService.updateTournament(tournament.getUrl()); //TODO: This should be done in a separate thread

            if(updates.getUpdateList().isEmpty()) {
                tracking.setNextDate(computeNextDate(tracking.getNoUpdateRetries()));
                tracking.setNoUpdateRetries(tracking.getNoUpdateRetries() + 1);
                if(tracking.getNoUpdateRetries() > NO_UPDATE_WAIT_MS.length - 1) { //TODO: add other ways of setting tracking to done (e.g. tournament is finished)
                    tracking.setDone(true);
                }
            }
            else {
                addNotification(tournament.getUrl(), updates);
                if(updates.isTournamentDone()) {
                    tracking.setDone(true);
                }
                else {
                    tracking.setNextDate(new Date());
                    tracking.setNoUpdateRetries(0);
                }
            }

            trackingRepository.save(tracking);
        }
    }

    private void addNotification(String tournamentUrl, Updates updates) {
        List<Subscription> subscriptionList = subscriptionRepository.findByTournamentUrlAndActive(tournamentUrl, true);
        String setUpdatesStr;
        try {
            setUpdatesStr = objectMapper.writeValueAsString(updates);
        } catch (JsonProcessingException e) {
            log.warn("Could not serialize updates "+ updates +": "+ e.getMessage());
            return;
        }
        for(Subscription subscription : subscriptionList) {
            Notification notification = new Notification(subscription, setUpdatesStr, new Date(), new Date());
            notificationRepository.save(notification);
        }
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
