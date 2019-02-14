package net.migwel.tournify.communication.commons;

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
        return new Updates(Collections.emptyList(), false);
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
