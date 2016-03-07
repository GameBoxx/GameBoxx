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

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Option} with an indexed list of values.
 *
 * @param <O> The object the option parses.
 * @param <L> The ListOption class.
 * @param <S> The {@link SingleOption} class.
 */
public abstract class ListOption<O, L extends ListOption, S extends SingleOption> extends Option<L> {

    protected List<S> values = new ArrayList<>();
    protected List<O> defaultValues = new ArrayList<>();
    protected O defaultValue = null;

    protected int minValues = -1;
    protected int maxValues = -1;


    /**
     * Get the default template value.
     * <p/>
     * In most cases you'll want to use {@link #getDefault(int)}
     *
     * @return The default value. (May be {@code null}!)
     */
    public O getDefault() {
        return defaultValue;
    }

    /**
     * Set the default template value to use when parsing fails.
     * <p/>
     * This does not actually add any values to the list.
     * Use {@link #def(O...)} to set default values for indexes which will be added to the list.
     * <p/>
     * This value is just used as a template for parsing new values.
     * <p/>
     * Default values at a specified index will override this value.
     * See {@link #def(O...)} for more info.
     *
     * @param defaultValue The default template value.
     * @return this instance
     */
    public L def(O defaultValue) {
        this.defaultValue = defaultValue;
        updateList();
        return (L)this;
    }

    /**
     * Get the default value for the specified index.
     * <p/>
     * If no index specific default has been set with {@link #def(O...)},
     * it will return the default template value set with {@link #def(O)}.
     *
     * @param index The index of the default to get.
     * @return The default value for the specified index. (May be {@code null}!)
     */
    public O getDefault(int index) {
        if (index < defaultValues.size()) {
            return defaultValues.get(index);
        }
        return defaultValue;
    }

    /**
     * Set the default values.
     * <p/>
     * The list with values will be updated/populated with the values specified.
     * These values override the template value set with {@link #def(O)}
     * <p/>
     * Note that this will clear any previous set default values.
     *
     * @param defaultValues default values for this list.
     * @return this instance
     */
    public L def(O... defaultValues) {
        this.defaultValues.clear();
        if (defaultValues == null) {
            return (L)this;
        }
        for (int i = 0; i < defaultValues.length; i++) {
            this.defaultValues.add(defaultValues[i]);
        }
        updateList();
        return (L)this;
    }

    /**
     * Set the default value for the specified index in the list.
     * <p/>
     * If the index is above the size of the list it will be filled with {@code null} values up to the index.
     *
     * @param index The index to set the default value at.
     * @param defaultValue The default value for the specified index.
     * @return this instance
     */
    public L def(int index, O defaultValue) {
        for (int i = defaultValues.size(); i <= index; i++) {
            defaultValues.add(null);
        }
        this.defaultValues.set(index, defaultValue);
        updateList();
        return (L)this;
    }


    /**
     * Update the value list after setting properties like min/max values and default values.
     * <p/>
     * It will first update the default value for all the existing values.
     * Then it will add missing values and remove values that exceed the limit.
     */
    private void updateList() {
        //Update defaults
        for (int i = 0; i < values.size(); i++) {
            values.get(i).def(getDefault(i));
        }

        //Add missing default values to list.
        int index = values.size();
        while ((this.defaultValues.size() > values.size()) || (minValues > 0 && minValues > values.size())) {
            values.add(getSingleOption(index));
            index++;
        }

        //Remove values that exceed limit
        while (maxValues > 0 && values.size() > maxValues) {
            values.remove(values.size() - 1);
        }
    }



    /**
     * Get the minimum amount of required values this list must have.
     *
     * @return The minimum amount of values the list must have.
     */
    public int getMinValues() {
        return minValues;
    }

    /**
     * Set the minimum amount of required values in this list.
     * <p/>
     * Set to -1 or 0 to not have a minimum requirement.
     * <p/>
     * The list with option values will be filled with defaults up to the specified amount.
     *
     * @param minValues The amount of values this list must have.
     * @return this instance
     */
    public L minValues(int minValues) {
        this.minValues = minValues;
        updateList();
        return (L)this;
    }

    /**
     * Get the maximum amount of allowed values this list can have.
     *
     * @return The maximum amount of values this list can have.
     */
    public int getMaxValues() {
        return maxValues;
    }

    /**
     * Set the maximum amount of allowed values in this list.
     * <p/>
     * Set to -1 to not have a maximum limit.
     * <p/>
     * Values exceeding the specified amount will be removed from the list and new ones can't be set/added.
     *
     * @param maxValues The amount of values allowed in this list.
     * @return this instance
     */
    public L maxValues(int maxValues) {
        this.maxValues = maxValues;
        updateList();
        return (L)this;
    }



