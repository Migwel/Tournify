package net.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggPhaseGroupData implements SmashggData<PhaseGroup> {
    private PhaseGroup phaseGroup;

    public PhaseGroup getPhaseGroup() {
        return phaseGroup;
    }

    public void setPhaseGroup(PhaseGroup phaseGroup) {
        this.phaseGroup = phaseGroup;
    }

    @Override
    public PhaseGroup getObject() {
        return phaseGroup;
    }
}
