package net.migwel.tournify.smashgg.data;

import java.util.Collection;

public class GroupEntities {

    private Group groups;
    private Collection<Set> sets;
    private Collection<Seed> seeds;

    public Group getGroups() {
        return groups;
    }

    public Collection<Set> getSets() {
        return sets;
    }

    public Collection<Seed> getSeeds() {
        return seeds;
    }

    public void setGroups(Group groups) {
        this.groups = groups;
    }

    public void setSets(Collection<Set> sets) {
        this.sets = sets;
    }

    public void setSeeds(Collection<Seed> seeds) {
        this.seeds = seeds;
    }
}
