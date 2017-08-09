package net.migwel.tournify.data;

import java.util.Arrays;
import java.util.List;

public class Game {

    private List<Player>[] participants;
    private int winner = -1;

    public Game(List<Player>[] participants, int winner) {
        this.participants = participants;
        this.winner = winner;
    }

    public List<Player>[] getParticipants() {
        return participants;
    }

    public void setParticipants(List<Player>[] participants) {
        this.participants = participants;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "Game{" +
                "participants=" + Arrays.toString(participants) +
                ", winner=" + winner +
                '}';
    }
}
