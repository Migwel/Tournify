package dev.migwel.tournify.smashgg.data;

public class Set {

    private String id;
    private String entrant1Id;
    private String entrant2Id;
    private String winnerId;
    private Long phaseGroupId;
    private Long bestOf;
    private String fullRoundText;
    private boolean unreachable;
    private int displayRound;

    public String getId() {
        return id;
    }

    public String getEntrant1Id() {
        return entrant1Id;
    }

    public String getEntrant2Id() {
        return entrant2Id;
    }

    public String getWinnerId() {
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

    public boolean isUnreachable() {
        return unreachable;
    }

    public int getDisplayRound() {
        return displayRound;
    }
}
