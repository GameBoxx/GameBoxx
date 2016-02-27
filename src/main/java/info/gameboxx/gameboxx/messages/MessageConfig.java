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

package info.gameboxx.gameboxx.messages;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.util.EProperties;
import info.gameboxx.gameboxx.util.Parse;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.*;

/**
 * Responsible for loading translatable message configuration files.
 * The messages will be loaded from the plugin jar based on language settings.
 * <p/>
 * It will also try to loadFull a changelog for removed/changes settings.
 * Messages will not be overwritten but it can be used to manually override messages.
 * <p/>
 * The name specified for this config must match with the file name within the jar of the provided plugin.
 * It will look for a file named '{name}_{language-id}.properties' and for changes '{name}_{language-id}.yml' (optional!)
 * <p/>
 * Make sure there is at least a file with the 'en' language key.
 * It will always load the English messages as fallback for when the user selected language file is missing or if it's missing options etc.
 * <p/>
 * The file will be loaded from the jar at the path '/messages/{name}_{language}.properties' so make sure it's there! For example messages_en.properties if the name is messages. The changelog file
 * would be located at '/changelogs/messages_en.yml' then. <b>Message files are '.properties' files and changelogs are '.yml'!</b>
 * <p/>
 * More details can be found on the wiki.
 */
public class MessageConfig {

    private static final List<MessageConfig> configs = new ArrayList<>();
    private static final String VERSION_KEY = "_VERSION_NO_EDIT";

    private final GameBoxx gb;
    private final Plugin plugin;
    private final Language loadedLanguage;
    private MessageConfig fallback = null;
    private String name;
    private int version = 1;
    private int targetVersion = 1;
    private File file = null;
    private EProperties config;
    private YamlConfiguration changelog;

    /**
     * Use this to register/loadFull a message file.
     * See the description of this class for full details about the name.
     *
     * @param plugin The plugin that will have the message file within the jar.
     * @param name The name for the messages file. For example if you specify 'test' the file for English would be 'test_en.properties' and 'test_en.yml'
     */
    public MessageConfig(Plugin plugin, String name) {
        this(plugin, name, false);
    }

    private MessageConfig(Plugin plugin, String name, boolean fallback) {
        gb = GameBoxx.get();
        this.plugin = plugin;
        this.name = name;

        if (! fallback) {
            configs.add(this);
        }

        loadFull(fallback ? "en" : gb.getLanguage().getID());

        if (! fallback) {
            loadedLanguage = gb.getLanguage();
            if (! gb.getLanguage().getID().equalsIgnoreCase("en")) {
                this.fallback = new MessageConfig(plugin, name, true);
            }
        } else {
            loadedLanguage = Language.ENGLISH;
        }
    }

    /**
     * Get a list with all the registered {@link MessageConfig} instances.
     *
     * @return list with registered message configurations.
     */
    public static List<MessageConfig> getConfigs() {
        return configs;
    }

    /**
     * Get a {@link MessageConfig} instance for the specified name.
     * <p/>
     * If there is no configuration with the specified name this will return {@code null}!
     * <p/>
     * When there are multiple configurations registered with the same name (by different plugins) it will return the first match. You should use {@link #isDuplicate(String)} before using this and if
     * there is a duplicate use {@link #getConfig(String, String)} If you are using this without user input just be safe and specify the name of the plugin to be sure.
     *
     * @param name The name of the configuration file.
     * @return {@link MessageConfig} instance for the specified name or {@code null} when there is no match.
     */
    public static MessageConfig getConfig(String name) {
        for (MessageConfig config : configs) {
            if (config.getName().equalsIgnoreCase(name)) {
                return config;
            }
        }
        return null;
    }

    /**
     * Get a {@link MessageConfig} instance for the specified name and plugin name.
     * <p/>
     * If there is no configuration registered by the specified plugin with the specified name it will return {@code null}!
     *
     * @param name The name of the configuration file.
     * @param plugin The name of the plugin that registered the configuration. (Needs to match {@link Plugin#getName()}
     * @return {@link MessageConfig} instance for the specified plugin/name or {@code null} when there is no match.
     */
    public static MessageConfig getConfig(String name, String plugin) {
        for (MessageConfig config : configs) {
            if (config.getName().equalsIgnoreCase(name) && config.getPlugin().getName().equalsIgnoreCase(plugin)) {
                return config;
            }
        }
        return null;
    }

