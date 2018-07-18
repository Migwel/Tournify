package net.migwel.tournify.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

//This is basically a bracket (TODO: check how it works with pools)
@Entity
public class PhaseGroup {

    @Id
    @GeneratedValue
    private Long id;
    private String displayIdentifier;

    @OneToMany
    private List<Set> sets;

    public PhaseGroup() {
    }

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
