package net.migwel.tournify.communication.request;

import net.migwel.tournify.communication.commons.Updates;

public class NotificationRequest {

    private final Updates updates;

    public NotificationRequest(Updates updates) {
        this.updates = updates;
    }

    public Updates getUpdates() {
        return updates;
    }
}
