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

package info.gameboxx.gameboxx.commands;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.messages.*;
import info.gameboxx.gameboxx.options.single.ItemO;
import info.gameboxx.gameboxx.util.Str;
import info.gameboxx.gameboxx.util.entity.EntityParser;
import info.gameboxx.gameboxx.util.item.ItemParser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameBoxxCmd implements CommandExecutor {

    private final GameBoxx gb;

    public GameBoxxCmd(GameBoxx gb) {
        this.gb = gb;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
            Msg.get("gameboxx.help", Param.P("cmd", label)).send(sender);
            return true;
        }

        //Reload
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("gameboxx.cmd.reload")) {
                Msg.get("no-permission", Param.P("node", "gameboxx.cmd.reload")).send(sender);
                return true;
            }

            gb.getCfg().load();
            gb.setupLanguage();
            for (MessageConfig cfg : MessageConfig.getConfigs()) {
                cfg.loadFull();
            }

            Msg.get("gameboxx.reloaded", Param.P("type", "all")).send(sender);
            return true;
        }

        //Language
        if (args[0].equalsIgnoreCase("language") || args[0].equalsIgnoreCase("lang")) {
            if (!sender.hasPermission("gameboxx.cmd.language")) {
                Msg.get("no-permission", Param.P("node", "gameboxx.cmd.language")).send(sender);
                return true;
            }

            if (args.length < 2) {
                Msg.get("gameboxx.language.get", Param.P("language", gb.getLanguage().getName()), Param.P("cmd", label)).send(sender);
                return true;
            }

            Language lang = Language.find(args[1]);
            if (lang == null) {
                Msg.get("gameboxx.language.invalid", Param.P("input", args[1]), Param.P("languages", Str.implode(Language.getNames()))).send(sender);
                return true;
            }

            gb.getCfg().language = lang.getID();
            gb.getCfg().save();
            gb.setupLanguage();

            for (MessageConfig config : MessageConfig.getConfigs()) {
                config.loadFull();
            }

            Msg.get("gameboxx.language.set", Param.P("language", lang.getName())).send(sender);
            return true;
        }

        //Info
        if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("plugin") || args[0].equalsIgnoreCase("version")) {
            sender.sendMessage(Str.color("&8===== &4&lGameBoxx &8=====\n" +
                    "&6&lAuthors&8&l: &a" + Str.implode(gb.getDescription().getAuthors(), ", ", " & ") + "\n" +
                    "&6&lVersion&8&l: &a" + gb.getDescription().getVersion() + "\n" +
                    "&6&lSpigot URL&8&l: &9https://www.spigotmc.org/resources/{placeholder}\n" +
                    "&6&lWebsite URL&8&l: &9&lhttp://gameboxx.info"));
            return true;
        }


        //Summon
        if (args[0].equalsIgnoreCase("summon") || args[0].equalsIgnoreCase("spawnmob") || args[0].equalsIgnoreCase("entity")) {
            String entityString = Str.implode(args, " ", " ", 1, args.length);

            EntityParser parser = new EntityParser(entityString, sender, false);
            if (!parser.isValid()) {
                Msg.fromString(parser.getError()).send(sender);
            } else {
                sender.sendMessage("Spawned!");
            }
            return true;
        }

        //Item
        if (args[0].equalsIgnoreCase("item") || args[0].equalsIgnoreCase("i")) {
            if (!(sender instanceof Player)) {
                Msg.get("player-only").send(sender);
                return true;
            }
            Player player = (Player)sender;

            String itemString = Str.implode(args, " ", " ", 1, args.length);

            ItemO itemO = new ItemO();
            if (!itemO.parse(player, itemString)) {
                Msg.fromString(itemO.getError()).send(player);
                return true;
            }
            player.getInventory().addItem(itemO.getValue());
            player.sendMessage("Item given!");
            return true;
        }

        //Item
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 2) {
                sender.sendMessage("Specify a player.");
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage("Invalid player");
                return true;
            }

            String itemString = Str.implode(args, " ", " ", 2, args.length);

            ItemO itemO = new ItemO();
            if (!itemO.parse(player, itemString)) {
                Msg.fromString(itemO.getError()).send(sender);
                return true;
            }
            player.getInventory().addItem(itemO.getValue());
            sender.sendMessage("Item given!");
            return true;
        }

        if (args[0].equalsIgnoreCase("test")) {
            if (!(sender instanceof Player)) {
                Msg.get("player-only").send(sender);
                return true;
            }
            Player player = (Player)sender;

            ItemParser parser = new ItemParser(player.getInventory().getItemInMainHand());
            String itemStr = Str.replaceColor(parser.getString());
            player.sendMessage(itemStr);
            System.out.println(itemStr);
            return true;
        }

        Msg.get("gameboxx.help", Param.P("cmd", label)).send(sender);
        return true;
    }
}
