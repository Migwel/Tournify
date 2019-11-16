package dev.migwel.tournify.core.data;

import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Set {

    @Id
    @GeneratedValue
    private Long id;

    private String externalId;

    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Collection<Player> players;

    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Collection<Player> winners;
    private String name;

    private boolean done;

    public Set() {
    }

    public Set(String externalId, Collection<Player> players, Collection<Player> winners, String name, boolean done) {
        this.externalId = externalId;
        this.players = players;
        this.winners = winners;
        this.name = name;
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
        return winners != null ? winners : new ArrayList<>();
    }

    public void setWinners(Collection<Player> winners) {
        this.winners = winners;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Set{" +
                "players=" + players +
                ", winners=" + winners +
                '}';
    }
}
