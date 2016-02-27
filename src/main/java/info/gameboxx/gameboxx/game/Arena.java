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
import info.gameboxx.gameboxx.config.internal.OptionCfg;
import info.gameboxx.gameboxx.exceptions.MissingArenaWorldException;
import info.gameboxx.gameboxx.exceptions.SessionLimitException;
import info.gameboxx.gameboxx.nms.NMS;
import info.gameboxx.gameboxx.options.ListOption;
import info.gameboxx.gameboxx.options.Option;
import info.gameboxx.gameboxx.options.SingleOption;
import org.apache.commons.io.FileUtils;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Base Arena class.
 * Each {@link Game} can have multiple arenas.
 * Each Arena can have one or multiple {@link GameSession}s depending on the {@link ArenaType}.
 */
public class Arena {

    private Game game;
    private ArenaType type;
    private String name;

    private Map<Integer, GameSession> sessions = new HashMap<>();

    private OptionCfg config;

    private Long lastSave = System.currentTimeMillis();

    /**
     * Use the {@link Game#createArena(ArenaType, String)} method to create a new arena.
     *
     * @param game The {@link Game} that has this arena.
     * @param configFile The {@link File} for the arena config.
     * @param type The {@link ArenaType} for the arena.
     * @param name The name of the arena.
     */
    public Arena(Game game, File configFile, ArenaType type, String name) {
        this.game = game;
        this.type = type;
        this.name = name;

        config = new OptionCfg(configFile);

        loadOptions();
        config.load(false);
        config.getConfig().set("general.name", name);
        config.getConfig().set("general.type", type.toString());
        config.getConfig().set("general.world-settings", "");
        config.save(true);
    }

    /**
     * Arenas from config will be instantiated using this constructor.
     *
     * @param game The {@link Game} that has this arena.
     * @param configFile The {@link File} for the arena config.
     */
    public Arena(Game game, File configFile) {
        this.game = game;

        config = new OptionCfg(configFile);
        loadOptions();
        config.load();

        name = config.getConfig().getString("general.name");
        type = ArenaType.valueOf(config.getConfig().getString("general.type"));
    }

    /**
     * Load all the arena options from the game.
     * Creates a copy of all the options.
     * The arena options in the game are used as templates only.
     */
    public void loadOptions() {
        for (Map.Entry<String, Option> entry : game.getArenaOptions().entrySet()) {
            config.setOption(entry.getKey(), entry.getValue().clone());
        }
    }

    /**
     * Check if all the setup options have been set up correctly.
     *
     * @return True when all setup options have been set up.
     */
    public boolean isSetupCorrectly() {
        for (Option option : config.getOptions()) {
            if (option == null) {
                return false;
            }
            if (option instanceof SingleOption) {
                if (!((SingleOption)option).hasValue()) {
                    return false;
                }
            }
            if (option instanceof ListOption) {
                if (((ListOption)option).getValues().isEmpty()) {
                    return false;
                }
            }
            //TODO: Map options
        }
        return true;
    }

    /**
     * Get the total amount of sessions this arena has.
     *
     * @return The amount of active sessions.
     */
    public int getSessionCount() {
        return sessions.size();
    }

    /**
     * Get the map with all the game sessions.
     *
     * @return map with sessions where the key is the session ID and the value is the session instance.
     */
    public Map<Integer, GameSession> getSessions() {
        return sessions;
    }

