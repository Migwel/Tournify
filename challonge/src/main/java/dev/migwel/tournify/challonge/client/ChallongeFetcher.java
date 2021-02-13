package dev.migwel.tournify.challonge.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.migwel.tournify.challonge.config.ChallongeConfiguration;
import dev.migwel.tournify.challonge.impl.ChallongeUrlService;
import dev.migwel.tournify.core.exception.FetchException;
import dev.migwel.tournify.core.http.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.util.Collections;

@Component
public class ChallongeFetcher {
    private static final Logger log = LoggerFactory.getLogger(ChallongeFetcher.class);

    private final ChallongeConfiguration configuration;
    private final ChallongeUrlService challongeUrlService;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ChallongeFetcher(ChallongeConfiguration configuration, ChallongeUrlService challongeUrlService, HttpClient httpClient, ObjectMapper objectMapper) {
        this.configuration = configuration;
        this.challongeUrlService = challongeUrlService;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Nonnull
    <T> T fetchWithRetries(String url, Class<T> responseClass) throws FetchException {
        String urlWithToken = challongeUrlService.addUsernamePasswordToUrl(url, configuration.getUsername(), configuration.getApiToken());
        for (int i = 0; i < configuration.getRetryNumber(); i++) {
            try {
                return fetch(urlWithToken, responseClass);
            } catch (FetchException e) {
                log.info("An error occurred while fetching the data", e);
                if (!e.isRetryable()) {
                    throw e;
                }
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
    private <T> T fetch(String request, Class<T> responseClass) throws FetchException {
        String responseStr = get(request);
        return parseResponse(responseClass, responseStr);
    }

    private String get(String url) throws FetchException {
        try {
            return httpClient.get(url, Collections.emptyList());
        }
        catch (HTTPException e) {
            boolean retryable = httpClient.isRetryable(e);
            log.warn("HttpException while posting request", e);
            throw new FetchException("HttpException while posting request", e, retryable);
        }
    }

    @Nonnull
    private <T> T parseResponse(Class<T> responseClass, String responseStr) throws FetchException {
        try {
            return responseClass.cast(objectMapper.readValue(responseStr, responseClass));
        } catch (IOException e) {
            log.warn("Could not convert JSON response "+ responseStr +" to "+ responseClass, e);
            throw new FetchException("Could not convert JSON response "+ responseStr +" to "+ responseClass, e, false);
        }
    }
}
