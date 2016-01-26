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

import info.gameboxx.gameboxx.events.PlayerJoinSessionEvent;
import info.gameboxx.gameboxx.exceptions.OptionAlreadyExistsException;
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.components.internal.GameComponent;
import info.gameboxx.gameboxx.game.GameSession;
import info.gameboxx.gameboxx.setup.SetupType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Adding this component adds a player limit for the game session.
 * When the limit is reached players wont be able to join anymore.
 * If the game has a countdown it will reduce the countdown to 5 seconds if it has more than 5 seconds remaining.
 */
public class MaxPlayersCP extends GameComponent {

	private int max;
	
	/**
	 * @see GameComponent
	 * @param max The maximum amount of players allowed
	 */
	public MaxPlayersCP(Game game, int max) {
		super(game);
		addDependency(PlayersCP.class);

		this.max = max;
		Bukkit.getPluginManager().registerEvents(new Events(), getAPI());
	}

    @Override
    public void registerOptions() throws OptionAlreadyExistsException {
        game.registerSetupOption("maxplayers", SetupType.INT);
    }

	@Override
	public MaxPlayersCP newInstance(GameSession session) {
		return (MaxPlayersCP) new MaxPlayersCP(getGame(), max).setSession(session);
	}

    /**
     * Get the max player count allowed in the arena.
     * @return The maximum player amount allowed.
     */
    public int getMax() {
        return max;
    }

	private static class Events implements Listener {
		@EventHandler
		private void onJoin(PlayerJoinSessionEvent event) {
			GameSession session = event.getJoinedSession();
			if (!session.hasComponent(MaxPlayersCP.class)) {
				return;
			}

			int maxPlayers = session.getComponent(MaxPlayersCP.class).max;
			int playerCount = session.getComponent(PlayersCP.class).getPlayers().size();

			if (playerCount >= maxPlayers) {
				//Prevent joining.
				//TODO: Cancel event.

			} else if (playerCount + 1 >= maxPlayers) {
				//Reduce countdown when max players have joined.
				if (session.hasComponent(CountdownGC.class)) {
					CountdownGC countdown = session.getComponent(CountdownGC.class);
					//TODO: No magic number add config option.
					if (countdown.getCountdown() > 5) {
						countdown.setCountdown(5);
					}
				}
			}
		}
	}
}
