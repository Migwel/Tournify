package dev.migwel.tournify.core.data;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Phase {

    @Id
    @GeneratedValue
    private Long id;

    private String externalId;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "phase_id")
    private Collection<Set> sets;

    private String name;

    private boolean done;

    public Phase() {
    }

    public Phase(String externalId, Collection<Set> sets, String name, boolean done) {
        this.externalId = externalId;
        this.sets = sets;
        this.name = name;
        this.done = done;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Nonnull
    public Collection<Set> getSets() {
        return sets != null ? sets : new ArrayList<>();
    }

    public void setSets(Collection<Set> sets) {
        this.sets = sets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", phaseName='" + name + '\'' +
                '}';
    }
}
