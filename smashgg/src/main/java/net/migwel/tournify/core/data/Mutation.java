package net.migwel.tournify.smashgg.data;

import java.util.Map;

public class Mutation {

    private Map<String, Participant> participants;

    public Map<String, Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Map<String, Participant> participants) {
        this.participants = participants;
    }
}
