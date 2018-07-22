package net.migwel.tournify.smashgg.data;

public class Group {

    private long id;
    private long phaseId;
    private String displayIdentifier;

    public long getId() {
        return id;
    }

    public long getPhaseId() {
        return phaseId;
    }

    public String getDisplayIdentifier() {
        return displayIdentifier;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPhaseId(long phaseId) {
        this.phaseId = phaseId;
    }

    public void setDisplayIdentifier(String displayIdentifier) {
        this.displayIdentifier = displayIdentifier;
    }
}
