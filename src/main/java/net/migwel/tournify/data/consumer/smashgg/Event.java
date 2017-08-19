package net.migwel.tournify.data.consumer.smashgg;

public class Event {

    private long id;
    private long videogameId;

    private String name;
    private String description;


    public long getId() {
        return id;
    }

    public long getVideogameId() {
        return videogameId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