    /**
     * Get the cached list with single options.
     * <p/>
     * The options might not have been parsed yet.
     * <p/>
     * If no default values are set and there is no {@link #minValues(int)} set this list will be empty.
     *
     * @return The cached list with {@link SingleOption}s. (May be empty and contain non parsed values)
     */
    public List<S> getOptions() {
        return values;
    }

    /**
     * Get the cached single option value from the list at the specified index.
     *
     * @param index The list index to get the option at.
     * @return {@link SingleOption} from the specified index. (May be {@code null}!)
     */
    public S getOption(int index) {
        return values.get(index);
    }

    /**
     * Get the list with parsed object values.
     * <p/>
     * The list may contain default values and null values.
     *
     * @see #getValue(int)
     * @return List with parsed values. (May be empty and contain {@code null} values)
     */
    public List<O> getValues() {
        List<O> values = new ArrayList<>();
        for (int i = 0; i < this.values.size(); i++) {
            values.add((O)getValue(i));
        }
        return values;
    }

    /**
     * Get a value out of the list at the specified index.
     * <p/>
     * It will try to get the default value if there is no value.
     * There might not be a default value and the default might be ignored if the option has the {@link OptionFlag#REQUIRED} flag.
     *
     * @see SingleOption#getValue()
     * @param index The list index to get the value at.
     * @return The parsed value object. (May be {@code null}!)
     */
    public O getValue(int index) {
        return (O)values.get(index).getValue();
    }



    /**
     * Check whether or not the option at the specified index has a value.
     * <p/>
     * It will check both the parsed value and the default value for the specified index.
     * So, even if the parsing failed it will still be true when there is a default value.
     * <p/>
     * Use {@link #success(int)} to check if the parsing was successful.
     *
     * @param index The list index of the option to check.
     * @return Whether or not the option at the specified index has a value or default value.
     */
    public boolean hasValue(int index) {
        return values.get(index).hasValue();
    }

    /**
     * Check whether or not the parsing was successful for the option at the specified index.
     * <p/>
     * If not, there might still be a default value see {@link #hasValue(int)}
     * When false you can use {@link #getError(int)} to get the error message.
     *
     * @param index The list index of the option to check.
     * @return True when there is a non null value at the specified index.
     */
    public boolean success(int index) {
        return values.get(index).success();
    }


    /**
     * Get the parsing error for the option at the specified index.
     * <p/>
     * Use {@link #getError()} to get the general list option error instead of a specific error.
     *
     * @param index The list index of the option to get the error from.
     * @return The error message. (Empty string when there is no error)
     */
    public String getError(int index) {
        return values.get(index).getError() + " " + Msg.getString("list.index", Param.P("index", index));
    }



    /**
     * Serialize all the option values in the list to strings.
     * <p/>
     * It will use the {@link SingleOption#serialize()} method for all the options in this list.
     *
     * @return List with serialized strings for each option in the list. (May be an empty list)
     */
    public List<String> serialize() {
        List<String> values = new ArrayList<>();
        for (S option : this.values) {
            values.add(option.serialize());
        }
        return values;
    }

    /**
     * Serialize a specific option value in the list to a string.
     * <p/>
     * It will use the {@link SingleOption#serialize()} method for the option at the specified index.
     *
     * @param index The list index of the option to be serialized.
     * @return Serialized value as a string for the option value at the specified index. ({@code null} when no value)
     */
    public String serialize(int index) {
        return values.get(index).serialize();
    }

    /**
     * Get the display strings for all hte options in the list.
     * <p/>
     * It will use the {@link SingleOption#getDisplayValue()} method for all the options in this list.
     *
     * @return List with display values for each option in the list. (May be an empty list)
     */
    public List<String> getDisplayValues() {
        List<String> values = new ArrayList<>();
        for (S option : this.values) {
            values.add(option.getDisplayValue());
        }
        return values;
    }

    /**
     * Get the display string for a specific option in the list.
     * <p/>
     * It will use the {@link SingleOption#getDisplayValue()} method for the option at the specified index.
     *
     * @param index The list index of the option to get display value from.
     * @return Display value string for the option value at the specified index. ({@code null} when no value)
     */
    public String getDisplayValue(int index) {
        return values.get(index).getDisplayValue();
    }



