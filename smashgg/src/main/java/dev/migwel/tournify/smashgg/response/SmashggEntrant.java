package dev.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.migwel.tournify.smashgg.data.Participant;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggEntrant {
    private long id;
    private String name;

    private Collection<Participant> participants;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Collection<Participant> participants) {
        this.participants = participants;
    }
}
