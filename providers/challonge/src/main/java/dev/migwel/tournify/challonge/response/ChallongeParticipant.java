package dev.migwel.tournify.challonge.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChallongeParticipant implements Comparable<ChallongeParticipant> {
    private String id;

    @JsonProperty("display_name")
    private String displayName;

    public ChallongeParticipant() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public int compareTo(ChallongeParticipant o) {
        return displayName.compareTo(o.displayName);
    }
}
