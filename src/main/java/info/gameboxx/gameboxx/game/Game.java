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
import info.gameboxx.gameboxx.components.internal.ComponentHolder;
import info.gameboxx.gameboxx.components.internal.GameComponent;
import info.gameboxx.gameboxx.config.internal.OptionCfg;
import info.gameboxx.gameboxx.exceptions.*;
import info.gameboxx.gameboxx.options.Option;
import info.gameboxx.gameboxx.options.single.IntOption;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Represents a game instance.
 * You extend this game class in your plugin to design your own game like SpleefGame.
 * <b>Make sure you register your game class using the {@link GameManager#register(Game)} method!</b>
 * This class is a {@link ComponentHolder} so you can add any of the {@link info.gameboxx.gameboxx.components}.
 */
public abstract class Game extends ComponentHolder {

    protected GameBoxx gb;
    protected String name;
    protected JavaPlugin plugin;

    private File gameFolder;

    private OptionCfg config;
    private Map<String, Option> arenaOptions = new HashMap<>();

    private File arenaFolder;
    private Map<String, Arena> arenas = new HashMap<>();

    /**
     * <b>Make sure you register your game class using the {@link GameManager#register(Game)} method!</b>
     * @param plugin The plugin instance the game belongs to.
     * @param name The name of the game like for example Spleef.
     */
    public Game(JavaPlugin plugin, String name) {
        gb = GameBoxx.get();

        this.name = name;
        this.plugin = plugin;

        gameFolder = getGameFolder();
        gameFolder.mkdirs();

        arenaFolder = new File(gameFolder, "arenas");
        arenaFolder.mkdirs();

        config = new OptionCfg(new File(gameFolder, name + "Options.yml"));
    }


    //region Sessions
    /**
     * Override this method and return your own version of the {@link GameSession}
     * Each time a new session will be created it will call this method to create the new session.
     * Create a class like SpleefSession and extend from GameSession and then do {@code return new SpleefSession(this, sessionUID);}
     * If you don't have to override anything for sessions which would be unlikely you could just use {@code return new GameSession(this, sessionUID);}
     * @param arena The arena which the session will belong to.
     * @param sessionID The unique session ID used to reference the session.
     * @return GameSession
     */
    public abstract GameSession getNewGameSession(Arena arena, int sessionID);
    //endregion


    //region Components

    /**
     * Override this method and inside the method body you will add all your components.
     * @see ComponentHolder#addComponent(GameComponent)
     */
    public abstract void addComponents();

    /**
     * Used by the {@link GameManager} to validate all the added components.
     * It will check for dependencies and conflicts and such.
     * If there is any validation error the game won't be registered and an exception will be thrown by the {@link GameManager#register(Game)} method.
     */
    public void validate() throws DependencyNotFoundException, ComponentConflictException {
        for (GameComponent component : getComponents().values()) {
            component.validate();
        }
    }
    //endregion


    //region Arenas

    /**
     * Load all the arenas from their config files.
     * The arena will only load when the config has a valid name and there is no arena loaded with that name.
     */
    public void loadArenas() {
        Map<String, File> configFiles = Utils.getFiles(arenaFolder, "yml");
        int count = 0;
        for (Map.Entry<String, File> entry : configFiles.entrySet()) {
            Arena arena = new Arena(this, entry.getValue());
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

        //Create a session for each arena.
        //TODO: Move this
        for (Arena arena : arenas.values()) {
            try {
                arena.createSession();
            } catch (SessionLimitException e) {
                getPlugin().getLogger().warning(e.getMessage());
            } catch (MissingArenaWorldException e) {
                getPlugin().getLogger().warning(e.getMessage());
            } catch (IOException e) {
                getPlugin().getLogger().warning(e.getMessage());
            }
        }
    }

    /**
     * Create/Register a new {@link Arena} with the specified name.
     * This is used in the {@link info.gameboxx.gameboxx.commands.ArenaCmd} so it shouldn't be needed to manually create arenas.
     * @return The created {@link Arena} instance.
     * @throws ArenaAlreadyExistsException If an arena with the specified name already exists.
     * @throws IOException When failing to create a new config file for the arena.
     */
    public Arena createArena(ArenaType type, String arenaName) throws ArenaAlreadyExistsException, IOException {
        if (arenas.containsKey(arenaName.trim().toLowerCase())) {
            throw new ArenaAlreadyExistsException("An arena with the name " + arenaName + " already exists!");
        }
        File configFile = new File(arenaFolder, arenaName + ".yml");
        if (configFile.exists()) {
            throw new ArenaAlreadyExistsException("An arena with the name " + arenaName + " already exists!");
        }

        Arena arena = new Arena(this, configFile, type, arenaName);
        arenas.put(arenaName.trim().toLowerCase(), arena);
        return arena;
    }

    /**
     * Check if an {@link Arena} with the specified name exists or not.
     * @param arenaName The arena name to check for.
     * @return True when the arena exists.
     */
    public boolean hasArena(String arenaName) {
        return arenas.containsKey(arenaName.trim().toLowerCase());
    }

    /**
     * Get an {@link Arena} instance by it's unique ID.
     * @param arenaName The arena name to check for.
     * @return The {@link Arena} instance or {@code null} if no arena with the specified id exists.
     */
    public Arena getArena(String arenaName) {
        return arenas.get(arenaName.trim().toLowerCase());
    }

    /**
     * Get all the registered arenas for this game.
     * @return Map with arenas where the key is the lowercase arena name and the value is the arena instance.
     */
    public Map<String, Arena> getArenas() {
        return arenas;
    }

    /**
     * Get a list with all the registered arena names.
     * @return List of arena names.
     */
    public List<String> getArenaNames() {
        return new ArrayList<String>(arenas.keySet());
    }
    //endregion


    //region Options

    /**
     * Get the game config file.
     * Used for component options and for your own options.
     * @see #registerOptions()
     * @return {@link OptionCfg} with game settings.
     */
    public OptionCfg getConfig() {
        return config;
    }

    /**
     * Register an arena option.
     * Make sure to call this from the {@link #registerOptions()} method.
     * These options will be passed on to all arenas.
     *
     * <b>The path may not start with 'components' or 'general' as that path is reserved for component/general options!</b>
     * The path/name should be all lower cased and words should be separated with a dash 'my-awesome-option'.
     * You can use the dot '.' to create different sections like 'items.example-option'
     *
     * @param path The path/name for the option. (lower cased with a dash '-' as word separator)
     * @param option The option instance.
     */
    public void registerArenaOption(String path, Option option) {
        arenaOptions.put(path, option);
    }

    /**
     * Register a game option.
     * Make sure to call this from the {@link #registerOptions()} method.
     *
     * <b>The path may not start with 'components' or 'general' as that path is reserved for component/general options!</b>
     * The path/name should be all lower cased and words should be separated with a dash 'my-awesome-option'.
     * You can use the dot '.' to create different sections like 'items.example-option'
     *
     * @param path The path/name for the option. (lower cased with a dash '-' as word separator)
     * @param option The option instance.
     */
    public void registerGameOption(String path, Option option) {
        getConfig().setOption(path, option);
    }

    /**
     * Used for games to register custom game/arena options for the game.
     * These are component independent options for custom gameplay elements.
     *
     * <b>Do not register options with a path starting with 'components' or 'general'!</b>
     * This path is reserved for component options and general game options as they get stored in the same files
     * The name/path should be all lower cased and words should be separated with a dash 'my-awesome-option'.
     *
     * Use {@link #registerGameOption(String, Option)} to register a game option.
     * Use {@link #registerArenaOption(String, Option)} to register an arena option.
     */
    public abstract void registerOptions();

    /**
     * Register all the general game/arena options and all the component game/arena options.
     * It will only register component options from components that have been registered for this game.
     * This method is called when registering a game using {@link GameManager#register(Game)}.
     * Do not manually call this.
     */
    public void registerCoreOptions() {
        //General game options
        registerGameOption("general.max-sessions", new IntOption("MaxSessions", -1).min(-1).setDescription("The maximum amount of sessions that can be created for the game."));

        //General arena options
        registerArenaOption("general.max-sessions", new IntOption("MaxSessions", -1).min(-1).setDescription("The maximum amount of sessions that can be created for the arena"));

        //Component options
        for (GameComponent component : getComponents().values()) {
            component.registerOptions();
        }
    }

    /**
     * Get a map with all the arena options.
     * These options are used as a template only as each {@link Arena} stores a copy of these options.
     * @return Map with arena options where the key is the path and the value is the option instance (template).
     */
    public Map<String, Option> getArenaOptions() {
        return arenaOptions;
    }
    //endregion


    //region Game

    /**
     * Get the game folder where all the game configuration and data will be stored.
     * By default this will return the plugin datafolder.
     * Override this method and specify a different folder if you have multiple games in your plugin to prevent conflicts.
     * So if you have like a spleef and race game in one plugin in your SpleefGame class you would override this method and return:
     * {@code return new File(plugin.getDataFolder(), "spleef");}
     * There is no need to call mkdirs();
     * @return Game folder where configuration files and game data will be stored like arenas etc.
     */
    public File getGameFolder() {
        return plugin.getDataFolder();
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

    /**
     * Get the {@link GameBoxx} API.
     * @return The {@link GameBoxx} plugin instance.
     */
    public GameBoxx getAPI() {
        return gb;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Game other = (Game)obj;
        if (!other.getName().equals(this.getName())) {
            return false;
        }
        if (!other.getPlugin().getName().equals(this.getPlugin().getName())) {
            return false;
        }
        return true;
    }
    //endregion
}
