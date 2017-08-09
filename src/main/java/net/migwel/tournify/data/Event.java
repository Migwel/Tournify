package net.migwel.tournify.data;

import java.util.List;

public class Event {

    private List<Phase> phases;
    private GameType gameType;

    public Event(List<Phase> phases, GameType gameType) {
        this.phases = phases;
        this.gameType = gameType;
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

    @Override
    public String toString() {
        return "Event{" +
                "phases=" + phases +
                ", gameType=" + gameType +
                '}';
    }
}
