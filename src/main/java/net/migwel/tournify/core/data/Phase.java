package net.migwel.tournify.core.data;

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

    private String externalId;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Set> sets;

    private String phaseName;

    private boolean done;

    public Phase() {
    }

    public Phase(String externalId, List<Set> sets, String phaseName, boolean done) {
        this.externalId = externalId;
        this.sets = sets;
        this.phaseName = phaseName;
        this.done = done;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Nonnull
    public List<Set> getSets() {
        return sets != null ? sets : new ArrayList<>();
    }

    public void setSets(List<Set> sets) {
        this.sets = sets;
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
                "sets=" + sets +
                ", phaseName='" + phaseName + '\'' +
                '}';
    }
}
