package dev.migwel.tournify.challonge.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchResponse {

    @JsonProperty("match")
    private ChallongeMatch match;

    public MatchResponse() {
    }

    public ChallongeMatch getMatch() {
        return match;
    }

    public void setMatch(ChallongeMatch match) {
        this.match = match;
    }
}
