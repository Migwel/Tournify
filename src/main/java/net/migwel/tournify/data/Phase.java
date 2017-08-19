package net.migwel.tournify.data;

import java.util.List;

public class Phase {

    private List<PhaseGroup> phaseGroups;
    private String phaseName;

    public Phase(List<PhaseGroup> phaseGroups, String phaseName) {
        this.phaseGroups = phaseGroups;
        this.phaseName = phaseName;
    }

    public List<PhaseGroup> getPhaseGroups() {
        return phaseGroups;
    }

    public void setPhaseGroups(List<PhaseGroup> phaseGroups) {
        this.phaseGroups = phaseGroups;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    @Override
    public String toString() {
        return "Phase{" +
                "phaseGroups=" + phaseGroups +
                ", phaseName='" + phaseName + '\'' +
                '}';
    }
}
