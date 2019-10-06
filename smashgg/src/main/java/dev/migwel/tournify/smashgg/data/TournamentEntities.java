package dev.migwel.tournify.smashgg.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TournamentEntities {
    private Tournament tournament;

    private Collection<Event> event;

    private Collection<VideoGame> videogame;

    private Collection<Phase> phase;

    private Collection<Group> groups;

    private Collection<Set> sets;

    public Tournament getTournament() {
        return tournament;
    }

    public Collection<Event> getEvent() {
        return event;
    }

    public Collection<VideoGame> getVideogame() {
        return videogame;
    }

    public Collection<Phase> getPhase() {
        return phase;
    }

    public Collection<Group> getGroups() {
        return groups;
    }

    public Collection<Set> getSets() {
        return sets;
    }
}
