package dev.migwel.tournify.core.exception;

public class FetchException extends Exception {

    private final boolean retryable;

    public FetchException(String reason, boolean retryable) {
        super(reason);
        this.retryable = retryable;
    }

    public FetchException(String reason, Throwable t, boolean retryable) {
        super(reason, t);
        this.retryable = retryable;
    }

    public boolean isRetryable() {
        return retryable;
    }
}
