package net.migwel.tournify.data;

public class Update {

    private Set set;

    private String description;

    public Update() {
    }

    public Update(Set set, String description) {
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
        final StringBuilder sb = new StringBuilder("Update{");
        sb.append(", set=").append(set);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
