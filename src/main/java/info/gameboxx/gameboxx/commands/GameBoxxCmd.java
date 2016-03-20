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
import info.gameboxx.gameboxx.commands.api.*;
import info.gameboxx.gameboxx.commands.api.data.ArgRequirement;
import info.gameboxx.gameboxx.commands.api.parse.SubCmdO;
import info.gameboxx.gameboxx.messages.*;
import info.gameboxx.gameboxx.options.single.StringO;
import info.gameboxx.gameboxx.util.Str;

import java.io.File;
import java.util.Set;

public class GameBoxxCmd extends BaseCmd {

    public GameBoxxCmd(File file) {
        super("gameboxx", new String[] {"gamebox", "gb", "gboxx", "gbox"}, "Main GameBoxx command.", file);

        addArgument("action", ArgRequirement.REQUIRED, new SubCmdO(new Info(this), new Reload(this), new Lang(this))).desc("A sub command.");
    }

    @Override
    public void onCommand(CmdData data) {}


    public class Info extends SubCmd<GameBoxxCmd> {
        private Info(GameBoxxCmd gameBoxxCmd) {
            super(gameBoxxCmd, "info", new String[] {"plugin", "details", "detail"}, "Display plugin information.");
        }

        @Override
        public void onCommand(CmdData data) {
            data.getSender().sendMessage(Str.color("&8===== &4&lGameBoxx &8=====\n" +
                    "&6&lAuthors&8&l: &a" + Str.implode(getGB().getDescription().getAuthors(), ", ", " & ") + "\n" +
                    "&6&lVersion&8&l: &a" + getGB().getDescription().getVersion() + "\n" +
                    "&6&lSpigot URL&8&l: &9https://www.spigotmc.org/resources/{placeholder}\n" +
                    "&6&lWebsite URL&8&l: &9&lhttp://gameboxx.info"));
        }
    }


    public class Reload extends SubCmd<GameBoxxCmd> {
        private Reload(GameBoxxCmd gameBoxxCmd) {
            super(gameBoxxCmd, "reload", new String[] {"load"}, "Reload all the configuration files including messages and such.", "gameboxx.cmd.reload");
        }

        @Override
        public void onCommand(CmdData data) {
            getGB().getCfg().load();

            getGB().setupLanguage();
            for (MessageConfig cfg : MessageConfig.getConfigs()) {
                cfg.loadFull();
            }

            Set<BaseCmd> cmds = CmdRegistration.getCommands(GameBoxx.class);
            if (cmds != null) {
                for (BaseCmd cmd : cmds) {
                    cmd.load();
                }
            }

            Msg.get("gameboxx.reloaded", Param.P("type", "all")).send(data.getSender());
        }
    }


    public class Lang extends SubCmd<GameBoxxCmd> {
        private Lang(GameBoxxCmd gameBoxxCmd) {
            super(gameBoxxCmd, "language", new String[] {"lang", "locale"}, "Get or set the language used for messages.", "gameboxx.cmd.language");

            addArgument("language", ArgRequirement.OPTIONAL, new StringO().match(Language.getAliases())).desc("The language name to set.").perm("gameboxx.cmd.language.set");
        }

        @Override
        public void onCommand(CmdData data) {
            if (data.hasArg("language")) {
                Language lang = Language.find((String)data.getArg("language"));

                getGB().getCfg().language = lang.getID();
                getGB().getCfg().save();
                getGB().setupLanguage();

                for (MessageConfig config : MessageConfig.getConfigs()) {
                    config.loadFull();
                }

                Msg.get("gameboxx.language.set", Param.P("language", lang.getName())).send(data.getSender());
                return;
            }

            Msg.get("gameboxx.language.get", Param.P("language", getGB().getLanguage().getName())).send(data.getSender());
        }
    }
}
