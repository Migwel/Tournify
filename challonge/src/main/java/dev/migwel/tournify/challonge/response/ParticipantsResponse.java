package dev.migwel.tournify.challonge.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantsResponse {

    private ChallongeParticipant participant;

    public ParticipantsResponse() {
    }

    public ChallongeParticipant getParticipant() {
        return participant;
    }

    public void setParticipant(ChallongeParticipant participant) {
        this.participant = participant;
    }
}
