package info.gameboxx.gameboxx.commands;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.GameMsg;
import info.gameboxx.gameboxx.util.ItemUtil;
import info.gameboxx.gameboxx.util.Str;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands {

    private GameBoxx plugin;

    public Commands(GameBoxx plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (label.equalsIgnoreCase("gameboxx") || label.equalsIgnoreCase("gamebox") || label.equalsIgnoreCase("gb")) {

            if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
                GameMsg.HELP.send(sender);
                return true;
            }

            //Reload
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("parkours.cmd.reload")) {
                    GameMsg.NO_PERMISSION.send(sender);
                    return true;
                }

                plugin.getCfg().load();
                plugin.getMsgCfg().load();

                GameMsg.RELOADED.send(sender);
                return true;
            }

            //Info
            if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("plugin") || args[0].equalsIgnoreCase("version")) {
                sender.sendMessage(Str.color("&8===== &4&lGameBoxx &8=====\n" +
                        "&6&lAuthor&8&l: &aRojoss\n" +
                        "&6&lVersion&8&l: &a" + plugin.getDescription().getVersion() + "\n" +
                        "&6&lSpigot URL&8&l: &9https://www.spigotmc.org/resources/{placeholder}\n" +
                        "&6&lWebsite URL&8&l: &9&lhttp://gameboxx.info"));
                return true;
            }

            //Wand
            if (args[0].equalsIgnoreCase("wand") || args[0].equalsIgnoreCase("w")) {
                if (!(sender instanceof Player)) {
                    GameMsg.PLAYER_COMMAND.send(sender);
                    return true;
                }
                if (!sender.hasPermission("gameboxx.cmd.wand")) {
                    GameMsg.NO_PERMISSION.send(sender);
                    return true;
                }

                ItemUtil.add(((Player)sender).getInventory(), plugin.getSM().getWand(), true, true);
                return true;
            }

            GameMsg.HELP.send(sender);
            return true;
        }
        return false;
    }

}
