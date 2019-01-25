package net.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Node {

    private String id;
    private long winnerId;
    private String fullRoundText;
    private List<Slot> slots;

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

    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }
}
