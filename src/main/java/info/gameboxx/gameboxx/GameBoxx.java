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
import info.gameboxx.gameboxx.config.messages.MessageCfg;
import info.gameboxx.gameboxx.game.GameManager;
import info.gameboxx.gameboxx.listeners.MainListener;
import info.gameboxx.gameboxx.menu.Menu;
import info.gameboxx.gameboxx.user.UserManager;
import info.gameboxx.gameboxx.util.cuboid.Cuboid;
import info.gameboxx.gameboxx.util.cuboid.SelectionManager;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class GameBoxx extends JavaPlugin {

    private static GameBoxx instance;
    private Vault vault;
    private Economy economy;

    private UserManager um;
    private SelectionManager sm;
    private GameManager gm;

    private PluginCfg cfg;
    private MessageCfg msgCfg;

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

        Plugin vaultPlugin = getServer().getPluginManager().getPlugin("Vault");
        if (vaultPlugin != null) {
            vault = (Vault)vaultPlugin;
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }
        }
        if (economy == null) {
            log("Failed to load Economy from Vault. The plugin will still work fine but some features might not work!");
        }

        ConfigurationSerialization.registerClass(Cuboid.class);

        cfg = new PluginCfg("plugins/GameBoxx/GameBoxx.yml");
        msgCfg = new MessageCfg("plugins/GameBoxx/Messages.yml");

        um = new UserManager();
        sm = new SelectionManager();
        gm = new GameManager();

        getCommand("gameboxx").setExecutor(new GameBoxxCmd(this));
        getCommand("play").setExecutor(new PlayCmd(this));
        getCommand("select").setExecutor(new SelectCmd(this));
        getCommand("arena").setExecutor(new ArenaCmd(this));
        getCommand("setup").setExecutor(new SetupCmd(this));

        registerListeners();

        log("loaded successfully");
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new Menu.Events(), this);
        getServer().getPluginManager().registerEvents(sm.getListener(), this);
        getServer().getPluginManager().registerEvents(new MainListener(this), this);
    }

    public void log(Object msg) {
        log.info("[GameBoxx " + getDescription().getVersion() + "] " + msg.toString());
    }

    public void warn(Object msg) {
        log.warning("[GameBoxx " + getDescription().getVersion() + "] " + msg.toString());
    }

    public void error(Object msg) {
        log.warning("[GameBoxx " + getDescription().getVersion() + "] " + msg.toString());
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

    public MessageCfg getMsgCfg() {
        return msgCfg;
    }

}
