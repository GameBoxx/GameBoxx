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
import info.gameboxx.gameboxx.util.Str;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * A {@link Option} map with string keys.
 *
 * @param <O> The object the option parses.
 * @param <M> The MapOption class.
 * @param <S> The {@link SingleOption} class.
 */
public abstract class MapOption<O, M extends MapOption, S extends SingleOption> extends Option<M> {

    protected Map<String, S> values = new HashMap<>();
    protected Map<String, O> defaultValues = new HashMap<>();
    protected O defaultValue = null;

    protected List<String> requiredKeys = new ArrayList<>();
    protected boolean customKeys = true;


    /**
     * Get the default template value.
     * <p/>
     * In most cases you'll want to use {@link #getDefault(String)}
     *
     * @return The default value. (May be {@code null}!)
     */
    public O getDefault() {
        return defaultValue;
    }

    /**
     * Set the default template value to use when parsing fails.
     * <p/>
     * This does not actually add any values to the map.
     * Use {@link #def(Map<String, O>)} to set default values for keys which will be added to the map.
     * <p/>
     * This value is just used as a template for parsing new values.
     * <p/>
     * Default values at a specified keys will override this value.
     * See {@link #def(Map<String, O>)} for more info.
     *
     * @param defaultValue The default template value.
     * @return this instance
     */
    public M def(O defaultValue) {
        this.defaultValue = defaultValue;
        updateMap();
        return (M)this;
    }

    /**
     * Get the default value for the specified key.
     * <p/>
     * If no key specific default has been set with {@link #def(Map<String, O>)},
     * it will return the default template value set with {@link #def(O)}.
     *
     * @param key The key of the default to get.
     * @return The default value for the specified key. (May be {@code null}!)
     */
    public O getDefault(String key) {
        if (defaultValues.containsKey(key)) {
            return defaultValues.get(key);
        }
        return defaultValue;
    }

    /**
     * Set the default values.
     * <p/>
     * The map with values will be updated/populated with the default values specified.
     * These values override the template value set with {@link #def(O)}
     * <p/>
     * Note that this will clear any previous set default values.
     *
     * @param defaultValues default values for this map.
     * @return this instance
     */
    public M def(Map<String, O> defaultValues) {
        this.defaultValues.clear();
        if (defaultValues == null) {
            return (M)this;
        }
        this.defaultValues.putAll(defaultValues);
        updateMap();
        return (M)this;
    }

    /**
     * Set the default value for the specified key in the map.
     *
     * @param key The key to set the default value for.
     * @param defaultValue The default value for the specified key.
     * @return this instance
     */
    public M def(String key, O defaultValue) {
        this.defaultValues.put(key, defaultValue);
        updateMap();
        return (M)this;
    }


    /**
     * Update the value map after setting properties like default values and required values etc.
     * <p/>
     * It will first update the default value for all the existing values.
     * Then it will remove values with custom keys when {@link #customKeys(boolean)} is set to false.
     */
    private void updateMap() {
        //Update defaults
        for (Map.Entry<String, O> def : defaultValues.entrySet()) {
            if (!values.containsKey(def.getKey())) {
                values.put(def.getKey(), getSingleOption(def.getKey()));
            } else {
                values.get(def.getKey()).def(def.getValue());
            }
        }

        //Remove custom keys
        if (!customKeys && requiredKeys.size() > 0 && values.size() > 0) {
            Map<String, S> valuesClone = new HashMap<>(values);
            for (Map.Entry<String, S> entry : valuesClone.entrySet()) {
                if (!requiredKeys.contains(entry.getKey())) {
                    values.remove(entry.getKey());
                }
            }
        }
    }



    /**
     * Get the list with required keys that must be set for this map.
     *
     * @return List with required keys that must be set.
     */
    public List<String> getRequiredKeys() {
        return requiredKeys;
    }

    /**
     * Set the list with required that must be set for this map.
     * <p/>
     * When {@link #customKeys(boolean)} is set to false only the required keys can be parsed.
     * <p/>
     * It will produce an error when parsing multiple values and not all required keys are being parsed.
     *
     * @param requiredKeys The list with required keys that must be set for the map.
     * @return this instance
     */
    public M keys(List<String> requiredKeys) {
        this.requiredKeys = requiredKeys;
        return (M)this;
    }

