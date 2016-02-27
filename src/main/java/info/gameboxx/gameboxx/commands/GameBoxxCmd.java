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
import info.gameboxx.gameboxx.messages.MessageConfig;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.util.Str;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
            for (MessageConfig cfg : MessageConfig.getConfigs()) {
                cfg.load(true);
            }

            Msg.get("gameboxx.reloaded", Param.P("type", "all")).send(sender);
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

        Msg.get("gameboxx.help", Param.P("cmd", label)).send(sender);
        return true;
    }
}
