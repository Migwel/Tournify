package dev.migwel.tournify.communication.commons;

import java.util.Objects;

public class Player {

    private String prefix;
    private String username;

    public Player() {
        this(null);
    }

    public Player(String username) {
        this(null, username);
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
