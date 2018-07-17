package net.migwel.tournify.smashgg.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TournamentEntities {
    private Tournament tournament;

    private List<Event> event;

    private List<VideoGame> videogame;

    private List<Phase> phase;

    private List<Group> groups;

    private List<Set> sets;

    public Tournament getTournament() {
        return tournament;
    }

    public List<Event> getEvent() {
        return event;
    }

    public List<VideoGame> getVideogame() {
        return videogame;
    }

    public List<Phase> getPhase() {
        return phase;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Set> getSets() {
        return sets;
    }
}
