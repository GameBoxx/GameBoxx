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

import info.gameboxx.gameboxx.exceptions.DependencyNotFoundException;
import info.gameboxx.gameboxx.game.GameComponent;
import info.gameboxx.gameboxx.game.HardDependable;

/**
 * @author Msrules123 (Matthew Smith)
 */
// TODO: Start game when player count is reached (How do I get to the game?)

public class MinPlayersCP extends GameComponent implements HardDependable {
	
	private static final Set<Class<? extends GameComponent>> HARD_DEPENDENCIES = getHardDependencies();

	private int minimumPlayers;
	
	/**
	 * @see {@link GameComponent}
	 */
	public MinPlayersCP(GameComponent parent, int minimumPlayers) {
		super(parent);
		this.minimumPlayers = minimumPlayers;
	}

	/**
	 * @see {@link GameComponent}
	 */
	@Override
	public MinPlayersCP deepCopy() {
		MinPlayersCP clone = new MinPlayersCP(getParent(), this.minimumPlayers);
		copyChildComponents(this, clone);
		return clone;
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
	 * Gets the set implementation of this class's hard dependencies.
	 * @return The set implementation.
	 */
	private static Set<Class<? extends GameComponent>> getHardDependencies() {
		Set<Class<? extends GameComponent>> hardDependencies = new HashSet<Class<? extends GameComponent>>();
		hardDependencies.add(PlayersCP.class);
		return hardDependencies;
	}
	
	/**
	 * Gets the boolean value by checking if the players are more than the set minimum player value.
	 * @return The boolean value.
	 * @throws DependencyNotFoundException If the hard dependency was not found.
	 */
	public boolean hasMinimumPlayers() throws DependencyNotFoundException {
		PlayersCP players = (PlayersCP) getHardDependency(PlayersCP.class);
		return (players.getPlayers().size() >= this.minimumPlayers);
	}

}
