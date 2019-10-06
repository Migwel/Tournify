package dev.migwel.tournify.communication.response;

public class SubscriptionResponse {
    private String id;
    private String tournamentUrl;
    private String callbackUrl;

    public SubscriptionResponse() {
    }

    public SubscriptionResponse(String id, String tournamentUrl, String callbackUrl) {
        this.id = id;
        this.tournamentUrl = tournamentUrl;
        this.callbackUrl = callbackUrl;
    }

    public String getId() {
        return id;
    }

    public String getTournamentUrl() {
        return tournamentUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }
}
