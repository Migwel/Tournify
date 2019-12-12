package dev.migwel.tournify.core.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tournamenttracking")
public class TournamentTracking {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Tournament tournament;

    private boolean done;

    private int noUpdateRetries;

    private Date startDate;

    private Date nextDate;

    public TournamentTracking() {
    }

    public TournamentTracking(Tournament tournament, Date startDate, Date nextDate) {
        this.tournament = tournament;
        this.startDate = new Date(startDate.getTime());
        this.nextDate = new Date(nextDate.getTime());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Date getStartDate() {
        return new Date(startDate.getTime());
    }

    public void setStartDate(Date startDate) {
        this.startDate = new Date(startDate.getTime());
    }

    public Date getNextDate() {
        return new Date(nextDate.getTime());
    }

    public void setNextDate(Date nextDate) {
        this.nextDate = new Date(nextDate.getTime());
    }

    public int getNoUpdateRetries() {
        return noUpdateRetries;
    }

    public void setNoUpdateRetries(int noUpdateRetries) {
        this.noUpdateRetries = noUpdateRetries;
    }
}
