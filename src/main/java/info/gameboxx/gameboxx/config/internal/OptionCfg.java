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

package info.gameboxx.gameboxx.config.internal;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.exceptions.InvalidOptionCastException;
import info.gameboxx.gameboxx.exceptions.OptionNotRegisteredException;
import info.gameboxx.gameboxx.exceptions.OptionNotSetException;
import info.gameboxx.gameboxx.options.ListOption;
import info.gameboxx.gameboxx.options.Option;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.options.list.*;
import info.gameboxx.gameboxx.options.single.*;
import info.gameboxx.gameboxx.util.cuboid.Cuboid;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration file for {@link Option} values.
 * Options have to be set with {@link #setOption(String, Option)}
 * The path is name the same as the name of the option.
 * Use {@link #save()} and {@link #load()} to save/load the added options.
 * It will not load options that haven't been set.
 */
public abstract class OptionCfg {

    private File file = null;
    private YamlConfiguration config = new YamlConfiguration();

    private Long saveDelay = 0l;
    private Long lastSave = 0l;

    private Map<String, Option> options = new HashMap<>();

    /**
     * Create a new options config for the specified string file path.
     * The path must be absolute from the server directory like: 'plugins/GameBoxx/Example.yml'
     * Make sure you also put the file extension.
     * Directories and files get created automatically no need to manually check/do this.
     * @param file The file for the config file. (see description)
     */
    public OptionCfg(String file) {
        setFile(file);
    }

    /**
     * Create a new options config for the specified file path.
     * Make sure you also put the file extension.
     * For example: {@code new File(plugin.getDataFolder(), "Example.yml")}
     * Directories and files get created automatically no need to manually check/do this.
     * @param file the file for the config file. (see description)
     */
    public OptionCfg(File file) {
        setFile(file);
    }

    /**
     * Load all the options from config and save the defaults.
     * @see #load(boolean)
     */
    public void load() {
        load(true);
    }

    /**
     * Load all the option values from config.
     * <b>This does not load options that haven't been added!</b>
     * If there are exceptions the stacktrace will be printed.
     * @param save Whether or not to save the config after loading the values to save the defaults.
     */
    public void load(boolean save) {
        try {
            createFile();

            config.load(file);
            loadOptions(config);

            if (save) {
                config.save(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the options to config.
     * If a delay is set it will check if the saving is still delayed and if so, it will not save.
     * Use {@link #save(boolean)} with force set to true to ignore the save delay and force save the config.
     * @return Whether or not the config was saved. When it's on delay it will return false.
     */
    public boolean save() {
        return save(false);
    }

    /**
     * Save the options to config.
     * If a delay is set it will check if the saving is still delayed and if so, it will not save unless force is set to true.
     * @param force When set to true it will ignore the save delay if it's set and force save the file.
     * @return Whether or not the config was saved. When it's on delay it will return false.
     */
    public boolean save(boolean force) {
        createFile();
        if (!force && saveDelay > 0 && lastSave + saveDelay > System.currentTimeMillis()) {
            return false;
        }
        lastSave = System.currentTimeMillis();
        try {
            if (!file.exists()) {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            saveOptions(config);
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Creates the config file if it doesn't exist yet.
     * If there are exceptions the stacktrace will be printed.
     */
    private void createFile() {
        if (file == null) {
            new InvalidConfigurationException("File cannot be null!").printStackTrace();
        }
        try {
            if (!file.exists()) {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load all the option values from config.
     * See {@link #loadOption(ConfigurationSection, String, Option, boolean)} for more details about loading options.
     * @param cs The configuration section/file to load the options from.
     */
    public void loadOptions(ConfigurationSection cs) {
        for (Map.Entry<String, Option> option : options.entrySet()) {
            loadOption(cs, option.getKey(), option.getValue(), false);
        }
    }

    /**
     * Save all the option values in the config.
     * See {@link #saveOption(ConfigurationSection, String, Option, boolean)} for more details about saving options.
     * @param cs the configuration section/file to save the options in.
     */
    public void saveOptions(ConfigurationSection cs) {
        for (Map.Entry<String, Option> option : options.entrySet()) {
            saveOption(cs, option.getKey(), option.getValue(), false);
        }
    }

    /**
     * Load the value from the specified config + path in to the specified option.
     * The parse method from the option will be used to load the value.
     *
     * If there are errors when parsing the errors will be printed in the console.
     * It will keep the invalid options in the config but when retrieving the option value it will be the default value if there was an error.
     *
     * @param cs The configuration section/file to load the option value from.
     * @param path the path in the config section/file to load the option value from.
     * @param option The option to load. This option will be used for parsing and the value will be put in this option when parsing was successful.
     * @param loadConfig When calling this manually you probably want this to be true to load the config file. {@link YamlConfiguration#load(File)}
     * @return Whether or not the option was loaded successful.
     */
    private boolean loadOption(ConfigurationSection cs, String path, Option option, boolean loadConfig) {
        //Load config if specified.
        if (loadConfig) {
            createFile();
            try {
                config.load(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //If config doesn't have the option save the default.
        if (!cs.isSet(path)) {
            saveOption(cs, path, option, loadConfig);
        }

        //If the option doesn't have a default it may not be in the config still.
        if (!cs.isSet(path)) {
            return false;
        }

        //Parse options.
        if (option instanceof SingleOption) {
            SingleOption singleOption = (SingleOption)option;
            String value = cs.getString(path);

            if (!singleOption.parse(value)) {
                logError(path, option.getName(), option.getClass().getSimpleName(), value, option.getError());
                return false;
            }
            return true;
        } else if (option instanceof ListOption) {
            ListOption listOption = (ListOption)option;
            List<?> values = config.getList(path);

            if (!listOption.parse(false, values.toArray(new Object[values.size()]))) {
                logError(path, option.getName(), option.getClass().getSimpleName(), "See Error Index", option.getError());
                return false;
            }
            return true;
        }
        //TODO: Map options.

        return false;
    }

    private void saveOption(ConfigurationSection cs, String path, Option option, boolean saveConfig) {
        //TODO: Save option
    }

    /**
     * When parsing fails log full details about the error in the console.
     * @param path The path of the option value.
     * @param optionName The option name. {@link Option#getName()}
     * @param optionType The option type. {@link Class#getSimpleName()}
     * @param value The option value from the config file. (which is invalid)
     * @param error The error message produced from the option parser. {@link Option#getError()}
     */
    private void logError(String path, String optionName, String optionType, String value, String error) {
        GameBoxx.get().error("Invalid configuration option value found! '" + file.getAbsolutePath() + "'");
        GameBoxx.get().error("Path: '" + path + "' Option: '" + optionName + "' Type: '" + optionType + "' Config Value: '" + value + "'");
        GameBoxx.get().error("Error: '" + error + "'");
    }


    /**
     * Get the configuration {@link File}
     * @return The configuration {@link File}
     */
    public File getFile() {
        return file;
    }

    /**
     * Set/change the configuration file.
     * @see #OptionCfg(String)
     * @param file The absolute path for the configuration file.
     */
    public void setFile(String file) {
        if (file == null) {
            new InvalidConfigurationException("File cannot be null!").printStackTrace();
        }
        this.file = new File(file);
    }

    /**
     * Set/change the configuration file.
     * @see #OptionCfg(File)
     * @param file The configuration file.
     */
    public void setFile(File file) {
        if (file == null) {
            new InvalidConfigurationException("File cannot be null!").printStackTrace();
        }
        this.file = file;
    }


    /**
     * Get the {@link YamlConfiguration} used for this config.
     * @return The {@link YamlConfiguration}
     */
    public YamlConfiguration getConfig() {
        return config;
    }


    /**
     * Set the save delay in milliseconds.
     * Every time the {@link #save()} method is called it will check this delay.
     * For example with a delay of 5000 it would not save the file when it was saved 3 seconds ago.
     * <b>This is not a scheduled delay it just puts the saving on cooldown for this duration!</b>
     * @param saveDelay The delay in milliseconds.
     */
    public void setSaveDelay(Long saveDelay) {
        this.saveDelay = saveDelay;
    }

    /**
     * Clear the save delay by setting it to 0ms.
     */
    public void clearSaveDelay() {
        saveDelay = 0l;
    }

    /**
     * Get the save delay set with {@link #setSaveDelay(Long)}
     * @return The save delay in milliseconds.
     */
    public Long getSaveDelay() {
        return saveDelay;
    }


    /**
     * Get the option at the specified path.
     * If there is no option set at the specified path this will be {@code null}!
     * @param path The path of the option to return.
     * @return The option at the specified path or {@code null} when there is no option at the specified path.
     */
    public Option getOption(String path) {
        if (!hasOption(path)) {
            return null;
        }
        return options.get(path);
    }

    /**
     * Get a collection with all the options in this config.
     * This does not contain all the options in the config file itself.
     * It only contains options that have been set with {@link #setOption(String, Option)}
     * @return {@link Collection<Option>} with options.
     */
    public Collection<Option> getOptions() {
        return options.values();
    }

    /**
     * Get the map with all the options with the path of the option as key.
     * This does not contain all the options in the config file itself.
     * It only contains options that have been set with {@link #setOption(String, Option)}
     * @return {@link Map<String, Option>} with the path as key and the option as value.
     */
    public Map<String, Option> getOptionMap() {
        return options;
    }

    /**
     * Set/register a option for the config.
     * The path is where the option will be saved in the config.
     * Just like regular {@link YamlConfiguration#set(String, Object)} the path may contain dots (.) for different sections.
     *
     * It will overwrite any previous options at the same path!
     * Use {@link #hasOption(String, boolean)} to check if this config has a option for the specified path.
     *
     * It's recommended to always give options a default value.
     * It will work without but there won't be a entry in the config file for the option till a value is set and the option is saved.
     *
     * @param path The path in the config file for the option.
     * @param option The option instance.
    */
    public void setOption(String path, Option option) {
        options.put(path, option);
        loadOption(config, path, option, true);
    }

    /**
     * Check if the config has a option for the specified path.
     * This will not check if there is a value in the config file it just checks for the options set with {@link #setOption(String, Option)}
     * Use {@link #hasOption(String, boolean)} with checkConfig to true to also check if the option is set in the config.
     * @param path The path of the option to check.
     * @return Whether or not the config has a option for the specified path.
     */
    public boolean hasOption(String path) {
        return hasOption(path, false);
    }

    /**
     * Check if the config has a option for the specified path.
     * @param path The path of the option to check.
     * @param checkConfig When true it will also return true when the config file has the specified path.
     * @return Whether or not the config has a option for the specified path.
     */
    public boolean hasOption(String path, boolean checkConfig) {
        if (options.containsKey(path)) {
            return true;
        }
        if (config.isSet(path)) {
            return true;
        }
        return false;
    }

    /**
     * Remove/unregister a option from the config.
     * @param path The path of the option to remove.
     * @param fromConfig When set to true it will also remove the value out of the config file and save it.
     */
    public void removeOption(String path, boolean fromConfig) {
        options.remove(path);
        if (fromConfig) {
            config.set(path, null);
            save(true);
        }
    }


    /**
     * Get a option value for the specified path.
     * You should only use this method for custom options because all the other options have their own methods like {@link #getInt(String)} etc.
     * @param path The path of the option to get.
     * @param optionClass The type of option for example LocationOption.class.
     * @return The value as {@link Object}. You can cast this to the value of the optionClass you specified.
     */
    public Object getObject(String path, Class<? extends Option> optionClass) {
        Option option = getOption(path);
        if (option == null) {
            throw new OptionNotRegisteredException(path);
        }

        if (!option.getClass().equals(optionClass)) {
            throw new InvalidOptionCastException(path, optionClass, option.getClass());
        }

        Object value = null;
        if (option instanceof SingleOption) {
            value = ((SingleOption)option).getValue();
        } else if (option instanceof ListOption) {
            value = ((ListOption)option).getValues();
        }

        if (value == null) {
            throw new OptionNotSetException(path);
        }
        return value;
    }


    /**
     * Get a {@link String} option value for the specified path.
     * @param path The path of the option to get.
     * @return String value.
     */
    public String getString(String path) {
        return (String) getObject(path, StringOption.class);
    }

    /**
     * Get a list with {@link String} option values for the specified path.
     * @param path The path of the option to get.
     * @return List with string values.
     */
    public List<String> getStringList(String path) {
        return (List<String>) getObject(path, StringListOption.class);
    }

    /**
     * Get a {@link Boolean} option value for the specified path.
     * @param path The path of the option to get.
     * @return Boolean value.
     */
    public Boolean getBool(String path) {
        return (Boolean) getObject(path, BoolOption.class);
    }

    /**
     * Get a list with {@link Boolean} option values for the specified path.
     * @param path The path of the option to get.
     * @return List with boolean values.
     */
    public List<Boolean> getBoolList(String path) {
        return (List<Boolean>) getObject(path, BoolListOption.class);
    }

    /**
     * Get a {@link Integer} option value for the specified path.
     * @param path The path of the option to get.
     * @return Integer value.
     */
    public Integer getInt(String path) {
        return (Integer) getObject(path, IntOption.class);
    }

    /**
     * Get a list with {@link Integer} option values for the specified path.
     * @param path The path of the option to get.
     * @return List with integer values.
     */
    public List<Integer> getIntList(String path) {
        return (List<Integer>) getObject(path, IntListOption.class);
    }

    /**
     * Get a {@link Double} option value for the specified path.
     * @param path The path of the option to get.
     * @return Double value.
     */
    public Double getDouble(String path) {
        return (Double) getObject(path, DoubleOption.class);
    }

    /**
     * Get a list with {@link Double} option values for the specified path.
     * @param path The path of the option to get.
     * @return List with double values.
     */
    public List<Double> getDoubleList(String path) {
        return (List<Double>) getObject(path, DoubleListOption.class);
    }

    /**
     * Get a {@link Vector} option value for the specified path.
     * @param path The path of the option to get.
     * @return Vector value.
     */
    public Vector getVector(String path) {
        return (Vector) getObject(path, VectorOption.class);
    }

    /**
     * Get a list with {@link Vector} option values for the specified path.
     * @param path The path of the option to get.
     * @return List with vector values.
     */
    public List<Vector> getVectorList(String path) {
        return (List<Vector>) getObject(path, VectorListOption.class);
    }

    /**
     * Get a {@link MaterialData} option value for the specified path.
     * @param path The path of the option to get.
     * @return MaterialData value.
     */
    public MaterialData getMaterial(String path) {
        return (MaterialData) getObject(path, MaterialOption.class);
    }

    /**
     * Get a list with {@link MaterialData} option values for the specified path.
     * @param path The path of the option to get.
     * @return List with MaterialData values.
     */
    public List<MaterialData> getMaterialList(String path) {
        return (List<MaterialData>) getObject(path, MaterialListOption.class);
    }

    /**
     * Get a {@link Player} option value for the specified path.
     * @param path The path of the option to get.
     * @return Player value.
     */
    public Player getPlayer(String path) {
        return (Player) getObject(path, PlayerOption.class);
    }

    /**
     * Get a list with {@link Player} option values for the specified path.
     * @param path The path of the option to get.
     * @return List with Player values.
     */
    public List<Player> getPlayerList(String path) {
        return (List<Player>) getObject(path, PlayerListOption.class);
    }

    /**
     * Get a {@link World} option value for the specified path.
     * @param path The path of the option to get.
     * @return World value.
     */
    public World getWorld(String path) {
        return (World) getObject(path, WorldOption.class);
    }

    /**
     * Get a list with {@link World} option values for the specified path.
     * @param path The path of the option to get.
     * @return List with World values.
     */
    public List<World> getWorldList(String path) {
        return (List<World>) getObject(path, WorldListOption.class);
    }

    /**
     * Get a {@link Location} option value for the specified path.
     * @param path The path of the option to get.
     * @param world Override the world from the location with the world specified. {@code null} to use the world from the option value.
     * @return Location value.
     */
    public Location getLocation(String path, World world) {
        if (world == null) {
            return (Location)getObject(path, LocationOption.class);
        }
        Location loc = (Location) getObject(path, LocationOption.class);
        loc.setWorld(world);
        return loc;
    }

    /**
     * Get a list with {@link Location} option values for the specified path.
     * @param path The path of the option to get.
     * @param world Override the world from the locations with the world specified. {@code null} to use the world from the option values.
     * @return List with location values.
     */
    public List<Location> getLocationList(String path, World world) {
        List<Location> locs = (List<Location>) getObject(path, LocationListOption.class);
        if (world == null) {
            return locs;
        }
        for (Location loc : locs) {
            loc.setWorld(world);
        }
        return locs;
    }

    /**
     * Get a {@link Block} option value for the specified path.
     * @param path The path of the option to get.
     * @param world Override the world from the block with the world specified. {@code null} to use the world from the option value.
     * @return Block value.
     */
    public Block getBlock(String path, World world) {
        if (world == null) {
            return (Block)getObject(path, BlockOption.class);
        }
        Location loc = ((Block) getObject(path, BlockOption.class)).getLocation();
        loc.setWorld(world);
        return loc.getBlock();
    }

    /**
     * Get a list with {@link Block} option values for the specified path.
     * @param path The path of the option to get.
     * @param world Override the world from the blocks with the world specified. {@code null} to use the world from the option values.
     * @return List with block values.
     */
    public List<Block> getBlockList(String path, World world) {
        List<Block> blocks = (List<Block>) getObject(path, BlockListOption.class);
        if (world == null) {
            return blocks;
        }
        for (int i = 0; i < blocks.size(); i++) {
            Location loc = blocks.get(i).getLocation();
            loc.setWorld(world);
            blocks.set(i, loc.getBlock());
        }
        return blocks;
    }

    /**
     * Get a {@link Cuboid} option value for the specified path.
     * @param path The path of the option to get.
     * @param world Override the world from the Cuboid with the world specified. {@code null} to use the world from the option value.
     * @return Cuboid value.
     */
    public Cuboid getCuboid(String path, World world) {
        if (world == null) {
            return (Cuboid) getObject(path, CuboidOption.class);
        }
        Cuboid cub = (Cuboid) getObject(path, CuboidOption.class);
        cub.setWorld(world);
        return cub;
    }

    /**
     * Get a list with {@link Cuboid} option values for the specified path.
     * @param path The path of the option to get.
     * @param world Override the world from the cuboids with the world specified. {@code null} to use the world from the option values.
     * @return List with cuboid values.
     */
    public List<Cuboid> getCuboidList(String path, World world) {
        List<Cuboid> cubs = (List<Cuboid>) getObject(path, CuboidListOption.class);
        if (world == null) {
            return cubs;
        }
        for (Cuboid cub : cubs) {
            cub.setWorld(world);
        }
        return cubs;
    }

    //TODO: Add missing options and map options

}
