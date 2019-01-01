package net.migwel.tournify.request;

public class TournamentRequest {

    private final String url;

    public TournamentRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "TournamentRequest{" +
                "url='" + url + '\'' +
                '}';
    }
}
