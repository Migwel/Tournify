package dev.migwel.tournify.communication.commons;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value=SetUpdate.class, name = "SetUpdate"),
        @JsonSubTypes.Type(value=TournamentUpdate.class, name = "TournamentUpdate")
})
public class Update {
    private String description;

    public Update() {
    }

    public Update(String description) {
        this.description = description;
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
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
