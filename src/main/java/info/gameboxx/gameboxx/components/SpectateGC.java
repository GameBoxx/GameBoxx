package info.gameboxx.gameboxx.components;

import info.gameboxx.gameboxx.game.GameComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Adding this component allows players to spectate the game.
 */
public class SpectateGC extends GameComponent {

    private List<UUID> spectators = new ArrayList<UUID>();

    public SpectateGC(GameComponent parent) {
        super(parent);
    }

    /**
     * Get the list with spectating players.
     * @return List of spectating players their {@link UUID}s
     */
    public List<UUID> getSpectators() {
        return spectators;
    }

    /**
     * Checks whether or not the specified players {@link UUID} is in the spectator list.
     * @param player The players {@link UUID} to check.
     * @return True when the spectator list contains the players {@link UUID}.
     */
    public boolean isSpectating(UUID player) {
        return spectators.contains(player);
    }

    /**
     * Add the given players {@link UUID} to the spectator list.
     * @param player The players {@link UUID} to add.
     */
    public void addSpectator(UUID player) {
        spectators.add(player);
    }

    /**
     * Remove the given players {@link UUID} from the spectator list.
     * @param player The players {@link UUID} to remove.
     */
    public void removeSpectator(UUID player) {
        spectators.add(player);
    }

    /**
     * Clear the spectator list.
     */
    public void removeSpectators() {
        spectators.clear();
    }

    /** @see GameComponent#deepCopy() */
    @Override
    public SpectateGC deepCopy() {
        SpectateGC clone = new SpectateGC(getParent());
        copyChildComponents(this, clone);
        return clone;
    }
}
