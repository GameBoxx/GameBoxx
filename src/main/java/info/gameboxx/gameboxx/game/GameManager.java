package info.gameboxx.gameboxx.game;

import info.gameboxx.gameboxx.exceptions.GameAlreadyExistsException;
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
    public void register(Game gameClass) throws GameAlreadyExistsException {
        String name = gameClass.getName().trim().toLowerCase();
        if (games.containsKey(name)) {
            throw new GameAlreadyExistsException("Failed to register the game '" + name + "' because it's already registered.\n" +
                    "It could be that you have two plugins running for the same game!");
        }

        //Add components and validate them.
        gameClass.addComponents();
        //TODO: Throw exceptions when validation fails.
        gameClass.validate();

        //Registered successfully!
        games.put(name, gameClass);
        gameClass.loadArenas();
    }

    /**
     * Unregister a {@link Game}.
     * This should be called {@link JavaPlugin#onDisable()} for cleanup.
     * @param name The name of the game to unregister.
     */
    public void unregister(String name) {
        name.trim().toLowerCase();
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
        name.trim().toLowerCase();
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
