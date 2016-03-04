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

import org.bukkit.entity.Player;

public abstract class SingleOption<O, S extends SingleOption> extends Option<S> {

    protected O value = null;
    protected O defaultValue = null;

    /**
     * Reset the error message and the value.
     * This is called before parsing.
     * Which means you can use the parse method multiple times for an option with different values.
     * There should be no need to manually call this.
     */
    public void reset() {
        error = "";
        value = null;
    }


    /**
     * Get the cached value after parsing it.
     * If the value is null and there is a default value it will return the default.
     * <b>Please note that the default value can be null too!</b>
     *
     * @return The parse result value or the default value.
     */
    public O getValue() {
        return getValueOrDefault();
    }

    /**
     * Get the value with user friendly format for displaying purposes.
     * If a option doesn't override this value it will return the same as the {@link #serialize()} output.
     *
     * @return String with user friendly formatting.
     */
    public String getDisplayValue() {
        return serialize();
    }

    /**
     * Serialize the value to a string so it can be saved.
     * The serialized string has the same format as the parse method accepts.
     *
     * @return Serialized value as string.
     */
    public String serialize() {
        O value = getValue();
        if (value == null) {
            return null;
        }
        return value.toString();
    }



    /**
     * Used by {@link #getValue()} to return the value and if it's null the default value.
     * <b>Please note that the default value can be null too!</b>
     *
     * @return The value or the default value if it's null.
     */
    public O getValueOrDefault() {
        if (value == null && defaultValue != null) {
            return defaultValue;
        }
        return value;
    }

    /**
     * Get the default value if it's set.
     *
     * @return The default value or {@code null} if it's not set.
     */
    public O getDefault() {
        return defaultValue;
    }

    /**
     * Change/set the default value.
     *
     * @param defaultValue The default value to set. (set to null for no default)
     * @return The option instance.
     */
    public S def(O defaultValue) {
        this.defaultValue = defaultValue;
        return (S)this;
    }



    /**
     * Check whether or not the option has a value.
     * It will check both the parsed value and the default value.
     * So, even if the parsing failed it will still be true when there is a default value.
     *
     * @return True when there is a value or default value and false if not.
     */
    public boolean hasValue() {
        return getValueOrDefault() != null;
    }

    /**
     * Check whether or not the parsing was successful.
     * If not, there might still be a default value see {@link #hasValue()}
     * In many cases there will be an error when this is false.
     *
     * @return True when there is a value and false if not.
     */
    public boolean success() {
        return getValue() != null;
    }



    protected boolean parseObject(Object input) {
        reset();
        if (input == null) {
            error = "Invalid input! [type=null]";
            return false;
        }
        if (!(input instanceof String)) {
            try {
                value = (O)input;
                return true;
            } catch (ClassCastException e) {
                error = "Invalid input! Must be a string or a proper option value. [type=" + input.getClass().getSimpleName() + "]";
            }
        }
        return true;
    }


    public boolean parse(Object input) {
        if (!parseObject(input)) {
            return false;
        }
        if (value != null) {
            return true;
        }
        return parse((String)input);
    }

    public boolean parse(String input) {
        return parse(null, input);
    }

    public S cloneData(S option) {
        return (S)option.def(defaultValue).desc(description).flag(flag);
    }

    public abstract boolean parse(Player player, String input);
}
