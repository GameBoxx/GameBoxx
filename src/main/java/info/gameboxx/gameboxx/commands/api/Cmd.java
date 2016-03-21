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
import info.gameboxx.gameboxx.commands.api.data.link.*;
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
    private List<Link> links = new ArrayList<>();

    /**
     * Construct a new command with the given name and aliases.
     *
     * @param name The name/label of the command.
     * @param aliases Aliases for the command.
     */
    public Cmd(String name, String... aliases) {
        super(name, "", "", Arrays.asList(aliases));
        setPermission("");
        setDescription("");
    }



    /**
     * Get the description of the command.
     *
     * @return The description set with {@link #desc(String)}
     */
    public String desc() {
        return getDescription() == null ? "" : getDescription();
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
        return getPermission() == null ? "" : getPermission();
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
        setUsage(getUsage(plugin.getServer().getConsoleSender(), getBaseCmd().getName()));

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
        } else {
            return ((SubCmd)this).getParent();
        }
    }

    /**
     * Check whether or not this command has sub commands.
     * <p/>
     * If it does you can get the sub commands with {@link #getSubCmds()}
     *
     * @return true when this command has sub commands.
     */
    public boolean hasSubCmds() {
        return getSubCmds() != null;
    }

    /**
     * Try to get an array with sub commands that this command has.
     * <p/>
     * If this command doesn't have sub commands this will return {@code null}.
     * See {@link #hasSubCmds()}
     *
     * @return Array with {@link SubCmd}s (May be {@code null} when the command doesn't have sub commands)
     */
    public SubCmd[] getSubCmds() {
        if (isSub() || arguments.size() < 1) {
            return null;
        }
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
     *                                  if the argument option is a sub command option and the command already has an sub command argument
     *                                  or if the argument option is a sub command option and the command is a sub command.
     */
    public Argument addArgument(String name, ArgRequirement requirement, SingleOption option) {
        Argument argument = new Argument(name, requirement, option);

        if (getAllArguments().containsKey(name.toLowerCase())) {
            throw new IllegalArgumentException("The command already has an argument with the name '" + name + "'!");
        }

        if (argument.option() instanceof SubCmdO) {
            if (isSub()) {
                throw new IllegalArgumentException("Sub commands can not have sub command arguments. [argument=" + name + "]");
            }
            for (Argument arg : arguments.values()) {
                if (arg.option() instanceof SubCmdO) {
                    throw new IllegalArgumentException("The command already has a sub command argument." +
                            "Commands can only have one sub command option. [argument=" + name + "]");
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
        LinkedHashMap<String, Argument> clone = new LinkedHashMap<>();
        for (Map.Entry<String, Argument> entry : arguments.entrySet()) {
            clone.put(entry.getKey(), entry.getValue().clone());
        }
        return clone;
    }

    /**
     * Get the map with all the registered arguments.
     * <p/>
     * If this command is a sub command this will include all the arguments from the parent.
     *
     * @return Map with registered arguments.
     */
    public LinkedHashMap<String, Argument> getAllArguments() {
        if (isBase()) {
            return getArguments();
        }
        LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>(getBaseCmd().getArguments());
        arguments.putAll(this.getArguments());
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

        if (getAllModifiers().containsKey(name.toLowerCase())) {
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
        return new HashMap<>(modifiers);
    }

    /**
     * Get a map with all the registered modifiers.
     * <p/>
     * If this command is a sub command this will include all the modifiers from the parent.
     *
     * @return Map with registered modifiers.
     */
    public Map<String, Modifier> getAllModifiers() {
        if (isBase()) {
            return new HashMap<>(modifiers);
        }
        Map<String, Modifier> modifiers = new LinkedHashMap<>(getBaseCmd().getModifiers());
        modifiers.putAll(this.modifiers);
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

        Flag flag = new Flag(name);
        if (getAllFlags().containsKey(name.toLowerCase())) {
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
        return new HashMap<>(flags);
    }

    /**
     * Get a map with all the registered flags.
     * <p/>
     * The flag names do not have the '-' in front of it.
     * <p/>
     * If this command is a sub command this will include all the flags from the parent.
     *
     * @return Map with registered flags.
     */
    public Map<String, Flag> getAllFlags() {
        if (isBase()) {
            return new HashMap<>(flags);
        }
        Map<String, Flag> flags = new LinkedHashMap<>(getBaseCmd().getFlags());
        flags.putAll(this.flags);
        return flags;
    }



    /**
     * Register a {@link} for this command.
     *
     * @see Link
     * @see #addRemoveLink(String, String...)
     * @see #addRequirementLink(String, ArgRequirement, String...)
     *
     * @param link The link instance to add.
     * @return The added {@link Link}
     */
    public Link addLink(Link link) {
        links.add(link);
        return link;
    }

    /**
     * Register a {@link RemoveLink} for this command.
     * <p/>
     * If all the specified names have been specified the removeName will be removed.
     * This can be the name of an argument, modifier or flag.
     *
     * @param removeName The name of the argument, modifier or flag that needs to be removed when the specified names have been specified.
     * @param names One or more names of arguments, modifiers or flags to check for.
     *              When all of these names have been specified the argument, modifier or flag with the removeName name will be removed/ignored.
     * @return The added {@link RemoveLink}
     */
    public RemoveLink addRemoveLink(String removeName, String... names) {
        return (RemoveLink)addLink(new RemoveLink(removeName, names));
    }

    /**
     * Register a {@link BlacklistLink} for this command.
     * <p/>
     * If all the specified names have been specified the blacklist of this command will be overwritten.
     * This can be the name of an argument, modifier or flag.
     *
     * @param newBlacklist New blacklist array with {@link SenderType}s that will be set when the specified names have been specified.
     * @param names One or more names of arguments, modifiers or flags to check for.
     *              When all of these names have been specified the blacklist of this command will be overwritten.
     * @return The added {@link BlacklistLink}
     */
    public BlacklistLink addBlacklistLink(SenderType[] newBlacklist, String... names) {
        return (BlacklistLink)addLink(new BlacklistLink(newBlacklist, names));
    }

    /**
     * Register a {@link RequirementLink} for this command.
     * <p/>
     * If all the specified names have been specified the modifyName requirement will be changed to the specified requirement.
     * The names can be the name of an argument, modifier or flag.
     * <p/>
     * The name of the argument to modify can not be a modifier or flag as those don't have requirements.
     *
     * @param argName The name of the argument that needs to be modified when the specified names have been specified.
     *                This must be an Argument and it can not be a modifier or a flag!
     * @param newRequirement The new {@link ArgRequirement}
     * @param names One or more names of arguments, modifiers or flags to check for.
     *              When all of these names have been specified the argument, modifier or flag with the removeName name will be removed/ignored.
     * @return The added {@link RequirementLink}
     */
    public RequirementLink addRequirementLink(String argName, ArgRequirement newRequirement, String... names) {
        return (RequirementLink)addLink(new RequirementLink(argName, newRequirement, names));
    }

    /**
     * Register a {@link ConflictLink} for this command.
     * <p/>
     * If one of the names is specified you can not specify another name from the specified names.
     * The names can be the name of an argument, modifier or flag.
     *
     * @param names One or more names of arguments, modifiers or flags to check for.
     *              If one of the names is specified you can not specify another name from the specified names.
     * @return The added {@link ConflictLink}
     */
    public ConflictLink addConflictLink(String... names) {
        return (ConflictLink)addLink(new ConflictLink(names));
    }

    /**
     * Register a {@link ForceLink} for this command.
     * <p/>
     * If one of the names is specified you must specify all the other names too.
     * The names can be the name of an argument, modifier or flag.
     *
     * @param names One or more names of arguments, modifiers or flags to check for.
     *              If one of the names is specified you must specify all the other names too.
     * @return The added {@link ForceLink}
     */
    public ForceLink addForceLink(String... names) {
        return (ForceLink)addLink(new ForceLink(names));
    }

    /**
     * Get the list with all the registered links.
     *
     * @return List with registered links.
     */
    public List<Link> getLinks() {
        return new ArrayList<>(links);
    }

    /**
     * Get a list with all the registered links.
     * <p/>
     * If this command is a sub command this will include all the links from the parent.
     *
     * @return List with registered links.
     */
    public List<Link> getAllLinks() {
        if (isBase()) {
            return new ArrayList<>(links);
        }
        List<Link> links = new ArrayList<>(getBaseCmd().getLinks());
        links.addAll(this.links);
        return links;
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
    public String getUsage(CommandSender sender, String label) {
        return new CmdUsageParser(this, sender, label, new String[0]).getString();
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
    public String getUsage(CommandSender sender, String label, String[] args) {
        return new CmdUsageParser(this, sender, label, args).getString();
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
        showHelp(sender, label, new String[0]);
    }

    /**
     * Display a detailed help message to the specified {@link CommandSender}
     * <p/>
     * If the sender is a player or console it will display 'all' details.
     * If it's not it will only display the usage string.
     *
     * @param sender The {@link CommandSender} to send the message to.
     */
    public void showHelp(CommandSender sender, String label, String[] args) {
        if ((!(sender instanceof Player) && sender instanceof Entity) || sender instanceof BlockCommandSender) {
            Msg.fromString(getUsage(sender, label, args)).send(sender);
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
                Param.P("cmd", getBaseCmd().getName()),
                Param.P("usage", sender instanceof ConsoleCommandSender ? getUsage(sender, label, args) : new CmdUsageParser(this, sender, label, args).getJSON()),
                Param.P("description", desc().isEmpty() ? noDesc : desc()),
                Param.P("permission", perm().isEmpty() ? none : perm()),
                Param.P("aliases", isSub() ? (((SubCmd)this).getSubAliases().isEmpty() ? none : Str.implode(((SubCmd)this).getSubAliases())) : (getAliases().isEmpty() ? none : Str.implode(getAliases()))),
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
     * If the command has a sub command argument and it's specified, the {@link #onCommand(CmdData)} for that specific sub command will be executed instead.
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
