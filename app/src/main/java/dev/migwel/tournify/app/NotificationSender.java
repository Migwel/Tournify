package dev.migwel.tournify.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.migwel.tournify.app.service.TournamentServiceFactory;
import dev.migwel.tournify.communication.commons.Update;
import dev.migwel.tournify.communication.request.NotificationRequest;
import dev.migwel.tournify.communication.response.NotificationResponse;
import dev.migwel.tournify.core.data.Notification;
import dev.migwel.tournify.core.data.Subscription;
import dev.migwel.tournify.core.store.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

@Component
@Immutable
public class NotificationSender {

    private static final Logger log = LoggerFactory.getLogger(NotificationSender.class);

    private final static long SEC = 1000;
    private final static long MIN = 60 * SEC;
    private final static long NOTIFY_WAIT_MS = 5 * SEC;
    private final static long[] NO_UPDATE_WAIT_MS = {MIN, MIN, 5 * MIN, 5 * MIN};
    public static final String ACCEPTED = "accepted";

    private final NotificationRepository notificationRepository;
    private final TournamentServiceFactory tournamentServiceFactory;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PenaltyBox penaltyBox;

    public NotificationSender(NotificationRepository notificationRepository, TournamentServiceFactory tournamentServiceFactory, RestTemplate restTemplate, ObjectMapper objectMapper, PenaltyBox penaltyBox) {
        this.notificationRepository = notificationRepository;
        this.tournamentServiceFactory = tournamentServiceFactory;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.penaltyBox = penaltyBox;
    }

    @Scheduled(fixedDelay = NOTIFY_WAIT_MS)
    private void startNotifying() {
        Collection<Notification> notificationList = notificationRepository.findByNextDateBeforeAndDone(new Date(), false);
        for(Notification notification : notificationList) {
            log.info("Sending notification "+ notification.getId());
            processNotification(notification);
            notificationRepository.save(notification);
        }
    }

    private void processNotification(Notification notification) {
        if(!isNotificationStillRelevant(notification)) {
            notification.setDone(true);
            return;
        }
        String callBackUrl = getCallbackUrl(notification.getSubscription());
        if (callBackUrl == null) {
            log.warn("Callback URL is null for subscription "+ notification.getSubscription());
            notification.setDone(true);
            return;
        }

        if(penaltyBox.isBlocked(callBackUrl)) {
            log.info("URL is penalty boxed: "+ callBackUrl);
            notification.setNextDate(computeNextDate(notification.getNoUpdateRetries()));
            return;
        }

        NotificationResponse response = sendNotification(callBackUrl, notification);
        if (response != null && ACCEPTED.equals(response.getStatus())) {
            notification.setDone(true);
            penaltyBox.success(callBackUrl);
        } else {
            notification.setNextDate(computeNextDate(notification.getNoUpdateRetries()));
            notification.setNoUpdateRetries(notification.getNoUpdateRetries() + 1);
            penaltyBox.failure(callBackUrl);
        }
    }

    private boolean isNotificationStillRelevant(Notification notification) {
        if(!notification.getSubscription().isActive()) {
            return false;
        }

        if(notification.getNoUpdateRetries() > NO_UPDATE_WAIT_MS.length - 1) {
            return false;
        }

        return true;
    }

    @CheckForNull
    private NotificationResponse sendNotification(@Nonnull String callBackUrl, Notification notification) {
        Update update;
        try {
            byte[] contentByte = Base64.getDecoder().decode(notification.getContent());
            update = objectMapper.readValue(new String(contentByte, StandardCharsets.UTF_8), new TypeReference<Update>(){});
        } catch (IOException e) {
            log.warn("Could not deserialize notification: "+ notification.getContent(), e);
            return null;
        }

        NotificationRequest request = new NotificationRequest(update);
        try {
            return restTemplate.postForObject(callBackUrl, request, NotificationResponse.class);
        }
        catch(RestClientException e) {
            log.info("Could not send notification: "+ e.getMessage());
        }
        return null;
    }

    @CheckForNull
    private String getCallbackUrl(Subscription subscription) {
        if(!subscription.isActive()) {
            return null;
        }

        return subscription.getCallbackUrl();
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
