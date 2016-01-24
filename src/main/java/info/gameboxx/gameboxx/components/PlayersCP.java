package info.gameboxx.gameboxx.components;

import info.gameboxx.gameboxx.game.GameComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Adding this component allows to have players in the game.
 * This component gets added by default in each {@link info.gameboxx.gameboxx.game.Arena}
 */
public class PlayersCP extends GameComponent {

    private List<UUID> players = new ArrayList<UUID>();

    public PlayersCP(GameComponent parent) {
        super(parent);
    }

    /**
     * Get the list with players.
     * @return List of players their {@link UUID}s
     */
    public List<UUID> getPlayers() {
        return players;
    }

    /**
     * Checks whether or not the specified players {@link UUID} is in the players list.
     * @param player The players {@link UUID} to check.
     * @return True when the player list contains the players {@link UUID}.
     */
    public boolean isPlaying(UUID player) {
        return players.contains(player);
    }

    /**
     * Add the given players {@link UUID} to the player list.
     * @param player The players {@link UUID} to add.
     */
    public void addPlayer(UUID player) {
        players.add(player);
    }

    /**
     * Remove the given players {@link UUID} from the player list.
     * @param player The players {@link UUID} to remove.
     */
    public void removePlayer(UUID player) {
        players.add(player);
    }

    /**
     * Clear the player list.
     */
    public void removePlayers() {
        players.clear();
    }

    /** @see GameComponent#deepCopy() */
    @Override
    public PlayersCP deepCopy() {
        PlayersCP clone = new PlayersCP(getParent());
        copyChildComponents(this, clone);
        return clone;
    }
}
