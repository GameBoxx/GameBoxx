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
import info.gameboxx.gameboxx.commands.api.data.link.*;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.util.Str;
import org.bukkit.command.CommandSender;

import java.util.*;

public class CmdParser {

    private Cmd cmd = null;
    private CmdData cmdData = null;
    private String error = "";
    private int state = 0;

    List<String> argsList;

    public CmdParser(BaseCmd baseCmd, CommandSender sender, String label, String[] inputArgs) {
        //Combine quoted arguments
        String input = Str.implode(inputArgs, " ");
        argsList = Str.splitQuotes(input, ' ', true);
        for (int i = 0; i < argsList.size(); i++) {
            argsList.set(i, Str.removeQuotes(argsList.get(i)));
        }

        cmd = getSub(baseCmd, inputArgs);
        cmdData = new CmdData(sender, inputArgs);

        //Check basic command permission.
        if (cmd.isSub() && !cmd.getBaseCmd().perm().isEmpty() && !sender.hasPermission(cmd.getBaseCmd().perm())) {
            error = Msg.getString("no-permission", Param.P("node", cmd.getPermission()));
            return;
        }
        if (!cmd.perm().isEmpty() && !sender.hasPermission(cmd.perm())) {
            error = Msg.getString("no-permission", Param.P("node", cmd.getPermission()));
            return;
        }

        //Create ParseData with the data from the command.
        ParseData data = new ParseData();
        data.arguments = cmd.getAllArguments();
        data.modifiers = cmd.getAllModifiers();
        data.flags = cmd.getAllFlags();

        //First time parsing to get most data.
        state = 1;
        data = parse(sender, label, data);

        //Parse links
        List<Link> links = cmd.getAllLinks();
        for (Link link : links) {
            List<String> names = Arrays.asList(link.names());
            boolean specified = data.specifiedNames.containsAll(names);

            if (link instanceof RemoveLink) {
                if (specified) {
                    data.arguments.remove(((RemoveLink)link).removeName());
                    data.modifiers.remove(((RemoveLink)link).removeName());
                    data.flags.remove(((RemoveLink)link).removeName());
                }
            } else if (link instanceof RequirementLink) {
                if (specified) {
                    if (data.arguments.get(((RequirementLink)link).argName()) != null) {
                        data.arguments.get(((RequirementLink)link).argName()).requirement(((RequirementLink)link).requirement());
                    }
                }
            } else if (link instanceof BlacklistLink) {
                if (specified) {
                    cmd.clearSenderBlacklist();
                    cmd.blacklistSenders(((BlacklistLink)link).blacklist());
                }
            } else if (link instanceof ConflictLink || link instanceof ForceLink) {
                List<String> linkNames = new ArrayList<>();
                for (String name : link.names()) {
                    if (data.specifiedNames.contains(name)) {
                        linkNames.add(name);
                    }
                }
                if (link instanceof ConflictLink) {
                    if (linkNames.size() > 1) {
                        //TODO: set error
                        error = "ConflictLink Conflicting names have been specified!";
                        return;
                    }
                } else {
                    if (linkNames.size() > 0 && !specified) {
                        //TODO: set error
                        error = "ForceLink not all names have been specified!";
                        return;
                    }
                }
            }
        }

        //Check if sender is blacklisted.
        Cmd.SenderType senderType = Cmd.SenderType.getType(sender);
        if (senderType != null) {
            if (cmd.getSenderBlacklist().contains(senderType)) {
                error = Msg.getString("cmdparser.sender-blacklisted", Param.P("type", Msg.getString("cmdparser.sender-blacklist." + senderType.toString().toLowerCase())));
                return;
            }
        }

        //Parse again with all the links applied.
        state = 2;
        data = parse(sender, label, data);
        if (data == null) {
            error = "";
            return;
        }

        //Check if all the required arguments have been parsed.
        for (Argument arg : data.arguments.values()) {
            if (arg.required(sender) && !cmdData.getArgs().containsKey(arg.name().toLowerCase())) {
                if (arg.option() instanceof SubCmdO) {
                    cmd.showSubCmds(sender, label, cmdData.hasMod("page") ? (int)cmdData.getMod("page") : 1);
                    error = "";
                    return;
                }
                setError(Msg.getString("cmdparser.missing-arg", Param.P("arg", arg.name()),
                        Param.P("desc", arg.desc().isEmpty() ? Msg.getString("cmdparser.no-desc") : arg.desc()),
                        Param.P("type", arg.option().getTypeName()), Param.P("usage", cmd.getUsage(sender, label, inputArgs)), Param.P("cmd", cmd.getName())));
            }
        }

        //Check if all the user input has been parsed
        if (data.argsToParse.size() > 0) {
            setError(Msg.getString("cmdparser.unknown-arg", Param.P("input", data.argsToParse.get(0)), Param.P("usage", baseCmd.getUsage(sender, label, inputArgs)), Param.P("cmd", baseCmd.getName())));
        }
    }

    private ParseData parse(CommandSender sender, String label, ParseData data) {
        data.argsToParse = new ArrayList<>(argsList);

        //Go through all the arguments from the input.
        int index = 0;
        List<String> args = new ArrayList<>(data.argsToParse);
        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);

