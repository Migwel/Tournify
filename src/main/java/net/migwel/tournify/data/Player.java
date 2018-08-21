package net.migwel.tournify.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

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

    public String getDisplayUsername() {
        StringBuilder displayUsername = new StringBuilder();
        if(prefix != null && !prefix.isEmpty()) {
            displayUsername.append(prefix).append(" ");
        }
        displayUsername.append(username);
        return displayUsername.toString();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(prefix, player.prefix) &&
                Objects.equals(username, player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix, username);
    }

    @Override
    public String toString() {
        return "Player{" +
                "prefix='" + prefix + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
