package net.migwel.tournify.data;

import java.util.Date;
import java.util.List;

public class Tournament {

    private List<Event> events;
    private String name;
    private String location;
    private Date date;

    public Tournament(List<Event> events, String name, String location, Date date) {
        this.events = events;
        this.name = name;
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "events=" + events +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", date=" + date +
                '}';
    }
}
