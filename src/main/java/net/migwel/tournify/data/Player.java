package net.migwel.tournify.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    private String prefix;
    private String username;

    public Player() {
    }

    public Player(String prefix, String username) {
        this.prefix = prefix;
        this.username = username;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Player{" +
                "prefix='" + prefix + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
