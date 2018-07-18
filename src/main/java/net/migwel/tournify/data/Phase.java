package net.migwel.tournify.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Phase {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<PhaseGroup> phaseGroups;
    private String phaseName;

    public Phase() {
    }

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
