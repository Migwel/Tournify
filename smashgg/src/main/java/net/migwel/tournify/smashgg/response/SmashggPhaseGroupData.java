package net.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggPhaseGroupData implements SmashggData<SmashggPhaseGroup> {
    private SmashggPhaseGroup phaseGroup;

    public SmashggPhaseGroup getPhaseGroup() {
        return phaseGroup;
    }

    public void setPhaseGroup(SmashggPhaseGroup phaseGroup) {
        this.phaseGroup = phaseGroup;
    }

    @Override
    public SmashggPhaseGroup getObject() {
        return phaseGroup;
    }
}
