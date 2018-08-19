package net.migwel.tournify.data;

public class SetUpdate {

    private Set set;

    private String description;

    public SetUpdate() {
    }

    public SetUpdate(Set set, String description) {
        this.set = set;
        this.description = description;
    }

    public Set getSet() {
        return set;
    }

    public void setSet(Set set) {
        this.set = set;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SetUpdate{");
        sb.append(", set=").append(set);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
