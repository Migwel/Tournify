package net.migwel.tournify.communication.request;

public class SubscriptionRequest {

    private String tournamentUrl;
    private String callbackUrl;

    public String getTournamentUrl() {
        return tournamentUrl;
    }

    public void setTournamentUrl(String tournamentUrl) {
        this.tournamentUrl = tournamentUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
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
