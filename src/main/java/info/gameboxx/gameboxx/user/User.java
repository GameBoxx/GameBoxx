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
import org.bukkit.entity.Player;

import java.util.UUID;

public class User {

    private Player player;
    private UUID uuid;
    private String name;

    public User(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void join(String game) {
        Game gameObj = GameBoxx.get().getGM().getGame(game);
        Arena arenaObj = gameObj.getArenas().get(0);
        GameSession sessionObj = arenaObj.getSessions().get(0);
        sessionObj.addPlayer(player);
    }

    // TODO: 1/26/2016 Choose session based on player count
    public void join(String game, String arena) {
        Game gameObj = GameBoxx.get().getGM().getGame(game);
        Arena arenaObj = gameObj.getArena(arena);
        GameSession sessionObj = arenaObj.getSessions().get(0);
        sessionObj.addPlayer(player);
    }

    public void join(String game, String arena, UUID gameSession) {
        Game gameObj = GameBoxx.get().getGM().getGame(game);
        Arena arenaObj = gameObj.getArena(arena);
        GameSession sessionObj = arenaObj.getSession(gameSession);
        sessionObj.addPlayer(player);
    }
}
