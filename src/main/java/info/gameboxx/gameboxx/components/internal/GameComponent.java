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

package info.gameboxx.gameboxx.components.internal;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.config.internal.OptionCfg;
import info.gameboxx.gameboxx.exceptions.ComponentConflictException;
import info.gameboxx.gameboxx.exceptions.DependencyNotFoundException;
import info.gameboxx.gameboxx.exceptions.OptionAlreadyExistsException;
import info.gameboxx.gameboxx.game.*;
import info.gameboxx.gameboxx.options.Option;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * The base class for a game component.
 * See {@link info.gameboxx.gameboxx.components} for all the components.
 * The components need to be added to the {@link Game} to design the game.
 * When a new {@link GameSession} gets created it will copy all the components from the {@link Game} in to the session.
 */
public abstract class GameComponent {

    private Game game;
    private GameSession session;

    private String configKey;
    private String name;

    private Set<Class<? extends GameComponent>> depends = new HashSet<>();
    private Set<Class<? extends GameComponent>> softDepends = new HashSet<>();
    private Set<Class<? extends GameComponent>> conflicts = new HashSet<>();

    private Map<Class<? extends GameComponent>, GameComponent> dependencies = new HashMap<>();

    /**
     * Instantiate a new game component for the specified game.
     * It will <b>not</b> automatically add the component to the game.
     * Use {@link Game#addComponent(GameComponent)} to add a component to the game.
     *
     * @param game The game this component belongs to.
     * @throws OptionAlreadyExistsException When the component tries to register a setup option and a setup option with the specified name is already registered.
     */
    public GameComponent(Game game) {
        this.game = game;

        name = getClass().getSimpleName();
        name = name.substring(0, name.length() - 2);
        name = Utils.splitCamelCase(name, " ");

        configKey = name.replace(" ", "-").toLowerCase();
    }


    //region Options

    /**
     * Register an arena option.
     * Make sure to call this from the {@link #registerOptions()} method.
     * These options will be passed on to all arenas.
     * <p/>
     * The path/name should be all lower cased and words should be separated with a dash 'my-awesome-option'.
     * You can use the dot '.' to create different sections like 'items.example-option'
     *
     * @param path The path/name for the option. (lower cased with a dash '-' as word separator)
     * @param option The option instance.
     */
    public void registerArenaOption(String path, Option option) {
        if (!path.startsWith("components")) {
            path = path(path);
        }
        game.registerArenaOption(path, option);
    }

    /**
     * Register a game option.
     * Make sure to call this from the {@link #registerOptions()} method.
     * <p/>
     * The path/name should be all lower cased and words should be separated with a dash 'my-awesome-option'.
     * You can use the dot '.' to create different sections like 'items.example-option'
     *
     * @param path The path/name for the option. (lower cased with a dash '-' as word separator)
     * @param option The option instance.
     */
    public void registerGameOption(String path, Option option) {
        if (!path.startsWith("components")) {
            path = path(path);
        }
        game.registerGameOption(path, option);
    }

    /**
     * Called after all components have been added to the game to register game/arena options.
     * <p/>
     * Call {@link #registerGameOption(String, Option)} to register a game option.
     * Call {@link #registerArenaOption(String, Option)} to register an arena option.
     * <p/>
     * the option name should be all lower cased with a dash '-' as word separator.
     */
    public abstract void registerOptions();

    /**
     * Get the full path for the specified option name.
     * Component options are saved like: 'components.{component-name}.{option-name}'
     * When registering options you don't have to use this but working with the config you have to use this.
     *
     * @param path The path/name of the option.
     * @return The full path for the specified option like 'components.{component-name}.{option-name}'
     */
    public String path(String path) {
        return "components." + configKey + "." + path;
    }

    /**
     * Get the game options config.
     *
     * @return {@link OptionCfg} with all the game options.
     * @see Game#getConfig()
     */
    public OptionCfg gameOptions() {
        return game.getConfig();
    }

    /**
     * Get the arena options config.
     *
     * @return {@link OptionCfg} with all the arena options.
     * @see Arena#getConfig()
     */
    public OptionCfg arenaOptions() {
        return getArena().getConfig();
    }


    //endregion


    //region Dependencies

    /**
     * Adds a hard dependency.
     * When validating all components it won't validate if the dependency is missing.
     *
     * @param component The component to depend on.
     */
    protected void addDependency(Class<? extends GameComponent> component) {
        depends.add(component);
    }

