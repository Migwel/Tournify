package net.migwel.tournify.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
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

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER) //This needs to be changed to fetch in repository
    private List<Event> events;
    private String name;

    @ManyToOne(cascade=CascadeType.ALL)
    private Address address;
    private String url;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;

    private boolean done;

    public Tournament() {
    }

    public Tournament(List<Event> events, String name, Address address, String url, Date date) {
        this.events = events;
        this.name = name;
        this.address = address;
        this.url = url;
        this.date = new Date(date.getTime());
    }

    @Nonnull
    public List<Event> getEvents() {
        return events != null ? events : new ArrayList<>();
    }

    public void setEvents(List<Event> events) {
        this.events = events;
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
                "events=" + events +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", url='" + url + '\'' +
                ", date=" + date +
                '}';
    }
}
