package net.migwel.tournify;

import net.migwel.tournify.data.Update;

import java.util.Collections;
import java.util.List;

public class Updates {

    private List<Update> updateList;
    private boolean tournamentDone;

    public Updates() {
    }

    public Updates(List<Update> updateList, boolean tournamentDone) {
        this.updateList = Collections.unmodifiableList(updateList);
        this.tournamentDone = tournamentDone;
    }

    public static Updates nothingNew() {
        return new Updates(Collections.emptyList(), false);
    }

    public List<Update> getUpdateList() {
        return updateList;
    }

    public void setUpdateList(List<Update> updateList) {
        this.updateList = updateList;
    }

    public boolean isTournamentDone() {
        return tournamentDone;
    }

    public void setTournamentDone(boolean tournamentDone) {
        this.tournamentDone = tournamentDone;
    }
}
