package net.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggPhaseGroupResponse implements SmashggResponse<PhaseGroup> {
    private SmashggPhaseGroupData data;

    @Override
    public SmashggPhaseGroupData getData() {
        return data;
    }

    public void setData(SmashggPhaseGroupData data) {
        this.data = data;
    }
}
