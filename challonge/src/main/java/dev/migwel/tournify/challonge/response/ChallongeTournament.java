package dev.migwel.tournify.challonge.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChallongeTournament {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("started_at")
    private Date startDate;

    @JsonProperty("game_name")
    private String gameName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        if(startDate == null) {
            return null;
        }
        return new Date(startDate.getTime());
    }

    public void setStartDate(Date startDate) {
        if (startDate == null) {
            return;
        }
        this.startDate = new Date(startDate.getTime());
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
