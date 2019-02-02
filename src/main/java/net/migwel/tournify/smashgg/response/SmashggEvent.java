package net.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggEvent {

    private String slug;
    private long id;
    private long startAt;
    private String name;
    private SmashggTournament tournament;
    private List<SmashggPhaseGroup> phaseGroups;
    private List<SmashggPhase> phases;
    private SmashggVideoGame videogame;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStartAt() {
        return startAt;
    }

    public void setStartAt(long startAt) {
        this.startAt = startAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SmashggTournament getTournament() {
        return tournament;
    }

    public void setTournament(SmashggTournament tournament) {
        this.tournament = tournament;
    }

    public SmashggVideoGame getVideogame() {
        return videogame;
    }

    public void setVideogame(SmashggVideoGame videogame) {
        this.videogame = videogame;
    }

    public List<SmashggPhaseGroup> getPhaseGroups() {
        return phaseGroups;
    }

    public void setPhaseGroups(List<SmashggPhaseGroup> phaseGroups) {
        this.phaseGroups = phaseGroups;
    }

    public List<SmashggPhase> getPhases() {
        return phases;
    }

    public void setPhases(List<SmashggPhase> phases) {
        this.phases = phases;
    }
}
