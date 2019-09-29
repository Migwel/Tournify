package net.migwel.tournify.smashgg.data;

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

    public void setId(long id) {
        this.id = id;
    }

    public void setVideogameId(long videogameId) {
        this.videogameId = videogameId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
