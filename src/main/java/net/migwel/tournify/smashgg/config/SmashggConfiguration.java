package net.migwel.tournify.smashgg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:smashgg.properties")
public class SmashggConfiguration {

    @Value("${smashgg.apiToken}")
    private String apiToken;

    @Value("${smashgg.apiUrl}")
    private String apiUrl;

    @Value("${smashgg.setsPerPage}")
    private int setsPerPage;

    @Value("${smashgg.retryNumber}")
    private int retryNumber;

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public int getSetsPerPage() {
        return setsPerPage;
    }

    public void setSetsPerPage(int setsPerPage) {
        this.setsPerPage = setsPerPage;
    }

    public int getRetryNumber() {
        return retryNumber;
    }

    public void setRetryNumber(int retryNumber) {
        this.retryNumber = retryNumber;
    }
}
