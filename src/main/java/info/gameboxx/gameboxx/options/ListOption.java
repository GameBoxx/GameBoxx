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

import java.util.ArrayList;
import java.util.List;

public abstract class ListOption extends Option {

    protected List<SingleOption> value = new ArrayList<>();
    protected List<Object> defaultValues = new ArrayList<>();
    protected Object defaultValue = null;

    protected int minValues = -1;
    protected int maxValues = -1;

    public ListOption() {}

    public ListOption(String name) {
        super(name);
    }

    public ListOption(String name, Object defaultValue) {
        super(name);
        this.defaultValue = defaultValue;
    }


    /**
     * Reset the option.
     * It will reset the error message and the list with values.
     * Use this when you want to parse a new list of objects.
     */
    public void reset() {
        error = "";
        value = new ArrayList<>();
    }


    /**
     * Get the cached list with values.
     * The list may contain default values if the parsing failed.
     * @return The cached list with values. (May be empty)
     */
    public List<SingleOption> getOptions() {
        return value;
    }


    /**
     * Get the default value.
     * This value will be added to the list when parsing of a value failed.
     *
     * <b>In most cases you'll want to use {@link #getDefault(int)}</b>
     *
     * @return The default value. (May be {@code null}!)
     */
    public Object getDefault() {
        return defaultValue;
    }

    /**
     * Set the default value to use when parsing fails.
     * The value must of the same type as the raw class type of the single option. So a IntList can only have an integer as default option here.
     *
     * This does not actually add any values to the list.
     * Use {@link #setDefaults(Object...)} to set default values for indexes which will be added to the list.
     *
     * @param defaultValue The default value. (This value will be added to the list when parsing of a value failed.)
     * @return The option instance.
     */
    public ListOption setDefault(Object defaultValue) {
        if (defaultValue == null || !defaultValue.getClass().equals(getSingleOption(0).getRawClass())) {
            this.defaultValue = null;
        } else {
            this.defaultValue = defaultValue;
        }
        updateList();
        return this;
    }

    /**
     * Get the default value for the specified index.
     * If no index specific default has been set with {@link #setDefaults(Object...)} it will return the default.
     * @param index The index of the default to get.
     * @return The default value for the specified index. (May be {@code null}!)
     */
    public Object getDefault(int index) {
        if (index < defaultValues.size()) {
            return defaultValues.get(index);
        }
        return defaultValue;
    }

    /**
     * Set the default values.
     * The order you specify the values will be the indexes of the the values.
     * The value must of the same type as the raw class type of the single option. So a IntList can only have an integer as default option here.
     * @param defaultValues Object array with default values for this list.
     * @return The option instance.
     */
    public ListOption setDefaults(Object... defaultValues) {
        this.defaultValues.clear();
        if (defaultValues == null) {
            return this;
        }
        for (int i = 0; i < defaultValues.length; i++) {
            Object obj = defaultValues[i];
            if (obj == null || !obj.getClass().equals(getSingleOption(0).getRawClass())) {
                this.defaultValues.add(null);
            } else {
                this.defaultValues.add(obj);
            }
        }
        updateList();
        return this;
    }

    /**
     * Get the minimum amount of required values this list must have.
     * @return The minimum amount of values the list must have.
     */
    public int getMinValues() {
        return minValues;
    }

    /**
     * Set the minimum amount of required values in this list.
     * Set to -1 or 0 to not have a minimum requirement.
     * @param minValues The amount of values this list must have.
     * @return The option instance.
     */
    public ListOption setMinValues(int minValues) {
        this.minValues = minValues;
        updateList();
        return this;
    }

    /**
     * Get the maximum amount of allowed values this list can have.
     * @return The maximum amount of values this list can have.
     */
    public int getMaxValues() {
        return maxValues;
    }

    /**
     * Set the maximum amount of allowed values in this list.
     * Set to -1 to not have a maximum limit.
     * @param maxValues The amount of values allowed in this list.
     * @return The option instance.
     */
    public ListOption setMaxValues(int maxValues) {
        this.maxValues = maxValues;
        updateList();
        return this;
    }

    /**
     * Update the value list after setting properties like min/max values and default values.
     * It will first update the default value for all the existing values.
     * Then it will add missing values and remove values that exceed the limit.
     */
    private void updateList() {
        //Update defaults
        for (int i = 0; i < value.size(); i++) {
            value.get(i).setDefault(getDefault(i));
        }

        //Add missing default values to list.
        int index = value.size();
        while ((this.defaultValues.size() > value.size()) || (minValues > 0 && minValues > value.size())) {
            value.add(getSingleOption(index));
            index++;
        }

        //Remove values that exceed limit
        while (maxValues > 0 && value.size() > maxValues) {
            value.remove(value.size()-1);
        }
    }

