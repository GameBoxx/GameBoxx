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

package info.gameboxx.gameboxx.commands.api;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.commands.api.data.*;
import info.gameboxx.gameboxx.commands.api.exception.CmdAlreadyRegisteredException;
import info.gameboxx.gameboxx.commands.api.parse.CmdParser;
import info.gameboxx.gameboxx.commands.api.parse.CmdUsageParser;
import info.gameboxx.gameboxx.commands.api.parse.SubCmdO;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.options.single.PlayerO;
import info.gameboxx.gameboxx.util.Str;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Base class for custom commands.
 * <p/>
 * Stores arguments, modifiers and flags for both {@link BaseCmd}s and {@link SubCmd}s.
 */
public abstract class Cmd extends BukkitCommand {

    private Plugin plugin;

    private List<SenderType> senderBlacklist = new ArrayList<>();
    private LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
    private Map<String, Modifier> modifiers = new HashMap<>();
    private Map<String, Flag> flags = new HashMap<>();

    /**
     * Construct a new command with the given name and aliases.
     *
     * @param name The name/label of the command.
     * @param aliases Aliases for the command.
     */
    public Cmd(String name, String... aliases) {
        super(name, "", "/<command>", Arrays.asList(aliases));
    }



    /**
     * Get the description of the command.
     *
     * @return The description set with {@link #desc(String)}
     */
    public String desc() {
        return getDescription();
    }

    /**
     * Set the description of the command.
     * <p/>
     * This description is displayed in the command help map and in the command help with the '-?' flag.
     *
     * @param description The description to set for the command.
     * @return command instance
     */
    public void desc(String description) {
        setDescription(description == null ? "" : description);
    }

    /**
     * Get the permission node required to execute the command.
     *
     * @return The permission set with {@link #perm(String)}
     */
    public String perm() {
        return getPermission();
    }

    /**
     * Set the permission node required to execute the command.
     *
     * @param permission The permission node to set for the command.
     */
    public void perm(String permission) {
        setPermission(permission == null ? "" : permission);
    }


    /**
     * Get the {@link Plugin} that registered this command.
     *
     * @return {@link Plugin} that registered this command.
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Get the {@link GameBoxx} instance.
     *
     * @return {@link GameBoxx} instance.
     */
    public GameBoxx getGB() {
        return GameBoxx.get();
    }



