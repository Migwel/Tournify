package net.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggNode {

    private String id;
    private long winnerId;
    private String fullRoundText;
    private List<SmashggSlot> slots;

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

    public List<SmashggSlot> getSlots() {
        return slots;
    }

    public void setSlots(List<SmashggSlot> slots) {
        this.slots = slots;
    }
}
