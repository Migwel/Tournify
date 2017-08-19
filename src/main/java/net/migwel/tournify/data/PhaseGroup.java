package net.migwel.tournify.data;

import java.util.List;

//This is basically a bracket (TODO: check how it works with pools)
public class PhaseGroup {

    private long id;
    private String displayIdentifier;
    private List<Set> sets;

    public PhaseGroup(long id, String displayIdentifier, List<Set> sets) {
        this.id = id;
        this.displayIdentifier = displayIdentifier;
        this.sets = sets;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplayIdentifier() {
        return displayIdentifier;
    }

    public void setDisplayIdentifier(String displayIdentifier) {
        this.displayIdentifier = displayIdentifier;
    }

    public List<Set> getSets() {
        return sets;
    }

    public void setSets(List<Set> sets) {
        this.sets = sets;
    }

    @Override
    public String toString() {
        return "PhaseGroup{" +
                "id=" + id +
                ", displayIdentifier='" + displayIdentifier + '\'' +
                ", sets=" + sets +
                '}';
    }
}
