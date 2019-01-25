package net.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggEvent {

    private String slug;
    private long id;
    private long startAt;
    private SmashggTournament tournament;
    private List<PhaseGroup> phaseGroups;
    private List<Phase> phases;
    private VideoGame videogame;

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

    public SmashggTournament getTournament() {
        return tournament;
    }

    public void setTournament(SmashggTournament tournament) {
        this.tournament = tournament;
    }

    public VideoGame getVideogame() {
        return videogame;
    }

    public void setVideogame(VideoGame videogame) {
        this.videogame = videogame;
    }

    public List<PhaseGroup> getPhaseGroups() {
        return phaseGroups;
    }

    public void setPhaseGroups(List<PhaseGroup> phaseGroups) {
        this.phaseGroups = phaseGroups;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }
}
