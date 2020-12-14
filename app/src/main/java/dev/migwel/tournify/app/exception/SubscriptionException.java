package dev.migwel.tournify.app.exception;

public class SubscriptionException extends Exception {
    public SubscriptionException() {
    }

    public SubscriptionException(String reason) {
        super(reason);
    }

    public SubscriptionException(String reason, Throwable t) {
        super(reason, t);
    }

    public SubscriptionException(Throwable t) {
        super(t);
    }
}
