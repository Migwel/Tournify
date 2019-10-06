package dev.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggPhaseGroupResponse implements SmashggResponse<SmashggPhaseGroup> {
    private SmashggPhaseGroupData data;

    @Override
    public SmashggPhaseGroupData getData() {
        return data;
    }

    public void setData(SmashggPhaseGroupData data) {
        this.data = data;
    }
}
