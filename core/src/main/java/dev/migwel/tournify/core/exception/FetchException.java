package dev.migwel.tournify.core.exception;

public class FetchException extends Exception {
    public FetchException() {
    }

    public FetchException(String reason) {
        super(reason);
    }

    public FetchException(String reason, Throwable t) {
        super(reason, t);
    }

    public FetchException(Throwable t) {
        super(t);
    }
}
