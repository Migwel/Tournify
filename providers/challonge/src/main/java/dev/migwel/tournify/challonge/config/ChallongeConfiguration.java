package dev.migwel.tournify.challonge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:challonge.properties")
public class ChallongeConfiguration {

    @Value("${challonge.username}")
    private String username;

    @Value("${challonge.apiToken}")
    private String apiToken;

    @Value("${challonge.retryNumber}")
    private int retryNumber;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public int getRetryNumber() {
        return retryNumber;
    }

    public void setRetryNumber(int retryNumber) {
        this.retryNumber = retryNumber;
    }
}
