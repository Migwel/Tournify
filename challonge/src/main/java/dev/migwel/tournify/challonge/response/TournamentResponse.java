package dev.migwel.tournify.challonge.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TournamentResponse {

    @JsonProperty("tournament")
    private ChallongeTournament tournament;

    public TournamentResponse() {
    }

    public ChallongeTournament getTournament() {
        return tournament;
    }

    public void setTournament(ChallongeTournament tournament) {
        this.tournament = tournament;
    }
}
