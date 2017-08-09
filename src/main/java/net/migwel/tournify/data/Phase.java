package net.migwel.tournify.data;

import java.util.List;

public class Phase {

    private List<Set> sets;
    private String phaseName;

    public Phase(List<Set> sets, String phaseName) {
        this.sets = sets;
        this.phaseName = phaseName;
    }

    public List<Set> getSets() {
        return sets;
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

    @Override
    public String toString() {
        return "Phase{" +
                "sets=" + sets +
                ", phaseName='" + phaseName + '\'' +
                '}';
    }
}
