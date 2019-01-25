package net.migwel.tournify.request;

public class TournamentRequest {

    private String url;

    public TournamentRequest() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "TournamentRequest{" +
                "url='" + url + '\'' +
                '}';
    }
}
