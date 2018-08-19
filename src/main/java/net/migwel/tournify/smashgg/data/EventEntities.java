package net.migwel.tournify.smashgg.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventEntities {
    private Event event;

    @JsonProperty("phase")
    private List<Phase> phases;

    private List<Group> groups;

    public Event getEvent() {
        return event;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public List<Group> getGroups() {
        return groups;
    }
}
