package dev.migwel.tournify.communication.commons;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Collection;
import java.util.Date;

public class Tournament {
    private String externalId;
    private Collection<Phase> phases;
    private String name;
    private String gameType;
    private Address address;
    private String url;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private boolean done;

    public Tournament() {
    }

    public Tournament(String externalId, Collection<Phase> phases, String name, String gameType, Address address, String url, Date date, boolean done) {
        this.externalId = externalId;
        this.phases = phases;
        this.name = name;
        this.gameType = gameType;
        this.address = address;
        this.url = url;
        this.done = done;
        this.date = date == null ? null : new Date(date.getTime());
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Collection<Phase> getPhases() {
        return phases;
    }

    public void setPhases(Collection<Phase> phases) {
        this.phases = phases;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        if(date == null) {
            return null;
        }
        return new Date(date.getTime());
    }

    public void setDate(Date date) {
        if(date == null) {
            return;
        }
        this.date = new Date(date.getTime());
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
