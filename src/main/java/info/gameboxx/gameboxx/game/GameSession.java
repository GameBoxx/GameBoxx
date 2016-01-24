package info.gameboxx.gameboxx.game;

import java.util.UUID;

//TODO: Implement this class it will handle the game flow like joining/leaving/starting/stopping/resetting etc. All of that stuff will be dependent on components obviously.
public abstract class GameSession extends GameComponent {

    protected UUID uid;

    public GameSession(Game game, UUID uid) {
        super(game);
        this.uid = uid;

    }

}
