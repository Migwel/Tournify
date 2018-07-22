package net.migwel.tournify.smashgg.data;

import java.util.List;

public class GroupEntities {

    private Group groups;
    private List<Set> sets;
    private List<Seed> seeds;

    public Group getGroups() {
        return groups;
    }

    public List<Set> getSets() {
        return sets;
    }

    public List<Seed> getSeeds() {
        return seeds;
    }

    public void setGroups(Group groups) {
        this.groups = groups;
    }

    public void setSets(List<Set> sets) {
        this.sets = sets;
    }

    public void setSeeds(List<Seed> seeds) {
        this.seeds = seeds;
    }
}
