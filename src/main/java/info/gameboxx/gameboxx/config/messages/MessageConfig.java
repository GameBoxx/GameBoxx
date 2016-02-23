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

package info.gameboxx.gameboxx.config.messages;

import info.gameboxx.gameboxx.GameBoxx;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Responsible for loading translatable message configuration files.
 * The messages will be loaded from the plugin jar based on language settings.
 *
 * It will also try to load a changelog for removed/changes settings.
 * Settings will not be overwritten but it can be used to manually override settings.
 *
 * The name specified for this config must match with the file name within the jar of the provided plugin.
 * It will look for a file named '{name}_{language-id}.yml' and for changes '{name}_{language-id}_changes.yml' (optional!)
 * Make sure there is at least a file with the 'en' language key.
 * It will always load the English messages as fallback for when the user selected language file is missing or if it's missing options etc.
 *
 * The file will be loaded from the jar at the path '/messages/{language-id}/{file}' so make sure it's there!
 * If you want to use this in your plugin and compile with maven just put this in your build > resources
 * <resource>
 *     <targetPath>./messages</targetPath>
 *     <filtering>false</filtering>
 *     <directory>${basedir}/src/main/resources/messages</directory>
 * </resource>
 * Make sure you have folders within your messages folder for the language ids.
 * More details can be found on the wiki.
 */
public class MessageConfig {

    private static List<MessageConfig> configs = new ArrayList<>();
    private static final String VERSION_KEY = "VERSION-NUMBER_DO-NOT-EDIT";

    private GameBoxx gb;
    private Plugin plugin;
    private MessageConfig fallback = null;

    private String name;
    private int version = 0;
    private int targetVersion = 0;

    private Language loadedLanguage;

    private File file = null;
    private YamlConfiguration config;
    private YamlConfiguration changelog;

    /**
     * Use this to register/load a message file.
     * See the description of this class for full details about the name.
     *
     * @param plugin The plugin that will have the message file within the jar.
     * @param name The name for the messages file. For example if you specify 'test' the file for English would be 'test_en.yml' and 'test_en_changes.yml'
     */
    public MessageConfig(Plugin plugin, String name) {
        this(plugin, name, false);
    }

    private MessageConfig(Plugin plugin, String name, boolean fallback) {
        gb = GameBoxx.get();
        this.plugin = plugin;
        this.name = name;

        if (!fallback) {
            configs.add(this);
        }

        load(fallback ? "en" : gb.getLanguage().getID());

        if (!fallback) {
            loadedLanguage = gb.getLanguage();
            if (!gb.getLanguage().getID().equalsIgnoreCase("en")) {
                this.fallback = new MessageConfig(plugin, name, true);
            }
        } else {
            loadedLanguage = Language.ENGLISH;
        }
    }

    /**
     * <b>This gets called in the constructor and when changing languages already!</b>
     * Only call this when you want to force load messages.
     * If you just want to update the config file call {@link #loadSimple(boolean)}
     *
     * Load the message file.
     * Defaults will be set from the loaded file out of the plugin jar.
     *
     * Changes will be loaded from a file with the _changes suffix.
     * This file must have a version number and a list of keys that have been changed/removed for each version. (see wiki for details)
     *
     * Changes will not be automatically updated/overwritten in the config file.
     * The changes config is stored seperate and it has to be confirmed by the user.
     *
     * @return Whether or not the loading was successful.
     */
    public boolean load() {
        return load(gb.getLanguage().getID());
    }

