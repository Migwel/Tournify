package dev.migwel.tournify.communication.commons;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

public class Set {

    private String externalId;
    private String tournamentName;
    private String phaseName;
    private String round;

    private Collection<Player> players;

    private Collection<Player> winners;

    private boolean done;

    public Set() {
    }

    public Set(String externalId, String tournamentName, String phaseName, Collection<Player> players, Collection<Player> winners, String round, boolean done) {
        this.externalId = externalId;
        this.tournamentName = tournamentName;
        this.phaseName = phaseName;
        this.players = players;
        this.winners = winners;
        this.round = round;
        this.done = done;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Nonnull
    public Collection<Player> getPlayers() {
        return players != null ? players : new ArrayList<>();
    }

    public void setPlayers(Collection<Player> players) {
        this.players = players;
    }

    public Collection<Player> getWinners() {
        return winners;
    }

    public void setWinners(Collection<Player> winners) {
        this.winners = winners;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    @Override
    public String toString() {
        return "Set{" +
                "players=" + players +
                ", winner=" + winners +
                '}';
    }
}
