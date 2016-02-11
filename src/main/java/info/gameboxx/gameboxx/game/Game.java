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
import info.gameboxx.gameboxx.exceptions.*;
import info.gameboxx.gameboxx.setup.OptionData;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private File configFile;
    private YamlConfiguration config;
    private Map<String, Object> defaultSettings = new HashMap<>();
    private Map<String, OptionData> setupOptions = new HashMap<>();

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

        configFile = new File(gameFolder, name + "Settings.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        config.options().copyDefaults(true);
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
     * It loads the config files from the owning plugins data folder then the Arenas folder and then the game name. For example /plugins/Spleef/Arenas/Spleef
     * If there are multiple games per plugin it shouldn't have any conflicts with data files.
     * The arena will only load when the config has a valid name and there is no arena loaded with that name.
     */
    public void loadArenas() {
        Map<String, File> configFiles = Utils.getFiles(arenaFolder, "yml");
        int count = 0;
        for (Map.Entry<String, File> entry : configFiles.entrySet()) {
            YamlConfiguration arenaCfg = YamlConfiguration.loadConfiguration(entry.getValue());
            Arena arena = new Arena(this, entry.getValue(), arenaCfg);
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
            arena.loadOptions();
            arenas.put(name, arena);
            count++;
        }

        //Create a session for each arena.
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
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        Arena arena = new Arena(this, configFile, config, type, arenaName);
        arena.loadOptions();
        arena.forceSave();
        arenas.put(arenaName, arena);
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


    //region Setup options

    /**
     * Register a new setup option.
     * This is used for components to register options that have to be set per arena.
     * Arenas will fail to create sessions if not all the options have been set up correctly.

     * @throws OptionAlreadyExistsException When an option with the specified name is already registered.
     */
    public void registerSetupOption(OptionData option) throws OptionAlreadyExistsException {
        String name = option.getName().trim().toLowerCase();
        if (setupOptions.containsKey(option.toString())) {
            throw new OptionAlreadyExistsException(name);
        }
        setupOptions.put(name, option);
    }

    /**
     * Go through all the components and register setup options.
     * This method is called when registering a game using {@link GameManager#register(Game)}
     * @throws OptionAlreadyExistsException When a component tries to register an option with a name that is already used by another component.
     */
    public void registerSetupOptions() throws OptionAlreadyExistsException {
        for (GameComponent component : getComponents().values()) {
            component.registerOptions();
        }
    }

    /**
     * Get a map with all the setup options.
     * @return Map with setup options where the key is the name and the value is the {@link OptionData}.
     */
    public Map<String, OptionData> getSetupOptions() {
        return setupOptions;
    }
    //endregion


    //region Configuration

    /**
     * Loads all the default config settings.
     * Game specific settings that have been added with {@link #addSetting(String, Object)}
     * And component settings that have been added with {@link GameComponent#addSetting(String, Object)}
     */
    public void loadSettings() {
        try {
            if (configFile.exists()) {
                config.load(configFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        for (Map.Entry<String, Object> entry : defaultSettings.entrySet()) {
            config.addDefault("settings." + entry.getKey(), entry.getValue());
        }
        for (GameComponent component : getComponents().values()) {
            for (Map.Entry<String, Object> entry : component.getDefaultSettings().entrySet()) {
                config.addDefault("components." + component.getConfigKey() + "." + entry.getKey(), entry.getValue());
            }
            component.setConfig(config.getConfigurationSection("components." + component.getConfigKey()));
        }
        saveConfig();
    }

    /**
     * Register a setting for your game.
     * This should be used for game specific config settings.
     * You can use {@link #getSettings()} to get the {@link ConfigurationSection} with setting values.
     * @param key The key name for the config setting.
     *            As with regular {@link ConfigurationSection}s you can use a dot (.) to create different sections.
     * @param defaultValue The default value to put in the configuration file which the user can edit.
     */
    public void addSetting(String key, Object defaultValue) {
        defaultSettings.put(key, defaultValue);
    }

    /**
     * Get the {@link ConfigurationSection} with all the game specific settings.
     * This contains all the settings you've specified with {@link #addSetting(String, Object)}.
     * <b>This may contain no values when using it in your constructor because settings get loaded after the game is fully registered and validated.</b>
     * @return The {@link ConfigurationSection} with game specific settings.
     */
    public ConfigurationSection getSettings() {
        return config.getConfigurationSection("settings");
    }

    /**
     * Get the configuration file for this game.
     * It contains all the settings for components and game specific settings.
     * Use {@link #getSettings()} to get the settings section for your game instead of using getSettings directly.
     * @return The {@link YamlConfiguration} with all the game settings.
     */
    public YamlConfiguration getConfig() {
        return config;
    }

    /**
     * Get the configuration file with component settings and game specific settings.
     * @return The {@link File} for the configuration settings file.
     */
    public File getConfigFile() {
        return configFile;
    }

    /**
     * Save all the game settings.
     * This includes component settings and game specific settings.
     * The only reason you would want to call this if you manually force set a setting.
     */
    public void saveConfig() {
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
