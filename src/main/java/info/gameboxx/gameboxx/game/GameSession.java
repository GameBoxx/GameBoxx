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
import info.gameboxx.gameboxx.util.cuboid.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

//TODO: Implement this class it will handle the game flow like joining/leaving/starting/stopping/resetting etc. All of that stuff will be dependent on components obviously.
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


    /**
     * Get a {@link Location} option value for the specified name.
     * Make sure that the option is registered!
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return Location with the proper world.
     */
    public Location getLocationOption(String name) {
        Object value = arena.getOptionValue(name);
        if (value instanceof Location) {
            Location loc = (Location)value;
            if (getWorld() == null) {
                return loc;
            }
            loc.setWorld(getWorld());
            return loc;
        }
        return null;
    }

    /**
     * Get a list with {@link Location} option values for the specified name.
     * Make sure that the option is registered!
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return List with locations with the proper world.
     */
    public List<Location> getLocationsOption(String name) {
        Object value = arena.getOptionValue(name);
        if (value instanceof List) {
            try {
                List<Location> locations = (List<Location>)value;
                if (getWorld() == null) {
                    return locations;
                }
                for (Location loc : locations) {
                    loc.setWorld(getWorld());
                }
                return locations;
            } catch (ClassCastException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Get a {@link Block} option value for the specified name.
     * Make sure that the option is registered!
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return Block with the proper world.
     */
    public Block getBlockOption(String name) {
        Object value = arena.getOptionValue(name);
        if (value instanceof Location) {
            Location loc = (Location)value;
            if (getWorld() == null) {
                return loc.getBlock();
            }
            loc.setWorld(getWorld());
            return loc.getBlock();
        }
        return null;
    }

    /**
     * Get a list with {@link Block} option values for the specified name.
     * Make sure that the option is registered!
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return List with blocks with the proper world.
     */
    public List<Block> getBlocksOption(String name) {
        Object value = arena.getOptionValue(name);
        if (value instanceof List) {
            try {
                List<Location> locations = (List<Location>)value;
                List<Block> blocks = new ArrayList<>();
                for (Location loc : locations) {
                    if (getWorld() != null) {
                        loc.setWorld(getWorld());
                    }
                    blocks.add(loc.getBlock());
                }
                return blocks;
            } catch (ClassCastException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Get a {@link Cuboid} option value for the specified name.
     * Make sure that the option is registered!
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return Cuboid with the proper world.
     */
    public Cuboid getCuboidOption(String name) {
        Object value = arena.getOptionValue(name);
        if (value instanceof Cuboid) {
            Cuboid cub = (Cuboid)value;
            if (getWorld() == null) {
                return cub;
            }
            cub.setWorld(getWorld());
            return cub;
        }
        return null;
    }

    /**
     * Get a list with {@link Cuboid} option values for the specified name.
     * Make sure that the option is registered!
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return List with locations with the proper world.
     */
    public List<Cuboid> getLCuboidsOption(String name) {
        Object value = arena.getOptionValue(name);
        if (value instanceof List) {
            try {
                List<Cuboid> cuboids = (List<Cuboid>)value;
                if (getWorld() == null) {
                    return cuboids;
                }
                for (Cuboid cub : cuboids) {
                    cub.setWorld(getWorld());
                }
                return cuboids;
            } catch (ClassCastException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Get a {@link String} option value for the specified name.
     * Make sure that the option is registered!
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return String value.
     */
    public String getStringOption(String name) {
        Object value = arena.getOptionValue(name);
        if (value instanceof String) {
            return (String)value;
        }
        return value.toString();
    }

    /**
     * Get a {@link Boolean} option value for the specified name.
     * Make sure that the option is registered!
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return Boolean value.
     */
    public Boolean getBoolOption(String name) {
        Object value = arena.getOptionValue(name);
        if (value instanceof Boolean) {
            return (Boolean)value;
        }
        return null;
    }

    /**
     * Get a {@link Integer} option value for the specified name.
     * Make sure that the option is registered!
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return Integer value.
     */
    public Integer getIntOption(String name) {
        Object value = arena.getOptionValue(name);
        if (value instanceof Integer) {
            return (Integer)value;
        }
        return null;
    }

    /**
     * Get a {@link Double} option value for the specified name.
     * Make sure that the option is registered!
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return Double value.
     */
    public Double getDoubleOption(String name) {
        Object value = arena.getOptionValue(name);
        if (value instanceof Double) {
            return (Double)value;
        }
        return null;
    }

    /**
     * Get a {@link Float} option value for the specified name.
     * Make sure that the option is registered!
     * @param name The name of the option to get. (Casing doesn't matter)
     * @return Float value.
     */
    public Float getFloatOption(String name) {
        Object value = arena.getOptionValue(name);
        if (value instanceof Float) {
            return (Float)value;
        }
        return null;
    }


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
