package net.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggPhaseGroup {
    private long id;
    private long phaseId;
    private String displayIdentifier;
    private int state;
    private SmashggPaginatedSets paginatedSets;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(long phaseId) {
        this.phaseId = phaseId;
    }

    public String getDisplayIdentifier() {
        return displayIdentifier;
    }

    public void setDisplayIdentifier(String displayIdentifier) {
        this.displayIdentifier = displayIdentifier;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public SmashggPaginatedSets getPaginatedSets() {
        return paginatedSets;
    }

    public void setPaginatedSets(SmashggPaginatedSets paginatedSets) {
        this.paginatedSets = paginatedSets;
    }
}
