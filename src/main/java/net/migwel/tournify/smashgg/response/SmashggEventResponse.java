package net.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggEventResponse implements SmashggResponse<SmashggEvent> {
    private SmashggEventData data;

    @Override
    public SmashggEventData getData() {
        return data;
    }

    public void setData(SmashggEventData data) {
        this.data = data;
    }
}
