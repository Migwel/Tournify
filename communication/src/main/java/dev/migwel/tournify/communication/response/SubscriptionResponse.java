package dev.migwel.tournify.communication.response;

public class SubscriptionResponse {
    private boolean success;
    private String id;
    private String tournamentUrl;
    private String callbackUrl;

    public SubscriptionResponse() {
    }

    public SubscriptionResponse(String id, String tournamentUrl, String callbackUrl) {
        this.id = id;
        this.tournamentUrl = tournamentUrl;
        this.callbackUrl = callbackUrl;
        this.success = true;
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

    public boolean isSuccess() {
        return success;
    }

    public static SubscriptionResponse error() {
        SubscriptionResponse response = new SubscriptionResponse();
        response.success = false;
        return response;
    }
}
