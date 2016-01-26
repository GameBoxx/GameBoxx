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

import info.gameboxx.gameboxx.exceptions.DependencyNotFoundException;
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.game.GameComponent;
import info.gameboxx.gameboxx.game.GameSession;

/**
 * Adding this component will make it so the game wont start till the minimum player count is reached.
 * If the game has no countdown it will start as soon as the minimum player count is reached.
 */
// TODO: Start game when player count is reached (How do I get to the game?)
public class MinPlayersCP extends GameComponent {

	private int minimumPlayers;

	/**
	 * @see GameComponent
	 * @param minimumPlayers The minimum amount of players required to start the game.
     */
	public MinPlayersCP(Game game, int minimumPlayers) {
		super(game);
		addDependency(PlayersCP.class);

		this.minimumPlayers = minimumPlayers;
	}

	@Override
	public MinPlayersCP newInstance(GameSession session) {
		return (MinPlayersCP) new MinPlayersCP(getGame(), minimumPlayers).setSession(session);
	}
	
	/**
	 * Gets the boolean value by checking if the players are more than the set minimum player value.
	 * @return The boolean value.
	 * @throws DependencyNotFoundException If the hard dependency was not found.
	 */
	public boolean hasMinimumPlayers() throws DependencyNotFoundException {
		return getDependency(PlayersCP.class).getPlayers().size() >= this.minimumPlayers;
	}

}
