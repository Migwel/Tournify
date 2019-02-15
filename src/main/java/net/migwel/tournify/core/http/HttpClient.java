package net.migwel.tournify.core.http;

import net.migwel.tournify.smashgg.config.SmashggConfiguration;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.CheckForNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class HttpClient {

    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);
    private final SmashggConfiguration configuration;

    public HttpClient(SmashggConfiguration configuration) {
        this.configuration = configuration;
    }

    @CheckForNull
    public String postRequest(String request) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(configuration.getApiUrl());

        StringEntity requestEntity;
        try {
            requestEntity = new StringEntity(request);
        } catch (UnsupportedEncodingException e) {
            log.warn("An error occurred while creating StringEntity", e);
            return null;
        }
        httpPost.setEntity(requestEntity);
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + configuration.getApiToken());
        CloseableHttpResponse response;
        try {
            response = client.execute(httpPost);
        } catch (IOException e) {
            log.warn("An error occurred while executing the POST request", e);
            return null;
        }

        HttpEntity responseEntity = response.getEntity();
        if (responseEntity == null) {
            log.warn("Response entity was null");
            return null;
        }

        try {
            return EntityUtils.toString(responseEntity);
        } catch (IOException e) {
            log.warn("Could not get content from response entity", e);
            return null;
        }
    }
}
