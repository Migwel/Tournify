package net.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggSlot {
    private SmashggEntrant entrant;

    public SmashggEntrant getEntrant() {
        return entrant;
    }

    public void setEntrant(SmashggEntrant entrant) {
        this.entrant = entrant;
    }
}
