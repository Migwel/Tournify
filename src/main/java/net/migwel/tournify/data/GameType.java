package net.migwel.tournify.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class GameType {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public GameType() {
    }

    public GameType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GameType{" +
                "name='" + name + '\'' +
                '}';
    }
}
