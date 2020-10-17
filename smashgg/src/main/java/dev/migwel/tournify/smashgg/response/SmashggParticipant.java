package dev.migwel.tournify.smashgg.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmashggParticipant implements Comparable<SmashggParticipant> {
    private String gamerTag;
    private String prefix;

    public String getGamerTag() {
        return gamerTag;
    }

    public void setGamerTag(String gamerTag) {
        this.gamerTag = gamerTag;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public int compareTo(SmashggParticipant o) {
        if (gamerTag.compareTo(o.gamerTag) != 0) {
            return gamerTag.compareTo(o.gamerTag);
        }
        return prefix.compareTo(o.prefix);
    }
}
