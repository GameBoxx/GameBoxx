package info.gameboxx.gameboxx.game;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The game manager used to register games.
 * Games must be registered using this class for configurations to load properly and such.
 * <b>DO NOT</b> create a {@link Game} instance directly.
 */
public class GameManager {

    private Map<UUID, Game> games = new HashMap<UUID, Game>();

    /**
     * Register a new {@link Game}.
     * @param plugin The plugin that owns this game. <i>(This should be your plugin and not the API)</i>
     * @param name The name of the game for example Spleef.
     *             When changing the name of a game all the old arena configurations won't load anymore.
     *             You'll have to manually update the type entry in the arena configs.
     * @param arenaClass Your custom {@link Arena} class. <i>(For example SpleefArena.class)</i>
     * @return The registered Game instance.
     *         Store this instance or the game UID in your plugin class so that you can unregister it on disable.
     */
    public Game register(JavaPlugin plugin, String name, Class<? extends Arena> arenaClass) {
        UUID gameUID = UUID.randomUUID();
        while (games.containsKey(gameUID)) {
            gameUID = UUID.randomUUID();
        }
        Game game = new Game(gameUID, plugin, name, arenaClass);
        games.put(gameUID, game);
        return game;
    }

    /**
     * Unregister a {@link Game}.
     * This should be called {@link JavaPlugin#onDisable()}.
     * If you don't unregister the event a user reloads just your plugin it would be registered twice.
     * So make sure you clean up by unregistering it.
     * @param gameUID The unique ID of the game instance.
     */
    public void unregister(UUID gameUID) {
        games.remove(gameUID);
    }
}