    /**
     * Called when the command is executed from {@link BukkitCommand}.
     * This method is implemented in the {@link BaseCmd}
     */
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return false;
    }


    /**
     * Used by the {@link CmdRegistration} to register the command.
     * <p/>
     * <b>Do not call this to register a command!</b>
     * <p/>
     * You'll have to use {@link CmdRegistration#register(Plugin, BaseCmd)} to register a command properly!
     * Also don't forget to call {@link CmdRegistration#unregister(Plugin)} in onDisable()
     *
     * @param plugin The plugin that registered the command.
     * @throws CmdAlreadyRegisteredException When the command is already registered.
     */
    public void register(Plugin plugin) throws CmdAlreadyRegisteredException {
        if (this.plugin != null) {
            throw new CmdAlreadyRegisteredException(plugin, this);
        }
        this.plugin = plugin;

        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            CommandMap commandMap = (CommandMap)f.get(Bukkit.getServer());

            commandMap.register(plugin.getName(), this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * Get the {@link BaseCmd} of this command.
     * <p/>
     * If this command is a base command it will return itself.
     * If it's a sub command it will get the base command from the parent.
     *
     * @return The {@link BaseCmd} of this command.
     */
    public BaseCmd getBaseCmd() {
        if (isBase()) {
            return (BaseCmd)this;
        }
        Cmd cmd = this;
        while (cmd.isSub()) {
            cmd = ((SubCmd)cmd).getParent();
        }
        return (BaseCmd)cmd;
    }

    /**
     * Check whether or not this command has sub commands.
     * <p/>
     * If it does you can get the sub commands with {@link #getSubCmds()}
     *
     * @return
     */
    public boolean hasSubCmds() {
        if (arguments.size() < 1) {
            return false;
        }
        for (Argument arg : arguments.values()) {
            if (arg.option() instanceof SubCmdO) {
                return true;
            }
        }
        return false;
    }

    /**
     * Try to get an array with sub commands that this command has.
     * <p/>
     * If this command doesn't have sub commands this will return {@code null}.
     * See {@link #hasSubCmds()}
     * <p/>
     * This does not get the entire sub command stack.
     * It will only try to get the sub commands from the command you call this method on.
     *
     * @return Array with {@link SubCmd}s (May be {@code null} when the command doesn't have sub commands)
     */
    public SubCmd[] getSubCmds() {
        for (Argument arg : arguments.values()) {
            if (arg.option() instanceof SubCmdO) {
                return ((SubCmdO)arg.option()).getSubCmds();
            }
        }
        return null;
    }

    /**
     * Check if this command is a {@link BaseCmd}
     *
     * @return true when this command is a base command.
     */
    public boolean isBase() {
        return this instanceof BaseCmd;
    }

    /**
     * Check if this command is a {@link SubCmd}
     *
     * @return true when this command is a sub command.
     */
    public boolean isSub() {
        return this instanceof SubCmd;
    }



    /**
     * Get a list of {@link SenderType}s that have been added to the blacklist.
     * <p/>
     * The types of senders in this list won't be able to execute the command.
     *
     * @return List with blacklisted {@link SenderType}s
     */
    public List<SenderType> getSenderBlacklist() {
        return senderBlacklist;
    }

    /**
     * Clear the list with blacklisted {@link SenderType}s.
     */
    public void clearSenderBlacklist() {
        senderBlacklist.clear();
    }

    /**
     * Add one or more {@link SenderType}s on the sender blacklist.
     * <p/>
     * The added sender types won't be able to execute the command.
     *
     * @param type One or more {@link SenderType}s to be blacklisted.
     */
    public void blacklistSenders(SenderType... type) {
        senderBlacklist.addAll(Arrays.asList(type));
    }



    /**
     * Register a regular command argument/parameter for this command.
     * <p/>
     * When adding multiple arguments the order you add them in determines the indexing of the argument!
     *
     * @param name The argument name/key used to identify the argument.
     *             This name must be used with the {@link CmdData} result to get the argument value.
     * @param requirement The requirement for this argument. (See {@link ArgRequirement} for more info)
     * @param option The {@link SingleOption} used for parsing the argument.
     *               This option determines the argument value and everything else.
     *               For example if it's a {@link PlayerO} the argument value must be a player and the result value would be a player.
     * @return The added {@link Argument}
     * @throws IllegalArgumentException if an argument with the specified name is already registered for this command
     *                                  or if the argument option is a sub command option and the command already has an sub command argument.
     */
    public Argument addArgument(String name, ArgRequirement requirement, SingleOption option) {
        Argument argument = new Argument(name, requirement, option);

        //TODO: Also check sub commands etc.
        if (arguments.containsKey(name.toLowerCase())) {
            throw new IllegalArgumentException("The command already has an argument with the name '" + name + "'!");
        }

        if (argument.option() instanceof SubCmdO) {
            for (Argument arg : arguments.values()) {
                if (arg.option() instanceof SubCmdO) {
                    throw new IllegalArgumentException("The command already has a sub command argument." +
                            "Commands can only have one sub command option for each set of arguments." +
                            "It is possible to specify another sub command argument within a sub command. [argument=" + argument.name() + "]");
                }
            }
        }

        arguments.put(name.toLowerCase(), argument);
        return argument;
    }

    /**
     * Get the map with all the registered arguments.
     *
     * @return Map with registered arguments.
     */
    public LinkedHashMap<String, Argument> getArguments() {
        return arguments;
    }



    /**
     * Register a command modifier for this command.
     * <p/>
     * A command modifier is an optional argument that can be added anywhere in the command.
     * The specified name must be used by the user to specify the modifier.
     * <p/>
     * For example if you register a modifier with the name player and the option is a {@link PlayerO}
     * The user would put player:{player} anywhere in the command to set the modifer value.
     *
     * @param name The modifier name/key used to identify the argument.
     *             This name must be used with the {@link CmdData} result to get the modifier value.
     *             This name is also what the user needs to use to specify the modifier. ({name}:{value})
     * @param option The {@link SingleOption} used for parsing the modifier.
     *               This option determines the modifier value and everything else.
     *               For example if it's a {@link PlayerO} the modifier value must be a player and the result value would be a player.
     *               This option can't be a sub command option!
     * @return The added {@link Modifier}
     * @throws IllegalArgumentException if a modifier with the specified name is already registered for this command or if the modifier has a sub command option.
     */
    public Modifier addModifier(String name, SingleOption option) {
        Modifier modifier = new Modifier(name, option);

        if (option instanceof SubCmdO) {
            throw new IllegalArgumentException("Modifiers can not be a sub command option! [modifier=" + name + "]");
        }
        //TODO: Check sub commands and such too.
        if (modifiers.containsKey(name.toLowerCase())) {
            throw new IllegalArgumentException("The command already has a modifier with the name '" + name + "'!");
        }

        modifiers.put(name.toLowerCase(), modifier);
        return modifier;
    }

    /**
     * Get the map with all the registered modifiers.
     *
     * @return Map with registered modifiers.
     */
    public Map<String, Modifier> getModifiers() {
        return modifiers;
    }



    /**
     * Register a command flag for this command.
     * <p/>
     * A command flag represents a boolean that can be added anywhere in the command.
     * The specified name must be used by the user to specify the flag.
     * <p/>
     * For example if the name is 'f' the user can specify '-f' anywhere in the command.
     * <p/>
     * You don't have to prefix the name with a '-'
     * If you do it will be removed so if you want to have the user put '--f' you have to set the name as '--f'
     *
     * @param name The flag name/key used to identify the flag.
     *             This name must be used with the {@link CmdData} result to check if the flag was specified.
     *             This name is also what the user needs to use to specify the flag. (-{name})
     *             The name '?' is reserved!
     * @return The added {@link Flag}
     * @throws IllegalArgumentException if a flag with the specified name is already registered for this command.
     */
    public Flag addFlag(String name) {
        if (name.startsWith("-")) {
            name = name.substring(1);
        }

        //TODO: Check sub commands and such too.
        Flag flag = new Flag(name);
        if (flags.containsKey(name.toLowerCase())) {
            throw new IllegalArgumentException("The command already has a flag with the name '-" + name + "'!");
        }

        flags.put(name.toLowerCase(), flag);
        return flag;
    }

    /**
     * Get the map with all the registered flags.
     * <p/>
     * The flag names do not have the '-' in front of it.
     *
     * @return Map with registered flags.
     */
    public Map<String, Flag> getFlags() {
        return flags;
    }



    /**
     * Get the usage string for the specified {@link CommandSender}
     * <p/>
     * The string is generated using the {@link CmdUsageParser}
     * <p/>
     * Can be used for both specific sub commands and base commands.
     * If it's a base command it will list all the sub command options.
     *
     * @param sender The {@link CommandSender} to generate the usage string for.
     * @return The usage string for the specified sender.
     */
    public String getUsage(CommandSender sender) {
        return new CmdUsageParser(this, sender).getString();
    }

    /**
     * Display a detailed help message to the specified {@link CommandSender}
     * <p/>
     * If the sender is a player or console it will display 'all' details.
     * If it's not it will only display the usage string.
     *
     * @param sender The {@link CommandSender} to send the message to.
     */
    public void showHelp(CommandSender sender, String label) {
        if ((!(sender instanceof Player) && sender instanceof Entity) || sender instanceof BlockCommandSender) {
            Msg.fromString(getUsage(sender)).send(sender);
            return;
        }
        String none = Msg.getString("command.none");
        String noDesc = Msg.getString("command.no-description");

        List<String> blacklisted = new ArrayList<>();
        for (SenderType type : getSenderBlacklist()) {
            blacklisted.add(Str.camelCase(type.toString()));
        }

        List<String> flagFormats = new ArrayList<>();
        for (Flag flag : flags.values()) {
            if (sender instanceof Player) {
                flagFormats.add(Msg.getString("command.flag-entry",
                        Param.P("name", flag.name()),
                        Param.P("description", flag.desc().isEmpty() ? noDesc : flag.desc()),
                        Param.P("permission", flag.perm().isEmpty() ? none : flag.perm())
                ));
            } else {
                flagFormats.add("&a&l-" + flag.name());
            }
        }

        List<String> modifierFormats = new ArrayList<>();
        for (Modifier mod : modifiers.values()) {
            if (sender instanceof Player) {
                modifierFormats.add(Msg.getString("command.modifier-entry",
                        Param.P("name", mod.name()),
                        Param.P("description", mod.desc().isEmpty() ? noDesc : mod.desc()),
                        Param.P("permission", mod.perm().isEmpty() ? none : mod.perm()),
                        Param.P("type", mod.option().getTypeName())
                ));
            } else {
                modifierFormats.add("&a" + mod.name() + ":&8[&a" + mod.option().getTypeName() + "&8]");
            }
        }

        String msg = Msg.getString("command.help",
                Param.P("label", label),
                Param.P("usage", sender instanceof ConsoleCommandSender ? getUsage(sender) : new CmdUsageParser(this, sender, true).getJSON()),
                Param.P("description", getDescription().isEmpty() ? noDesc : getDescription()),
                Param.P("permission", getPermission().isEmpty() ? none : getPermission()),
                Param.P("aliases", getAliases().isEmpty() ? none : Str.implode(getAliases())),
                Param.P("blacklisted", blacklisted.isEmpty() ? none : Str.implode(blacklisted)),
                Param.P("flags", flagFormats.isEmpty() ? none : Str.implode(flagFormats, " ")),
                Param.P("modifiers", modifierFormats.isEmpty() ? none : Str.implode(modifierFormats))
        );

        Msg.fromString(msg).send(sender);
    }



    /**
     * Called when the command is executed.
     * <p/>
     * The command will be parsed using the {@link CmdParser}.
     * If the parser fails this won't be executed and the sender will receive a proper error message.
     * <p/>
     * The {@link CmdParser} will generate {@link CmdData} which is provided in this method.
     * With the {@link CmdData} you can get arguments, modifiers, flags, the sender and the original input.
     * <p/>
     * If the command has a sub command argument and it's specified the {@link #onCommand(CmdData)} for that specific sub command will be executed instead.
     *
     * @param data the {@link CmdData} generated by the {@link CmdParser} containing all the parsed arguments, modifiers, flags etc.
     */
    abstract public void onCommand(CmdData data);



    /**
     * Represents a type of a command sender.
     * Mainly used for sender blacklist.
     */
    public enum SenderType {
        /** An online player. {@link Player} */
        PLAYER,
        /** The console. {@link ConsoleCommandSender} */
        CONSOLE,
        /** A command block. {@link BlockCommandSender} */
        CMD_BLOCK,
        /** Any entity. {@link Entity} */
        ENTITY;

        /**
         * Get the {@link SenderType} that matches with the specified {@link CommandSender}.
         *
         * @param sender The {@link CommandSender} to find a match with.
         * @return A matching {@link SenderType} or {@code null} when no match is found.
         */
        public static SenderType getType(CommandSender sender) {
            if (sender instanceof Player) {
                return PLAYER;
            } else if (sender instanceof ConsoleCommandSender) {
                return CONSOLE;
            } else if (sender instanceof BlockCommandSender) {
                return CMD_BLOCK;
            } else if (sender instanceof Entity) {
                return ENTITY;
            }
            return null;
        }
    }
}