    public boolean parse(boolean ignoreErrors, Object... input) {
        if (input == null || input.length == 0) {
            if (minValues > 0) {
                error = "List must have at least " + minValues + " values!";
                return false;
            }
            return true;
        }

        for (int i = 0; i < input.length; i++) {
            if (maxValues > 0 && i > maxValues) {
                error = "List can not have more than " + maxValues + " values!";
                return false;
            }
            Object obj = input[i];

            SingleOption option = getSingleOption(i);
            option.parse(obj);
            if (option.hasError() && (!ignoreErrors || !option.hasValue())) {
                error = option.getError() + " [index:" + i + "]";
                return false;
            }
            if (!option.hasValue()) {
                error = "Unknown parsing error. [index:" + i + "]";
                return false;
            }
            value.add(option);
        }

        return true;
    }

    public boolean parse(boolean ignoreErrors, String... input) {
        return parse(ignoreErrors, null, input);
    }

    public boolean parse(boolean ignoreErrors, Player player, String... input) {
        reset();
        if (input == null || input.length == 0) {
            if (minValues > 0) {
                error = "List must have at least " + minValues + " values!";
                return false;
            }
            return true;
        }

        for (int i = 0; i < input.length; i++) {
            if (maxValues > 0 && i > maxValues) {
                error = "List can not have more than " + maxValues + " values!";
                return false;
            }
            String str = input[i];

            SingleOption option = getSingleOption(i);
            option.parse(player, str);
            if (option.hasError() && (!ignoreErrors || !option.hasValue())) {
                error = option.getError() + " [index:" + i + "]";
                return false;
            }
            if (!option.hasValue()) {
                error = "Unknown parsing error. [index:" + i + "]";
                return false;
            }
            value.add(option);
        }

        return true;
    }

    public boolean parse(int index, Object input) {
        if (maxValues > 0 && index > maxValues) {
            error = "List can not have more than " + maxValues + " values!";
            return false;
        }
        if (index >= value.size()) {
            value.add(getSingleOption(index));
            index = value.size()-1;
        }
        SingleOption option = value.get(index);
        boolean result = option.parse(input);
        error = option.getError();
        return result;
    }

    public boolean parse(int index, String input) {
        return parse(null, index, input);
    }

    public boolean parse(Player player, int index, String input) {
        if (maxValues > 0 && index > maxValues) {
            error = "List can not have more than " + maxValues + " values!";
            return false;
        }
        if (index >= value.size()) {
            value.add(getSingleOption(index));
            index = value.size()-1;
        }
        SingleOption option = value.get(index);
        boolean result = option.parse(player, input);
        error = option.getError();
        return result;
    }

    public boolean hasError(int index) {
        if (index >= value.size()) {
            throw new IndexOutOfBoundsException();
        }
        if (value.get(index) == null) {
            return true;
        }
        return value.get(index).hasError();
    }

    public String getError(int index) {
        if (index >= value.size() || (maxValues > 0 && index > maxValues)) {
            throw new IndexOutOfBoundsException();
        }
        if (value.get(index) == null) {
            if (!hasError()) {
                return "Unknown parsing error. [Index:" + index + "]";
            }
            return getError();
        }
        return value.get(index).getError();
    }

    public boolean hasValue(int index) {
        if (index >= value.size() || (maxValues > 0 && index > maxValues)) {
            throw new IndexOutOfBoundsException();
        }
        if (value.get(index) == null) {
            return false;
        }
        return value.get(index).hasValue();
    }

    public boolean success(int index) {
        if (index >= value.size() || (maxValues > 0 && index > maxValues)) {
            throw new IndexOutOfBoundsException();
        }
        if (value.get(index) == null) {
            return false;
        }
        return value.get(index).success();
    }

    protected Object getValueOrDefault(int index) {
        if (index >= value.size() || (maxValues > 0 && index > maxValues)) {
            throw new IndexOutOfBoundsException();
        }
        if (value.get(index) == null) {
            return null;
        }
        return value.get(index).getValueOrDefault();
    }

    public List<String> serialize() {
        List<String> values = new ArrayList<>();
        for (SingleOption option : value) {
            values.add(option.serialize());
        }
        return values;
    }

    public String serialize(int index) {
        if (index >= value.size() || (maxValues > 0 && index > maxValues)) {
            throw new IndexOutOfBoundsException();
        }
        if (value.get(index) == null) {
            return null;
        }
        return value.get(index).serialize();
    }

    public List<String> getDisplayValues() {
        List<String> values = new ArrayList<>();
        for (SingleOption option : value) {
            values.add(option.getDisplayValue());
        }
        return values;
    }

    public String getDisplayValue(int index) {
        if (index >= value.size() || (maxValues > 0 && index > maxValues)) {
            throw new IndexOutOfBoundsException();
        }
        if (value.get(index) == null) {
            return null;
        }
        return value.get(index).getDisplayValue();
    }

    public abstract List<?> getValues();

    public abstract Object getValue(int index);

    public abstract SingleOption getSingleOption(int index);

}
