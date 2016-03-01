/*
 The MIT License (MIT)

 Copyright (c) 2016 GameBoxx <http://gameboxx.info>
 Copyright (c) 2016 contributors

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

package info.gameboxx.gameboxx.game;

import info.gameboxx.gameboxx.components.internal.ComponentHolder;
import info.gameboxx.gameboxx.config.internal.OptionCfg;
import info.gameboxx.gameboxx.events.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public abstract class GameSession extends ComponentHolder {

    protected Game game;
    protected Arena arena;
    private int id;

    private boolean ready = false;
    private World world = null;

    public static final PluginManager PLUGIN_MANAGER = Bukkit.getPluginManager();

    public GameSession(Game game, Arena arena, int id) {
        this.game = game;
        this.arena = arena;
        this.id = id;
        PLUGIN_MANAGER.callEvent(new SessionStartEvent(this));
    }

    /**
     * Get the session ID.
     *
     * @return The session ID.
     */
    public int getID() {
        return id;
    }

    /**
     * Get the {@link Game} of the session.
     *
     * @return The game of the session.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Get the game options config.
     *
     * @return {@link OptionCfg} with all the game options.
     * @see Game#getConfig()
     */
    public OptionCfg getGameOptions() {
        return game.getConfig();
    }

    /**
     * Get the {@link Arena} of the session.
     *
     * @return The arena of the session.
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Get the arena options config.
     *
     * @return {@link OptionCfg} with all the arena options.
     * @see Arena#getConfig()
     */
    public OptionCfg getArenaOptions() {
        return arena.getConfig();
    }

    /**
     * Get the {@link World} the session is hosted in.
     *
     * @return The world of the session.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Set the {@link World} the session is hosted in.
     * There is no need to call this as this will be set when new sessions get created.
     *
     * @param world The world of the session.
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Get whether or not the session is ready to be joined and played.
     * After the world is generated this will be set to true.
     * While this is false players shouldn't be able to join etc.
     *
     * @return True when the session is ready and false if not.
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Set whether or not the session is ready to be joined and played etc.
     * This method is used when creating sessions when worlds are generated/loaded.
     * There is no need to manually call this.
     *
     * @param ready The ready state to set.
     */
    public void setReady(boolean ready) {
        this.ready = ready;
    }


    //region Management

    /**
     * Adds a player to a session.
     *
     * @param player The player to add.
     */
    public void addPlayer(Player player) {
        PLUGIN_MANAGER.callEvent(new PlayerJoinSessionEvent(player, this));
    }

    /**
     * Removes a player from the active players list.
     *
     * @param player The player who is leaving the session.
     * @param reason The reason for which the player is leaving the session.
     */
    public void removePlayer(Player player, LeaveReason reason) {
        PLUGIN_MANAGER.callEvent(new PlayerLeaveSessionEvent(player, this, reason));
    }

    /**
     * Stops the session.
     */
    public void stop() {
        PLUGIN_MANAGER.callEvent(new SessionStopEvent(this));
        // TODO: Handle implementation later
    }

    /**
     * Restarts the session.
     */
    public void restart() {
        PLUGIN_MANAGER.callEvent(new SessionResetEvent(this));
        // TODO: Handle implementation later
    }
    //endregion


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameSession other = (GameSession)obj;
        if (other.getID() != this.getID()) {
            return false;
        }
        if (!other.getArena().equals(this.getArena())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = arena.hashCode();
        result = 31 * result + id;
        return result;
    }
}
