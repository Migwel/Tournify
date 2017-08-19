package net.migwel.tournify.data;

import java.util.List;

public class Set {

    private List<Player> participants;
    private Long winner;
    private String round;

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
