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

package info.gameboxx.gameboxx.components;

import info.gameboxx.gameboxx.components.internal.ComponentListener;
import info.gameboxx.gameboxx.components.internal.GameComponent;
import info.gameboxx.gameboxx.events.PlayerJoinSessionEvent;
import info.gameboxx.gameboxx.events.PlayerLeaveSessionEvent;
import info.gameboxx.gameboxx.exceptions.OptionAlreadyExistsException;
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.game.GameSession;
import info.gameboxx.gameboxx.game.LeaveReason;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Adding this component allows to have players in the game.
 * This component gets added by default in each {@link info.gameboxx.gameboxx.game.Arena}
 */
public class PlayersCP extends GameComponent {

    private static final Events EVENT = new Events();
    
    private Set<UUID> players = new HashSet<>();
    private Set<UUID> removedPlayers = new HashSet<>();
    
    private Set<Player> cachedPlayers = new HashSet<>();

    public PlayersCP(Game game) {
        super(game);
        
        EVENT.register(getAPI());
    }

    @Override
    public void registerOptions() throws OptionAlreadyExistsException {}

    @Override
    public PlayersCP newInstance(GameSession session) {
        return (PlayersCP) new PlayersCP(getGame()).setSession(session);
    }

    /**
     * Get the list with players.
     * @return List of players their {@link UUID}s
     */
    public Set<Player> getPlayers() {
        return cachedPlayers;
    }

    /**
     * Get the list with online players.
     * @return List of players that are online.
     */
    public Set<Player> getOnlinePlayers() {
        return cachedPlayers;
    }

    /**
     * Checks whether or not the specified players {@link UUID} is in the players list.
     * @param player The players {@link UUID} to check.
     * @return True when the player list contains the players {@link UUID}.
     */
    public boolean isPlaying(UUID player) {
        return (players.contains(player) || removedPlayers.contains(player));
    }

    /**
     * Add the given players {@link UUID} to the player list.
     * @param player The players {@link UUID} to add.
     * @return Returns whether or not the player left by disconnection earlier in the session.
     */
    public boolean addPlayer(UUID player) {
        players.add(player);
        return removedPlayers.remove(player);
    }

    /**
     * Remove the given players {@link UUID} from the player list.
     * @param player The players {@link UUID} to remove.
     * @param reason The reason why the player is to be removed, will add to removedPlayers if == DISCONNECT.
     */
    public void removePlayer(UUID player, LeaveReason reason) {
        players.remove(player);
        if (reason == LeaveReason.DISCONNECT) {
            removedPlayers.add(player);
        }
        session.removePlayer(Bukkit.getPlayer(player), reason);
    }

    /**
     * Clear the player list.
     */
    public void removePlayers() {
        players.clear();
        removedPlayers.clear();
    }
    /**
     * Add a cached player to a set of cached players.
     * @param player The player to add.
     */
    public void addCachedPlayer(Player player) {
        cachedPlayers.add(player);
    }
    
    /**
     * Remove a cached player from the set of cached players.
     * @param player The player to remove.
     */
    public void removeCachedPlayer(Player player) {
        cachedPlayers.remove(player);
    }
    
    /**
     * Gets the set of original players in this component.
     * @return The set of original players.
     */
    public Set<UUID> getOriginalPlayers() {
        Set<UUID> originals = new HashSet<>();
        originals.addAll(players);
        originals.addAll(removedPlayers);
        return originals;
    }

    private static class Events extends ComponentListener {

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onPlayerJoinSessionEvent(PlayerJoinSessionEvent event) {
            GameSession session = event.getJoinedSession();
            if (session.hasComponent(PlayersCP.class)) {
                PlayersCP players = session.getComponent(PlayersCP.class);
                Player player = event.getPlayer();
                players.addPlayer(player.getUniqueId());
                players.addCachedPlayer(player);
            }
        }
        
        @EventHandler
        public void onPlayerLeaveSessionEvent(PlayerLeaveSessionEvent event) {
            GameSession session = event.getSession();
            if (session.hasComponent(PlayersCP.class)) {
                PlayersCP players = (PlayersCP) session.getComponent(PlayersCP.class);
                Player player = event.getPlayer();
                players.removeCachedPlayer(player);
                players.removePlayer(player.getUniqueId(), event.getLeaveReason());
            }
        }
    }
}
