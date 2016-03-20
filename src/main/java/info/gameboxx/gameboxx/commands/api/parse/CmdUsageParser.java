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

package info.gameboxx.gameboxx.commands.api.parse;

import info.gameboxx.gameboxx.commands.api.BaseCmd;
import info.gameboxx.gameboxx.commands.api.Cmd;
import info.gameboxx.gameboxx.commands.api.SubCmd;
import info.gameboxx.gameboxx.commands.api.data.Argument;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.util.Str;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CmdUsageParser {

    private Cmd cmd;
    private List<String> subCmds = new ArrayList<>();
    private BaseCmd baseCmd = null;
    private List<String> usage = new ArrayList<>();

    private boolean JSONFormat = false;
    private List<String> JSON = new ArrayList<>();

    public CmdUsageParser(Cmd cmd, CommandSender sender) {
        this(cmd, sender, false);
    }

    public CmdUsageParser(Cmd cmd, CommandSender sender, boolean JSONFormat) {
        this.cmd = cmd;
        this.JSONFormat = JSONFormat;

        if (cmd instanceof BaseCmd) {
            baseCmd = (BaseCmd)cmd;
        } else {
            do {
                subCmds.add(((SubCmd)cmd).getSubName());
                cmd = ((SubCmd)cmd).getParent();
            } while (cmd instanceof SubCmd && ((SubCmd)cmd).getParent() instanceof SubCmd && ((SubCmd)cmd).getParent() != null);
            if (cmd instanceof BaseCmd) {
                baseCmd = (BaseCmd)cmd;
            } else {
                baseCmd = (BaseCmd)((SubCmd)cmd).getParent();
            }
        }

        usage.add("/" + baseCmd.getName());
        if (JSONFormat) {
            JSON.add("/" + baseCmd.getName());
        }
        generate(baseCmd, sender);
    }

    private void generate(Cmd cmd, CommandSender sender) {
        for (Argument arg : cmd.getArguments().values()) {
            if (arg.option() instanceof SubCmdO) {

                List<String> subNames = new ArrayList<>();
                boolean match = false;

                for (SubCmd sub : ((SubCmdO)arg.option()).getSubCmds()) {
                    subNames.add(sub.getSubName());
                    if (subCmds.contains(sub.getSubName())) {
                        usage.add(sub.getSubName());
                        if (JSONFormat) {
                            List<String> permissions = new ArrayList<>();
                            if (!arg.perm().isEmpty()) {
                                permissions.add(arg.perm());
                            }
                            if (!sub.getPermission().isEmpty()) {
                                permissions.add(sub.getPermission());
                            }

                            JSON.add(Msg.getString("command.subcmd-sub-entry",
                                    Param.P("name", sub.getSubName()),
                                    Param.P("description", sub.getDescription().isEmpty() ? Msg.getString("command.no-description") : sub.getDescription()),
                                    Param.P("permission", permissions.isEmpty() ? Msg.getString("command.none") : Str.implode(permissions)),
                                    Param.P("type", arg.option().getTypeName()),
                                    Param.P("aliases", sub.getAliases().isEmpty() ? Msg.getString("command.none") : Str.implode(sub.getAliases()))
                            ));
                        }

                        generate(sub, sender);
                        match = true;
                        break;
                    }
                }

                if (!match) {
                    usage.add(arg.usage(sender).replace(arg.name(), Str.implode(subNames, "|")));
                    if (JSONFormat) {
                        List<String> subCmdFormats = new ArrayList<>();
                        for (SubCmd sub : ((SubCmdO)arg.option()).getSubCmds()) {
                            subCmdFormats.add(Msg.getString("command.subcmd-general-entry-desc",
                                    Param.P("name", sub.getSubName()),
                                    Param.P("usage", sub.getUsage(sender)),
                                    Param.P("description", sub.getDescription().isEmpty() ? Msg.getString("command.no-description") : sub.getDescription()),
                                    Param.P("permission", sub.getPermission().isEmpty() ? Msg.getString("command.none") : sub.getPermission()),
                                    Param.P("aliases", sub.getAliases().isEmpty() ? Msg.getString("command.none") : Str.implode(sub.getAliases()))
                            ));
                        }

                        JSON.add(Msg.getString("command.subcmd-general-entry",
                                Param.P("name", arg.usage(sender).replace(arg.name(), Str.implode(subNames, "|"))),
                                Param.P("description", arg.desc().isEmpty() ? Msg.getString("command.no-description") : arg.desc()),
                                Param.P("permission", arg.perm().isEmpty() ? Msg.getString("command.none") : arg.perm()),
                                Param.P("type", arg.option().getTypeName()),
                                Param.P("subcmds", Str.implode(subCmdFormats, "\n"))
                        ));
                    }
                }
            } else {
                usage.add(arg.usage(sender));
                if (JSONFormat) {
                    JSON.add(Msg.getString("command.argument-entry",
                            Param.P("name", arg.usage(sender)),
                            Param.P("description", arg.desc().isEmpty() ? Msg.getString("command.no-description") : arg.desc()),
                            Param.P("permission", arg.perm().isEmpty() ? Msg.getString("command.none") : arg.perm()),
                            Param.P("type", arg.option().getTypeName())
                    ));
                }
            }
        }
    }



    public String getString() {
        return Str.implode(usage, " ");
    }

    public String getJSON() {
        return Str.implode(JSON, " ");
    }

    public Cmd getCmd() {
        return cmd;
    }

    public BaseCmd getBaseCmd() {
        return baseCmd;
    }
}
