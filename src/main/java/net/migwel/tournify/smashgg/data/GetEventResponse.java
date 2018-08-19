package net.migwel.tournify.smashgg.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetEventResponse {

    private EventEntities entities;

    public EventEntities getEntities() {
        return entities;
    }
}