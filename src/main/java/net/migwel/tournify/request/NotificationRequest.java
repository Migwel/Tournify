package net.migwel.tournify.request;

import net.migwel.tournify.data.SetUpdate;

import java.util.List;

public class NotificationRequest {

    private List<SetUpdate> setUpdates;

    public NotificationRequest(List<SetUpdate> setUpdates) {
        this.setUpdates = setUpdates;
    }

    public List<SetUpdate> getSetUpdates() {
        return setUpdates;
    }

    public void setSetUpdates(List<SetUpdate> setUpdates) {
        this.setUpdates = setUpdates;
    }
}
