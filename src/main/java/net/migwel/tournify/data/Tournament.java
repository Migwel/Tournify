package net.migwel.tournify.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Tournament {

    @Id
    @GeneratedValue
    private Long id;

    private String externalId;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER) //This needs to be changed to fetch in repository
    private List<Phase> phases;
    private String name;

    @ManyToOne(cascade=CascadeType.ALL)
    private GameType gameType;

    private String description;

    @ManyToOne(cascade=CascadeType.ALL)
    private Address address;

    @Column(unique = true)
    private String url;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;

    private boolean done;

    public Tournament() {
    }

    public Tournament(String url) {
        this.url = url;
    }

    public Tournament(String externalId, List<Phase> phases, String name, GameType gameType, Address address, String url, Date date) {
        this.externalId = externalId;
        this.phases = phases;
        this.name = name;
        this.gameType = gameType;
        this.address = address;
        this.url = url;
        this.date = new Date(date.getTime());
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Nonnull
    public List<Phase> getPhases() {
        return phases != null ? phases : new ArrayList<>();
    }

    public void setPhases(List<Phase> phases) {
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
        return new Date(date.getTime());
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
