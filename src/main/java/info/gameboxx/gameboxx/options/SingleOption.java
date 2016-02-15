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

import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public abstract class SingleOption extends Option {

    protected Object value = null;
    protected Object defaultValue = null;

    public SingleOption() {}

    public SingleOption(String name) {
        super(name);
    }

    public SingleOption(String name, Object defaultValue) {
        super(name);
        this.defaultValue = defaultValue;
    }


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
     * @return The parse result value or the default value.
     */
    public abstract Object getValue();

    /**
     * Used by {@link #getValue()} to return the value and if it's null the default value.
     * <b>Please note that the default value can be null too!</b>
     * @return The value or the default value if it's null.
     */
    public Object getValueOrDefault() {
        if (value == null && defaultValue != null) {
            return defaultValue;
        }
        return value;
    }


    /**
     * Get the default value if it's set.
     * @return The default value or {@code null} if it's not set.
     */
    public Object getDefault() {
        return defaultValue;
    }

    /**
     * Change/set the default value.
     * @param defaultValue The default value to set. (set to null for no default)
     * @return The option instance.
     */
    public SingleOption setDefault(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /**
     * Check whether or not the option has a value.
     * It will check both the parsed value and the default value.
     * So, even if the parsing failed it will still be true when there is a default value.
     * @return True when there is a value or default value and false if not.
     */
    public boolean hasValue() {
        return getValueOrDefault() != null;
    }

    /**
     * Check whether or not the parsing was successful.
     * If not, there might still be a default value see {@link #hasValue()}
     * In many cases there will be an error when this is false.
     * @return True when there is a value and false if not.
     */
    public boolean success() {
        return getValue() != null;
    }


    protected boolean parseObject(Object input) {
        reset();
        if (input == null) {
            error = "Invalid input, must be a string or a " + getTypeName() + ". [type=null]";
            return false;
        }
        if (!(input instanceof String)) {
            if (input instanceof MemorySection && this instanceof SerializableOptionValue) {
                return ((SerializableOptionValue)this).parse(((MemorySection)input).getValues(true));
            }
            if (input instanceof LinkedHashMap && this instanceof SerializableOptionValue) {
                return ((SerializableOptionValue)this).parse((LinkedHashMap)input);
            }
            if (input.getClass().equals(getRawClass())) {
                value = input;
                return true;
            } else {
                error = "Invalid input, must be a string or a " + getTypeName() + ". [type=" + input.getClass().getSimpleName() + "]";
                return false;
            }
        }
        return true;
    }


    public abstract boolean parse(Object input);

    public abstract boolean parse(String input);

    public abstract boolean parse(Player player, String input);

    public abstract String getTypeName();

    public abstract Class getRawClass();

}
