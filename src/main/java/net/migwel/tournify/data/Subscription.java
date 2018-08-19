package net.migwel.tournify.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Subscription {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Tournament tournament;

    private String callbackUrl;

    private boolean active;

    public Subscription() {
    }

    public Subscription(Tournament tournament, String callbackUrl, boolean active) {
        this.tournament = tournament;
        this.callbackUrl = callbackUrl;
        this.active = active;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Subscription{");
        sb.append("id=").append(id);
        sb.append(", tournament=").append(tournament);
        sb.append(", callbackUrl=").append(callbackUrl);
        sb.append(", active=").append(active);
        sb.append('}');
        return sb.toString();
    }
}
