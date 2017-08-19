package net.migwel.tournify.data;

public class Player {

    private String prefix;
    private String username;

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
