package info.gameboxx.gameboxx.game;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.util.Parse;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a game instance.
 * Handles arena creation and management and global game registration.
 */
//TODO: For arena creation add different types (world and default)
public class Game {

    private GameBoxx gb;
    private UUID uid;
    private String name;
    private JavaPlugin plugin;
    private Class<? extends Arena> arenaClass;

    private File arenaDir;

    private Map<UUID, Arena> arenas = new HashMap<UUID, Arena>();
    private Map<UUID, YamlConfiguration> configs = new HashMap<UUID, YamlConfiguration>();

    /**
     * Creates a new game instance.
     * This should not be used and you should use the {@link GameManager#register(JavaPlugin, String, Class)} method in all cases.
     * @param uid The unique ID for the game which is generated by the {@link GameManager}.
     * @param plugin The plugin instance the game belongs to.
     * @param name The name of the game type like for example Spleef.
     * @param arenaClass The arena class for this game which extends from {@link Arena}.
     */
    public Game(UUID uid, JavaPlugin plugin, String name, Class<? extends Arena> arenaClass) {
        gb = GameBoxx.get();
        this.uid = uid;
        this.name = name;
        this.plugin = plugin;
        this.arenaClass = arenaClass;

        arenaDir = new File(plugin.getDataFolder(), "Arenas");
        arenaDir.mkdirs();

        loadArenas();
    }

    /**
     * Load all the arenas from their config files.
     * It loads the config files from the owning plugins data folder and then the arenas folder. For example /plugins/Spleef/Arenas
     * The arena will only load when the config has a valid UID and the type matches with this game type.
     * Technically it would be alright to save arenas in the same folder but it's more efficient to give each game it's own folder.
     */
    private void loadArenas() {
        Map<String, File> configFiles = Utils.getFiles(arenaDir, "yml");
        int count = 0;
        for (Map.Entry<String, File> entry : configFiles.entrySet()) {
            YamlConfiguration arenaCfg = YamlConfiguration.loadConfiguration(entry.getValue());
            UUID uid = Parse.UUID(arenaCfg.getString("uid"));
            if (uid == null) {
                gb.warn("Failed to load the arena data for arena: " + entry.getKey() + "!");
                continue;
            }
            String type = arenaCfg.getString("type");
            if (type == null || !type.equalsIgnoreCase(name)) {
                gb.warn("Skipping arena " + entry.getKey() + " because it's not a " + name + " arena!");
                continue;
            }
            count++;
            configs.put(uid, arenaCfg);
            loadArena(arenaCfg.getDefaultSection());
        }
        gb.log("Loaded in " + count + " arenas for " + name + "!");
    }

    /**
     * Load all the arena data from the configuration and create the {@link Arena} instance.
     * @param data The configuration data from the arena config file.
     * @return The created {@link Arena} instance or null if it failed to create the instance.
     */
    private void loadArena(ConfigurationSection data) {
        try {
            Arena arena = arenaClass.getConstructor(ConfigurationSection.class).newInstance(data);
            //TODO: Throw custom exceptions for arena validation.
            initializeArena(arena);
            arenas.put(arena.getUid(), arena);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create/Register a new {@link Arena}.
     * The arena won't have a name asigned but it could be set later.
     * A random unique ID will will be generated and asigned for the arena so the name is not used for referencing.
     * @return The created {@link Arena} instance.
     * @throws InstantiationException If the arena class is abstract.
     * @throws NoSuchMethodException If the arena constructor doesn't have the correct constructor with UUID, String, String.
     * @throws InvocationTargetException If the arena constructor throws an exception.
     * @throws IllegalAccessException  If the arena constructor can't be accessed like if it's private.
     */
    public Arena createArena() throws InstantiationException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return createArena("");
    }

    /**
     * Create/Register a new {@link Arena} with the specified name.
     * A random unique ID will will be generated and asigned for the arena so the name is not used for referencing.
     * @return The created {@link Arena} instance.
     * @throws InstantiationException If the arena class is abstract.
     * @throws NoSuchMethodException If the arena constructor doesn't have the correct constructor with UUID, String, String.
     * @throws InvocationTargetException If the arena constructor throws an exception.
     * @throws IllegalAccessException  If the arena constructor can't be accessed like if it's private.
     */
    public Arena createArena(String arenaName) throws InstantiationException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        UUID arenaUID = UUID.randomUUID();
        while (arenas.containsKey(arenaUID)) {
            arenaUID = UUID.randomUUID();
        }
        Arena arena = arenaClass.getConstructor(UUID.class, String.class, String.class).newInstance(uid, name, arenaName);
        //TODO: Throw custom exceptions for arena validation.
        initializeArena(arena);
        arenas.put(arenaUID, arena);
        return arena;
    }

    /**
     * Initialize an arena by adding the components and validating them.
     * @param arena The arena to initialize.
     */
    private void initializeArena(Arena arena) {
        arena.addComponents();
        //TODO: Validate arena and such.
    }

    /**
     * Check if an {@link Arena} with the specified unique ID exists or not.
     * @param uid The unique ID to check for.
     * @return True when the arena exists.
     */
    public boolean hasArena(UUID uid) {
        return arenas.containsKey(uid);
    }

    /**
     * Get an {@link Arena} instance by it's unique ID.
     * @param uid The unique ID to check for.
     * @return The {@link Arena} instance or {@code null} if no arena with the specified id exists.
     */
    public Arena getArena(UUID uid) {
        return arenas.get(uid);
    }

    /**
     * Get the unique ID for this game.
     * @return Unique ID as {@link UUID}
     */
    public UUID getUid() {
        return uid;
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
