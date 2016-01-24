package info.gameboxx.gameboxx.exceptions;

/**
 * Thrown when trying to create an arena but an arena with the name already exists.
 */
public class ArenaAlreadyExistsException extends RuntimeException {
    //TODO: Empty constructor with configurable error message.
    public ArenaAlreadyExistsException(String msg) {
        super(msg);
    }
}