    /**
     * Parse multiple objects for the list options.
     * <p/>
     * Previous values will be cleared and the list option will be fully reset.
     * If the list has a {@link #minValues(int)} set you must specify that amount of objects to be parsed at least.
     *
     * @see SingleOption#parse(Object)
     * @param ignoreErrors When set to true it will continue and try to parse all the objects even if it failed parsing.
     *                     The error will still be set so you can still use {@link #getError()} or {@link #getError(int)}
     * @param input The input objects that needs to be parsed.
     * @return Whether or not the parsing was successful. (will return true with ignoreErrors true even when it wasn't successfull!)
     */
    public boolean parse(boolean ignoreErrors, Object... input) {
        error = "";
        values.clear();
        updateList();
        if (input == null || input.length == 0) {
            if (minValues > 0) {
                error = Msg.getString("list.min", Param.P("min", minValues));
                return false;
            }
            return true;
        }

        for (int i = 0; i < input.length; i++) {
            if (!parse(i, input[i]) && !ignoreErrors) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parse multiple strings for the list options.
     * <p/>
     * Previous values will be cleared and the list option will be fully reset.
     * If the list has a {@link #minValues(int)} set you must specify that amount of strings to be parsed at least.
     *
     * @see SingleOption#parse(Player, String)
     * @param ignoreErrors When set to true it will continue and try to parse all the strings even if it failed parsing.
     *                     The error will still be set so you can still use {@link #getError()} or {@link #getError(int)}
     * @param input The input strings that needs to be parsed.
     * @return Whether or not the parsing was successful. (will return true with ignoreErrors true even when it wasn't successfull!)
     */
    public boolean parse(boolean ignoreErrors, String... input) {
        return parse(ignoreErrors, null, input);
    }

    /**
     * Parse multiple strings for the list options.
     * <p/>
     * Previous values will be cleared and the list option will be fully reset.
     * If the list has a {@link #minValues(int)} set you must specify that amount of strings to be parsed at least.
     *
     * @see SingleOption#parse(Player, String)
     * @param ignoreErrors When set to true it will continue and try to parse all the strings even if it failed parsing.
     *                     The error will still be set so you can still use {@link #getError()} or {@link #getError(int)}
     * @param player The player used for parsing player specific syntax. (May be {@code null})
     * @param input The input strings that needs to be parsed.
     * @return Whether or not the parsing was successful. (will return true with ignoreErrors true even when it wasn't successfull!)
     */
    public boolean parse(boolean ignoreErrors, Player player, String... input) {
        error = "";
        values.clear();
        updateList();
        if (input == null || input.length == 0) {
            if (minValues > 0) {
                error = Msg.getString("list.min", Param.P("min", minValues));
                return false;
            }
            return true;
        }

        for (int i = 0; i < input.length; i++) {
            if (!parse(player, i, input[i]) && !ignoreErrors) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parse the specified object for the option at the specified index.
     *
     * @see SingleOption#parse(Object)
     * @param index The index of the option in the list to parse the object with.
     * @param input The input object that needs to be parsed. (Must be a string or the object type of the option)
     * @return Whether or not the parsing was successful.
     */
    public boolean parse(int index, Object input) {
        if (maxValues > 0 && index > maxValues) {
            error = Msg.getString("list.max", Param.P("max", maxValues));
            return false;
        }
        for (int i = values.size(); i <= index; i++) {
            values.add(getSingleOption(i));
        }
        S option = values.get(index);
        if (!option.parse(input)) {
            error = getError(index);
            return false;
        }
        return true;
    }

    /**
     * Parse the specified string for the option at the specified index.
     *
     * @see SingleOption#parse(String)
     * @param index The index of the option in the list to parse the string with.
     * @param input The input string that needs to be parsed.
     * @return Whether or not the parsing was successful.
     */
    public boolean parse(int index, String input) {
        return parse(null, index, input);
    }

    /**
     * Parse the specified string for the option at the specified index.
     *
     * @see SingleOption#parse(Player, String)
     * @param player The player used for parsing player specific syntax. (May be {@code null})
     * @param index The index of the option in the list to parse the string with.
     * @param input The input string that needs to be parsed.
     * @return Whether or not the parsing was successful.
     */
    public boolean parse(Player player, int index, String input) {
        if (maxValues > 0 && index > maxValues) {
            error = Msg.getString("list.max", Param.P("max", maxValues));
            return false;
        }
        for (int i = values.size(); i <= index; i++) {
            values.add(getSingleOption(i));
        }
        S option = values.get(index);
        if (!option.parse(player, input)) {
            error = getError(index);
            return false;
        }
        return true;
    }



    /**
     * Used by {@link #getSingleOption(int)} to get the {@link SingleOption} used by this list option.
     *
     * @return A new {@link SingleOption} instance with modifiers copied in.
     */
    protected abstract S getSingleOption();

    /**
     * Get a new {@link SingleOption} instance for the specified index.
     * <p/>
     * The index is used to copy the default value.
     * <p/>
     * Just like {@link #clone()} the name, description, flag and modifiers will be cloned in the new option.
     *
     * @param index The list index this option will be for.
     * @return A new SingleOption instance.
     */
    public S getSingleOption(int index) {
        return (S)getSingleOption().def(getDefault(index)).name(name).desc(description).flag(flag);
    }

}
