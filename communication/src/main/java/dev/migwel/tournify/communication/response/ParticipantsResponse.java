package dev.migwel.tournify.communication.response;

import dev.migwel.tournify.communication.commons.Player;

import java.util.Collection;

public class ParticipantsResponse {

    Collection<Player> participants;

    public ParticipantsResponse() {
    }

    public ParticipantsResponse(Collection<Player> participants) {
        this.participants = participants;
    }

    public Collection<Player> getParticipants() {
        return participants;
    }

    public void setParticipants(Collection<Player> participants) {
        this.participants = participants;
    }
}
