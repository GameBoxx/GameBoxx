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

import info.gameboxx.gameboxx.commands.api.*;
import info.gameboxx.gameboxx.commands.api.data.Argument;
import info.gameboxx.gameboxx.commands.api.data.Flag;
import info.gameboxx.gameboxx.commands.api.data.Modifier;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.util.Str;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CmdParser {

    private List<String> argsToParse;
    private Cmd executor = null;

    private CmdData data = null;
    private String error = "";

    public CmdParser(BaseCmd baseCmd, CommandSender sender, String[] args) {
        //Combine quoted arguments
        String argStr = Str.implode(args, " ");
        argsToParse = Str.splitQuotes(argStr, ' ', true);

        //Remove quotes at start and end from arguments.
        for (int i = 0; i < argsToParse.size(); i++) {
            String arg = argsToParse.get(i);
            if (arg.startsWith("'") || arg.startsWith("\"") || arg.startsWith("\\\"")) {
                arg = arg.substring(1);
            }
            if (arg.endsWith("'") || arg.endsWith("\"") || arg.endsWith("\\\"")) {
                arg = arg.substring(0, arg.length()-1);
            }
            argsToParse.set(i, arg);
        }

        //Create CmdData and set base cmd as executor
        data = new CmdData(sender, args);
        executor = baseCmd;

        //Recursively parse the command and it's sub commands.
        parse(baseCmd, sender);

        //If there are arguments that aren't parsed give an error.
        if (error.isEmpty() && argsToParse.size() > 0) {
            error = Msg.getString("cmdparser.unknown-arg", Param.P("input", argsToParse.get(0)), Param.P("usage", baseCmd.getUsage(sender)), Param.P("cmd", baseCmd.getName()));
        }
    }

    private boolean parse(Cmd cmd, CommandSender sender) {
        //Check if sender is blacklisted.
        Cmd.SenderType senderType = Cmd.SenderType.getType(sender);
        if (senderType != null) {
            if (cmd.getSenderBlacklist().contains(senderType)) {
                error = Msg.getString("cmdparser.sender-blacklisted", Param.P("type", Msg.getString("cmdparser.sender-blacklist." + senderType.toString().toLowerCase())));
                return false;
            }
        }

        //Check command permission.
        if (!cmd.getPermission().isEmpty() && !sender.hasPermission(cmd.getPermission())) {
            error = Msg.getString("no-permission", Param.P("node", cmd.getPermission()));
            return false;
        }

        //Go through all the arguments from the user input.
        int index = 0;
        List<String> args = new ArrayList<>(argsToParse);
        for (String arg : args) {
            if (!argsToParse.contains(arg)) {
                continue;
            }

            //Parse - Flags
            if (arg.startsWith("-")) {
                String name = arg.substring(1).toLowerCase();
                if (cmd.getFlags().containsKey(name)) {
                    Flag flag = cmd.getFlags().get(name);
                    if (!flag.perm().isEmpty() && !sender.hasPermission(flag.perm())) {
                        error = Msg.getString("no-permission", Param.P("node", flag.perm()));
                        return false;
                    }

                    data.getFlags().add(name);
                    argsToParse.remove(arg);
                    continue;
                }
            }

            //Parse - Modifiers
            if (arg.contains(":")) {
                String[] split = arg.split(":");
                String name = split[0].toLowerCase();
                String value = "";
                if (split.length > 1) {
                    value = split[1];
                }

                if (cmd.getModifiers().containsKey(name)) {
                    Modifier mod = cmd.getModifiers().get(name);
                    if (!mod.perm().isEmpty() && !sender.hasPermission(mod.perm())) {
                        error = Msg.getString("no-permission", Param.P("node", mod.perm()));
                        return false;
                    }

                    SingleOption option = (SingleOption)cmd.getModifiers().get(name).option().clone();
                    if (!option.parse(sender, value)) {
                        error = option.getError();
                        return false;
                    }

                    data.getModifiers().put(name, option);
                    argsToParse.remove(arg);
                    continue;
                }
            }


            //Look for regular arguments that matches the current argument input.
            //Skip optional arguments if parsing failed for current argument input.
            while (true) {
                if (index >= cmd.getArguments().size()) {
                    break;
                }
                Argument argument = new ArrayList<>(cmd.getArguments().values()).get(index);
                SingleOption option = (SingleOption)argument.option().clone();
                index++;


                //Sub command argument.
                if (option instanceof SubCmdO) {
                    //Find a matching sub command for the input.
                    if (!option.parse(sender, arg)) {
                        if (!argument.required(sender)) {
                            continue;
                        }
                        error = option.getError();
                        return false;
                    }

                    //Permission check for subcmd argument. (Each sub command can have it's own permission too this is the general permission for specifying any sub cmd)
                    if (!argument.perm().isEmpty() && !sender.hasPermission(argument.perm())) {
                        error = Msg.getString("no-permission", Param.P("node", argument.perm()));
                        return false;
                    }

                    //Parse sub command as command.
                    data.getArgs().put(argument.name().toLowerCase(), option);
                    argsToParse.remove(arg);
                    if (!parse(((SubCmdO)option).getValue(), sender)) {
                        return false;
                    }
                    if (!(executor instanceof SubCmd)) {
                        executor = ((SubCmdO)option).getValue();
                    }
                    break;
                }


                //Regular option argument.
                if (!option.parse(sender, arg)) {
                    if (argument.required(sender)) {
                        error = option.getError();
                        return false;
                    }
                    continue;
                }

                //Permission check to specify the argument.
                if (!argument.perm().isEmpty() && !sender.hasPermission(argument.perm())) {
                    error = Msg.getString("no-permission", Param.P("node", argument.perm()));
                    return false;
                }

                argsToParse.remove(arg);
                data.getArgs().put(argument.name().toLowerCase(), option);
                break;
            }

        }

        for (Argument arg : cmd.getArguments().values()) {
            if (arg.required(sender) && !data.getArgs().containsKey(arg.name().toLowerCase())) {
                error = Msg.getString("cmdparser.missing-arg", Param.P("arg", arg.name()),
                        Param.P("desc", arg.desc().isEmpty() ? Msg.getString("cmdparser.no-desc") : arg.desc()),
                        Param.P("type", arg.option().getTypeName()), Param.P("usage", cmd.getUsage(sender)), Param.P("cmd", cmd.getName()));
                return false;
            }
        }

        return true;
    }

    public CmdData getData() {
        return data;
    }

    public boolean success() {
        return data != null && executor != null && error.isEmpty();
    }

    public String getError() {
        return error;
    }

    public Cmd getExecutor() {
        return executor;
    }
}
