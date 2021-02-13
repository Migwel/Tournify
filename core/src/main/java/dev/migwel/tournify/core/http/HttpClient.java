package dev.migwel.tournify.core.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Collection;

@Component
public class HttpClient {

    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    public static final int HTTP_TOO_MANY_REQUESTS = 429;

    public HttpClient() {
        //
    }

    @Nonnull
    public String postRequest(String request, String url, Collection<Pair<String, String>> headers) {
        CloseableHttpClient client = HttpClients.createDefault();

        StringEntity requestEntity;
        try {
            requestEntity = new StringEntity(request);
        } catch (UnsupportedEncodingException e) {
            log.warn("An error occurred while creating StringEntity", e);
            throw new HTTPException(HttpStatus.SC_BAD_REQUEST);
        }
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(requestEntity);
        httpPost.setHeader("Content-type", "application/json");
        for(Pair<String, String> header : headers) {
            httpPost.setHeader(header.getFirst(), header.getSecond());
        }
        CloseableHttpResponse response;
        try {
            response = client.execute(httpPost);
        } catch (UnknownHostException e) {
            log.warn("An UnknownHostException occurred while executing the POST request", e);
            throw new HTTPException(HttpStatus.SC_SERVICE_UNAVAILABLE);
        } catch (IOException e) {
            log.warn("An IOException occurred while executing the POST request", e);
            throw new HTTPException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }

        HttpEntity responseEntity = response.getEntity();
        if (responseEntity == null) {
            log.warn("Response entity was null");
            throw new HTTPException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }

        try {
            return EntityUtils.toString(responseEntity);
        } catch (IOException e) {
            log.warn("Could not get content from response entity", e);
            throw new HTTPException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Nonnull
    public String get(@Nonnull String url, @Nonnull Collection<Pair<String, String>> headers) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpPost = new HttpGet(url);
        httpPost.setHeader("Content-type", "application/json");
        for(Pair<String, String> header : headers) {
            httpPost.setHeader(header.getFirst(), header.getSecond());
        }
        CloseableHttpResponse response;
        try {
            response = client.execute(httpPost);
        } catch (UnknownHostException e) {
            log.warn("An UnknownHostException occurred while executing the POST request", e);
            throw new HTTPException(HttpStatus.SC_SERVICE_UNAVAILABLE);
        } catch (IOException e) {
            log.warn("An IOException occurred while executing the POST request", e);
            throw new HTTPException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }

        HttpEntity responseEntity = response.getEntity();
        if (responseEntity == null) {
            log.warn("Response entity was null");
            throw new HTTPException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }

        try {
            return EntityUtils.toString(responseEntity);
        } catch (IOException e) {
            log.warn("Could not get content from response entity", e);
            throw new HTTPException(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public boolean isRetryable(HTTPException e) {
        int statusCode = e.getStatusCode();
        if (statusCode == HTTP_TOO_MANY_REQUESTS || String.valueOf(statusCode).charAt(0) == '5') {
            return true;
        }
        return false;
    }
}
