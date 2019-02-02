package net.migwel.tournify.core.data;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

@Entity
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"tournament_id", "callbackUrl"})}
)
public class Subscription {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @ManyToOne
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

    public UUID getId() {
        return id;
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
