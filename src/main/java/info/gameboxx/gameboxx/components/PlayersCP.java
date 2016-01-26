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

import info.gameboxx.gameboxx.components.internal.GameComponent;
import info.gameboxx.gameboxx.events.PlayerJoinSessionEvent;
import info.gameboxx.gameboxx.exceptions.OptionAlreadyExistsException;
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.game.GameSession;
import info.gameboxx.gameboxx.game.LeaveReason;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Adding this component allows to have players in the game.
 * This component gets added by default in each {@link info.gameboxx.gameboxx.game.Arena}
 */
public class PlayersCP extends GameComponent {

    private List<UUID> players = new ArrayList<UUID>();
    private List<UUID> removedPlayers = new ArrayList<UUID>();

    public PlayersCP(Game game) {
        super(game);
        
        Bukkit.getPluginManager().registerEvents(new Events(), getAPI());
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
    public List<UUID> getPlayers() {
        return players;
    }

    /**
     * Get the list with online players.
     * @return List of players that are online.
     */
    public List<Player> getOnlinePlayers() {
        //TODO: Cache this probably as it will prob get called a lot.
        return Utils.getPlayerList(players);
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

    private static class Events implements Listener {

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onPlayerJoinSessionEvent(PlayerJoinSessionEvent event) {
            GameSession session = event.getJoinedSession();
            if (session.hasComponent(PlayersCP.class)) {
                session.getComponent(PlayersCP.class).addPlayer(event.getWhoJoined().getUniqueId());
            }
        }
    }
}
