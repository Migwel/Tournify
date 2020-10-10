package dev.migwel.tournify.communication.commons;

public class SetUpdate extends Update {

    private Set set;

    public SetUpdate() {
    }

    public SetUpdate(Set set, String description) {
        super(description);
        this.set = set;
    }

    public Set getSet() {
        return set;
    }

    public void setSet(Set set) {
        this.set = set;
    }
}
