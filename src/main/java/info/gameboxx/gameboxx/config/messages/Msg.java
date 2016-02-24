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

package info.gameboxx.gameboxx.config.messages;

import info.gameboxx.gameboxx.util.Str;
import info.gameboxx.gameboxx.util.text.TextAction;
import info.gameboxx.gameboxx.util.text.TextParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores cached images from {@link MessageConfig} files.
 * It has a bunch of methods for formatting the cached messages.
 *
 * <b>Parameters</b>
 * Parameters can be set and those will be replaced.
 * See {@link #replaceParams(String, Param...)} for details.
 *
 * <b>JSON</b>
 * Messages can be converted to JSON format with custom syntax parsing.
 * The formatted JSON will be cached so using this often shouldn't affect performance.
 * See {@link #toJSON(String)} for details.
 *
 * <b>Placeholders</b>
 * Placeholders can be replaced in the message.
 * TODO: IMPLEMENT
 *
 * <b>Colors</b>
 * The method {@link #getRaw(String, Param...)} and the overload methods will not format color codes.
 * The default {@link #get(String, Param...)} and the overload methods will format color codes like '&a' etc.
 * For raw messages you can also choose to completely strip the color codes from the message.
 *
 */
public class Msg {

    private static Map<String, String> messages = new HashMap<>();
    private static Map<String, String> jsonMessages = new HashMap<>();

    /** The message returned when there is no message found for the specified key. */
    public static final String UNDEFINED = "&c&nundefined";

    //TODO: Placeholder/variable replacement

    /**
     * Get the raw message for the specified key.
     *
     * @param key The key of the message to retrieve.
     * @return The raw message or {@link #UNDEFINED} when there is no message with the specified key.
     */
    private static String getRaw(String key) {
        String msg = messages.get(key);
        if (msg != null) {
            return msg;
        }
        return UNDEFINED;
    }

    /**
     * Get the raw message for the specified key.
     *
     * Color codes will remain in the message. A message like '&aExample' will literally return that.
     * Use {@link #get(String, Param...)} to get the colored message or,
     * use {@link #getRaw(String, boolean, Param...)} with stripColors true to remove the color codes.
     *
     * @param key The key of the message to retrieve.
     * @param params optional list of parameters to replace in the message.
     *               See {@link #replaceParams(String, Param...)} for details.
     * @return The raw message or {@link #UNDEFINED} when there is no message with the specified key.
     */
    public static String getRaw(String key, Param... params) {
        return replaceParams(getRaw(key), params);
    }

    /**
     * Get the raw message for the specified key.
     * @param key The key of the message to retrieve.
     * @param stripColors When set to true color codes like &a and such will be removed/stripped from the message.
     * @param params optional list of parameters to replace in the message.
     *               See {@link #replaceParams(String, Param...)} for details.
     * @return The raw message or {@link #UNDEFINED} when there is no message with the specified key.
     */
    public static String getRaw(String key, boolean stripColors, Param... params) {
        return stripColors ? Str.stripColor(getRaw(key, params)) : getRaw(key, params);
    }

    /**
     * Get the colored message for the specified key.
     * @param key The key of the message to retrieve.
     * @param params optional list of parameters to replace in the message.
     *               See {@link #replaceParams(String, Param...)} for details.
     * @return The colored message or {@link #UNDEFINED} when there is no message with the specified key.
     */
    public static String get(String key, Param... params) {
        return Str.color(getRaw(key, params));
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
    public static String getJSON(String key, Param... params) {
        String msg = "";
        if (params.length == 0 && jsonMessages.containsKey(key)) {
            msg = jsonMessages.get(key);
        } else {
            msg = toJSON(getRaw(key));
        }
        return replaceParams(msg, params);
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
    public static String replaceParams(String msg, Param... params) {
        for (Param param : params) {
            msg = msg.replace("<" + param.getParam().toLowerCase() + ">", param.toString());
            msg = msg.replace("<_" + param.getParam().toLowerCase() + ">", param.toString().toLowerCase());
            msg = msg.replace("<" + param.getParam().toUpperCase() + ">", param.toString().toUpperCase());
            msg = msg.replace("<" + Str.capitalize(param.getParam().toLowerCase()) + ">", Str.capitalize(param.toString()));
        }
        msg = msg.replace("<p>", getRaw("prefix"));
        return msg;
    }


    /**
     * Format the specified msg to a JSON message.
     * It uses the {@link TextParser} for parsing the input.
     * Syntax for all the actions can be used. See {@link TextAction} for the syntax for each action.
     *
     * @param msg The message that needs to be parsed.
     * @return The parsed JSON message.
     */
    public static String toJSON(String msg) {
        return new TextParser(msg).getJSON();
    }


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

    /**
     * Clear/flush the cache with JSON messages.
     * This should be used when messages are changed for example when changing languages.
     */
    public static void clearCache() {
        jsonMessages.clear();
    }
}
