package dev.migwel.tournify.communication.commons;

import java.util.Collection;
import java.util.Collections;

public class Updates {

    private Collection<Update> updateList;
    private boolean tournamentDone;

    public Updates() {
    }

    public Updates(Collection<Update> updateList, boolean tournamentDone) {
        this.updateList = Collections.unmodifiableCollection(updateList);
        this.tournamentDone = tournamentDone;
    }

    public static Updates nothingNew() {
        return nothingNew(false);
    }

    public static Updates nothingNew(boolean tournamentDone) {
        return new Updates(Collections.emptyList(), tournamentDone);
    }

    public Collection<Update> getUpdateList() {
        return updateList;
    }

    public void setUpdateList(Collection<Update> updateList) {
        this.updateList = updateList;
    }

    public boolean isTournamentDone() {
        return tournamentDone;
    }

    public void setTournamentDone(boolean tournamentDone) {
        this.tournamentDone = tournamentDone;
    }
}
