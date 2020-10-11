package dev.migwel.tournify.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.migwel.tournify.communication.commons.Update;
import dev.migwel.tournify.communication.request.NotificationRequest;
import dev.migwel.tournify.communication.response.NotificationResponse;
import dev.migwel.tournify.core.data.Notification;
import dev.migwel.tournify.core.data.Subscription;
import dev.migwel.tournify.core.store.NotificationRepository;
import dev.migwel.tournify.util.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Immutable
public class NotificationSender {

    private static final Logger log = LoggerFactory.getLogger(NotificationSender.class);

    private final static long SEC = 1000;
    private final static long MIN = 60 * SEC;
    private final static long NOTIFY_WAIT_MS = 5 * SEC;
    private final static long[] NO_UPDATE_WAIT_MS = {MIN, MIN, 5 * MIN, 5 * MIN};
    public static final String ACCEPTED = "accepted";

    private static final int NB_THREADS = 10; //If needed, make this configurable
    private final ExecutorService threadPool;

    private final NotificationRepository notificationRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PenaltyBox penaltyBox;

    public NotificationSender(NotificationRepository notificationRepository, RestTemplate restTemplate, ObjectMapper objectMapper, PenaltyBox penaltyBox) {
        this.notificationRepository = notificationRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.penaltyBox = penaltyBox;

        this.threadPool = Executors.newFixedThreadPool(NB_THREADS);
    }

    @Scheduled(fixedDelay = NOTIFY_WAIT_MS)
    private void startNotifying() {
        Collection<Notification> notificationList = notificationRepository.findByNextDateBeforeAndDone(new Date(), false);
        List<Future<?>> futureList = new ArrayList<>();
        for(Notification notification : notificationList) {
            futureList.add(threadPool.submit(() -> {
                log.info("Sending notification "+ notification.getId());
                processNotification(notification);
                notificationRepository.save(notification);
            }
            ));
        }
        for(Future<?> futureItem : futureList) {
            try {
                futureItem.get(20, TimeUnit.MINUTES);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                log.warn("An exception occurred while sending notification", e);
            }
        }
    }

    private void processNotification(Notification notification) {
        if(!isNotificationStillRelevant(notification)) {
            notification.setDone(true);
            return;
        }

        Subscription subscription = notification.getSubscription();
        if (!subscription.isActive()) {
            log.info("Subscription is no longer active: "+ subscription.getId());
            notification.setDone(true);
            return;
        }

        String callBackUrl = subscription.getCallbackUrl();
        if (callBackUrl == null) {
            log.warn("Callback URL is null for subscription "+ subscription);
            notification.setDone(true);
            return;
        }

        if(penaltyBox.isBlocked(callBackUrl)) {
            log.info("URL is penalty boxed: "+ callBackUrl);
            notification.setNextDate(computeNextDate(notification.getNoUpdateRetries()));
            return;
        }

        String username = subscription.getUsername();
        String password = subscription.getPassword();
        NotificationResponse response = sendNotification(callBackUrl, notification, username, password);
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
    NotificationResponse sendNotification(@Nonnull String callBackUrl, Notification notification, String username, String password) {
        Update update;
        try {
            byte[] contentByte = Base64.getDecoder().decode(notification.getContent());
            update = objectMapper.readValue(new String(contentByte, StandardCharsets.UTF_8), new TypeReference<Update>(){});
        } catch (IOException e) {
            log.warn("Could not deserialize notification: "+ notification.getContent(), e);
            return null;
        }

        NotificationRequest request = new NotificationRequest(update);
        HttpHeaders headers = null;
        if (TextUtil.hasText(username) && TextUtil.hasText(password)) {
            headers = createHeaders(username, password);
        }
        HttpEntity<NotificationRequest> entity = new HttpEntity<>(request,headers);
        try {
            return restTemplate.postForObject(callBackUrl, entity, NotificationResponse.class);
        }
        catch(RestClientException e) {
            log.info("Could not send notification: "+ notification.getId(), e);
        }
        return null;
    }

    private HttpHeaders createHeaders(String username, String password){
        HttpHeaders httpHeaders = new HttpHeaders();
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(
                auth.getBytes(StandardCharsets.UTF_8) );
        String authHeader = "Basic " + new String( encodedAuth );
        httpHeaders.set( "Authorization", authHeader );

        return httpHeaders;
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
