package dev.migwel.tournify.smashgg.data;

public class Phase {

    private Long id;
    private Long eventId;
    private String name;

    public Long getId() {
        return id;
    }

    public Long getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
