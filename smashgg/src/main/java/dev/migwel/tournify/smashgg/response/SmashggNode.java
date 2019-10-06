package dev.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggNode {

    private String id;
    private long winnerId;
    private String fullRoundText;
    private Collection<SmashggSlot> slots;
    private Collection<SmashggParticipant> participants;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(long winnerId) {
        this.winnerId = winnerId;
    }

    public String getFullRoundText() {
        return fullRoundText;
    }

    public void setFullRoundText(String fullRoundText) {
        this.fullRoundText = fullRoundText;
    }

    public Collection<SmashggSlot> getSlots() {
        return slots;
    }

    public void setSlots(Collection<SmashggSlot> slots) {
        this.slots = slots;
    }

    public Collection<SmashggParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(Collection<SmashggParticipant> participants) {
        this.participants = participants;
    }
}