    /**
     * Checks whether or not the specified config file name is a duplicate.
     * <p/>
     * Multiple plugins can register configurations with the same name.
     * When using {@link #getConfig(String)} it will return the first available match.
     * <p/>
     * It's recommended to first check for a duplicate using this method.
     * If there is a duplicate you should use {@link #getConfig(String, String)} to get a plugin specific config.
     *
     * @param name The name of the configuration file.
     * @return When there is more than one config with the specified name true otherwise false.
     */
    public static boolean isDuplicate(String name) {
        int count = 0;
        for (MessageConfig config : configs) {
            if (config.getName().equalsIgnoreCase(name)) {
                count++;
            }
        }
        return count > 1;
    }

    /**
     * <b>This gets called in the constructor and when changing languages!</b>
     * <p/>
     * Only call this when you want to force loadFull messages.
     * If you just want to update the config file call {@link #load(boolean)}
     * <p/>
     * Load the message properties file.
     * Defaults will be set from the loaded file out of the plugin jar.
     * <p/>
     * Changes will be loaded from a file with the _changes suffix.
     * This file must have a version number and a list of keys that have been changed/removed for each version. (see wiki for details)
     * <p/>
     * Changes will not be automatically updated/overwritten in the config file.
     * The changes config is stored separate and it has to be confirmed by the user.
     *
     * @return Whether or not the loading was successful.
     */
    public boolean loadFull() {
        return loadFull(gb.getLanguage().getID());
    }

    /**
     * @param lang The language to loadFull.
     *             You should just use {@link #loadFull()} to loadFull the user selected language.
     *             Only use this to force loadFull a different langauge.
     * @return Whether or not the loading was successful.
     * @see #loadFull()
     */
    public boolean loadFull(String lang) {
        File dir = new File(new File(plugin.getDataFolder(), "messages"), lang);
        dir.mkdirs();
        file = new File(dir, name+"_"+lang+".properties");

        boolean save = false;
        if (file.exists()) {
            //Load config file from disk
            config = new EProperties();
            try {
                config.load(new FileInputStream(file));
            } catch (Exception e) {
                gb.error("An error occurred trying to load the language file '"+file.getAbsolutePath()+"'!");
                gb.error(e.getMessage());
                config = null;
                return false;
            }

            //Add missing/new values
            EProperties defaults = getConfigFromJar(lang);
            for (Map.Entry<Object, Object> entry : defaults.entrySet()) {
                if (entry.getKey() instanceof String && entry.getValue() instanceof String && config.getProperty((String)entry.getKey()) == null) {
                    config.setProperty((String)entry.getKey(), (String)entry.getValue());
                    save = true;
                }
            }

        } else {
            //Load config file from jar
            config = getConfigFromJar(lang);
            if (config == null) {
                return false;
            }
            save = true;
        }

        //Get/set the version
        if (config.getProperty(name.toUpperCase()+VERSION_KEY) != null) {
            version = Parse.Int(config.getProperty(name.toUpperCase()+VERSION_KEY).trim());
        } else {
            config.setProperty(name.toUpperCase()+VERSION_KEY, Integer.toString(version));
            save = true;
        }

        //Load changes if there is a changes file.
        String source = "changelogs/"+lang+"/"+name+"_"+lang+".yml";
        InputStream in = plugin.getResource(source);
        if (in != null) {
            changelog = YamlConfiguration.loadConfiguration(in);
            targetVersion = changelog.getInt("version");
            if (targetVersion > version) {
                gb.warn("Message file "+name+"_"+lang+".yml is "+(targetVersion-version)+" version(s) behind.\n"+
                        "Please run /gameboxx messages for more details.");

            }
        }

        //Cache loaded messages.
        cacheMessages();

        //Save file when loading config from jar or when setting version.
        if (save) {
            return save();
        }
        return true;
    }

