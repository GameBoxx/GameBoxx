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

package info.gameboxx.gameboxx.game;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.exceptions.ComponentConflictException;
import info.gameboxx.gameboxx.exceptions.DependencyNotFoundException;
import info.gameboxx.gameboxx.exceptions.GameAlreadyExistsException;
import info.gameboxx.gameboxx.exceptions.OptionAlreadyExistsException;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The game manager used to register games.
 * Games must be registered using this class so that the API can keep track of all the games.
 * If you don't register the game it won't work properly.
 */
public class GameManager {

    private Map<String, Game> games = new HashMap<String, Game>();

    /**
     * Register a new {@link Game}.
     * @param gameClass The custom game class instance.
     * @throws GameAlreadyExistsException When a game with the same name is already registered.
     *         You can either register it under a different name
     *         Or you can call {@link #unregister(String)} and register it again.
     */
    public void register(Game gameClass) throws GameAlreadyExistsException, ComponentConflictException, DependencyNotFoundException, OptionAlreadyExistsException {
        String name = gameClass.getName().trim().toLowerCase();
        if (games.containsKey(name)) {
            throw new GameAlreadyExistsException("Failed to register the game '" + name + "' because it's already registered.\n" +
                    "It could be that you have two plugins running for the same game!");
        }

        //Add components and validate them.
        gameClass.addComponents();
        gameClass.validate();
        gameClass.loadSettings();
        gameClass.registerOptions();
        gameClass.registerSetupOptions();

        //Registered successfully!
        games.put(name, gameClass);
        gameClass.loadArenas();

        GameBoxx.get().log("Successfully registered the game '" + gameClass.getName() + "'!");
    }

    /**
     * Unregister a {@link Game}.
     * This should be called {@link JavaPlugin#onDisable()} for cleanup.
     * @param name The name of the game to unregister.
     */
    public void unregister(String name) {
        name = name.trim().toLowerCase();
        if (games.containsKey(name)) {
            //TODO: Unregister all arenas and all sessions..
            games.remove(name);
        }
    }

    /**
     * Get a registered game by it's name.
     * This can be used to easily hook into other games instead of using the Bukkit plugin dependency system.
     * @param name The name of the game to get.
     * @return The game for the specified name or {@code null} if there is no game with the specified name.
     */
    public Game getGame(String name) {
        name = name.trim().toLowerCase();
        return games.get(name);
    }

    /**
     * Get the map with all the registered games.
     * @return Map with all the registered games.
     */
    public Map<String, Game> getGames() {
        return games;
    }

    /**
     * Get a list with all the registered game names.
     * @return List of game names.
     */
    public List<String> getGameNames() {
        return new ArrayList<String>(games.keySet());
    }
}
