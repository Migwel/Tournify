package dev.migwel.tournify.smashgg.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.core.http.HttpClient;
import dev.migwel.tournify.smashgg.config.SmashggConfiguration;
import dev.migwel.tournify.smashgg.response.SmashggResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Component
public class SmashggFetcher {
    private static final Logger log = LoggerFactory.getLogger(SmashggFetcher.class);

    private final SmashggConfiguration configuration;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public SmashggFetcher(SmashggConfiguration configuration, HttpClient httpClient, ObjectMapper objectMapper) {
        this.configuration = configuration;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    <T> T fetchWithRetries(String request, Class<? extends SmashggResponse> responseClass) throws FetchException {
        for (int i = 0; i < configuration.getRetryNumber(); i++) {
            try {
                return fetch(request, responseClass);
            } catch (FetchException e) {
                log.info("An error occurred while fetching the data", e);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e1) {
                    //
                }
            }
        }
        log.warn("Could not execute the fetch, no retries left");
        throw new FetchException("Could not execute request after " + configuration.getRetryNumber() + " retries", false);
    }

    @Nonnull
    private <T> T fetch(String request, Class<? extends SmashggResponse> responseClass) throws FetchException {
        String responseStr = postRequest(request);
        return parseResponse(responseClass, responseStr);
    }

    private String postRequest(String request) throws FetchException {
        Collection<Pair<String, String>> headers = Collections.singleton(Pair.of("Authorization", "Bearer " + configuration.getApiToken()));
        try {
            return httpClient.postRequest(request, configuration.getApiUrl(), headers);
        }
        catch (HTTPException e) {
            boolean retryable = httpClient.isRetryable(e);
            log.warn("HttpException while posting request", e);
            throw new FetchException("HttpException while posting request", e, retryable);
        }
    }

    private <T> T parseResponse(Class<? extends SmashggResponse> responseClass, String responseStr) throws FetchException {
        try {
            @SuppressWarnings("unchecked")
            SmashggResponse<T> response = responseClass.cast(objectMapper.readValue(responseStr, responseClass));
            if(response == null || response.getData() == null || response.getData().getObject() == null) {
                log.warn("Could not cast response: "+ responseStr);
                throw new FetchException("Error while casting response", false);
            }
            return response.getData().getObject();
        } catch (IOException e) {
            log.warn("Could not convert JSON response "+ responseStr +" to "+ responseClass, e);
            throw new FetchException("Error while casting response", e, false);
        }
    }
}