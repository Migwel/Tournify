package net.migwel.tournify;

public class SubscriptionResponse {
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
}
