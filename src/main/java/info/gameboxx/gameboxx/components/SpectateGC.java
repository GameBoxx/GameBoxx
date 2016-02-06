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

import info.gameboxx.gameboxx.exceptions.OptionAlreadyExistsException;
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.components.internal.GameComponent;
import info.gameboxx.gameboxx.game.GameSession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Adding this component allows players to spectate the game.
 */
public class SpectateGC extends GameComponent {

    private List<UUID> spectators = new ArrayList<>();

    public SpectateGC(Game game) {
        super(game);
    }

    @Override
    public void registerOptions() throws OptionAlreadyExistsException {}

    @Override
    public SpectateGC newInstance(GameSession session) {
        return (SpectateGC) new SpectateGC(getGame()).setSession(session);
    }

    /**
     * Get the list with spectating players.
     * @return List of spectating players their {@link UUID}s
     */
    public List<UUID> getSpectators() {
        return spectators;
    }

    /**
     * Checks whether or not the specified players {@link UUID} is in the spectator list.
     * @param player The players {@link UUID} to check.
     * @return True when the spectator list contains the players {@link UUID}.
     */
    public boolean isSpectating(UUID player) {
        return spectators.contains(player);
    }

    /**
     * Add the given players {@link UUID} to the spectator list.
     * @param player The players {@link UUID} to add.
     */
    public void addSpectator(UUID player) {
        spectators.add(player);
    }

    /**
     * Remove the given players {@link UUID} from the spectator list.
     * @param player The players {@link UUID} to remove.
     */
    public void removeSpectator(UUID player) {
        spectators.add(player);
    }

    /**
     * Clear the spectator list.
     */
    public void removeSpectators() {
        spectators.clear();
    }

}
