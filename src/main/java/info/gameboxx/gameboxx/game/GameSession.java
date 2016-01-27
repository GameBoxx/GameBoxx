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
import info.gameboxx.gameboxx.events.PlayerJoinSessionEvent;
import info.gameboxx.gameboxx.events.PlayerLeaveSessionEvent;
import info.gameboxx.gameboxx.events.SessionResetEvent;
import info.gameboxx.gameboxx.events.SessionStartEvent;
import info.gameboxx.gameboxx.events.SessionStopEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.UUID;

//TODO: Implement this class it will handle the game flow like joining/leaving/starting/stopping/resetting etc. All of that stuff will be dependent on components obviously.
public abstract class GameSession extends ComponentHolder {

    protected Game game;
    protected Arena arena;
    private UUID uid;
    public static final PluginManager PLUGIN_MANAGER = Bukkit.getPluginManager();

    public GameSession(Game game, Arena arena, UUID uid) {
        this.game = game;
        this.arena = arena;
        this.uid = uid;
        PLUGIN_MANAGER.callEvent(new SessionStartEvent(this));
    }

    public UUID getUid() {
        return uid;
    }

    /**
     * Get the {@link Game} of the session.
     * @return The game of the session.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Get the {@link Arena} of the session.
     * @return The arena of the session.
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Adds a player to a session.
     * @param player The player to add.
     */
    public void addPlayer(Player player) {
    	PLUGIN_MANAGER.callEvent(new PlayerJoinSessionEvent(player, this));
    }
    
    /**
     * Removes a player from the active players list.
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

}