    /**
     * Check whether or not the map can have custom keys. (true by default)
     * </p>
     * When false the map can only have key that have been set with {@link #keys(List)}
     *
     * @return Whether or not the map can have custom keys.
     */
    public boolean canHaveCustomKeys() {
        return customKeys;
    }

    /**
     * Set whether or not the map can have custom keys. (true by default)
     * </p>
     * When false the map can only have key that have been set with {@link #keys(List)}
     *
     * @param canHaveCustomKeys True to allow custom keys and False to only allow the required keys to be set.
     * @return this instance
     */
    public M customKeys(boolean canHaveCustomKeys) {
        this.customKeys = canHaveCustomKeys;
        return (M)this;
    }



    /**
     * Get the cached map with single options.
     * <p/>
     * The options might not have been parsed yet.
     * <p/>
     * If no default values are set with {@link #def(String, Object)} or {@link #def(Map)} and nothing has been parsed the map will be empty.
     *
     * @return The cached map with {@link SingleOption}s. (May be empty and contain non parsed values)
     */
    public Map<String, S> getOptions() {
        return values;
    }

    /**
     * Get the cached single option value from the map at the specified key.
     *
     * @param key The map key to get the option at.
     * @return {@link SingleOption} at the specified key. (May be {@code null}!)
     */
    public S getOption(String key) {
        if (!values.containsKey(key)) {
            return null;
        }
        return values.get(key);
    }

    /**
     * Get the map with parsed object values.
     * <p/>
     * The map may contain default values and null values.
     * <p/>
     * If no default values are set with {@link #def(String, Object)} or {@link #def(Map)} and nothing has been parsed the map will be empty.
     *
     * @see #getValue(String)
     * @return Map with parsed values. (May be empty and contain {@code null} values)
     */
    public Map<String, O> getValues() {
        Map<String, O> values = new HashMap<>();
        for (Map.Entry<String, S> option : this.values.entrySet()) {
            values.put(option.getKey(), (O)option.getValue().getValue());
        }
        return values;
    }

    /**
     * Get a value out of the map at the specified key.
     * <p/>
     * It will try to get the default value if there is no value.
     * There might not be a default value and the default might be ignored if the option has the {@link OptionFlag#REQUIRED} flag.
     *
     * @see SingleOption#getValue()
     * @param key The map key to get the value at.
     * @return The parsed value object. (May be {@code null}!)
     */
    public O getValue(String key) {
        if (!values.containsKey(key)) {
            return null;
        }
        return (O)values.get(key).getValue();
    }



    /**
     * Check whether or not the option at the specified key has a value.
     * <p/>
     * It will check both the parsed value and the default value for the specified key.
     * So, even if the parsing failed it will still be true when there is a default value.
     * <p/>
     * Use {@link #success(String)} to check if the parsing was successful.
     *
     * @param key The map key of the option to check.
     * @return Whether or not the option at the specified key has a value or default value.
     */
    public boolean hasValue(String key) {
        if (!values.containsKey(key)) {
            return false;
        }
        return values.get(key).hasValue();
    }

    /**
     * Check whether or not the parsing was successful for the option at the specified key.
     * <p/>
     * If not, there might still be a default value see {@link #hasValue(String)}
     * When false you can use {@link #getError(String)} to get the error message.
     *
     * @param key The map key of the option to check.
     * @return True when there is a non null value at the specified key.
     */
    public boolean success(String key) {
        if (!values.containsKey(key)) {
            return false;
        }
        return values.get(key).success();
    }


    /**
     * Get the parsing error for the option at the specified key.
     * <p/>
     * Use {@link #getError()} to get the general map option error instead of a specific error.
     *
     * @param key The map key of the option to get the error from.
     * @return The error message. (Empty string when there is no error)
     */
    public String getError(String key) {
        if (!values.containsKey(key)) {
            return Msg.getString("map.invalid-key", Param.P("input", key), Param.P("keys", Str.implode(values.keySet())));
        }
        return values.get(key).getError() + " " + Msg.getString("map.key", Param.P("key", key));
    }



