package net.migwel.tournify.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.Date;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Tournament {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<Event> events;
    private String name;

    @ManyToOne
    private Address address;
    private String url;
    private Date date;

    public Tournament() {
    }

    public Tournament(List<Event> events, String name, Address address, String url, Date date) {
        this.events = events;
        this.name = name;
        this.address = address;
        this.url = url;
        this.date = date;
    }

    public List<Event> getEvents() {
        return events;
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
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
