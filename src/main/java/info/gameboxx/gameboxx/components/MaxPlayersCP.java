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
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.game.GameSession;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.options.single.BoolO;
import info.gameboxx.gameboxx.options.single.IntO;
import info.gameboxx.gameboxx.options.single.StringO;
import org.bukkit.event.EventHandler;

/**
 * Adding this component adds a player limit for the game session.
 * When the limit is reached players wont be able to join anymore.
 * If the game has a countdown it will reduce the countdown to 5 seconds if it has more than 5 seconds remaining.
 */
public class MaxPlayersCP extends GameComponent {

    private static final Events EVENT = new Events();

    public MaxPlayersCP(Game game) {
        super(game);
        addDependency(PlayersCP.class);

        EVENT.register(getAPI());
    }

    @Override
    public void registerOptions() {
        //TODO: Implement these settings.
        registerGameOption("countdown", new IntO().name("Countdown").def(5).min(-1).desc(Msg.getString("opt.countdown")));
        registerGameOption("permission-bypass", new StringO().name("PermissionBypass").def("").desc(Msg.getString("opt.permission-bypass")));
        registerGameOption("auto-spectate", new BoolO().name("AutoSpectate").def(false).desc(Msg.getString("opt.auto-spectate")));

        registerArenaOption("max-players", new IntO().name("MaxPlayers").def(16).min(1).desc(Msg.getString("opt.max-players")));
    }

    @Override
    public MaxPlayersCP newInstance(GameSession session) {
        return (MaxPlayersCP)new MaxPlayersCP(getGame()).setSession(session);
    }

    /**
     * Get the max player count allowed in the arena.
     *
     * @return The maximum player amount allowed.
     */
    public int getMax() {
        return arenaOptions().<IntO>getOption(path("max-players")).getValue();
    }

    private static class Events extends ComponentListener {

        @EventHandler
        private void onJoin(PlayerJoinSessionEvent event) {
            GameSession session = event.getJoinedSession();
            if (!session.hasComponent(MaxPlayersCP.class)) {
                return;
            }

            int maxPlayers = session.getComponent(MaxPlayersCP.class).getMax();
            int playerCount = session.getComponent(PlayersCP.class).getPlayers().size();

            if (playerCount >= maxPlayers) {
                event.setCancelled(true);

            } else if (playerCount + 1 >= maxPlayers) {
                //Reduce countdown when max players have joined.
                if (session.hasComponent(CountdownCP.class)) {
                    CountdownCP countdown = session.getComponent(CountdownCP.class);
                    //TODO: No magic number add config option.
                    if (countdown.getCountdown() > 5) {
                        countdown.setCountdown(5);
                    }
                }
            }
        }

    }
}