    /**
     * Serialize all the option values in the map to strings.
     * <p/>
     * It will use the {@link SingleOption#serialize()} method for all the options in this list.
     *
     * @return Map with serialized strings for each option in the map. (May be an empty map)
     */
    public Map<String, String> serialize() {
        Map<String, String> values = new HashMap<>();
        for (Map.Entry<String, S> option : this.values.entrySet()) {
            values.put(option.getKey(), option.getValue().serialize());
        }
        return values;
    }

    /**
     * Serialize a specific option value in the map to a string.
     * <p/>
     * It will use the {@link SingleOption#serialize()} method for the option at the specified key.
     *
     * @param key The map key of the option to be serialized.
     * @return Serialized value as a string for the option value at the specified key. ({@code null} when no value)
     */
    public String serialize(String key) {
        if (!values.containsKey(key)) {
            return null;
        }
        return values.get(key).serialize();
    }

    /**
     * Get the display strings for all hte options in the map.
     * <p/>
     * It will use the {@link SingleOption#getDisplayValue()} method for all the options in this map.
     *
     * @return Map with display values for each option in the map. (May be an empty map)
     */
    public Map<String, String> getDisplayValues() {
        Map<String, String> values = new HashMap<>();
        for (Map.Entry<String, S> option : this.values.entrySet()) {
            values.put(option.getKey(), option.getValue().getDisplayValue());
        }
        return values;
    }

    /**
     * Get the display string for a specific option in the map.
     * <p/>
     * It will use the {@link SingleOption#getDisplayValue()} method for the option at the specified key.
     *
     * @param key The map key of the option to get display value from.
     * @return Display value string for the option value at the specified key. ({@code null} when no value)
     */
    public String getDisplayValue(String key) {
        if (!values.containsKey(key)) {
            return null;
        }
        return values.get(key).getDisplayValue();
    }



