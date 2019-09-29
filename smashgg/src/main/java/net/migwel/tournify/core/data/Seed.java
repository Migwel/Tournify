package net.migwel.tournify.smashgg.data;

public class Seed {

    private String entrantId;
    private Mutation mutations;

    public String getEntrantId() {
        return entrantId;
    }

    public Mutation getMutations() {
        return mutations;
    }

    public void setEntrantId(String entrantId) {
        this.entrantId = entrantId;
    }

    public void setMutations(Mutation mutations) {
        this.mutations = mutations;
    }
}
