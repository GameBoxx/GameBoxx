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

import info.gameboxx.gameboxx.commands.api.data.Argument;
import info.gameboxx.gameboxx.commands.api.data.TabCompleteData;
import info.gameboxx.gameboxx.commands.api.parse.CmdParser;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.options.single.PlayerO;
import info.gameboxx.gameboxx.options.single.StringO;
import info.gameboxx.gameboxx.util.Pair;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Holds all the command data values like arguments, modifiers, flags, the command sender etc.
 * <p/>
 * Used by the {@link CmdParser} to store all the data parsed from the user input.
 */
public class CmdData {

    private CommandSender sender;
    private final String[] input;

    private Map<String, SingleOption> arguments = new HashMap<>();
    private Map<String, SingleOption> modifiers = new HashMap<>();
    private List<String> flags = new ArrayList<>();

    private Map<Integer, TabCompleteData> dataValues = new HashMap();

    /**
     * Construct new CmdData.
     *
     * @param sender The {@link CommandSender} who executed the command.
     * @param input The original user input arguments.
     */
    public CmdData(CommandSender sender, String[] input) {
        this.sender = sender;
        this.input = input;
    }


    /**
     * Get the original user input argument array.
     * <p/>
     * In most cases you'll want to use {@link #getArg(String)} and such.
     * This array of arguments may contain flags, modifiers and invalid data.
     *
     * @return The original user input argument array.
     */
    public String[] getInput() {
        return input;
    }



    /**
     * Get a map with all the arguments that have been parsed.
     * <p/>
     * This map will always contain all the required arguments for the sender.
     * And it may contain optional arguments. {@link #hasArg(String)}
     * <p/>
     * Use {@link #getArg(String)} to get the argument value for the argument as an Object.
     *
     * @return Map with all the parsed arguments. The key is the name of the argument and the value is the {@link SingleOption} containing the value.
     */
    public Map<String, SingleOption> getArgs() {
        return arguments;
    }

    /**
     * Check if the map contains the specified argument.
     * <p/>
     * This only has to be used for optional arguments for the sender.
     * For example if you have an argument that is {@link Argument.Requirement#REQUIRED_CONSOLE} and the sender is the console it will always have the argument.
     *
     * @param argument The name/key of the argument to check.
     * @return True when there is an value for the specified argument.
     */
    public boolean hasArg(String argument) {
        return arguments.containsKey(argument);
    }

    /**
     * Get the {@link SingleOption} for the specified argument.
     * <p/>
     * The option will contain the value of the argument from the user input.
     * In most cases you'll want to use {@link #getArg(String)} to just get the value of the option.
     * <p/>
     * If the argument is optional for the sender this may return {@code null}.
     * Use {@link #hasArg(String)} for optional arguments to check if it's been specified by the user.
     * <p/>
     * It's safe to directly cast this {@link SingleOption} to the option type of the argument you want to retrieve.
     * It must match with the argument you set with {@link Cmd#addArgument(String, String, String, Argument.Requirement, SingleOption)}
     *
     * @param argument The name/key of the argument to get the option from.
     * @return {@link SingleOption} containing the argument value. ({@code null} when there is no argument parsed with the specified name)
     */
    public SingleOption getArgOption(String argument) {
        return arguments.get(argument);
    }

    /**
     * Get the value as an {@link Object} for the specified argument.
     * <p/>
     * If the argument is optional for the sender this may return {@code null}.
     * Use {@link #hasArg(String)} for optional arguments to check if it's been specified by the user.
     * <p/>
     * It's safe to directly cast this {@link Object} to the type of the argument you want to retrieve.
     * It must match with the argument you set with {@link Cmd#addArgument(String, String, String, Argument.Requirement, SingleOption)}
     * <p/>
     * For example if the argument is a {@link StringO} you can cast this value to a {@link String}
     *
     * @param argument The name/key of the argument to get the option from.
     * @return Object with the argument value. ({@code null} when there is no argument parsed with the specified name)
     */
    public Object getArg(String argument) {
        return arguments.get(argument) == null ? null : arguments.get(argument).getValue();
    }



    /**
     * Get a map with all the modifiers that have been parsed.
     * <p/>
     * Use {@link #getMod(String)} to get the modifier value for the modifier as an Object.
     *
     * @return Map with all the parsed modifiers. The key is the name of the modifier and the value is the {@link SingleOption} containing the value.
     */
    public Map<String, SingleOption> getModifiers() {
        return modifiers;
    }

    /**
     * Check if the map contains the specified modifier.
     *
     * @param modifier The name/key of the modifier to check.
     * @return True when there is an value for the specified modifier.
     */
    public boolean hasMod(String modifier) {
        return modifiers.containsKey(modifier);
    }

