package net.migwel.tournify.communication.request;

import net.migwel.tournify.communication.commons.Update;

public class NotificationRequest {

    private Update update;

    public NotificationRequest() {
    }

    public NotificationRequest(Update update) {
        this.update = update;
    }

    public Update getUpdate() {
        return update;
    }
}