    /**
     * @see #load()
     * @param lang The language to load.
     *             You should just use {@link #load()} to load the user selected language.
     *             Only use this to force load a different langauge.
     * @return Whether or not the loading was successful.
     */
    public boolean load(String lang) {
        File dir = new File(new File(plugin.getDataFolder(), "messages"), lang);
        dir.mkdirs();
        file = new File(dir, name + "_" + lang + ".yml");

        //Get the default file from the jar.
        String source = "messages/" + lang + "/" + name + "_" + lang + ".yml";
        InputStream in = plugin.getResource(source);
        if (in == null) {
            gb.warn("Language file '" + source + "' not found! Either a corrupt jar or " + plugin.getName() + " doesn't support the language '" + lang + "'!");
            return false;
        }

        //Load the config
        config = YamlConfiguration.loadConfiguration(file);
        config.options().copyDefaults(true);
        config.setDefaults(YamlConfiguration.loadConfiguration(in));

        //Get/set the version
        if (config.contains(VERSION_KEY)) {
            version = config.getInt(VERSION_KEY);
        } else {
            config.set(VERSION_KEY, version);
        }

        //Load changes if there is a changes file.
        source = "messages/" + lang + "/" + name + "_" + lang + "_changes.yml";
        in = plugin.getResource(source);
        if (in != null) {
            changelog = YamlConfiguration.loadConfiguration(in);
            targetVersion = changelog.getInt("version");
            if (targetVersion > version) {
                gb.warn("Message file " + name + "_" + lang + ".yml is " + (targetVersion - version) + " version(s) behind.\n" +
                        "Please run /gameboxx messages for more details.");

            }
        }

        cacheMessages();

        //Save the file
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            gb.error("Failed to save the default language file '" + file.getAbsolutePath() + "'");
            gb.error(e.getMessage());
            return false;
        }
    }

    /**
     * Load the message file.
     * This does not load default values or anything.
     * It just loads the local file in to to the current {@link YamlConfiguration}.
     * When there are errors the error will be logged and it will return false.
     * @param loadFallback Whether or not to try and load the fallback messages values too.
     * @return Whether or not the loading was successful
     */
    public boolean loadSimple(boolean loadFallback) {
        boolean success = false;
        if (config == null) {
            config = YamlConfiguration.loadConfiguration(file);
            success = true;
        } else {
            try {
                config.load(file);
                success = true;
            } catch (IOException e) {
                gb.error("Failed to load the messages file '" + file.getAbsolutePath() + "'");
                gb.error(e.getMessage());
            } catch (InvalidConfigurationException e) {
                gb.error("Failed to load the messages file '" + file.getAbsolutePath() + "'");
                gb.error(e.getMessage());
            }
        }
        if (loadFallback && fallback != null) {
            fallback.loadSimple(false);
        }
        cacheMessages();
        return success;
    }

    /**
     * Save the message configuration file.
     * You shouldn't really have to use this.
     * This is called when updating messages for example.
     * When there are errors the error will be logged and it will return false.
     * @return Whether or not the saving was successful.
     */
    public boolean save() {
        if (config != null) {
            try {
                config.save(file);
                return true;
            } catch (IOException e) {
                gb.error("Failed to save the messages file '" + file.getAbsolutePath() + "'");
                gb.error(e.getMessage());
                return false;
            }
        }
        return false;
    }

    /**
     * Get a list with all the registered {@link MessageConfig} instances.
     * @return list with registered message configurations.
     */
    public static List<MessageConfig> getConfigs() {
        return configs;
    }

    /**
     * Cache all the messages from this config in {@link Msg}
     * The {@link Msg#setMessages(Map)} will be called with all the messages from this config.
     * It will first set the fallback messages and then overwrite them with the messages from this config.
     */
    public void cacheMessages() {
        if (fallback != null && fallback.getConfig() != null) {
            cacheMessages(true);
        }
        cacheMessages(false);
    }

    /**
     * @see #cacheMessages()
     */
    private void cacheMessages(boolean fallback) {
        YamlConfiguration config = getConfig();
        if (fallback) {
            config = this.fallback.getConfig();
        }
        if (config == null) {
            return;
        }
        Set<String> keys = config.getKeys(true);
        for (String key : keys) {
            if (config.isString(key)) {
                Msg.setMessage(key, config.getString(key));
            }
        }
    }

    /**
     * Get the plugin that this message config belongs to.
     * @return The plugin this config belongs to.
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Get the fallback config.
     * If this is the registered config and the loaded language is not English this will return the English version of this config.
     * It will be null when this version is already English.
     * @return fallback {@link MessageConfig} for the English language
     */
    public MessageConfig getFallback() {
        return fallback;
    }

    /**
     * Get the {@link YamlConfiguration} which is currently loaded with the messages.
     * If the config file is null it will try to get the fallback config.
     * @return config file with messages. (May be {@code null}!)
     */
    public YamlConfiguration getConfig() {
        if (config == null && fallback != null) {
            return fallback.getConfig();
        }
        return config;
    }

    /**
     * Get the file where the current loaded configuration is saved/loaded from.
     * @return The file for this config for the currently loaded language.
     */
    public File getConfigFile() {
        return file;
    }

    /**
     * Get the name of the config which was provided in the constructor.
     * @return The name of the configuration.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the current loaded language for this message configuration.
     * @return The language of the current config.
     */
    public Language getLanguage() {
        return loadedLanguage;
    }

    /**
     * Get the changelog configuration file.
     * This file contains removed keys and changed keys for each version.
     * @return The changelog configuration file for the current loaded language.
     */
    public YamlConfiguration getChangelog() {
        return changelog;
    }

    /**
     * Get the version of the current loaded config.
     * @return The version of the loaded config.
     */
    public int getVersion() {
        return version;
    }

    /**
     * Get the target version of the current loaded version.
     * This is the latest version from the changelog.
     * @return The target version of the loaded config from the changelog.
     */
    public int getTargetVersion() {
        return targetVersion;
    }

    /**
     * Check whether or not the current loaded config has changes pending from the changelog.
     * @return True when the version is behind the target version otherwise false.
     */
    public boolean hasChanges() {
        return version < targetVersion;
    }

    /**
     * Used for {@link #getRemovedKeys()} and {@link #getChangedKeys()}
     * @param type Either 'removed' or 'changed'
     */
    private Map<Integer, List<String>> getKeys(String type) {
        Map<Integer, List<String>> keys = new HashMap<>();
        if (!hasChanges()) {
            return keys;
        }
        for (int v = version+1; v <= targetVersion; v++) {
            final String key = v + "." + type;
            if (!changelog.contains(key)) {
                continue;
            }
            if (changelog.isList(key)) {
                keys.put(v, changelog.getStringList(key));
            }
            if (changelog.isString(key)) {
                keys.put(v, Arrays.asList(changelog.getString(key)));
            }
        }
        return keys;
    }

    /**
     * Get a map with all the keys that have been removed in newer versions of the current loaded config.
     * The key of the map is the version number where the message was removed.
     * The value is the list of message keys that have been removed in that version.
     * @return map with message keys that have been removed. Will be empty when there are no keys removed.
     */
    public Map<Integer, List<String>> getRemovedKeys() {
        return getKeys("removed");
    }

    /**
     * Get a map with all the keys that have been changed in newer versions of the current loaded config.
     * The key of the map is the version number where the message was changed.
     * The value is the list of message keys that have been changed in that version.
     * @return map with message keys that have been changed. Will be empty when there are no keys changed.
     * @throws IllegalArgumentException when the specified version is too low or too high.
     */
    public Map<Integer, List<String>> getChangedKeys() {
        return getKeys("changed");
    }

    /**
     * Update the configuration file to the specified version.
     * Based on the {@link UpdateMode} it will either overwrite/remove values or skip them.
     *
     * The version of the config will be set to the specified version and the <b>file will be saved</b>.
     *
     * @param version The version to update to. This must be above the current version and not higher than the target version.
     * @param mode The {@link UpdateMode} to use for updating the messages.
     * @param keepOld When set to true old messages will remain in the config but they get prefixed with 'OLD-'
     *                For example gameboxx.help would become 'gameboxx.OLD-help' and the updated message will be set for 'gameboxx.help'
     * @throws IllegalArgumentException when the specified version is too low or too high.
     */
    public void update(int version, UpdateMode mode, boolean keepOld) {
        if (version > targetVersion || version <= version) {
            throw new IllegalArgumentException("The version number must be above the current version and not higher than the target version.");
        }

        loadSimple(false);

        //Skip everything only bump version
        if (mode == UpdateMode.SKIP_ALL) {
            config.set(VERSION_KEY, version);
            save();
            return;
        }

        Map<Integer, List<String>> removedKeys = getRemovedKeys();
        Map<Integer, List<String>> changedKeys = getChangedKeys();

        for (int v = version+1; v <= targetVersion; v++) {
            //Remove options
            if (removedKeys.containsKey(v) && (mode == UpdateMode.REMOVAL_ONLY || mode == UpdateMode.ALL)) {
                for (String key : removedKeys.get(v)) {
                    if (config.contains(key)) {
                        config.set(key, null);
                    }
                }
            }
            //Change options
            if (changedKeys.containsKey(v) && (mode == UpdateMode.CHANGES_ONLY || mode == UpdateMode.ALL)) {
                for (String key : changedKeys.get(v)) {
                    if (config.contains(key)) {
                        if (keepOld) {
                            //When keeping old value rename the key.
                            // 'gameboxx.help' would become 'gameboxx.OLD-help' and 'example' would become 'OLD-example'
                            String renamedKey;
                            if (!key.contains(".")) {
                                renamedKey = "OLD-" + key;
                            } else {
                                int index = key.lastIndexOf(".");
                                renamedKey = key.substring(0, index+1) + "OLD-" + key.substring(index+1, key.length());
                            }
                            config.set(renamedKey, config.get(key));
                        }
                        config.set(key, config.getDefaults().getString(key));
                    }
                }
            }
        }

        config.set(VERSION_KEY, version);
        save();
        cacheMessages(false);
    }


    /**
     * Mode used for updating the messages.
     */
    public enum UpdateMode {
        /**
         * Overwrites all the messages that have been changed.
         * Removes all messages that have been removed.
         */
        ALL,
        /**
         * Only bumps the configuration version
         * Won't overwrite messages that have been changed.
         * Won't remove messages that have been removed.
         */
        SKIP_ALL,
        /**
         * Won't overwrite messages that have been changed.
         * Removes all messages that have been removed.
         */
        REMOVAL_ONLY,
        /**
         * Overwrites all messages that have been changed.
         * Won't remove messages that have been removed.
         */
        CHANGES_ONLY
    }
}
