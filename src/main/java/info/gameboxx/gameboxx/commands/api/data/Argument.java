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

package info.gameboxx.gameboxx.commands.api.data;

import info.gameboxx.gameboxx.commands.api.Cmd;
import info.gameboxx.gameboxx.commands.api.CmdData;
import info.gameboxx.gameboxx.commands.api.parse.CmdParser;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.options.single.PlayerO;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * A regular command argument.
 * <p/>
 * Use {@link Cmd#addArgument(String, String, String, Requirement, SingleOption)} to add an argument to a command.
 */
public class Argument {

    /** Prefix for required arguments */
    public static final String REQUIRED_PREFIX = "{";
    /** Suffix for required arguments */
    public static final String REQUIRED_SUFFIX = "}";
    /** Prefix for optional arguments */
    public static final String OPTIONAL_PREFIX = "[";
    /** Suffix for optional arguments */
    public static final String OPTIONAL_SUFFIX = "]";
    /** Prefix for arguments required by the console */
    public static final String REQUIRED_CONSOLE_PREFIX = "<";
    /** Suffix for arguments required by the console */
    public static final String REQUIRED_CONSOLE_SUFFIX = ">";
    /** Prefix for arguments required by non players */
    public static final String REQUIRED_NON_PLAYER_PREFIX = "<";
    /** Suffix for arguments required by non players */
    public static final String REQUIRED_NON_PLAYER_SUFFIX = ">";

    private final String name;
    private String description;
    private String permission;
    private final Requirement requirement;
    private final SingleOption option;

    /**
     * Construct a new Argument.
     * <p/>
     * Use {@link Cmd#addArgument(String, String, String, Requirement, SingleOption)} to add an argument to a command.
     * Which means it shouldn't be needed to use this.
     *
     * @param name The argument name/key used to identify the argument.
     *             This name must be used with the {@link CmdData} result to get the argument value.
     * @param description Description that describes the argument used in the command help.
     * @param permission The permission node required to specify this argument.
     * @param requirement The requirement for this argument. (See {@link Requirement} for more info)
     * @param option The {@link SingleOption} used for parsing the argument.
     *               This option determines the argument value and everything else.
     *               For example if it's a {@link PlayerO} the argument value must be a player and the result value would be a player.
     */
    public Argument(String name, String description, String permission, Requirement requirement, SingleOption option) {
        this.name = name;
        this.description = description == null ? "" : description;
        this.permission = permission == null ? "" : permission;
        this.requirement = requirement;
        this.option = option;
    }


    /**
     * Get the name of the argument.
     *
     * @return The name of the argument.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the usage name of the argument.
     * <p/>
     * This will add a prefix/suffix to the argument based on the {@link Requirement}
     * <p/>
     * It's recommended to use {@link #getUsageName(CommandSender)} to get a more accurate usage string.
     *
     * @return The argument name with a prefix/suffix based on the requirement.
     */
    public String getUsageName() {
        if (requirement == Requirement.REQUIRED) {
            return REQUIRED_PREFIX + getName() + REQUIRED_SUFFIX;
        } else if (requirement == Requirement.OPTIONAL) {
            return OPTIONAL_PREFIX + getName() + OPTIONAL_SUFFIX;
        } else if (requirement == Requirement.REQUIRED_CONSOLE) {
            return REQUIRED_CONSOLE_PREFIX + getName() + REQUIRED_CONSOLE_SUFFIX;
        } else {
            return REQUIRED_NON_PLAYER_PREFIX + getName() + REQUIRED_NON_PLAYER_SUFFIX;
        }
    }

    /**
     * Get the usage name of the argument.
     * <p/>
     * This will add a prefix/suffix to the argument based on the {@link Requirement} for the specified {@link CommandSender}
     * @see {@link #isRequired(CommandSender)}
     *
     * @return The argument name with a prefix/suffix based on the requirement.
     */
    public String getUsageName(CommandSender sender) {
        if (isRequired(sender)) {
            return REQUIRED_PREFIX + getName() + REQUIRED_SUFFIX;
        } else {
            return OPTIONAL_PREFIX + getName() + OPTIONAL_SUFFIX;
        }
    }


    /**
     * Get the description of the argument.
     *
     * @return The description of the argument. (Empty string when no description)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the argument.
     * <p/>
     * This is mainly used when the command config file is loaded to override the default value.
     *
     * @param description The description to set. (Empty string for no description)
     */
    public void setDescription(String description) {
        this.description = description == null ? "" : description;
    }


    /**
     * Get the permission node that is required to specify this argument.
     *
     * @return The permission node required to specify the argument. (Empty string when no node)
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Set the permission node required to specify this argument.
     * <p/>
     * This is mainly used when the command config file is loaded to override the default value.
     *
     * @param permission The permission node to set. (Empty string for no permission)
     */
    public void setPermission(String permission) {
        this.permission = permission == null ? "" : permission;
    }


    /**
     * Get the {@link Requirement} for this argument.
     *
     * @return The {@link Requirement} for this argument.
     */
    public Requirement getRequirement() {
        return requirement;
    }

    /**
     * Check whether or not the argument is required for the the specified {@link CommandSender}
     * <p/>
     * If the requirement is {@link Requirement#REQUIRED} or if it's {@link Requirement#REQUIRED_CONSOLE} and the sender the console
     * or if it's {@link Requirement#REQUIRED_NON_PLAYER} and the sender isn't a player this will return true.
     *
     * @param sender The {@link CommandSender} to check the {@link Requirement} for.
     * @return True when required for the specified sender.
     */
    public boolean isRequired(CommandSender sender) {
        if (requirement == Requirement.REQUIRED || (requirement == Requirement.REQUIRED_CONSOLE && sender instanceof ConsoleCommandSender) ||
                (requirement == Requirement.REQUIRED_NON_PLAYER && !(sender instanceof Player))) {
            return true;
        }
        return false;
    }

    /**
     * Get the {@link SingleOption} for this argument.
     * <p/>
     * This option does not contain a value.
     * In the {@link CmdParser} a clone of this option will be created and then it will be parsed and added to the {@link CmdData}
     *
     * @return The {@link SingleOption} for this argument.
     */
    public SingleOption getOption() {
        return option;
    }


    /**
     * The requirement for an argument.
     * <p/>
     * If an argument is optional it can be left out in the user input.
     * If not, it must be specified in the command otherwise it will produce an error message.
     * <p/>
     * Use {@link #isRequired(CommandSender)} to check if the argument is required for the specified sender.
     */
    public enum Requirement {
        /** Any type of sender MUST specify the argument. */
        REQUIRED,
        /** Any type of sender MAY specify the argument but it's not required. */
        OPTIONAL,
        /** The console MUST specify the argument but any other sender MAY specify it. */
        REQUIRED_CONSOLE,
        /** The player MAY specify the argument but all the other senders MUST specify it. */
        REQUIRED_NON_PLAYER
    }
}
