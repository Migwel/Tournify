package dev.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggPhaseGroup {
    private long id;
    private SmashggPhase phase;
    private String displayIdentifier;
    private int state;
    private SmashggPaginatedSets paginatedSets;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SmashggPhase getPhase() {
        return phase;
    }

    public void setPhase(SmashggPhase phase) {
        this.phase = phase;
    }

    public long getPhaseId() {
        return phase.getId();
    }

    public void setPhaseId(long phaseId) {
        this.phase.setId(phaseId);
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
