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

import info.gameboxx.gameboxx.commands.api.Cmd;
import info.gameboxx.gameboxx.commands.api.SubCmd;
import info.gameboxx.gameboxx.commands.api.data.Argument;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.util.Str;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CmdUsageParser {

    /** If the command has this amount of sub commands it will display the simple description. */
    public static int MIN_SUB_COMMANDS = 5;
    /** If the command has more than this amount of sub commands it will only display sub commands up to this number. */
    public static int MAX_SUB_COMMANDS = 10;

    private List<String> subCmds = new ArrayList<>();

    private List<String> usage = new ArrayList<>();
    private List<String> JSON = new ArrayList<>();

    public CmdUsageParser(Cmd cmd, CommandSender sender, String label, String[] args) {
        cmd = CmdParser.getSub(cmd, args);

        usage.add("/" + label);
        JSON.add("/" + label);

        Collection<Argument> arguments = cmd.getAllArguments().values();
        for (Argument arg : arguments) {

            //Sub command
            if (arg.option() instanceof SubCmdO) {
                if (cmd instanceof SubCmd) {
                    //Display specific sub command.
                    SubCmd sub = (SubCmd)cmd;

                    List<String> permissions = new ArrayList<>();
                    if (!arg.perm().isEmpty()) {
                        permissions.add(arg.perm());
                    }
                    if (!sub.getPermission().isEmpty()) {
                        permissions.add(sub.getPermission());
                    }

                    usage.add(sub.getSubName());
                    JSON.add(Msg.getString("command.subcmd-sub-entry",
                            Param.P("name", sub.getSubName()),
                            Param.P("description", sub.getDescription().isEmpty() ? Msg.getString("command.no-description") : sub.getDescription()),
                            Param.P("permission", permissions.isEmpty() ? Msg.getString("command.none") : Str.implode(permissions)),
                            Param.P("type", arg.option().getTypeName()),
                            Param.P("aliases", sub.getAliases().isEmpty() ? Msg.getString("command.none") : Str.implode(sub.getAliases()))
                    ));
                } else {
                    //No sub command specified try to display all the options
                    SubCmd[] subCmds = cmd.getSubCmds();
                    List<String> subNames = new ArrayList<>();
                    for (SubCmd sub : subCmds) {
                        subNames.add(sub.getSubName());
                    }

                    String usageDisplay = Str.implode(subNames, "|", "|", 0, MIN_SUB_COMMANDS) + (subNames.size() > MIN_SUB_COMMANDS ? "|..." : "");
                    usage.add(arg.usage(sender).replace(arg.name(), usageDisplay));

                    List<String> subCmdFormats = new ArrayList<>();
                    if (subCmds.length <= MIN_SUB_COMMANDS) {
                        for (SubCmd sub : subCmds) {
                            subCmdFormats.add(Msg.getString("command.subcmd-general-entry-desc",
                                    Param.P("name", sub.getSubName()),
                                    Param.P("usage", sub.getUsage(sender, label)),
                                    Param.P("description", sub.getDescription().isEmpty() ? Msg.getString("command.no-description") : sub.getDescription()),
                                    Param.P("permission", sub.getPermission().isEmpty() ? Msg.getString("command.none") : sub.getPermission()),
                                    Param.P("aliases", sub.getAliases().isEmpty() ? Msg.getString("command.none") : Str.implode(sub.getAliases()))
                            ));
                        }
                    } else {
                        for (int i = 0; i < subCmds.length && i < MAX_SUB_COMMANDS; i++) {
                            subCmdFormats.add(Msg.getString("command.subcmd-general-entry-desc-simple",
                                    Param.P("name", subCmds[i].getSubName()),
                                    Param.P("usage", subCmds[i].getUsage(sender, label))
                            ));
                        }
                    }
                    if (subCmds.length >= MAX_SUB_COMMANDS) {
                        subCmdFormats.add(Msg.getString("command.subcmd-more", Param.P("amt", subCmds.length - MAX_SUB_COMMANDS)));
                    }

                    JSON.add(Msg.getString("command.subcmd-general-entry",
                            Param.P("name", arg.usage(sender).replace(arg.name(), usageDisplay)),
                            Param.P("description", arg.desc().isEmpty() ? Msg.getString("command.no-description") : arg.desc()),
                            Param.P("permission", arg.perm().isEmpty() ? Msg.getString("command.none") : arg.perm()),
                            Param.P("type", arg.option().getTypeName()),
                            Param.P("subcmds", Str.implode(subCmdFormats, "\n"))
                    ));
                }

                continue;
            }

            //Regular argument
            usage.add(arg.usage(sender));
            JSON.add(Msg.getString("command.argument-entry",
                    Param.P("name", arg.usage(sender)),
                    Param.P("description", arg.desc().isEmpty() ? Msg.getString("command.no-description") : arg.desc()),
                    Param.P("permission", arg.perm().isEmpty() ? Msg.getString("command.none") : arg.perm()),
                    Param.P("type", arg.option().getTypeName())
            ));
        }
    }


    public String getString() {
        return Str.implode(usage, " ");
    }

    public String getJSON() {
        return Str.implode(JSON, " ");
    }
}
