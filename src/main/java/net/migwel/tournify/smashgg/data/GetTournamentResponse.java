package net.migwel.tournify.smashgg.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTournamentResponse {

    private TournamentEntities entities;

    public TournamentEntities getEntities() {
        return entities;
    }
}