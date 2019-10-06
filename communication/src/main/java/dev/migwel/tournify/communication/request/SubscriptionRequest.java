package dev.migwel.tournify.communication.request;

import java.util.List;

public class SubscriptionRequest {

    private String tournamentUrl;
    private String callbackUrl;
    private List<String> players;

    public String getTournamentUrl() {
        return tournamentUrl;
    }

    public SubscriptionRequest() {
    }

    public SubscriptionRequest(String tournamentUrl, String callbackUrl, List<String> players) {
        this.tournamentUrl = tournamentUrl;
        this.callbackUrl = callbackUrl;
        this.players = players;
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

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubscriptionRequest{");
        sb.append("tournamentUrl='").append(tournamentUrl).append('\'');
        sb.append(", callbackUrl='").append(callbackUrl).append('\'');
        sb.append(", players='").append(players).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
