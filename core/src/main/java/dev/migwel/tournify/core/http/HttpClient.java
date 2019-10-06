package dev.migwel.tournify.core.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.CheckForNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

@Component
public class HttpClient {

    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    public HttpClient() {
        //
    }

    @CheckForNull
    public String postRequest(String request, String url, Collection<Pair<String, String>> headers) {
        CloseableHttpClient client = HttpClients.createDefault();

        StringEntity requestEntity;
        try {
            requestEntity = new StringEntity(request);
        } catch (UnsupportedEncodingException e) {
            log.warn("An error occurred while creating StringEntity", e);
            return null;
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
