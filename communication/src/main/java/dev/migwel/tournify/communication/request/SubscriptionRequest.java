package dev.migwel.tournify.communication.request;

import java.util.List;

public class SubscriptionRequest {

    private String tournamentUrl;
    private String callbackUrl;
    private String username;
    private String password;
    private List<String> players;

    public String getTournamentUrl() {
        return tournamentUrl;
    }

    public SubscriptionRequest() {
    }

    public SubscriptionRequest(String tournamentUrl, String callbackUrl, List<String> players) {
        this(tournamentUrl, callbackUrl, players, null, null);
    }

    public SubscriptionRequest(String tournamentUrl, String callbackUrl, List<String> players, String username, String password) {
        this.tournamentUrl = tournamentUrl;
        this.callbackUrl = callbackUrl;
        this.players = players;
        this.username = username;
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubscriptionRequest{");
        sb.append("tournamentUrl='").append(tournamentUrl).append('\'');
        sb.append(", callbackUrl='").append(callbackUrl).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", players='").append(players).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
