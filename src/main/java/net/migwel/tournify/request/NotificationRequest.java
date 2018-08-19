package net.migwel.tournify.request;

public class NotificationRequest {

    private String content;

    public NotificationRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
