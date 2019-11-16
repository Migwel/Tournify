package dev.migwel.tournify.communication.response;

import dev.migwel.tournify.communication.commons.Player;

import java.util.Set;

public class ParticipantsResponse {

    Set<Player> participants;

    public ParticipantsResponse() {
    }

    public ParticipantsResponse(Set<Player> participants) {
        this.participants = participants;
    }

    public Set<Player> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Player> participants) {
        this.participants = participants;
    }
}
