package net.migwel.tournify.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Phase> phases;

    @ManyToOne(cascade=CascadeType.ALL)
    private GameType gameType;

    private String name;
    private String description;

    public Event() {
    }

    public Event(List<Phase> phases, GameType gameType, String name, String description) {
        this.phases = phases;
        this.gameType = gameType;
        this.name = name;
        this.description = description;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Event{" +
                "phases=" + phases +
                ", gameType=" + gameType +
                ", name=" + name +
                ", description=" + description +
                '}';
    }
}
