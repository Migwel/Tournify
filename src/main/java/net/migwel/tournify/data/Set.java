package net.migwel.tournify.data;

import java.util.Arrays;
import java.util.List;

public class Set {

    private List<Game> games;
    private List<Player>[] participants;
    private int winner=-1;
    private String round;

    public Set(List<Game> games, List<Player>[] participants, int winner) {
        this.games = games;
        this.participants = participants;
        this.winner = winner;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
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
        return "Set{" +
                "games=" + games +
                ", participants=" + Arrays.toString(participants) +
                ", winner=" + winner +
                '}';
    }
}