    /**
     * Used internally to copy the messages properties file from the jar in to memory.
     *
     * @param lang The language ID like 'en'
     * @return The {@link EProperties} config file.
     */
    private EProperties getConfigFromJar(String lang) {
        //Get the default file from the jar.
        String source = "messages/"+name+"_"+lang+".properties";
        InputStream in = plugin.getResource(source);
        if (in == null) {
            gb.warn("Language file '"+source+"' not found! Either a corrupt jar or "+plugin.getName()+" doesn't support the language '"+lang+"'!");
            return null;
        }

        //Load the config
        EProperties config = new EProperties();
        try {
            config.load(in);
            in.close();
            return config;
        } catch (IOException e) {
            gb.error("An error occurred trying to load the language file '"+source+"'!");
            gb.error(e.getMessage());
            return null;
        }
    }

    /**
     * Load the message properties file from disk in to memory.
     * <p/>
     * This does not load default values or anything.
     * It just loads the local file in to to memory.
     * <p/>
     * When there are errors the error will be logged and it will return false.
     *
     * @param loadFallback Whether or not to try and load the fallback messages values too. When true it will first load the fallback (english) messages and then load the current language messages.
     * All the fallback messages will be overwritten with the current language messages.
     * @return Whether or not the loading was successful
     */
    public boolean load(boolean loadFallback) {
        boolean success = false;
        if (loadFallback && fallback != null) {
            fallback.load(false);
        }
        if (config == null) {
            success = loadFull();
        } else {
            try {
                config.load(new FileInputStream(file));
                success = true;
            } catch (Exception e) {
                gb.error("An error occurred trying to load the language file '"+file.getAbsolutePath()+"'!");
                gb.error(e.getMessage());
            }
        }
        cacheMessages();
        return success;
    }

    /**
     * Save the message configuration file.
     * <p/>
     * You shouldn't really have to use this.
     * This is called when updating messages for example.
     * <p/>
     * When there are errors the error will be logged and it will return false.
     *
     * @return Whether or not the saving was successful.
     */
    public boolean save() {
        if (config != null) {
            try {
                config.store(new FileOutputStream(file), "");
                return true;
            } catch (Exception e) {
                gb.error("Failed to save the messages file '"+file.getAbsolutePath()+"'");
                gb.error(e.getMessage());
                return false;
            }
        }
        return false;
    }

    /**
     * Cache all the messages from this config in {@link Msg}
     * <p/>
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
        Properties config = getConfig();
        if (fallback) {
            config = this.fallback.getConfig();
        }
        if (config == null) {
            return;
        }

        for (Map.Entry<Object, Object> entry : config.entrySet()) {
            if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
                Msg.setMessage((String)entry.getKey(), (String)entry.getValue());
            }
        }
    }

    /**
     * Get the plugin that this message config belongs to.
     *
     * @return The plugin this config belongs to.
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Get the fallback config.
     * <p/>
     * If this is the registered config and the loaded language is not English this will return the English version of this config.
     * <p/>
     * It will be null when this version is already English.
     *
     * @return fallback {@link MessageConfig} for the English language or {@code null} when there is no fallback.
     */
    public MessageConfig getFallback() {
        return fallback;
    }

    /**
     * Get the {@link EProperties} file which is currently loaded with the messages.
     * <p/>
     * If the properties file is null it will try to get the fallback properties.
     *
     * @return properties file with messages. (May be {@code null}!)
     */
    public EProperties getConfig() {
        if (config == null && fallback != null) {
            return fallback.getConfig();
        }
        return config;
    }

    /**
     * Get the file where the current loaded properties file is saved/loaded from.
     *
     * @return The file for this config for the currently loaded language.
     */
    public File getFile() {
        return file;
    }

    /**
     * Get the name of the config which was provided in the constructor.
     *
     * @return The name of the configuration.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the current loaded language for this message configuration.
     *
     * @return The language of the current config.
     */
    public Language getLanguage() {
        return loadedLanguage;
    }

    /**
     * Get the changelog configuration file.
     * This file contains removed keys and changed keys for each version.
     * <p/>
     * Use the {@link #update(int, UpdateMode, boolean)} method to update the config.
     * Avoid doing it manually!
     *
     * @return The changelog configuration file for the current loaded language.
     */
    public YamlConfiguration getChangelog() {
        return changelog;
    }

