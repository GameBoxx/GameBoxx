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
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.game.GameComponent;
import info.gameboxx.gameboxx.game.GameSession;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Adding this component adds a player limit for the game session.
 * When the limit is reached players wont be able to join anymore.
 * If the game has a countdown it will reduce the countdown to 5 seconds if it has more than 5 seconds remaining.
 */
public class MaxPlayersCP extends GameComponent {

	private int maximumPlayers;
	
	/**
	 * @see GameComponent
	 * @param maximumPlayers The maximum amount of players allowed
	 */
	public MaxPlayersCP(Game game, int maximumPlayers) {
		super(game);
		addDependency(PlayersCP.class);

		this.maximumPlayers = maximumPlayers;
		Bukkit.getPluginManager().registerEvents(new Events(), getAPI());
	}

	@Override
	public MaxPlayersCP newInstance(GameSession session) {
		return (MaxPlayersCP) new MaxPlayersCP(getGame(), maximumPlayers).setSession(session);
	}

	private static class Events implements Listener {
		@EventHandler
		private void onJoin(PlayerJoinSessionEvent event) {
			GameSession session = event.getJoinedSession();
			if (!session.hasComponent(MaxPlayersCP.class)) {
				return;
			}
			if (session.getComponent(MaxPlayersCP.class).maximumPlayers >= session.getComponent(PlayersCP.class).getPlayers().size()) {
				//TODO: Cancel event.
			}
		}
	}
}
