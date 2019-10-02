package net.migwel.tournify.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.migwel.tournify.app.service.ServiceFactory;
import net.migwel.tournify.communication.commons.Update;
import net.migwel.tournify.communication.request.NotificationRequest;
import net.migwel.tournify.communication.response.NotificationResponse;
import net.migwel.tournify.core.data.Notification;
import net.migwel.tournify.core.data.Subscription;
import net.migwel.tournify.core.store.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.CheckForNull;
import javax.annotation.concurrent.Immutable;
import java.io.IOException;
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

    private final NotificationRepository notificationRepository;

    private final ServiceFactory serviceFactory;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public NotificationSender(NotificationRepository notificationRepository, ServiceFactory serviceFactory, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;
        this.serviceFactory = serviceFactory;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = NOTIFY_WAIT_MS)
    private void startNotifying() {
        Collection<Notification> notificationList = notificationRepository.findByNextDateBeforeAndDone(new Date(), false);
        log.info("Notification list size: "+ notificationList.size());
        for(Notification notification : notificationList) {
            if(notification.getNoUpdateRetries() > NO_UPDATE_WAIT_MS.length - 1) {
                notification.setDone(true);
            }
            else {
                NotificationResponse response = sendNotification(notification);
                if (response != null && "accepted".equals(response.getStatus())) {
                    notification.setDone(true);
                } else {
                    notification.setNextDate(computeNextDate(notification.getNoUpdateRetries()));
                    notification.setNoUpdateRetries(notification.getNoUpdateRetries() + 1);
                }
            }

            notificationRepository.save(notification);
        }
    }

    @CheckForNull
    private NotificationResponse sendNotification(Notification notification) {
        String callBackUrl = getCallbackUrl(notification.getSubscription());
        if(callBackUrl == null) {
            return null;
        }

        Update update;
        try {
            byte[] contentByte = Base64.getDecoder().decode(notification.getContent());
            update = objectMapper.readValue(new String(contentByte), new TypeReference<Update>(){});
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
