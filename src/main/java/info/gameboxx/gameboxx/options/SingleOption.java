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

package info.gameboxx.gameboxx.options;

import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import org.bukkit.entity.Player;

/**
 * A {@link Option} with a single value.
 *
 * @param <O> The object the option parses.
 * @param <S> The SingleOption class.
 */
public abstract class SingleOption<O, S extends SingleOption> extends Option<S> {

    protected O value = null;
    protected O defaultValue = null;


    /**
     * Get the default value if it's set.
     *
     * @return The default value or {@code null} if there is no default.
     */
    public O getDefault() {
        return defaultValue;
    }

    /**
     * Change/set the default value.
     *
     * @param defaultValue The default value to set. (set to null for no default)
     * @return this instance
     */
    public S def(O defaultValue) {
        this.defaultValue = defaultValue;
        return (S)this;
    }



    /**
     * Get the cached value after parsing it.
     * <p/>
     * If the option doesn't have the {@link OptionFlag#REQUIRED} flag and there is no value it will return the default.
     * <p/>
     * If the option has the required flag or if the default is null the return value will be null!
     *
     * @return The parse result value or the default value.
     */
    public O getValue() {
        if (value == null && defaultValue != null && getFlag() != OptionFlag.REQUIRED) {
            return defaultValue;
        }
        return value;
    }


    /**
     * Check whether or not the option has a value.
     * <p/>
     * It will check both the parsed value and the default value.
     * So, even if the parsing failed it will still be true when there is a default value.
     * <p/>
     * Use {@link #success()} to check if the parsing was successful.
     *
     * @see #getValue()
     * @return True when there is a value or default value and false if not.
     */
    public boolean hasValue() {
        return getValue() != null;
    }

    /**
     * Check whether or not the parsing was successful.
     * <p/>
     * If not, there might still be a default value see {@link #hasValue()}
     * When false you can use {@link #getError()} to get the error message.
     *
     * @return True when there is a non null value.
     */
    public boolean success() {
        return getValue() != null;
    }



    /**
     * Get the value with user friendly format for displaying purposes.
     * <p/>
     * If a option doesn't override this value it will return the same as the {@link #serialize()} output.
     *
     * @return String with user friendly formatting. ({@code null} when no value)
     */
    public String getDisplayValue() {
        return serialize();
    }

    /**
     * Serialize the value to a string so it can be saved.
     * <p/>
     * The serialized string has the same format as the parse method accepts.
     *
     * @return Serialized value as string. ({@code null} when no value)
     */
    public String serialize() {
        O value = getValue();
        if (value == null) {
            return null;
        }
        return value.toString();
    }



    /**
     * Used by the {@link #parse(Object)} method to validate the object.
     * <p/>
     * The object may not be null and must be either a string or the same type as the object O.
     *
     * @param input The object to parse.
     * @return Whether or not the parsing was successful.
     */
    protected boolean parseObject(Object input) {
        error = "";
        value = null;
        if (input == null) {
            error = Msg.getString("null");
            return false;
        }
        if (!(input instanceof String)) {
            try {
                value = (O)input;
                return true;
            } catch (ClassCastException e) {
                error = Msg.getString("unsupported-type", Param.P("type", input.getClass().getSimpleName()));
            }
        }
        return true;
    }


    /**
     * Parse the specified object.
     * <p/>
     * The object may be a String or an instance of the the object the option can parse.
     * It uses {@link #parseObject(Object)}.
     * <p/>
     * If the object is a string the {@link #parse(String)} will be called.
     *
     * @param input The object that needs to be parsed.
     * @return Whether or not the parsing was successful.
     */
    public boolean parse(Object input) {
        if (!parseObject(input)) {
            return false;
        }
        if (value != null) {
            return true;
        }
        return parse((String)input);
    }

    /**
     * Parse the specified string.
     * <p/>
     * Calls {@link #parse(Player, String)} with null for the player argument.
     *
     * @param input The input string that needs to be parsed.
     * @return Whether or not the parsing was successful.
     */
    public boolean parse(String input) {
        return parse(null, input);
    }

    /**
     * Parse the specified string.
     * <p/>
     * Each option has it's own way of parsing the string and most options support multiple formats.
     * <p/>
     * The specified player will be used for certain options for certain formats.
     * For example, the location option you can use @ to get the location of the player.
     * <p/>
     * When the parsing returns false you can use {@link #getError()} to get the error message why it failed.
     *
     * @param player The player used for parsing player specific syntax. (May be {@code null})
     * @param input The input string that needs to be parsed.
     * @return Whether or not the parsing was successful.
     */
    public abstract boolean parse(Player player, String input);

    /**
     * Used for the {@link #clone()} method to copy data in a new instance.
     * It will copy the name, description, flag and the default value.
     *
     * @param option The new option to clone the data into.
     * @return The specified option.
     */
    protected S cloneData(S option) {
        return (S)super.cloneData(option).def(defaultValue);
    }
}
