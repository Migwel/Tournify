package net.migwel.tournify.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Set {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    private List<Player> players;
    private Long winner;
    private String round;

    public Set() {
    }

    public Set(List<Player> players, Long winner) {
        this.players = players;
        this.winner = winner;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Long getWinner() {
        return winner;
    }

    public void setWinner(Long winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "Set{" +
                "players=" + players +
                ", winner=" + winner +
                '}';
    }
}
