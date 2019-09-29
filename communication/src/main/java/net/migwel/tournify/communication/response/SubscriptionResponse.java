package net.migwel.tournify.communication.response;

public class SubscriptionResponse {
    private final String id;
    private final String tournamentUrl;
    private final String callbackUrl;

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
