package net.migwel.tournify.smashgg.data;

public class Participant {

    private long id;
    private long playerId;
    private String gamerTag;
    private String prefix;

    public long getId() {
        return id;
    }

    public long getPlayerId() {
        return playerId;
    }

    public String getGamerTag() {
        return gamerTag;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public void setGamerTag(String gamerTag) {
        this.gamerTag = gamerTag;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