            //Parse - Flags
            if (arg.startsWith("-")) {
                String name = arg.substring(1).toLowerCase();
                if (data.flags.containsKey(name)) {
                    data.argsToParse.remove(arg);
                    data.specifiedNames.add(name);
                    Flag flag = data.flags.get(name);
                    if (!flag.perm().isEmpty() && !sender.hasPermission(flag.perm())) {
                        setError(Msg.getString("no-permission", Param.P("node", flag.perm())));
                        continue;
                    }
                    cmdData.getFlags().add(name);
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

                if (data.modifiers.containsKey(name)) {
                    data.argsToParse.remove(arg);
                    data.specifiedNames.add(name);
                    Modifier mod = data.modifiers.get(name);
                    if (!mod.perm().isEmpty() && !sender.hasPermission(mod.perm())) {
                        setError(Msg.getString("no-permission", Param.P("node", mod.perm())));
                        continue;
                    }

                    SingleOption option = (SingleOption)mod.option().clone();
                    if (!option.parse(sender, value)) {
                        setError(option.getError());
                        continue;
                    }

                    cmdData.getModifiers().put(name, option);
                    continue;
                }
            }


            //Look for regular arguments that matches the current argument input.
            //This loop is here for skippable arguments to parse the same input again with the next argument if the previous one was skipped.
            while (true) {
                if (index >= data.arguments.size()) {
                    break;
                }

                Argument argument = new ArrayList<>(data.arguments.values()).get(index);
                SingleOption option = (SingleOption)argument.option().clone();
                index++;

                //Argument span. (merge multiple arguments together)
                List<String> argStrings = new ArrayList<>();
                int span = argument.span();
                if (span > 1 || span == -1) {
                    if (span == -1) {
                        span = args.size()+1;
                    }
                    argStrings.add(arg);
                    while (argStrings.size() < span) {
                        i++;
                        if (i >= args.size()) {
                            break;
                        }
                        argStrings.add(args.get(i));
                    }
                    arg = Str.implode(argStrings, " ");
                }

                //parse the argument.
                if (!option.parse(sender, arg)) {
                    if (!argument.skippable() || argument.required(sender)) {
                        if (state == 2 && option instanceof SubCmdO) {
                            cmd.showSubCmds(sender, label, cmdData.hasMod("page") ? (int)cmdData.getMod("page") : 1);
                            return null;
                        }
                        setError(option.getError());
                        break;
                    }
                    //If parsing fails and the argument is a skippable optional argument ignore it and try parse the next one.
                    if (argStrings.size() > 0) {
                        i -= (argStrings.size() - 1);
                    }
                    continue;
                }

                //Permission check to specify the argument.
                if (!argument.perm().isEmpty() && !sender.hasPermission(argument.perm())) {
                    setError(Msg.getString("no-permission", Param.P("node", argument.perm())));
                    break;
                }

                if (argStrings.size() > 1) {
                    for (String a : argStrings) {
                        data.argsToParse.remove(a);
                    }
                } else {
                    data.argsToParse.remove(arg);
                }
                data.specifiedNames.add(argument.name().toLowerCase());
                cmdData.getArgs().put(argument.name().toLowerCase(), option);
                break;
            }

        }

        return data;
    }

    private void setError(String error) {
        if (this.error.isEmpty()) {
            this.error = error;
        }
    }

    public static Cmd getSub(Cmd cmd, String[] inputArgs) {
        //Sub command
        if (cmd.isSub()) {
            return cmd;
        }

        //Get sub commands and the argument index of it.
        SubCmd[] subCmds = null;
        int subCmdIndex = -1;
        List<Argument> args = new ArrayList<>(cmd.getArguments().values());
        for (int i = 0; i < args.size(); i++) {
            Argument arg = args.get(i);
            if (arg.option() instanceof SubCmdO) {
                subCmds = ((SubCmdO)arg.option()).getSubCmds();
                subCmdIndex = i;
                break;
            }
        }
        //No sub commands
        if (subCmds == null || subCmds.length < 1) {
            return cmd;
        }

        //Combine quoted arguments.
        String input = Str.implode(inputArgs, " ");
        List<String> argList = Str.splitQuotes(input, ' ', true);
        for (int i = 0; i < argList.size(); i++) {
            argList.set(i, Str.removeQuotes(argList.get(i)));
        }

        //Get all the matching sub commands.
        Map<Integer, SubCmd> matches = new HashMap<>();
        int index = 0;
        for (String arg : argList) {
            //Skip flags and modifiers for indexing.
            if (arg.startsWith("-") || arg.contains(":")) {
                continue;
            }
            //Try to get a match
            for (SubCmd sub : cmd.getSubCmds()) {
                if (sub.getSubName().equalsIgnoreCase(arg.toLowerCase()) || sub.getSubAliases().contains(arg.toLowerCase())) {
                    matches.put(index, sub);
                    break;
                }
            }
            index++;
        }

        //No matches
        if (matches.size() < 0) {
            return cmd;
        }

        //Get best match from matches
        matches = new TreeMap<>(matches);
        SubCmd backupMatch = null;
        for (Map.Entry<Integer, SubCmd> match : matches.entrySet()) {
            //Sub command index must be at least at the same index.
            if (match.getKey() < subCmdIndex) {
                backupMatch = match.getValue();
                continue;
            }
            //Return the sub command argument with the lowest index for best match.
            return match.getValue();
        }
        if (backupMatch != null) {
            return backupMatch;
        }

        //No match
        return cmd;
    }

    public CmdData getData() {
        return cmdData;
    }

    public boolean success() {
        return error.isEmpty();
    }

    public String getError() {
        return error;
    }

    public Cmd getCmd() {
        return cmd;
    }

    private class ParseData {
        public LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        public Map<String, Modifier> modifiers = new HashMap<>();
        public Map<String, Flag> flags = new HashMap<>();

        public List<String> argsToParse;
        public List<String> specifiedNames = new ArrayList<>();
    }
}
