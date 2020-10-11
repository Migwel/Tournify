package dev.migwel.tournify.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.migwel.tournify.communication.commons.Set;
import dev.migwel.tournify.communication.commons.SetUpdate;
import dev.migwel.tournify.communication.commons.Update;
import dev.migwel.tournify.communication.response.NotificationResponse;
import dev.migwel.tournify.core.data.Notification;
import dev.migwel.tournify.core.store.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationSenderTest {

    private final NotificationRepository notificationRepository = mock(NotificationRepository.class);
    private final PenaltyBox penaltyBox = mock(PenaltyBox.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final NotificationSender notificationSender = new NotificationSender(notificationRepository,
            restTemplate,
            objectMapper,
            penaltyBox);

    @BeforeEach
    void before() {
        NotificationResponse validResponse = new NotificationResponse("accepted");
        when(restTemplate.postForObject(contains("validUrl"), any(), any())).thenReturn(validResponse);
        when(restTemplate.postForObject(contains("invalidUrl"), any(), any())).thenThrow(new RestClientException("Invalid URL"));
    }

    @Test
    void sendValidNotificationTest() throws JsonProcessingException {
        Set set = new Set("1", "tournamentName", "phaseName", Collections.emptyList(), Collections.emptyList(), "round", true);
        Update update = new SetUpdate(set, "Test Description");
        String contentStr = objectMapper.writeValueAsString(update);
        Notification notification = new Notification(null, Base64.getEncoder().encodeToString(contentStr.getBytes(StandardCharsets.UTF_8)), new Date(), new Date());
        NotificationResponse response = notificationSender.sendNotification("validUrl", notification);
        assertNotNull(response);
        assertEquals("accepted", response.getStatus());
    }

    @Test
    void sendInvalidNotificationTest() throws JsonProcessingException {
        Set set = new Set("1", "tournamentName", "phaseName", Collections.emptyList(), Collections.emptyList(), "round", true);
        Update update = new SetUpdate(set, "Test Description");
        String contentStr = objectMapper.writeValueAsString(update);
        Notification notification = new Notification(null, Base64.getEncoder().encodeToString(contentStr.getBytes(StandardCharsets.UTF_8)), new Date(), new Date());
        NotificationResponse response = notificationSender.sendNotification("invalidUrl", notification);
        assertNull(response);
    }

}