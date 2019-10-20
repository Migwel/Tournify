package dev.migwel.tournify.communication.response;

import java.util.List;

public class ParticipantsResponse {

    List<String> participants;

    public ParticipantsResponse(List<String> participants) {
        this.participants = participants;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}
