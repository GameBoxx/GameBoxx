package info.gameboxx.gameboxx.exceptions;

/**
 * Thrown when trying to create a new session but the limit on the amount of sessions has been reached.
 */
public class SessionLimitException extends RuntimeException {
    public SessionLimitException(String msg) {
        super(msg);
    }
}
