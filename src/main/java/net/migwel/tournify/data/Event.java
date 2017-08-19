package net.migwel.tournify.data;

import java.util.List;

public class Event {

    private List<Phase> phases;
    private GameType gameType;

    private String name;
    private String description;

    public Event(List<Phase> phases, GameType gameType, String name, String description) {
        this.phases = phases;
        this.gameType = gameType;
        this.name = name;
        this.description = description;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Event{" +
                "phases=" + phases +
                ", gameType=" + gameType +
                ", name=" + name +
                ", description=" + description +
                '}';
    }
}