    /**
     * Get the version of the current loaded config.
     *
     * @return The version of the loaded config.
     */
    public int getVersion() {
        return version;
    }

    /**
     * Get the target version of the current loaded version.
     * This is the latest version from the changelog.
     *
     * @return The target version of the loaded config from the changelog.
     */
    public int getTargetVersion() {
        return targetVersion;
    }

    /**
     * Check whether or not the current loaded config has changes pending from the changelog.
     *
     * @return True when the version is behind the target version otherwise false.
     */
    public boolean hasChanges() {
        return version < targetVersion;
    }

    /**
     * Used for {@link #getRemovedKeys()} and {@link #getChangedKeys()}
     *
     * @param type Either 'removed' or 'changed'
     */
    private Map<Integer, List<String>> getKeys(String type) {
        Map<Integer, List<String>> keys = new HashMap<>();
        if (! hasChanges()) {
            return keys;
        }
        for (int v = version+1; v <= targetVersion; v++) {
            final String key = v+"."+type;
            if (! changelog.contains(key)) {
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
     * <p/>
     * The key of the map is the version number where the message was removed.
     * The value is the list of message keys that have been removed in that version.
     *
     * @return map with message keys that have been removed. Will be empty when there are no keys removed.
     */
    public Map<Integer, List<String>> getRemovedKeys() {
        return getKeys("removed");
    }

    /**
     * Get a map with all the keys that have been changed in newer versions of the current loaded config.
     * <p/>
     * The key of the map is the version number where the message was changed.
     * The value is the list of message keys that have been changed in that version.
     *
     * @return map with message keys that have been changed. Will be empty when there are no keys changed.
     * @throws IllegalArgumentException when the specified version is too low or too high.
     */
    public Map<Integer, List<String>> getChangedKeys() {
        return getKeys("changed");
    }

    /**
     * Update the configuration file to the specified version.
     * Based on the {@link UpdateMode} it will either overwrite/remove values or skip them.
     * <p/>
     * The version of the config will be set to the specified version and the <b>file will be saved</b>.
     *
     * @param version The version to update to. This must be above the current version and not higher than the target version.
     * @param mode The {@link UpdateMode} to use for updating the messages.
     * @param keepOld When set to true old messages will remain in the config but they get prefixed with 'OLD-'
     * For example gameboxx.help would become 'gameboxx.OLD-help' and the updated message will be set for 'gameboxx.help'
     * @throws IllegalArgumentException when the specified version is too low or too high.
     */
    public void update(int version, UpdateMode mode, boolean keepOld) {
        if (version > targetVersion || version <= this.version) {
            throw new IllegalArgumentException("The version number must be above the current version and not higher than the target version.");
        }

        load(false);

        //Skip everything only bump version
        if (mode == UpdateMode.SKIP_ALL) {
            config.setProperty(name.toUpperCase()+VERSION_KEY, Integer.toString(version));
            save();
            return;
        }

        Map<Integer, List<String>> removedKeys = getRemovedKeys();
        Map<Integer, List<String>> changedKeys = getChangedKeys();

        EProperties latest = getConfigFromJar(getLanguage().getID());

        for (int v = version+1; v <= targetVersion; v++) {
            //Remove options
            if (removedKeys.containsKey(v) && (mode == UpdateMode.REMOVAL_ONLY || mode == UpdateMode.ALL)) {
                for (String key : removedKeys.get(v)) {
                    if (config.contains(key)) {
                        config.remove(key);
                    }
                }
            }
            //Change options
            if (changedKeys.containsKey(v) && (mode == UpdateMode.CHANGES_ONLY || mode == UpdateMode.ALL)) {
                for (String key : changedKeys.get(v)) {
                    if (config.contains(key)) {
                        if (keepOld) {
                            //When keeping old value rename the key by prefixing it with OLD-
                            config.setProperty("OLD-"+key, config.getProperty(key));
                        }
                        config.setProperty(key, latest.getProperty(key));
                    }
                }
            }
        }

        config.setProperty(name.toUpperCase()+VERSION_KEY, Integer.toString(version));
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
         * Only bumps the configuration version Won't overwrite messages that have been changed.
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
