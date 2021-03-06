package dev.migwel.tournify.core.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Player implements Comparable<Player> {

    //Copied from https://stackoverflow.com/questions/481813/how-to-simplify-a-null-safe-compareto-implementation/23908426#23908426
    private final static Comparator<String> nullSafeStringComparator = Comparator
            .nullsFirst(String::compareToIgnoreCase);

    @Id
    private String id;

    private String prefix;
    private String username;

    private String externalId;

    public Player() {
        this(null);
    }

    public Player(String username) {
        this(null, username);
    }

    public Player(String prefix, String username) {
        this(prefix, username, null);
    }

    public Player(String prefix, String username, String externalId) {
        this.id = UUID.randomUUID().toString();
        this.prefix = prefix;
        this.username = username;
        this.externalId = externalId;
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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
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

    @Override
    public int compareTo(Player that) {
        int compareUsername = nullSafeStringComparator.compare(this.username, that.username);
        if(compareUsername != 0) {
            return compareUsername;
        }
        return nullSafeStringComparator.compare(this.prefix, that.prefix);
    }
}
