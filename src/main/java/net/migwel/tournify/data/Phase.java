package net.migwel.tournify.data;

import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Phase {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PhaseGroup> phaseGroups;
    private String phaseName;

    private boolean done;

    public Phase() {
    }

    public Phase(List<PhaseGroup> phaseGroups, String phaseName) {
        this.phaseGroups = phaseGroups;
        this.phaseName = phaseName;
    }

    @Nonnull
    public List<PhaseGroup> getPhaseGroups() {
        return phaseGroups != null ? phaseGroups : new ArrayList<>();
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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Phase{" +
                "phaseGroups=" + phaseGroups +
                ", phaseName='" + phaseName + '\'' +
                '}';
    }
}
