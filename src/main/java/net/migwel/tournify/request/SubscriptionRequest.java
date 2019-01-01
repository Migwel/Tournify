package net.migwel.tournify.request;

public class SubscriptionRequest {

    private final String tournamentUrl;
    private final String callbackUrl;

    public SubscriptionRequest(String tournamentUrl, String callbackUrl) {
        this.tournamentUrl = tournamentUrl;
        this.callbackUrl = callbackUrl;
    }

    public String getTournamentUrl() {
        return tournamentUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubscriptionRequest{");
        sb.append("tournamentUrl='").append(tournamentUrl).append('\'');
        sb.append(", callbackUrl='").append(callbackUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
