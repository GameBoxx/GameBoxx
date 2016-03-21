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
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * A regular command argument.
 * <p/>
 * Use {@link Cmd#addArgument(String, ArgRequirement, SingleOption)} to add an argument to a command.
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
    private final ArgRequirement requirement;
    private final SingleOption option;

    private int span = 1;

    private String description;
    private String permission;

    /** Construct a new command Argument. */
    public Argument(String name, ArgRequirement requirement, SingleOption option) {
        this.name = name;
        this.requirement = requirement;
        this.option = option;
    }


    /**
     * Get the name of the argument.
     *
     * @return The name of the argument.
     */
    public String name() {
        return name;
    }

    /**
     * Get the usage name of the argument.
     * <p/>
     * This will add a prefix/suffix to the argument based on the {@link ArgRequirement}
     * <p/>
     * It's recommended to use {@link #usage(CommandSender)} to get a more accurate usage string.
     *
     * @return The argument name with a prefix/suffix based on the requirement.
     */
    public String usage() {
        if (requirement == ArgRequirement.REQUIRED) {
            return REQUIRED_PREFIX + name() + REQUIRED_SUFFIX;
        } else if (requirement == ArgRequirement.OPTIONAL) {
            return OPTIONAL_PREFIX + name() + OPTIONAL_SUFFIX;
        } else if (requirement == ArgRequirement.REQUIRED_CONSOLE) {
            return REQUIRED_CONSOLE_PREFIX + name() + REQUIRED_CONSOLE_SUFFIX;
        } else {
            return REQUIRED_NON_PLAYER_PREFIX + name() + REQUIRED_NON_PLAYER_SUFFIX;
        }
    }

    /**
     * Get the usage name of the argument.
     * <p/>
     * This will add a prefix/suffix to the argument based on the {@link ArgRequirement} for the specified {@link CommandSender}
     * @see {@link #required(CommandSender)}
     *
     * @return The argument name with a prefix/suffix based on the requirement.
     */
    public String usage(CommandSender sender) {
        if (required(sender)) {
            return REQUIRED_PREFIX + name() + REQUIRED_SUFFIX;
        } else {
            return OPTIONAL_PREFIX + name() + OPTIONAL_SUFFIX;
        }
    }


    /**
     * Get the description of the argument.
     *
     * @return The description of the argument. (Empty string when no description)
     */
    public String desc() {
        return description;
    }

    /**
     * Set the description of the argument.
     *
     * @param description The description to set. (Empty string for no description)
     * @return argument instance
     */
    public Argument desc(String description) {
        this.description = description == null ? "" : description;
        return this;
    }


    /**
     * Get the permission node that is required to specify this argument.
     *
     * @return The permission node required to specify the argument. (Empty string when no node)
     */
    public String perm() {
        return permission;
    }

    /**
     * Set the permission node required to specify this argument.
     * <p/>
     * This is mainly used when the command config file is loaded to override the default value.
     *
     * @param permission The permission node to set. (Empty string for no permission)
     */
    public Argument perm(String permission) {
        this.permission = permission == null ? "" : permission;
        return this;
    }


    /**
     * Get the {@link ArgRequirement} for this argument.
     *
     * @return The {@link ArgRequirement} for this argument.
     */
    public ArgRequirement requirement() {
        return requirement;
    }

    /**
     * Check whether or not the argument is required for the the specified {@link CommandSender}
     * <p/>
     * If the requirement is {@link ArgRequirement#REQUIRED} or if it's {@link ArgRequirement#REQUIRED_CONSOLE} and the sender the console
     * or if it's {@link ArgRequirement#REQUIRED_NON_PLAYER} and the sender isn't a player this will return true.
     *
     * @param sender The {@link CommandSender} to check the {@link ArgRequirement} for.
     * @return True when required for the specified sender.
     */
    public boolean required(CommandSender sender) {
        if (requirement == ArgRequirement.REQUIRED || (requirement == ArgRequirement.REQUIRED_CONSOLE && sender instanceof ConsoleCommandSender) ||
                (requirement == ArgRequirement.REQUIRED_NON_PLAYER && !(sender instanceof Player))) {
            return true;
        }
        return false;
    }


    /**
     * Get the argument span for this argument.
     * 
     * @see #span(int)
     * @return The amount of span.
     */
    public int span() {
        return span;
    }

    /**
     * Set the argument span for this argument.
     * <p/>
     * By default arguments have a span of one.
     * When the span is greater than 1 it will try to combine multiple arguments in one.
     * <p/>
     * If the span is -1 it can have a infinite amount of arguments.
     * However, this can obviously only be used for the last argument.
     * There are no internal checks for this but it just won't span if you do it wrong.
     *
     * @param span The amount of span.
     * @return argument instance
     */
    public Argument span(int span) {
        this.span = span;
        return this;
    }


    /**
     * Get the {@link SingleOption} for this argument.
     * <p/>
     * This option does not contain a value.
     * In the {@link CmdParser} a clone of this option will be created and then it will be parsed and added to the {@link CmdData}
     *
     * @return The {@link SingleOption} for this argument.
     */
    public SingleOption option() {
        return option;
    }
}
