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

import info.gameboxx.gameboxx.components.internal.ComponentHolder;
import info.gameboxx.gameboxx.events.*;
import info.gameboxx.gameboxx.exceptions.InvalidOptionCastException;
import info.gameboxx.gameboxx.exceptions.OptionNotRegisteredException;
import info.gameboxx.gameboxx.exceptions.OptionNotSetException;
import info.gameboxx.gameboxx.options.ListOption;
import info.gameboxx.gameboxx.options.Option;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.options.list.*;
import info.gameboxx.gameboxx.options.single.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.List;

public abstract class GameSession extends ComponentHolder {

    protected Game game;
    protected Arena arena;
    private int id;

    private boolean ready = false;
    private World world = null;

    public static final PluginManager PLUGIN_MANAGER = Bukkit.getPluginManager();

    public GameSession(Game game, Arena arena, int id) {
        this.game = game;
        this.arena = arena;
        this.id = id;
        PLUGIN_MANAGER.callEvent(new SessionStartEvent(this));
    }

    /**
     * Get the session ID.
     * @return The session ID.
     */
    public int getID() {
        return id;
    }

    /**
     * Get the {@link Game} of the session.
     * @return The game of the session.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Get the {@link Arena} of the session.
     * @return The arena of the session.
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Get the {@link World} the session is hosted in.
     * @return The world of the session.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Set the {@link World} the session is hosted in.
     * There is no need to call this as this will be set when new sessions get created.
     * @param world The world of the session.
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Get whether or not the session is ready to be joined and played.
     * After the world is generated this will be set to true.
     * While this is false players shouldn't be able to join etc.
     * @return True when the session is ready and false if not.
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Set whether or not the session is ready to be joined and played etc.
     * This method is used when creating sessions when worlds are generated/loaded.
     * There is no need to manually call this.
     * @param ready The ready state to set.
     */
    public void setReady(boolean ready) {
        this.ready = ready;
    }


    //region Management

    /**
     * Adds a player to a session.
     * @param player The player to add.
     */
    public void addPlayer(Player player) {
    	PLUGIN_MANAGER.callEvent(new PlayerJoinSessionEvent(player, this));
    }
    
    /**
     * Removes a player from the active players list.
     * @param player The player who is leaving the session.
     * @param reason The reason for which the player is leaving the session.
     */
    public void removePlayer(Player player, LeaveReason reason) {
    	PLUGIN_MANAGER.callEvent(new PlayerLeaveSessionEvent(player, this, reason));
    }
    
    /**
     * Stops the session.
     */
    public void stop() {
    	PLUGIN_MANAGER.callEvent(new SessionStopEvent(this));
    	// TODO: Handle implementation later
    }
    
    /**
     * Restarts the session.
     */
    public void restart() {
        PLUGIN_MANAGER.callEvent(new SessionResetEvent(this));
        // TODO: Handle implementation later
    }
    //endregion


    //region Options

    /**
     * Get a option value for the specified name.
     * You should only use this method for custom options because all the other options have their own methods like {@link #getLocation(String)} etc.
     * @param name The name of the option to get. (Casing doesn't matter)
     * @param optionClass The type of option for example LocationOption.class.
     * @return The value as {@link Object}. You can cast this to the value of the optionclass you specified.
     */
    public Object getObject(String name, Class<? extends Option> optionClass) {
        Option option = arena.getOption(name);
        if (option == null) {
            throw new OptionNotRegisteredException(name);
        }

        if (!option.getClass().equals(optionClass)) {
            throw new InvalidOptionCastException(name, optionClass, option.getClass());
        }

        Object value = null;
        if (option instanceof SingleOption) {
            value = ((SingleOption)option).getValue();
        } else if (option instanceof ListOption) {
            value = ((ListOption)option).getValues();
        }

        if (value == null) {
            throw new OptionNotSetException(name);
        }
        return value;
    }

    /**
     * Get a {@link String} option value for the specified name.
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return String value.
     */
    public String getString(String name) {
        return (String) getObject(name, StringOption.class);
    }

    /**
     * Get a list with {@link String} option values for the specified name.
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return List with string values.
     */
    public List<String> getStringList(String name) {
        return (List<String>) getObject(name, StringListOption.class);
    }

    /**
     * Get a {@link Boolean} option value for the specified name.
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return Boolean value.
     */
    public Boolean getBool(String name) {
        return (Boolean) getObject(name, BoolOption.class);
    }

    /**
     * Get a list with {@link Boolean} option values for the specified name.
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return List with boolean values.
     */
    public List<Boolean> getBoolList(String name) {
        return (List<Boolean>) getObject(name, BoolListOption.class);
    }

    /**
     * Get a {@link Integer} option value for the specified name.
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return Integer value.
     */
    public Integer getInt(String name) {
        return (Integer) getObject(name, IntOption.class);
    }

    /**
     * Get a list with {@link Integer} option values for the specified name.
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return List with integer values.
     */
    public List<Integer> getIntList(String name) {
        return (List<Integer>) getObject(name, IntListOption.class);
    }

    /**
     * Get a {@link Double} option value for the specified name.
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return Double value.
     */
    public Double getDouble(String name) {
        return (Double) getObject(name, DoubleOption.class);
    }

    /**
     * Get a list with {@link Double} option values for the specified name.
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return List with double values.
     */
    public List<Double> getDoubleList(String name) {
        return (List<Double>) getObject(name, DoubleListOption.class);
    }

    /**
     * Get a {@link Location} option value for the specified name.
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return Location with the proper world.
     */
    public Location getLocation(String name) {
        if (getWorld() == null) {
            return (Location)getObject(name, LocationOption.class);
        }
        Location loc = (Location) getObject(name, LocationOption.class);
        loc.setWorld(getWorld());
        return loc;
    }

    /**
     * Get a list with {@link Location} option values for the specified name.
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return List with locations with the proper world.
     */
    public List<Location> getLocationList(String name) {
        List<Location> locs = (List<Location>) getObject(name, LocationListOption.class);
        if (getWorld() == null) {
            return locs;
        }
        for (Location loc : locs) {
            loc.setWorld(getWorld());
        }
        return locs;
    }

    /**
     * Get a {@link Block} option value for the specified name.
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return Block with the proper world.
     */
    public Block getBlock(String name) {
        if (getWorld() == null) {
            return (Block)getObject(name, BlockOption.class);
        }
        Location loc = ((Block) getObject(name, BlockOption.class)).getLocation();
        loc.setWorld(getWorld());
        return loc.getBlock();
    }

    /**
     * Get a list with {@link Block} option values for the specified name.
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return List with blocks with the proper world.
     */
    public List<Block> getBlockList(String name) {
        List<Block> blocks = (List<Block>) getObject(name, BlockListOption.class);
        if (getWorld() == null) {
            return blocks;
        }
        for (int i = 0; i < blocks.size(); i++) {
            Location loc = blocks.get(i).getLocation();
            loc.setWorld(getWorld());
            blocks.set(i, loc.getBlock());
        }
        return blocks;
    }
    //endregion


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameSession other = (GameSession)obj;
        if (other.getID() != this.getID()) {
            return false;
        }
        if (!other.getArena().equals(this.getArena())) {
            return false;
        }
        return true;
    }

}