    /**
     * Get the {@link SingleOption} for the specified modifier.
     * <p/>
     * The option will contain the value of the modifier from the user input.
     * In most cases you'll want to use {@link #getMod(String)} to just get the value of the option.
     * <p/>
     * If the modifier is not specified this may return {@code null}.
     * Use {@link #hasMod(String)} to check if it's been specified by the user.
     * <p/>
     * It's safe to directly cast this {@link SingleOption} to the option type of the modifier you want to retrieve.
     * It must match with the modifier you set with {@link Cmd#addModifier(String, String, String, SingleOption)}
     *
     * @param modifier The name/key of the modifier to get the option from.
     * @return {@link SingleOption} containing the modifier value. ({@code null} when there is no modifier parsed with the specified name)
     */
    public SingleOption getModOption(String modifier) {
        return modifiers.get(modifier);
    }

    /**
     * Get the value as an {@link Object} for the specified modifier.
     * <p/>
     * If the modifier is not specified this may return {@code null}.
     * Use {@link #hasMod(String)} to check if it's been specified by the user.
     * <p/>
     * It's safe to directly cast this {@link Object} to the type of the modifier you want to retrieve.
     * It must match with the modifier you set with {@link Cmd#addModifier(String, String, String, SingleOption)}
     * <p/>
     * For example if the modifier is a {@link StringO} you can cast this value to a {@link String}
     *
     * @param modifier The name/key of the modifier to get the option from.
     * @return Object with the modifier value. ({@code null} when there is no modifier parsed with the specified name)
     */
    public Object getMod(String modifier) {
        return modifiers.get(modifier) == null ? null : modifiers.get(modifier).getValue();
    }



    /**
     * Get all the flags that have been parsed.
     * <p/>
     * Use {@link #hasFlag(String)} to check if a specific flag has been specified by the user.
     *
     * @return The list of flags. (These flags do not start with the '-' prefix)
     */
    public List<String> getFlags() {
        return flags;
    }

    /**
     * Check if the specified flag name has been parsed/specified by the user.
     * <p/>
     * The flag name doesn't have to be prefixed with a '-'.
     * If it does it will be trimmed off so you don't have to worry about that.
     *
     * @param flag The flag name to check.
     * @return True when the flag has been specified/parsed.
     */
    public boolean hasFlag(String flag) {
        if (flag.startsWith("-")) {
            flag = flag.substring(1);
        }
        return flags.contains(flag);
    }



    /**
     * Get the {@link CommandSender} that executed the command.
     * <p/>
     * This can be a {@link Player}, {@link ConsoleCommandSender}, {@link BlockCommandSender} or another {@link Entity}
     *
     * @return The {@link CommandSender} who executed the command.
     */
    public CommandSender getSender() {
        return sender;
    }

    /**
     * Check if the {@link CommandSender} who executed the command is a player.
     *
     * @return True when the command was executed by a player.
     */
    public boolean isPlayer() {
        return sender instanceof Player;
    }

    /**
     * Check if the {@link CommandSender} who executed the command is the console.
     *
     * @return True when the command was executed by the console.
     */
    public boolean isConsole() {
        return sender instanceof ConsoleCommandSender;
    }

    /**
     * Check if the {@link CommandSender} who executed the command is a command block.
     *
     * @return True when the command was executed by a command block.
     */
    public boolean isCmdBlock() {
        return sender instanceof BlockCommandSender;
    }

    /**
     * Try to get the {@link Player} who executed the command.
     * <p/>
     * If the {@link CommandSender} is not a player this will return {@code null}
     *
     * @return The {@link Player} who executed the console or {@code null} if the sender isn't a player.
     */
    public Player getPlayer() {
        return isPlayer() ? (Player)sender : null;
    }

    /**
     * Get the player related to the command.
     * <p/>
     * This can either be the player from the specified argument/modifier or the sender.
     * <p/>
     * It will first try to get the player from the specified argument or modifier.
     * The argument or modifier must be a {@link PlayerO} argument and it must contain a {@link Player} value.
     * <p/>
     * If it fails to get the player from the argument or modifier it will try to get the player from the {@link CommandSender}
     * If this also fails (if the sender isn't a player) this will return {@code null}.
     *
     * @return The {@link Player} from the specified argument/modifier or the {@link Player} {@link CommandSender}. (May be {@code null} if both fail)
     */
    public Player getPlayer(String argument) {
        if (getArg(argument) instanceof Player) {
            return (Player)getArg(argument);
        }
        if (getMod(argument) instanceof Player) {
            return (Player)getMod(argument);
        }
        if (sender instanceof Player) {
            return (Player)sender;
        }
        return null;
    }

}
