package net.migwel.tournify.data;

import javax.persistence.CascadeType;
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

    private long externalId;
    private String displayIdentifier;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Set> sets;

    public PhaseGroup() {
    }

    public PhaseGroup(long externalId, String displayIdentifier, List<Set> sets) {
        this.externalId = externalId;
        this.displayIdentifier = displayIdentifier;
        this.sets = sets;
    }

    public long getExternalId() {
        return externalId;
    }

    public void setExternalId(long id) {
        this.externalId = id;
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
