package net.migwel.tournify.data.consumer.smashgg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTournamentResponse {

    private Entities entities;

    public Entities getEntities() {
        return entities;
    }
}