package net.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggEventData implements SmashggData<SmashggEvent> {
    private SmashggEvent event;

    public SmashggEvent getEvent() {
        return event;
    }

    public void setEvent(SmashggEvent event) {
        this.event = event;
    }

    @Override
    public SmashggEvent getObject() {
        return event;
    }
}
