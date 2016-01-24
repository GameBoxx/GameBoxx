package info.gameboxx.gameboxx.exceptions;

/**
 * Thrown when trying to register a game but it already exists.
 * For example if another plugin registers a game with the same before your plugin does.
 */
public class GameAlreadyExistsException extends RuntimeException {
    public GameAlreadyExistsException(String msg) {
        super(msg);
    }
}
