package net.migwel.tournify.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Set {

    @Id
    @GeneratedValue
    private Long id;

    private String externalId;

    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Player> players;
    private String winner;
    private String round;

    public Set() {
    }

    public Set(String externalId, List<Player> players, String winner, String round) {
        this.externalId = externalId;
        this.players = players;
        this.winner = winner;
        this.round = round;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    @Override
    public String toString() {
        return "Set{" +
                "players=" + players +
                ", winner=" + winner +
                '}';
    }
}
