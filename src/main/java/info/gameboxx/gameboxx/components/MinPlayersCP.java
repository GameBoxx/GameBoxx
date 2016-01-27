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

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.events.PlayerJoinSessionEvent;
import info.gameboxx.gameboxx.exceptions.DependencyNotFoundException;
import info.gameboxx.gameboxx.exceptions.OptionAlreadyExistsException;
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.components.internal.GameComponent;
import info.gameboxx.gameboxx.game.GameSession;
import info.gameboxx.gameboxx.setup.OptionData;
import info.gameboxx.gameboxx.setup.SetupType;

/**
 * Adding this component will make it so the game wont start till the minimum player count is reached.
 * If the game has no countdown it will start as soon as the minimum player count is reached.
 */
// TODO: Start game when player count is reached (How do I get to the game?)
public class MinPlayersCP extends GameComponent {

	private int min;

	/**
	 * @see GameComponent
	 * @param min The default value for the minimum amount of players required to start the game.
     */
	public MinPlayersCP(Game game, int min) {
		super(game);
		addDependency(PlayersCP.class);
		
		this.min = min;
		
		Events.register(getAPI());
	}

	@Override
	public void registerOptions() throws OptionAlreadyExistsException {
		game.registerSetupOption(new OptionData(SetupType.INT, "minPlayers", "The minimum amount of players required to start a game.", min));
	}

	@Override
	public MinPlayersCP newInstance(GameSession session) {
		return (MinPlayersCP) new MinPlayersCP(getGame(), min).setSession(session);
	}

    /**
     * Get the min player count required to start the game.
     * @return The minimum player amount required to start.
     */
    public int getMin() {
        return (int) getOption("minPlayers");
    }
	
	/**
	 * Gets the boolean value by checking if the players are more than the set minimum player value.
	 * @return The boolean value.
	 * @throws DependencyNotFoundException If the hard dependency was not found.
	 */
	public boolean hasMinimumPlayers() throws DependencyNotFoundException {
		return getDependency(PlayersCP.class).getPlayers().size() >= this.min;
	}
	
	/**
	 * Listens for events relating to this component.
	 */
	private static class Events implements Listener {
		
		private Events() {}
		
		private static boolean isRegistered = false;
		
		public static void register(GameBoxx api) {
			if (!(isRegistered)) {
				Bukkit.getPluginManager().registerEvents(new Events(), api);
				isRegistered = true;
			}
		}
		
		@EventHandler
		public void onPlayerJoinSessionEvent(PlayerJoinSessionEvent event) {
			try {
				if (event.getJoinedSession().hasComponent(MinPlayersCP.class)) {
					MinPlayersCP players = (MinPlayersCP) event.getJoinedSession().getComponent(MinPlayersCP.class);
					if (event.getJoinedSession().hasComponent(CountdownGC.class) && players.hasMinimumPlayers()) {
						CountdownGC countdown = (CountdownGC) event.getJoinedSession().getComponent(CountdownGC.class);
						// TODO: Implement the when the new Thread is created
					}
				}
			} catch (DependencyNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		
	}

}
