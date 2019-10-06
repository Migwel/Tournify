package dev.migwel.tournify.smashgg.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventEntities {
    private Event event;

    @JsonProperty("phase")
    private Collection<Phase> phases;

    private Collection<Group> groups;

    public Event getEvent() {
        return event;
    }

    public Collection<Phase> getPhases() {
        return phases;
    }

    public Collection<Group> getGroups() {
        return groups;
    }
}
