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

import info.gameboxx.gameboxx.components.internal.GameComponent;
import info.gameboxx.gameboxx.exceptions.InvalidSetupDataException;
import info.gameboxx.gameboxx.exceptions.SessionLimitException;
import info.gameboxx.gameboxx.setup.OptionData;
import info.gameboxx.gameboxx.setup.SetupOption;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Base Arena class.
 * Each {@link Game} can have multiple arenas.
 * Each Arena can have one or multiple {@link GameSession}s depending on the {@link ArenaType}.
 */
public class Arena {

    private Game game;
    private ArenaType type;
    private String name;

    private Map<String, SetupOption> setupOptions = new HashMap<String, SetupOption>();

    private Map<UUID, GameSession> sessions = new HashMap<UUID, GameSession>();
    private int maxSessions;

    private File configFile;
    private YamlConfiguration config;

    private Long lastSave = System.currentTimeMillis();

    /**
     * Use the {@link Game#createArena(ArenaType, String)} method to create a new arena.
     * @param game The {@link Game} that has this arena.
     * @param configFile The {@link File} for the arena config.
     * @param type The {@link ArenaType} for the arena.
     * @param name The name of the arena.
     */
    public Arena(Game game, File configFile, YamlConfiguration config, ArenaType type, String name) {
        this.game = game;
        this.configFile = configFile;
        this.config = config;
        this.type = type;
        this.name = name;
    }

    /**
     * Arenas from config will be instantiated using this constructor.
     * @param game The {@link Game} that has this arena.
     * @param configFile The {@link File} for the arena config.
     * @param config The {@link YamlConfiguration} with all the arena data.
     */
    public Arena(Game game, File configFile, YamlConfiguration config) {
        this.game = game;
        this.configFile = configFile;
        this.config = config;
        name = config.getString("name");
        type = ArenaType.valueOf(config.getString("type"));
        maxSessions = config.getInt("maxSessions");
    }

    /**
     * Save all arena data including all the options from components.
     * This save method will only save when the saving isn't on a delay (by default 5 seconds).
     * Use {@link #forceSave()} if you need to make sure that everything is saved.
     * It will automatically force save when the plugin gets disabled.
     */
    public void save() {
        if (lastSave + game.getAPI().getCfg().saveDelay__arena > System.currentTimeMillis()) {
            return;
        }
        lastSave = System.currentTimeMillis();
        forceSave();
    }

    /**
     * Save all arena data including the options from components.
     * There shouldn't be a need to call this method and in most cases you should be using the regular {@link #save()} method.
     */
    //TODO: Call this method when the plugin disables.
    public void forceSave() {
        config.set("name", name);
        config.set("type", type.toString());
        config.set("maxSessions", maxSessions);

        for (SetupOption option : setupOptions.values()) {
            if (option == null) {
                continue;
            }
            config.set(option.getName(), option.getValue());
        }

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load all the setup options from config.
     * Any setup options that are missing or that have corrupt data will be put as null in the setupOptions map.
     */
    public void loadOptions() {
        for (OptionData option : game.getSetupOptions().values()) {
            if (config.contains(option.getName())) {
                //Load option from config.
                try {
                    setupOptions.put(option.getName().trim().toLowerCase(), option.getType().newOption(option.getName(), option.getDescription(), config));
                } catch (InvalidSetupDataException e) {
                    game.getPlugin().getLogger().warning("Can't load arena '" + getName() + "' because the data is corrupt. Please run setup again to fix corrupt setup data.");
                    game.getPlugin().getLogger().warning(e.getMessage());
                    setupOptions.put(option.getName().trim().toLowerCase(), null);
                }
            } else {
                //Load option from defaults registered by components.
                if (option.getDefaultValue() == null) {
                    setupOptions.put(option.getName().trim().toLowerCase(), null);
                } else {
                    try {
                        setupOptions.put(option.getName().trim().toLowerCase(), option.getType().newOption(option.getName(), option.getDescription(), option.getDefaultValue()));
                    } catch (InvalidSetupDataException e) {
                        game.getPlugin().getLogger().warning("Can't load arena '" + getName() + "' because the default value is invalid. Please run setup to fix any issues.");
                        game.getPlugin().getLogger().warning(e.getMessage());
                        setupOptions.put(option.getName().trim().toLowerCase(), null);
                    }
                }
            }
        }
    }

    /**
     * Check if all the setup options have been set up correctly.
     * @return True when all setup options have been set up.
     */
    public boolean isSetupCorrectly() {
        for (SetupOption option : setupOptions.values()) {
            if (option == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the total amount of active sessions this arena has.
     * @return The amount of active sessions.
     */
    public int getSessionCount() {
        return sessions.size();
    }

    /**
     * Get a map with all the active game sessions for this arena.
     * @return The map with active game sessions.
     */
    public Map<UUID, GameSession> getSessions() {
        return sessions;
    }

    /**
     * Create a new {@link GameSession} for this arena.
     * An unique ID will be created and assigned to the session to reference it further on.
     * It will also copy all the components and child components with all settings from the {@link Game}.
     * @return The created new GameSession.
     * @throws SessionLimitException When the session limit has been reached.
     */
    public GameSession createSession() throws SessionLimitException {
        if (sessions.size() >= getMaxSessions()) {
            throw new SessionLimitException("Failed to create a new session for the arena " + getName() +
                    "The maximum amount of sessions has been reached. [" + getMaxSessions() + "]");
        }

        //TODO: Don't use UUID's for sessions but use indexes so they can be referenced easier by players etc.
        UUID sessionUID = UUID.randomUUID();
        while (sessions.containsKey(sessionUID)) {
            sessionUID = UUID.randomUUID();
        }

        //Create the new session.
        GameSession newSession = game.getNewGameSession(sessionUID);

        //Add new instances of all the components from the game.
        for (GameComponent component : game.getComponents().values()) {
            newSession.addComponent(component.newInstance(newSession));
        }

        //Load all the dependencies for each component.
        for (GameComponent component : newSession.getComponents().values()) {
            component.loadDependencies();
        }

        return newSession;
    }

    /**
     * Check whether or not the arena has a {@link GameSession} with the given unique ID.
     * @param uid The unique ID to check for.
     * @return True when the arena has a session with the given unique ID.
     */
    public boolean hasSession(UUID uid) {
        return sessions.containsKey(uid);
    }

    /**
     * Remove a session by it's unique ID.
     * @param uid
     */
    public void removeSession(UUID uid) {
        sessions.remove(uid);
    }

    /**
     * Get the game that this arena belongs to.
     * @return The game this arena belongs to.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Get the {@link ArenaType} of the arena.
     * @return The arena type
     */
    public ArenaType getType() {
        return type;
    }

    //TODO: Maybe add support for changing the arena type?

    /**
     * Get the name of the arena.
     * @return The name of the arena.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the arena.
     * @param name The new name for the arena.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the maximum allowed sessions for this arena.
     * It will force to return 1 if the {@link ArenaType} is a {@link ArenaType#DEFAULT} type or if the max limit is less than one.
     * @return The maximum amount of sessions this arena can have.
     */
    public int getMaxSessions() {
        if (maxSessions < 1 || type == ArenaType.DEFAULT) {
            return 1;
        }
        return maxSessions;
    }
}
