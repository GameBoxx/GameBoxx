package info.gameboxx.gameboxx;

import info.gameboxx.gameboxx.commands.Commands;
import info.gameboxx.gameboxx.config.PluginCfg;
import info.gameboxx.gameboxx.config.messages.MessageCfg;
import info.gameboxx.gameboxx.listeners.MainListener;
import info.gameboxx.gameboxx.menu.Menu;
import info.gameboxx.gameboxx.util.cuboid.Cuboid;
import info.gameboxx.gameboxx.util.cuboid.SelectionManager;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class GameBoxx extends JavaPlugin {

    private static GameBoxx instance;
    private Vault vault;
    private Economy economy;

    private SelectionManager sm;

    private PluginCfg cfg;
    private MessageCfg msgCfg;

    private Commands cmds;

    private final Logger log = Logger.getLogger("GameAPI");

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

        cfg = new PluginCfg("plugins/GameAPI/GameAPI.yml");
        msgCfg = new MessageCfg("plugins/GameAPI/Messages.yml");

        sm = new SelectionManager();
        cmds = new Commands(this);

        registerListeners();

        log("loaded successfully");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return cmds.onCommand(sender, cmd, label, args);
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


    public SelectionManager getSM() {
        return sm;
    }


    public PluginCfg getCfg() {
        return cfg;
    }

    public MessageCfg getMsgCfg() {
        return msgCfg;
    }

}
