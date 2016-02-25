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

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.nms.NMS;
import info.gameboxx.gameboxx.util.Str;
import info.gameboxx.gameboxx.util.text.TextAction;
import info.gameboxx.gameboxx.util.text.TextParser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Mainly used for getting translatable messages, formatting and displaying messages.
 * This class stores cached messages from {@link MessageConfig} files. ({@link Msg#setMessages(Map)})
 *
 * <p>The proper usage of this is to use the static {@link Msg#get(String, Param...)}.
 * Which will give you the {@link Msg} instance.
 * You can then apply more formatting or display it using the display methods.
 * <b>By default it already colors messages!</b>
 *
 * <p><b>After displaying the message, changes in formatting and params etc won't affect the JSON message!</b>
 * The JSON message will only be parsed once for displaying and it's then cached for when you want to display it multiple times using the same {@link Msg} instance.
 * If you really have to you can call {@link #toJSON()} after reformatting and before displaying again.
 *
 * <p>You can also use this for regular text messages that aren't configured.
 * Just pass the string in the constructor for example: {@code new Msg("[[hover||text]]").send(player)}
 * See {@link #Msg(String)} or use the static method {@link Msg#fromString(String, Param...)}
 *
 * <p><b>Parameters</b>
 * Parameters can be set in messages and those will be replaced.
 * See {@link #params(Param...)} for details and {@link Param}.
 *
 * <p><b>JSON</b>
 * Messages can have the custom JSON Syntax like [[this shows on hover||Hover over me!]] for example.
 * There are several JSON actions see this list for more details and the syntax. {{@link TextAction}}
 * It uses the {@link TextParser} for parsing the custom syntax.
 *
 * <p>JSON Messages are cached within the {@link Msg} instance.
 * This means you can not format a JSON message after displaying it unless you manually call {@link #toJSON()}.
 *
 * <p><b>Placeholders</b>
 * TODO: IMPLEMENT
 *
 * <p><b>Displaying</b>
 * Messages can be sent to a player or multiple players with {@link #send(Player...)}
 * Messages can be sent to the action bar too with {@link #sendBar(Player...)}
 * You can also just use {@link #get()}, {@link #getRaw()}, {@link #getOriginal()} or {@link Msg#getString(String, Param...)} to get the message string and display it manually.
 */
public class Msg {

    private static Map<String, String> messages = new HashMap<>();

    /** The message used when there is no message found for the specified message key. */
    public static final String UNDEFINED = "&c&nundefined";
    private static final char PARAM_OPEN = '<';
    private static final char PARAM_CLOSE = '>';

    private String original;
    private String message;
    private String json;

    /**
     * Construct a new message with the specified string message.
     * <p><b>This is not a message key!</b>
     *
     * <p>By default it will color messages.
     * You can always use {@link #removeClr()} or {@link #stripClr()} to remove/strip the color.
     *
     * <p>See {@link Msg#fromString(String, Param...)} for more details.
     *
     * @param message A regular string with a custom message.
     *                Just like configurable message this may contain custom JSON syntax and such.
     */
    public Msg(String message) {
        this.original = message;
        this.message = Str.color(message);
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
     * Color the message by converting color codes like '&a' ot chat colors.
     * <p>Uses {@link Str#color(String)}
     *
     * <p><b>By default messages get colored already.</b>
     * We've made it that way because in 99% of the cases you would want colored messages.
     * You can use {@link #removeClr()} to change the color back to color codes or {@link #stripClr()} to completely get rid of colors.
     *
     * @return This msg instance.
     */
    public Msg clr() {
        message = Str.color(message);
        return this;
    }

    /**
     * Remove color from the message and replace it with color codes.
     * <p>This can be used after {@link #clr()} is used to get a string with color codes again.
     * <p>Uses {@link Str#replaceColor(String)}
     *
     * @return This msg instance.
     */
    public Msg removeClr() {
        message = Str.replaceColor(message);
        return this;
    }

    /**
     * Strip color from message.
     * <p>This will remove any sort of color codes from the string.
     * <p>Uses {@link Str#stripColor(String)}
     *
     * @return This msg instance.
     */
    public Msg stripClr() {
        message = Str.stripColor(message);
        return this;
    }

    /**
     * Wrap the message to a specific length by splitting the message over multiple lines.
     * <p>Uses {@link Str#wrapString(String, int)}
     *
     * <p>A new line symbol '\n' will be added at the end of words that are too long.
     * Words will never be cut in half using this wrapping method even if they exceed the length.
     *
     * @param length The length used for wrapping.
     * @return This msg instance.
     */
    public Msg wrap(int length) {
        message = Str.wrapString(message, length);
        return this;
    }

    /**
     * Wrap the message to a specific length by splitting the message over multiple lines.
     * <p>Uses {@link Str#wrapStringExact(String, int)}
     *
     * <p>A new line symbol '\n' will be added at the specified length
     * Words will be cut in half using this wrapping method.
     *
     * @param length The length used for wrapping.
     * @return This msg instance.
     */
    public Msg wrapExact(int length) {
        message = Str.wrapStringExact(message, length);
        return this;
    }

    /**
     * Capitalize the first letter of the message.
     * <p>Uses {@link Str#capitalize(String)}
     *
     * @return This msg instance.
     */
    public Msg capitalize() {
        message = Str.capitalize(message);
        return this;
    }

    /**
     * Capitalize all characters of the message.
     * <p>Uses {@link String#toUpperCase()}
     *
     * @return This msg instance.
     */
    public Msg upper() {
        message = message.toUpperCase();
        return this;
    }

    /**
     * Lowercase all characters of the message.
     * <p>Uses {@link String#toLowerCase()}
     *
     * @return This msg instance.
     */
    public Msg lower() {
        message = message.toLowerCase();
        return this;
    }

    /**
     * Replaces parameters in the string with values specified.
     *
     * <p>For example a string like 'My name is &lt;name&gt;' and you provide {@code Param.P("name", "rojoss")} it would return 'My name is rojoss'
     * <b>Notice how there is no &lt; and &gt; sign around the parameter name for {@link Param}!</b>
     * If the message doesn't have parameters specified nothing will happen.
     *
     * <p><b>No need to call this for messages from {@link Msg#get(String, Param...)} as that already replaces the specified params using this method</b>
     * <p>It does not affect JSON messages unless used before parsing to JSON.
     *
     * <p>It supports capitalization for parameters too.
     * For example if you put &lt;Name&gt; it would be replaced with Rojoss and if you put &lt;NAME&gt; it would be ROJOSS.
     * Lowercase parameter names will use the value provided in the parameter.
     * The parameter can be prefixed with a underscore to force the value lowercase like &lt;_name&gt;
     *
     * @see Param
     * @param params The list of parameters to replace.
     *               The &lt;p&gt; parameter is reserved and this gets replaced with global prefix.
     * @return This msg instance.
     */
    public Msg params(Param... params) {
        if (!message.contains(Character.toString(PARAM_OPEN)) || !!message.contains(Character.toString(PARAM_CLOSE))) {
            return this;
        }
        if (message == null) {
            message = original;
        }
        for (Param param : params) {
            message = message.replace(PARAM_OPEN + param.getParam().toLowerCase() + PARAM_CLOSE, param.toString());
            message = message.replace(PARAM_OPEN + "_" + param.getParam().toLowerCase() + PARAM_CLOSE, param.toString().toLowerCase());
            message = message.replace(PARAM_OPEN + param.getParam().toUpperCase() + PARAM_CLOSE, param.toString().toUpperCase());
            message = message.replace(PARAM_OPEN + Str.capitalize(param.getParam().toLowerCase()) + PARAM_CLOSE, Str.capitalize(param.toString()));
        }
        message = message.replace("<p>", Msg.getRaw("prefix").message);
        return this;
    }

    //TODO: Placeholder/variable replacement

    /**
     * Formats the message to JSON format.
     *
     * <p><b>Important: Only use this when you want to force the JSON parsing.</b>
     *
     * <p>By default it will automatically parse once to JSON before displaying the message.
     * The JSON string will be cached and you can manually call this to reparse the JSON.
     * The reason you'd wanna do that is because after the JSON message is cached any formatting will not update the JSON message.
     * For example when you display the message, wrap it and display it again the second display will be the same message.
     *
     * <p>When using {@link #get()} it will also format the message to JSON if it's not yet cached.
     * So really, the only reason to call this is to force parse the json again.
     *
     * <p>It uses the {@link TextParser} for parsing the input.
     * Syntax for all the actions can be used. See {@link TextAction} for the syntax for each action.
     *
     * @return This msg instance.
     */
    public Msg toJSON() {
        json = new TextParser(message).getJSON();
        return this;
    }
    //endregion


    //region Displaying

    /**
     * Send the message to the specified {@link CommandSender}.
     *
     * <p>If the sender is a {@link Player} it will try to send the JSON formatted message.
     * If not, it will send the raw message. (also if there is no valid JSON)
     * If the JSON is invalid it will also send an error message to the console.
     *
     * <p><b>Raw messages will still have params replaced, colors and other formatting but no JSON.
     *
     * @param sender The {@link CommandSender} to send the message to.
     */
    public void send(CommandSender sender) {
        if (sender instanceof Player && isValidJSON()) {
            NMS.get().getChat().send(json, (Player) sender);
        } else {
            sendRaw(sender);
        }
    }

    /**
     * Send the raw message to the specified {@link CommandSender}.
     *
     * <p>Unlike {@link #send(CommandSender)} this does not send the JSON message to players.
     * It will always send the raw formatted message.
     *
     * <p><b>Raw messages will still have params replaced, colors and other formatting but no JSON.
     *
     * @param sender The {@link CommandSender} to send the message to.
     */
    public void sendRaw(CommandSender sender) {
        sender.sendMessage(message);
    }

    /**
     * Send the raw message to the specified array of {@link CommandSender}s.
     *
     * <p>Unlike {@link #send(CommandSender)} this does not send the JSON message to players.
     * It will always send the raw formatted message.
     *
     * <p><b>Raw messages will still have params replaced, colors and other formatting but no JSON.
     * By default this will convert color codes automatically.
     *
     * @param senders Array with {@link CommandSender}s to send the message to.
     */
    public void sendRaw(CommandSender... senders) {
        for (CommandSender sender : senders) {
            sendRaw(sender);
        }
    }

    /**
     * Send the raw message to the specified collection of {@link CommandSender}s.
     *
     * <p>Unlike {@link #send(CommandSender)} this does not send the JSON message to players.
     * It will always send the raw formatted message.
     *
     * <p><b>Raw messages will still have params replaced, colors and other formatting but no JSON.
     * By default this will convert color codes automatically.
     *
     * @param senders Collection with {@link CommandSender}s to send the message to.
     */
    public void sendRaw(Collection<? extends CommandSender> senders) {
        for (CommandSender sender : senders) {
            sendRaw(sender);
        }
    }

    /**
     * Try to send a JSON formatted message to the specified array of {@link Player}s.
     *
     * <p>If the JSON is invalid it will send the raw message {@link #sendRaw(CommandSender)}
     * It will also send an error message to the console.
     *
     * @param players The players to send the message to.
     */
    public void send(Player... players) {
        if (isValidJSON()) {
            NMS.get().getChat().send(json, players);
        } else {
            sendRaw(players);
        }
    }

    /**
     * Try to send a JSON formatted message to the specified collection of {@link Player}s.
     *
     * <p>If the JSON is invalid it will send the raw message {@link #sendRaw(CommandSender)}
     * It will also send an error message to the console.
     *
     * @param players The players to send the message to.
     */
    public void send(Collection<? extends Player> players) {
        if (isValidJSON()) {
            NMS.get().getChat().send(json, players);
        } else {
            sendRaw(players);
        }
    }


    /**
     * Try to send a JSON formatted message to the specified {@link Player} their action bar. (above hotbar)
     *
     * <p>If the JSON is invalid it will send the raw message {@link #sendRaw(CommandSender)} as a regular message.
     * It will also send an error message to the console.
     *
     * <p>Note that JSON actions won't really do anything on the actionbar except for colors and such.
     * It's still required to parse the message to JSON for coloring and such.
     *
     * @param player The player to send the action bar message to.
     */
    public void sendBar(Player player) {
        if (isValidJSON()) {
            NMS.get().getChat().sendBar(json, player);
        } else {
            sendRaw(player);
        }
    }

    /**
     * Try to send a JSON formatted message to the specified array of {@link Player}s their action bar. (above hotbar)
     *
     * <p>If the JSON is invalid it will send the raw message {@link #sendRaw(CommandSender...)} as a regular message.
     * It will also send an error message to the console.
     *
     * <p>Note that JSON actions won't really do anything on the actionbar except for colors and such.
     *
     * @param players Array with players to send the action bar message to.
     */
    public void sendBar(Player... players) {
        if (isValidJSON()) {
            NMS.get().getChat().sendBar(json, players);
        } else {
            sendRaw(players);
        }
    }

    /**
     * Try to send a JSON formatted message to the specified collection of {@link Player}s their action bar. (above hotbar)
     *
     * <p>If the JSON is invalid it will send the raw message {@link #sendRaw(CommandSender...)} as a regular message.
     * It will also send an error message to the console.
     *
     * <p>Note that JSON actions won't really do anything on the actionbar except for colors and such.
     *
     * @param players Collection with players to send the action bar message to.
     */
    public void sendBar(Collection<? extends Player> players) {
        if (isValidJSON()) {
            NMS.get().getChat().sendBar(json, players);
        } else {
            sendRaw(players);
        }
    }

    /**
     * Checks whether or not the message has been parsed to JSON and if the JSON is invalid.
     *
     * <p>This is mainly used for the send methods to check whether to send a JSON message or regular message.
     *
     * <p>If there is a JSON message but it's invalid an error will be sent to the console!
     * It will fallback to regular messages when the JSON fails for display methods.
     *
     * @return True when there is a valid JSON message and false if not.
     */
    public boolean isValidJSON() {
        if (json == null || json.isEmpty()) {
            toJSON();
        }
        try {
            JSONParser parser = new JSONParser();
            parser.parse(json);
            return true;
        } catch (ParseException e) {
            GameBoxx.get().error("Invalid JSON found for the message '" + original + "'! Error: " + e.getMessage());
        } catch (Exception e) {}
        return false;
    }

    //TODO: Send to title
    //endregion


    //region Getters

    /**
     * Get the JSON formatted message.
     *
     * <p><b>The JSON message returned may have malformed JSON!</b>
     * The {@link TextParser} is pretty robust but in some rare cases with weird user input it might produce malformed JSON.
     * You can use {@link #isValidJSON()} to validate that it's valid.
     *
     * @return The JSON formatted message.
     */
    public String get() {
        if (json == null) {
            toJSON();
        }
        return json;
    }

    /**
     * Get the raw formatted message.
     *
     * <p>In this case raw does not mean the original raw message from the config or the one specified in the constructor.
     * The raw message can have params replaces, colors and other formatting.
     *
     * @see #getOriginal()
     * @return The raw formatted message.
     */
    public String getRaw() {
        return message;
    }

    /**
     * Get the original message that was retrieved from the {@link MessageConfig} or that was specified in the constructor.
     *
     * <p>This message does not contain any formatting and it will have all raw params and such.
     *
     * @return The original message without any changes.
     */
    public String getOriginal() {
        return original;
    }

    /**
     * Checks whether or not this message was undefined.
     *
     * <p>This can be used for messages retrieved by key.
     * It will compare the original message with the {@link #UNDEFINED} value.
     *
     * @return Whether or not the message is undefined.
     */
    public boolean isUndefined() {
        return original.equals(UNDEFINED);
    }
    //endregion


    //region Static getter methods

    /**
     * Get the message for the specified key from a {@link MessageConfig}.
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
     * Get the message for the specified key from a {@link MessageConfig}.
     *
     * <p>Any parameters in the message will be replaced with the ones you specified (OPTIONAL!)
     *
     * @param key The key of the message to retrieve.
     *            This message key must be the path to the message in a config file.
     *            Use a dot '.' for different sections for example 'player.invalid' to get the invalid message within the player section.
     * @param params optional list of parameters to replace in the message.
     *               See {@link #params(Param...)} for details.
     * @return {@link Msg} instance with the message at the specified key or {@link #UNDEFINED} when there is no message with the specified key.
     */
    public static Msg get(String key, Param... params) {
        return getRaw(key).params(params);
    }

    /**
     * Get the message for the specified key from a {@link MessageConfig} as a {@link String}.
     *
     * <p>Normally with {@link Msg#get(String, Param...)} you get a {@link Msg} instance.
     * If you KNOW that there won't be any other formatting and that the message CAN'T be JSON formatted you can use this.
     * It will just get the message at the specified key from a {@link MessageConfig} and return it as a string.
     *
     * <p><b>Only use this if you KNOW that you don't have to do any formatting and have to display it somewhere custom.</b>
     *
     * @param key The key of the message to retrieve.
     *            This message key must be the path to the message in a config file.
     *            Use a dot '.' for different sections for example 'player.invalid' to get the invalid message within the player section.
     * @param params optional list of parameters to replace in the message.
     *               See {@link #params(Param...)} for details.
     * @return {@link String} with the message at the specified key or {@link #UNDEFINED} when there is no message with the specified key.
     */
    public static String getString(String key, Param... params) {
        String msg = messages.get(key);
        if (params.length == 0) {
            return msg;
        }
        if (msg != null) {
            return new Msg(msg).params(params).getRaw();
        } else {
            return UNDEFINED;
        }
    }

    /**
     * Get a new {@link Msg} with the specified text.
     *
     * <p>This is the same as creating a new instance with {@link #Msg(String)}.{@link #params(Param...)};
     *
     * <p>Any parameters in the message will be replaced with the ones you specified (OPTIONAL!)
     *
     * <p><b>Note: It's recommended to create translatable/configurable strings using a {@link MessageConfig}</b>
     *
     * @param message A regular string with a custom message.
     *                Just like configurable message this may contain custom JSON syntax and such.
     * @param params optional list of parameters to replace in the message.
     *               See {@link #params(Param...)} for details.
     * @return {@link Msg} instance with the message at the specified key or {@link #UNDEFINED} when there is no message with the specified key.
     */
    public static Msg fromString(String message, Param... params) {
        return new Msg(message).params(params);
    }
    //endregion


    //region Static cache

    /**
     * Set/update all the specified messages.
     * When a message with the same key is already registered it will be overwritten!
     *
     * There should be no need to call this as the API already does this internally.
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
     * There should be no need to call this as the API already does this internally.
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

    /**
     * Clone the message by creating a copy of the original message, raw message and JSON message.
     * @return A new {@link Msg} with a copy of the message values.
     */
    @Override
    public Msg clone() {
        return new Msg(original, message, json);
    }
}
