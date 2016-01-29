/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2016 GameBoxx <http://gameboxx.info>
 *  Copyright (c) 2016 contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package info.gameboxx.gameboxx.user;


import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.game.Arena;
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.game.GameSession;
import info.gameboxx.gameboxx.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.UUID;

public class User {

    private UUID uuid;
    private String name;

    private Arena selectedArena = null;

    public User(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public User(UUID uuid) {
        this.uuid = uuid;
        OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(uuid);
        if (player != null) {
            this.name = player.getName();
        }
    }

    public User(String name) {
        this.name = name;
        OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(name);
        if (player != null) {
            this.uuid = player.getUniqueId();
        }
    }

    public Player getPlayer() {
        return Bukkit.getServer().getPlayer(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void join(String game) {
        Game gameObj = GameBoxx.get().getGM().getGame(game);
        int rand = Random.Int(gameObj.getArenas().size());
        Arena arenaObj= (Arena) Collections.singletonList(gameObj.getArenas().values()).get(rand);
        GameSession sessionObj = Random.Item(arenaObj.getActiveSessions().toArray(new GameSession[arenaObj.getActiveSessions().size()]));
        sessionObj.addPlayer(getPlayer());
    }

    // TODO: 1/26/2016 Choose session based on player count
    public void join(String game, String arena) {
        Game gameObj = GameBoxx.get().getGM().getGame(game);
        Arena arenaObj = gameObj.getArena(arena);
        GameSession sessionObj = Random.Item(arenaObj.getActiveSessions().toArray(new GameSession[arenaObj.getActiveSessions().size()]));
        sessionObj.addPlayer(getPlayer());
    }

    public void join(String game, String arena, int gameSession) {
        Game gameObj = GameBoxx.get().getGM().getGame(game);
        Arena arenaObj = gameObj.getArena(arena);
        GameSession sessionObj = arenaObj.getSession(gameSession);
        sessionObj.addPlayer(getPlayer());
    }

    /**
     * Get the selected arena using the /select command.
     * Many other commands will use this selected arena.
     * @return The selected arena.
     */
    public Arena getSelectedArena() {
        return selectedArena;
    }

    /**
     * Set the selected arena.
     * Many commands will use this selected arena.
     * @param arena The arena to set as selected.
     */
    public void setSelectedArena(Arena arena) {
        this.selectedArena = arena;
    }
}
