package net.migwel.tournify.data.consumer.smashgg;

public class Set {

    private String id;
    private Long entrant1Id;
    private Long entrant2Id;
    private Long winnerId;
    private Long phaseGroupId;
    private Long bestOf;
    private String fullRoundText;

    public String getId() {
        return id;
    }

    public Long getEntrant1Id() {
        return entrant1Id;
    }

    public Long getEntrant2Id() {
        return entrant2Id;
    }

    public Long getWinnerId() {
        return winnerId;
    }

    public Long getPhaseGroupId() {
        return phaseGroupId;
    }

    public Long getBestOf() {
        return bestOf;
    }

    public String getFullRoundText() {
        return fullRoundText;
    }
}
