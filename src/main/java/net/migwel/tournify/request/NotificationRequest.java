package net.migwel.tournify.request;

import net.migwel.tournify.Updates;

public class NotificationRequest {

    private final Updates updates;

    public NotificationRequest(Updates updates) {
        this.updates = updates;
    }

    public Updates getUpdates() {
        return updates;
    }
}
