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
import info.gameboxx.gameboxx.exceptions.DependencyNotFoundException;
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.game.GameSession;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.options.single.IntO;
import org.bukkit.event.EventHandler;

/**
 * Adding this component will make it so the game wont start till the minimum player count is reached.
 * If the game has no countdown it will start as soon as the minimum player count is reached.
 */
// TODO: Start game when player count is reached (How do I get to the game?)
public class MinPlayersCP extends GameComponent {

    private static final Events EVENT = new Events();

    public MinPlayersCP(Game game) {
        super(game);
        addDependency(PlayersCP.class);

        EVENT.register(getAPI());
    }

    @Override
    public void registerOptions() {
        registerArenaOption("min-players", new IntO().name("MinPlayers").def(2).min(1).desc(Msg.getString("opt.min-players")));
    }

    @Override
    public MinPlayersCP newInstance(GameSession session) {
        return (MinPlayersCP)new MinPlayersCP(getGame()).setSession(session);
    }

    /**
     * Get the min player count required to start the game.
     *
     * @return The minimum player amount required to start.
     */
    public int getMin() {
        return arenaOptions().<IntO>getOption(path("min-players")).getValue();
    }

    /**
     * Gets the boolean value by checking if the players are more than the set minimum player value.
     *
     * @return The boolean value.
     * @throws DependencyNotFoundException If the hard dependency was not found.
     */
    public boolean hasMinimumPlayers() {
        return getDependency(PlayersCP.class).getPlayers().size() >= getMin();
    }

    /**
     * Listens for events relating to this component.
     */
    private static class Events extends ComponentListener {

        @EventHandler
        public void onPlayerJoinSessionEvent(PlayerJoinSessionEvent event) {
            if (event.getJoinedSession().hasComponent(MinPlayersCP.class)) {
                MinPlayersCP players = (MinPlayersCP)event.getJoinedSession().getComponent(MinPlayersCP.class);
                if (event.getJoinedSession().hasComponent(CountdownCP.class) && players.hasMinimumPlayers()) {
                    event.getJoinedSession().getComponent(CountdownCP.class).startCountdown();
                }
            }
        }

    }

}
