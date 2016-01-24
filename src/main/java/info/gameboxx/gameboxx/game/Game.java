package info.gameboxx.gameboxx.game;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.exceptions.ArenaAlreadyExistsException;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a game instance.
 * You extend this game class in your plugin to design your own game like SpleefGame.
 * <b>Make sure you register your game class using the {@link GameManager#register(Game)} method!</b>
 * This class is a GameComponent so you can add any of the components.
 * @see {@link com.jroossien.gameapi.components}
 */
//TODO: Make a sub class for GameComponent without the deepCopy method and extend that because we don't wanna confuse people with having to add a deepCopy method.
public abstract class Game extends GameComponent {

    protected GameBoxx gb;
    protected String name;
    protected JavaPlugin plugin;

    private File arenaDir;
    private Map<String, Arena> arenas = new HashMap<String, Arena>();

    /**
     * <b>Make sure you register your game class using the {@link GameManager#register(Game)} method!</b>
     * @param plugin The plugin instance the game belongs to.
     * @param name The name of the game like for example Spleef.
     */
    public Game(JavaPlugin plugin, String name) {
        super(null);
        gb = GameBoxx.get();

        this.name = name;
        this.plugin = plugin;

        arenaDir = new File(new File(plugin.getDataFolder(), "Arenas"), name);
        arenaDir.mkdirs();
    }

    /**
     * Override this method and return your own version of the {@link GameSession}
     * Each time a new session will be created it will call this method to create the new session.
     * Create a class like SpleefSession and extend from GameSession and then do return new SpleefSession(this, sessionUID);
     * @return GameSession
     */
    public abstract GameSession getNewGameSession(UUID sessionUID);

    /**
     * Override this method and inside the method body you will add all your components.
     */
    public abstract void addComponents();

    /**
     * Used by the {@link GameManager} to validate all the added components.
     * It will check for dependencies and conflicts and such.
     * If there is any validation error the game won't be registered and an exception will be thrown by the {@link GameManager#register(Game)} method.
     */
    public void validate() {
        //TODO: Implement this..
    }

    /**
     * Load all the arenas from their config files.
     * It loads the config files from the owning plugins data folder then the Arenas folder and then the game name. For example /plugins/Spleef/Arenas/Spleef
     * If there are multiple games per plugin it shouldn't have any conflicts with data files.
     * The arena will only load when the config has a valid name and there is no arena loaded with that name.
     */
    public void loadArenas() {
        Map<String, File> configFiles = Utils.getFiles(arenaDir, "yml");
        int count = 0;
        for (Map.Entry<String, File> entry : configFiles.entrySet()) {
            YamlConfiguration arenaCfg = YamlConfiguration.loadConfiguration(entry.getValue());
            Arena arena = new Arena(this, arenaCfg.getDefaultSection());
            if (arena.getName() == null || arena.getName().trim().isEmpty()) {
                gb.error("No valid arena name found while trying to load the arena from '" + entry.getKey() + ".yml'\n" +
                        "Please check your configuration for that arena.");
                continue;
            }
            String name = arena.getName().trim().toLowerCase();
            if (arenas.containsKey(name)) {
                gb.error("Failed to load the arena '" + name + "' because an arena with this name is already loaded.\n" +
                        "Make sure you don't have two arenas with the same name!");
                continue;
            }
            arenas.put(name, arena);
            count++;
        }
        gb.log("Loaded in " + count + " arenas for " + name + "!");
    }

    /**
     * Create/Register a new {@link Arena} with the specified name.
     * A random unique ID will will be generated and asigned for the arena so the name is not used for referencing.
     * @return The created {@link Arena} instance.
     * @throws ArenaAlreadyExistsException If an arena with the specified name already exists.
     */
    public Arena createArena(ArenaType type, String arenaName) throws ArenaAlreadyExistsException {
        if (arenas.containsKey(arenaName.trim().toLowerCase())) {
            throw new ArenaAlreadyExistsException("An arena with the name " + arenaName + " already exists!");
        }
        Arena arena = new Arena(this, type, name);
        arenas.put(name, arena);
        //TODO: Save..
        return arena;
    }

    /**
     * Check if an {@link Arena} with the specified name exists or not.
     * @param arenaName The arena name to check for.
     * @return True when the arena exists.
     */
    public boolean hasArena(String arenaName) {
        arenaName.trim().toLowerCase();
        return arenas.containsKey(arenaName);
    }

    /**
     * Get an {@link Arena} instance by it's unique ID.
     * @param arenaName The arena name to check for.
     * @return The {@link Arena} instance or {@code null} if no arena with the specified id exists.
     */
    public Arena getArena(String arenaName) {
        arenaName.trim().toLowerCase();
        return arenas.get(arenaName);
    }

    /**
     * Get the name/type for this game. For example Spleef.
     * @return The name of the game.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the plugin that owns this game. (The plugin that registered it)
     * @return {@link JavaPlugin} plugin instance.
     */
    public JavaPlugin getPlugin() {
        return plugin;
    }
}
