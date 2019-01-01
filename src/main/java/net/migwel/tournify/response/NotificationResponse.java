package net.migwel.tournify.response;

public class NotificationResponse {

    private final String status;

    public NotificationResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