    /**
     * Parse multiple objects for the map options.
     * <p/>
     * Previous values will be cleared and the map option will be fully reset.
     * <p/>
     * If the map has a {@link #keys(List)} set you must specify all those required keys.
     * If the map has {@link #customKeys(boolean)} set to false the specified value keys may only be the ones from the required keys.
     *
     * @see SingleOption#parse(Object)
     * @param ignoreErrors When set to true it will continue and try to parse all the objects even if it failed parsing.
     *                     The error will still be set so you can still use {@link #getError()} or {@link #getError(String)}
     * @param input The input objects that needs to be parsed.
     * @return Whether or not the parsing was successful.
     */
    public boolean parse(boolean ignoreErrors, Map<String, Object> input) {
        error = "";
        values.clear();
        updateMap();
        if (input == null || input.values().size() == 0) {
            return true;
        }
        for (String key : input.keySet()) {
            if (!requiredKeys.contains(key) && !customKeys) {
                error = Msg.getString("map.no-custom", Param.P("keys", Str.implode(requiredKeys)));
                return false;
            }
        }
        for (String key : requiredKeys) {
            if (!input.keySet().contains(key)) {
                error = Msg.getString("map.missing-required", Param.P("key", key));
                return false;
            }
        }
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            if (!parse(entry.getKey(), entry.getValue()) && !ignoreErrors) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parse multiple strings for the map options.
     * <p/>
     * Previous values will be cleared and the map option will be fully reset.
     * <p/>
     * If the map has a {@link #keys(List)} set you must specify all those required keys.
     * If the map has {@link #customKeys(boolean)} set to false the specified value keys may only be the ones from the required keys.
     *
     * @see SingleOption#parse(Player, String)
     * @param ignoreErrors When set to true it will continue and try to parse all the strings even if it failed parsing.
     *                     The error will still be set so you can still use {@link #getError()} or {@link #getError(String)}
     * @param player The player used for parsing player specific syntax. (May be {@code null})
     * @param input The input strings that needs to be parsed.
     * @return Whether or not the parsing was successful.
     */
    public boolean parse(boolean ignoreErrors, Player player, Map<String, String> input) {
        error = "";
        values.clear();
        updateMap();
        if (input == null || input.values().size() == 0) {
            return true;
        }
        for (String key : input.keySet()) {
            if (!requiredKeys.contains(key) && !customKeys) {
                error = Msg.getString("map.no-custom", Param.P("keys", Str.implode(requiredKeys)));
                return false;
            }
        }
        for (String key : requiredKeys) {
            if (!input.keySet().contains(key)) {
                error = Msg.getString("map.missing-required", Param.P("key", key));
                return false;
            }
        }
        for (Map.Entry<String, String> entry : input.entrySet()) {
            if (!parse(player, entry.getKey(), entry.getValue()) && !ignoreErrors) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parse the specified object for the option at the specified key.
     * <p/>
     * If the map has {@link #customKeys(boolean)} set to false the specified key may only be the ones from the required keys.
     *
     * @see SingleOption#parse(Object)
     * @param key The key of the option in the map to parse the object with.
     * @param input The input object that needs to be parsed. (Must be a string or the object type of the option)
     * @return Whether or not the parsing was successful.
     */
    public boolean parse(String key, Object input) {
        if (!customKeys && !requiredKeys.contains(key)) {
            error = Msg.getString("map.no-custom", Param.P("keys", Str.implode(requiredKeys)));
        }
        S option = values.get(key);
        if (option == null) {
            option = values.put(key, getSingleOption(key));
        }
        if (!option.parse(input)) {
            error = getError(key);
            return false;
        }
        return true;
    }

    /**
     * Parse the specified string for the option at the specified key.
     * <p/>
     * If the map has {@link #customKeys(boolean)} set to false the specified key may only be the ones from the required keys.
     *
     * @see SingleOption#parse(String)
     * @param key The key of the option in the map to parse the string with.
     * @param input The input string that needs to be parsed.
     * @return Whether or not the parsing was successful.
     */
    public boolean parse(String key, String input) {
        return parse(null, key, input);
    }

    /**
     * Parse the specified string for the option at the specified key.
     * <p/>
     * If the map has {@link #customKeys(boolean)} set to false the specified key may only be the ones from the required keys.
     *
     * @see SingleOption#parse(Player, String)
     * @param player The player used for parsing player specific syntax. (May be {@code null})
     * @param key The key of the option in the map to parse the string with.
     * @param input The input string that needs to be parsed.
     * @return Whether or not the parsing was successful.
     */
    public boolean parse(Player player, String key, String input) {
        if (!customKeys && !requiredKeys.contains(key)) {
            error = Msg.getString("map.no-custom", Param.P("keys", Str.implode(requiredKeys)));
        }
        S option = values.get(key);
        if (option == null) {
            option = values.put(key, getSingleOption(key));
        }
        if (!option.parse(player, input)) {
            error = getError(key);
            return false;
        }
        return true;
    }



    /**
     * Used by {@link #getSingleOption(String)} to get the {@link SingleOption} used by this map option.
     *
     * @return A new {@link SingleOption} instance with modifiers copied in.
     */
    protected abstract S getSingleOption();

    /**
     * Get a new {@link SingleOption} instance for the specified key.
     * <p/>
     * The key is used to copy the default value.
     * <p/>
     * Just like {@link #clone()} the name, description, flag and modifiers will be cloned in the new option.
     *
     * @param key The map key this option will be for.
     * @return A new SingleOption instance.
     */
    public S getSingleOption(String key) {
        return (S)getSingleOption().def(getDefault(key)).name(name).desc(description).flag(flag);
    }

    /**
     * Used for the {@link #clone()} method to copy data in a new instance.
     * It will copy the name, description, flag and the default value.
     *
     * @param option The new option to clone the data into.
     * @return The specified option.
     */
    protected M cloneData(M option) {
        return (M)super.cloneData(option).def(defaultValue).def(defaultValues).keys(requiredKeys).customKeys(customKeys);
    }
}
