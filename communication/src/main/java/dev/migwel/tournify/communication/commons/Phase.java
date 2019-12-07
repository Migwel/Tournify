package dev.migwel.tournify.communication.commons;

import java.util.Collection;

public class Phase {

    private String externalId;
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

    public Collection<Set> getSets() {
        return sets;
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
}
