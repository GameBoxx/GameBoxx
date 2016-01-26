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

import java.util.HashSet;
import java.util.Set;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import info.gameboxx.gameboxx.events.PlayerJoinSessionEvent;
import info.gameboxx.gameboxx.exceptions.DependencyNotFoundException;
import info.gameboxx.gameboxx.game.GameComponent;
import info.gameboxx.gameboxx.game.HardDependable;
import info.gameboxx.gameboxx.game.LeaveReason;

/**
 * @author Msrules123 (Matthew Smith)
 * 
 */
public class MaxPlayersCP extends GameComponent implements HardDependable, Listener {
	
	private static final Set<Class<? extends GameComponent>> HARD_DEPENDENCIES = getHardDependencies();

	private int maximumPlayers;
	
	/**
	 * @param maximumPlayers The maximum amount of players allowed
	 */
	public MaxPlayersCP(GameComponent parent, int maximumPlayers) {
		super(parent);
		this.maximumPlayers = maximumPlayers;
		PLUGIN_MANAGER.registerEvents(this, getAPI());
	}

	/**
	 * @see {@link HardDependable}
	 */
	@Override
	public Set<Class<? extends GameComponent>> getHardDependencySet() {
		return HARD_DEPENDENCIES;
	}

	/**
	 * @see {@link HardDependable}
	 */
	@Override
	public GameComponent getHardDependency(Class<? extends GameComponent> clazz)
			throws DependencyNotFoundException {
		GameComponent parent = getParent();
		if (parent.hasComponent(clazz)) {
			return parent.getComponent(clazz);
		} else {
			throw new DependencyNotFoundException(this, clazz);
		}
	}

	/**
	 * @see {@link GameComponent}
	 */
	@Override
	public MaxPlayersCP deepCopy() {
		MaxPlayersCP clone = new MaxPlayersCP(getParent(), this.maximumPlayers);
		clone.copyChildComponents(this, clone);
		return clone;
	}
	
	private static Set<Class<? extends GameComponent>> getHardDependencies() {
		Set<Class<? extends GameComponent>> hardDependencies = new HashSet<Class<? extends GameComponent>>();
		hardDependencies.add(PlayersCP.class);
		return hardDependencies;
	}
	/**
	 * Listens for the {@link PlayerJoinSessionEvent} to remove any player exceeding the limit from it's
	 * GameComponent parent.
	 * @param event The event to listen to.
	 */
	@EventHandler
	public void onPlayerJoinSessionEvent(PlayerJoinSessionEvent event) {
		if (event.getJoinedSession().equals(getParent())) {
			try {
				PlayersCP players = (PlayersCP) getHardDependency(PlayersCP.class);
				if (players.getPlayers().size() > this.maximumPlayers) {
					players.removePlayer(event.getWhoJoined().getUniqueId(), LeaveReason.KICK);
				}
			} catch (DependencyNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}

}