    /**
     * Create a new {@link GameSession} for this arena.
     * An unique ID will be created and assigned to the session to reference it further on.
     * It will also copy all the components and child components with all settings from the {@link Game}.
     *
     * @return The created new GameSession.
     * @throws SessionLimitException When the session limit has been reached.
     * @throws MissingArenaWorldException When the arena type is {@link ArenaType#WORLD} and there is no world for the arena.
     * @throws IOException When the arena type is {@link ArenaType#WORLD} and it failed to create a copy of the template world.
     */
    public GameSession createSession() throws SessionLimitException, MissingArenaWorldException, IOException {
        //Get and validate a new session id.
        int id = -1;
        //TODO: Implement this properly with game/arena max session limit and support infinite sessions.
        for (int i = 0; i < 16; i++) {
            if (!hasSession(i)) {
                id = i;
                break;
            }
        }
        if (id < 0) {
            throw new SessionLimitException("Failed to create a new session for the arena " + getName() +
                    "The maximum amount of sessions has been reached. [" + 16 + "]");
        }

        //Create the new session.
        final GameSession newSession = game.getNewGameSession(this, id);
        sessions.put(id, newSession);

        //Add new instances of all the components from the game.
        for (GameComponent component : game.getComponents().values()) {
            newSession.addComponent(component.newInstance(newSession));
        }

        //Load all the dependencies for each component.
        for (GameComponent component : newSession.getComponents().values()) {
            component.loadDependencies();
        }

        //TODO: Clean this up (This code doesn't really belong here) (Need general API for arena types)
        if (getType() == ArenaType.DEFAULT) {
            newSession.setReady(true);
        } else if (getType() == ArenaType.WORLD) {
            File mapDir = new File(game.getAPI().getDataFolder().getAbsolutePath() + File.separator + "maps" + File.separator + game.getName() + File.separator + getName());
            if (!mapDir.exists()) {
                throw new MissingArenaWorldException(game, this);
            }
            String mapName = getName() + "_" + id;
            FileUtils.copyDirectory(mapDir, new File(getGame().getAPI().getServer().getWorldContainer(), mapName));

            final WorldCreator wc = new WorldCreator(mapName);
            new BukkitRunnable() {
                @Override
                public void run() {
                    newSession.setWorld(NMS.get().getWorldLoader().createAsyncWorld(wc));
                    newSession.setReady(true);
                }
            }.runTaskAsynchronously(getGame().getAPI());
        } else if (getType() == ArenaType.GENERATE_WORLD) {
            String mapName = getName() + "_" + id;
            final WorldCreator wc = new WorldCreator(mapName);
            new BukkitRunnable() {
                @Override
                public void run() {
                    //TODO: Check for WorldBorderCP and load all the chunks to the border.
                    newSession.setWorld(NMS.get().getWorldLoader().createAsyncWorld(wc));
                    newSession.setReady(true);
                }
            }.runTaskAsynchronously(getGame().getAPI());
        }

        return newSession;
    }


    /**
     * Get the {@link GameSession} for the given ID.
     *
     * @param id The index/ID of the session to get.
     * @return The {@link GameSession} for the specified ID or {@code null} if there is no session for the given ID.
     */
    public GameSession getSession(int id) {
        return sessions.get(id);
    }

    /**
     * Check whether or not the arena has a {@link GameSession} with the given ID.
     *
     * @param id The session ID to check for.
     * @return True when the arena has a session with the given ID.
     */
    public boolean hasSession(int id) {
        return sessions.containsKey(id);
    }

    /**
     * Remove a {@link GameSession} by it's ID.
     *
     * @param id The session ID to remove.
     */
    public void removeSession(int id) {
        //TODO: More cleanup with the session itself.
        sessions.remove(id);
    }

    /**
     * Get the arena options config.
     *
     * @return {@link OptionCfg} with all the arena options.
     */
    public OptionCfg getConfig() {
        return config;
    }

    /**
     * Get the game that this arena belongs to.
     *
     * @return The game this arena belongs to.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Get the {@link ArenaType} of the arena.
     *
     * @return The arena type
     */
    public ArenaType getType() {
        return type;
    }

    /**
     * Get the name of the arena.
     *
     * @return The name of the arena.
     */
    public String getName() {
        return name;
    }

    //TODO: Add support for changing arena name.


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Arena other = (Arena)obj;
        if (!other.getName().equals(this.getName())) {
            return false;
        }
        if (!other.getType().equals(this.getType())) {
            return false;
        }
        if (!other.getGame().equals(this.getGame())) {
            return false;
        }
        return true;
    }
}
