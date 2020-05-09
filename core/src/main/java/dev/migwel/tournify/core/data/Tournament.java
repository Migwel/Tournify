package dev.migwel.tournify.core.data;

import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Tournament {

    @Id
    @GeneratedValue
    private Long id;

    private String externalId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) //This needs to be changed to fetch in repository
    @JoinColumn(name = "tournament_id", nullable = false)
    private Collection<Phase> phases;
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    private GameType gameType;

    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    @Column(unique = true)
    private String url;

    private Date date;

    private boolean done;

    public Tournament() {
    }

    public Tournament(String url) {
        this.url = url;
    }

    public Tournament(String externalId, Collection<Phase> phases, String name, GameType gameType, Address address, String url, Date date, boolean done) {
        this.externalId = externalId;
        this.phases = phases;
        this.name = name;
        this.gameType = gameType;
        this.address = address;
        this.url = url;
        this.date = new Date(date.getTime());
        this.done = done;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Nonnull
    public Collection<Phase> getPhases() {
        return phases != null ? phases : new ArrayList<>();
    }

    public void setPhases(Collection<Phase> phases) {
        this.phases = phases;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getDate() {
        return date == null ? null : new Date(date.getTime());
    }

    public void setDate(Date date) {
        this.date = new Date(date.getTime());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "phases=" + phases +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", url='" + url + '\'' +
                ", date=" + date +
                '}';
    }
}