    /**
     * Adds a soft dependency.
     * Adding soft dependencies makes it so you can use {@link #getDependency(Class)}.
     * Be warned though that the getDependency method can return {@code null} for soft dependencies.
     *
     * @param component The component to depend on.
     */
    protected void addSoftDependency(Class<? extends GameComponent> component) {
        softDepends.add(component);
    }

    /**
     * Adds a conflict with another component.
     * When validating all components it won't validate if there is a conflict.
     *
     * @param component The component it will conflict with.
     */
    protected void addConflict(Class<? extends GameComponent> component) {
        conflicts.add(component);
    }

    /**
     * Get another dependency component.
     * It can also be a soft dependency but in that case this will return {@code null} if the {@link Game}/{@link GameSession} doesn't have the component.
     * Regular dependencies will never return {@code null}!
     *
     * @param component The type of component/dependency to get.
     * @param <T> The type of component/dependency to get.
     * @return The component instance of the type specified or {@code null}.
     */
    public <T extends GameComponent> T getDependency(Class<T> component) {
        return Utils.<T>convertInstance(dependencies.get(component), component);
    }

    /**
     * Validate the component to check for dependencies and conflicts.
     * Soft dependencies won't be validated.
     * There is no need to manually call this!
     * It gets called when you register the game using {@link GameManager#register(Game)}
     *
     * @throws DependencyNotFoundException When a dependency hasn't been added to the game.
     * @throws ComponentConflictException When the game has two components that conflict with eachother.
     */
    public void validate() throws DependencyNotFoundException, ComponentConflictException {
        for (Class<? extends GameComponent> dependency : depends) {
            if (!game.hasComponent(dependency)) {
                throw new DependencyNotFoundException(this, dependency);
            }
        }
        for (Class<? extends GameComponent> conflict : conflicts) {
            if (game.hasComponent(conflict)) {
                throw new ComponentConflictException(this, conflict);
            }
        }
    }

    /**
     * Loads all the hard and soft dependencies into the dependencies map.
     * When a new {@link GameSession} is created this method will be called.
     *
     * @throws IllegalStateException when trying to call this method before a session is created.
     */
    public void loadDependencies() {
        if (session == null) {
            throw new IllegalStateException("Dependencies can only be loaded for game sessions. This method should only be called by the API.");
        }
        for (Class<? extends GameComponent> dependency : depends) {
            dependencies.put(dependency, session.getComponent(dependency));
        }
        for (Class<? extends GameComponent> dependency : softDepends) {
            if (session.hasComponent(dependency)) {
                dependencies.put(dependency, session.getComponent(dependency));
            }
        }
    }
    //endregion


    //region General

    /**
     * Get the display name for the component.
     * For example for MinPlayersCP the name would be 'Min Players'.
     *
     * @return Configuration key name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the {@link GameBoxx} instance.
     *
     * @return GameBoxx instance.
     */
    public GameBoxx getAPI() {
        return game.getAPI();
    }

    /**
     * Get the plugin instance that registered the game this component belongs to.
     *
     * @return owning {@link JavaPlugin} of the game.
     */
    public JavaPlugin getPlugin() {
        return game.getPlugin();
    }


    /**
     * Get the {@link Game} this component belongs too.
     *
     * @return The game the component has been added to.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Get the {@link Arena} from the {@link GameSession} this component belongs to.
     *
     * @return The arena from the game session. This will be null for components in the {@link Game} but those should only be used as templates.
     */
    public Arena getArena() {
        if (session == null) {
            return null;
        }
        return session.getArena();
    }

    /**
     * Get the {@link GameSession} this component belongs to.
     *
     * @return The game session. This will be null for components in the {@link Game} but those should only be used as templates.
     */
    public GameSession getSession() {
        return session;
    }

    /**
     * Set the {@link GameSession}.
     * When calling {@link #newInstance(GameSession)} set the session using this method.
     *
     * @param session The {@link GameSession} to set
     * @return Returns itself so you can use it easier.
     */
    protected GameComponent setSession(GameSession session) {
        this.session = session;
        return this;
    }
    //endregion


    /**
     * Return a new instance of the component for the provided {@link GameSession}.
     *
     * @param <T>
     * @return A new instance of the component with the session set to the provided session.
     */
    public abstract <T extends GameComponent> T newInstance(GameSession session);
}
