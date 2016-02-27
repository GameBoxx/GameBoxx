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

package info.gameboxx.gameboxx;

import info.gameboxx.gameboxx.commands.*;
import info.gameboxx.gameboxx.config.PluginCfg;
import info.gameboxx.gameboxx.game.GameManager;
import info.gameboxx.gameboxx.listeners.MainListener;
import info.gameboxx.gameboxx.menu.Menu;
import info.gameboxx.gameboxx.messages.Language;
import info.gameboxx.gameboxx.messages.MessageConfig;
import info.gameboxx.gameboxx.nms.NMS;
import info.gameboxx.gameboxx.nms.NMSVersion;
import info.gameboxx.gameboxx.user.UserManager;
import info.gameboxx.gameboxx.util.Parse;
import info.gameboxx.gameboxx.util.cuboid.Cuboid;
import info.gameboxx.gameboxx.util.cuboid.SelectionManager;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class GameBoxx extends JavaPlugin {

    private static GameBoxx instance;
    private Vault vault;
    private Economy economy;

    private Language language = null;

    private UserManager um;
    private SelectionManager sm;
    private GameManager gm;

    private PluginCfg cfg;

    private final Logger log = Logger.getLogger("GameBoxx");

    @Override
    public void onDisable() {
        instance = null;
        log("disabled");
    }

    @Override
    public void onEnable() {
        instance = this;
        log.setParent(this.getLogger());

        if (!NMS.get().isCompatible()) {
            error("Failed to load GameBoxx because your server version isn't supported!");
            error("This version of GameBoxx supports the following server versions: " + Parse.Array(NMSVersion.values()));
            error("Your server version: " + NMS.get().getVersionString());
            getPluginLoader().disablePlugin(this);
            return;
        }

        if (!loadEconomy()) {
            warn("Failed to load Economy from Vault!");
            warn("Everything will work just fine except features that use economy like shops.");
        }

        cfg = new PluginCfg("plugins/GameBoxx/GameBoxx.yml");

        if (!setupLanguage()) {
            warn("Invalid language specified in the config. Falling back to " + language.getName() + " [" + language.getID() + "]!");
        } else {
            log("Using " + language.getName() + " [" + language.getID() + "] as language!");
        }
        loadMessages();

        um = new UserManager();
        sm = new SelectionManager();
        gm = new GameManager();

        registerCommands();
        registerListeners();

        log("loaded successfully");
    }

    private void registerCommands() {
        getCommand("gameboxx").setExecutor(new GameBoxxCmd(this));
        getCommand("play").setExecutor(new PlayCmd(this));
        getCommand("select").setExecutor(new SelectCmd(this));
        getCommand("arena").setExecutor(new ArenaCmd(this));
        getCommand("setup").setExecutor(new SetupCmd(this));
        getCommand("option").setExecutor(new OptionCmd(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new Menu.Events(), this);
        getServer().getPluginManager().registerEvents(sm.getListener(), this);
        getServer().getPluginManager().registerEvents(new MainListener(this), this);
    }

    private boolean setupLanguage() {
        language = Language.find(getCfg().language);
        if (language == null) {
            language = Language.ENGLISH;
            return false;
        }
        return true;
    }

    private void loadMessages() {
        new MessageConfig(this, "messages");
        new MessageConfig(this, "commands");
        new MessageConfig(this, "options");
        new MessageConfig(this, "parser");
    }

    private boolean loadEconomy() {
        Plugin vaultPlugin = getServer().getPluginManager().getPlugin("Vault");
        if (vaultPlugin != null) {
            vault = (Vault)vaultPlugin;
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }
        }
        if (economy == null) {
            return false;
        }
        return true;
    }

    public void log(Object msg) {
        log.info("[GameBoxx] " + msg.toString());
    }

    public void warn(Object msg) {
        log.warning("[GameBoxx] " + msg.toString());
    }

    public void error(Object msg) {
        log.severe("[GameBoxx] " + msg.toString());
    }

    public static GameBoxx get() {
        return instance;
    }

    public Vault getVault() {
        return vault;
    }

    public Economy getEco() {
        return economy;
    }

    /**
     * Get the language used for messages.
     * If there was an error loading language files it will use the fall back english.
     * @return active language used for messages.
     */
    public Language getLanguage() {
        if (language == null) {
            return Language.ENGLISH;
        }
        return language;
    }


    /**
     * Get the {@link SelectionManager} for getting {@link Cuboid} selections and such.
     * @return The {@link SelectionManager}
     */
    public SelectionManager getSM() {
        return sm;
    }

    /**
     * Get the {@link GameManager} for registering {@link info.gameboxx.gameboxx.game.Game}s and such.
     * @return The {@link GameManager}
     */
    public GameManager getGM() {
        return gm;
    }

    /**
     * Get the {@link UserManager} for manager {@link info.gameboxx.gameboxx.user.User}s
     * @return The {@link UserManager}
     */
    public UserManager getUM() {
        return um;
    }


    public PluginCfg getCfg() {
        return cfg;
    }

}
