package dev.migwel.tournify.communication.commons;

public class TournamentUpdate extends Update {

    private Tournament tournament;

    public TournamentUpdate() {
    }

    public TournamentUpdate(String description, Tournament tournament) {
        super(description);
        this.tournament = tournament;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
}
