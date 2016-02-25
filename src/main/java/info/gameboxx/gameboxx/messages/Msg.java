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

package info.gameboxx.gameboxx.messages;

import info.gameboxx.gameboxx.nms.NMS;
import info.gameboxx.gameboxx.util.Str;
import info.gameboxx.gameboxx.util.text.TextAction;
import info.gameboxx.gameboxx.util.text.TextParser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores cached images from {@link MessageConfig} files.
 * Those messages can be formatted in many ways.
 *
 * The proper usage of this is to use the static {@link Msg#get(String, Param...)}.
 * Which will give you the {@link Msg} instance.
 * You can then apply more formatting or display it using the display methods.
 *
 * You can also use this for regular text messages that aren't configured.
 * Just pass the string in the constructor for example: {@code new Msg("[[hover||text]]").toJSON().send(player)}
 * See {@link #Msg(String)}
 *
 * <b>Parameters</b>
 * Parameters can be set in messages and those will be replaced.
 * See {@link #params(Param...)} for details and {@link Param}.
 *
 * <b>JSON</b>
 * By default when using {@link Msg#get(String, Param...)} the message will be converted to JSON automatically.
 * When using the display methods it will send the JSON formatted message if the string is JSON formatted and otherwise it will use regular send methods.
 * See {@link #toJSON()} for details.
 *
 * <b>Placeholders</b>
 * TODO: IMPLEMENT
 *
 * <b>Displaying</b>
 * Messages can be sent to a player or multiple players with {@link #send(Player...)}
 * Messages can be sent to the action bar too with {@link #sendBar(Player...)}
 * You can also just use {@link #get()}, {@link #getRaw()} or {@link Msg#getString()} to get the message and display it manually.
 */
public class Msg {

    private static Map<String, String> messages = new HashMap<>();

    /** The message used when there is no message found for the specified message key. */
    public static final String UNDEFINED = "&c&nundefined";

    private String original;
    private String message;
    private String json;

    /**
     * Construct a new message with the specified raw message.
     *
     * <b>This is not a message key!</b>
     * Use the static call {@link Msg#get()} for getting configurable/translatable messages.
     *
     * The message may contain JSON syntax for actions like [[hover||text]] and such.
     * The {@link #toJSON()} will be called and the specified message will be formatted.
     * If you do not wish to have automatic formatting you can use {@link #Msg(String, boolean)} with format set to false.
     *
     * @param text The raw message string.
     *             This message may contain JSON syntax like [[hover||text]] and such.
     */
    public Msg(String text) {
        this(text, true);
    }

    /**
     * Construct a new message with the specified raw message.
     *
     * <b>This is not a message key!</b>
     * Use the static call {@link Msg#get()} for getting configurable/translatable messages.
     *
     * @param text The raw message string.
     * @param format When set to true the {@link #toJSON()} will be called.
     *               If set to false you can still manually format the message.
     */
    public Msg(String text, boolean format) {
        original = text;
        if (format) {
            toJSON();
        }
    }

    /**
     * Used for cloning.
     */
    private Msg(String original, String message, String json) {
        this.original = original;
        this.message = message;
        this.json = json;
    }

    //region Formatting

    /**
     * Color the message by converting color codes like '&a' ot chat colors..
     * This does not affect the JSON message.
     * @return This msg instance.
     */
    public Msg clr() {
        json = Str.color(json);
        return this;
    }

    /**
     * Remove color from the message and replace it with color codes.
     * This can be used after {@link #clr()} is used to get a string with color codes again.
     * @return This msg instance.
     */
    public Msg removeClr() {
        json = Str.replaceColor(json);
        return this;
    }

    /**
     * Strip color from message.
     * @return
     */
    public Msg stripClr() {
        json = Str.stripColor(json);
        return this;
    }

    public Msg wrap(int length) {
        json = Str.wrapString(json, length);
        return this;
    }

    public Msg wrapExact(int length) {
        json = Str.wrapStringExact(json, length);
        return this;
    }

    public Msg capitalize() {
        json = Str.capitalize(json);
        return this;
    }

    public Msg upper() {
        json = json.toUpperCase();
        return this;
    }

    public Msg lower() {
        json = json.toLowerCase();
        return this;
    }

    /**
     * Replaces parameters in the specified string and returns the string with the parameters replaced.
     * For example a string like 'My name is <name>' and you provide {@code Param.P("name", "rojoss")} it would return 'My name is rojoss'
     *
     * It supports capitalization for parameters too.
     * For example if you put <Name> it would be replaced with Rojoss and if you put <NAME> it would be ROJOSS.
     * Lowercase parameters will use the value provided in the parameter.
     * The parameter can be prefixed with a underscore to force the value lowercase like <_name>
     *
     * @param msg The string to replace the parameters in.
     * @param params The list of parameters to replace.
     *               The <p> parameter is reserved and this gets replaced with the prefix.
     * @return
     */
    public Msg params(Param... params) {
        if (message == null) {
            message = original;
        }
        for (Param param : params) {
            message = message.replace("<" + param.getParam().toLowerCase() + ">", param.toString());
            message = message.replace("<_" + param.getParam().toLowerCase() + ">", param.toString().toLowerCase());
            message = message.replace("<" + param.getParam().toUpperCase() + ">", param.toString().toUpperCase());
            message = message.replace("<" + Str.capitalize(param.getParam().toLowerCase()) + ">", Str.capitalize(param.toString()));
        }
        message = message.replace("<p>", Msg.getRaw("prefix").message);
        return this;
    }

    //TODO: Placeholder/variable replacement

    /**
     * Format the specified msg to a JSON message.
     * It uses the {@link TextParser} for parsing the input.
     * Syntax for all the actions can be used. See {@link TextAction} for the syntax for each action.
     *
     * @param msg The message that needs to be parsed.
     * @return The parsed JSON message.
     */
    public Msg toJSON() {
        json = new TextParser(message).getJSON();
        return this;
    }
    //endregion


    //region Displaying

    public void send(CommandSender sender) {
        if (sender instanceof Player) {
            NMS.get().getChat().send(json, (Player) sender);
        } else {
            sendRaw(sender, true);
        }
    }

    public void sendRaw(CommandSender sender) {
        sender.sendMessage(message);
    }

    public void sendRaw(CommandSender sender, boolean color) {
        sender.sendMessage(color ? Str.color(message) : message);
    }

    public void send(Player... players) {
        NMS.get().getChat().send(json, players);
    }

    public void send(Collection<? extends Player> players) {
        NMS.get().getChat().send(json, players);
    }


    public void sendBar(Player player) {
        NMS.get().getChat().sendBar(json, player);
    }

    public void sendBar(Player... players) {
        NMS.get().getChat().sendBar(json, players);
    }

    public void sendBar(Collection<? extends Player> players) {
        NMS.get().getChat().sendBar(json, players);
    }

    //TODO: Send to title
    //endregion


    //region Getters
    public String get() {
        return json;
    }

    public String getRaw() {
        return message;
    }

    public boolean isUndefined() {
        return original.equals(UNDEFINED) || (message != null && message.equals(UNDEFINED));
    }
    //endregion


    //region Static getter methods

    /**
     * Get the raw message for the specified key.
     *
     * @param key The key of the message to retrieve.
     * @return The raw message or {@link #UNDEFINED} when there is no message with the specified key.
     */
    private static Msg getRaw(String key) {
        String msg = messages.get(key);
        if (msg != null) {
            return new Msg(msg);
        }
        return new Msg(UNDEFINED);
    }

    /**
     * Get the JSON formatted message for the specified key.
     * The syntax for all {@link TextAction}s will be parsed using the {@link TextParser}.
     *
     * The parsed JSON message will be cached for further usage.
     * If the cache has the specified message key already parsed that message will be returned.
     * Parms will be replaced either way.
     *
     * @param key The key of the message to retrieve.
     * @param params optional list of parameters to replace in the message.
     *               See {@link #replaceParams(String, Param...)} for details.
     * @return The JSON formatted message or {@link #UNDEFINED} as JSON when there is no message with the specified key.
     */
    public static Msg get(String key, Param... params) {
        return getRaw(key).params(params).toJSON();
    }

    /**
     * Get the colored message for the specified key without JSON formatting.
     * @param key The key of the message to retrieve.
     * @param params optional list of parameters to replace in the message.
     *               See {@link #replaceParams(String, Param...)} for details.
     * @return The colored message or {@link #UNDEFINED} when there is no message with the specified key.
     */
    public static Msg getRaw(String key, Param... params) {
        return getRaw(key).params(params).clr();
    }
    //endregion


    //region Static cache

    /**
     * Set/update all the specified messages.
     * When a message with the same key is already registered it will be overwritten!
     *
     * There should be no need to call this as the API already does this.
     * It gets called when registering a {@link MessageConfig}, when loading/reloading a message config and when updating a message config.
     * Also, when the language gets changed all messages from all configs will be updated.
     *
     * @param messages The map with messages to set. Where the key is the key for the message and the value is the message itself.
     */
    public static void setMessages(Map<String, String> messages) {
        messages.putAll(messages);
    }

    /**
     * Set/update the specified message.
     * When a message with the same key is already registered it will be overwritten!
     *
     * There should be no need to call this as the API already does this.
     * It gets called when registering a {@link MessageConfig}, when loading/reloading a message config and when updating a message config.
     * Also, when the language gets changed all messages from all configs will be updated.
     *
     * @param key The key of the message from config and used to reference it.
     * @param message The message itself.
     */
    public static void setMessage(String key, String message) {
        messages.put(key, message);
    }
    //endregion

    @Override
    public Msg clone() {
        return new Msg(original, message, json);
    }
}
