package net.migwel.tournify.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Set {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<Player> participants;
    private Long winner;
    private String round;

    public Set() {
    }

    public Set(List<Player> participants, Long winner) {
        this.participants = participants;
        this.winner = winner;
    }

    public List<Player> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Player> participants) {
        this.participants = participants;
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
                "participants=" + participants +
                ", winner=" + winner +
                '}';
    }
}
