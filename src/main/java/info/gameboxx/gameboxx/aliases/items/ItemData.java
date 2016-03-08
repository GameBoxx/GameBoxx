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

package info.gameboxx.gameboxx.aliases.items;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * ItemData used in {@link Items} for item aliases and display names etc.
 */
public class ItemData {

    private String name;
    private Material type;
    private int id;
    private short data;
    private Set<String> aliases = new HashSet<>();
    private Set<String> matchStrings = new HashSet<>();

    /**
     * Create new ItemData.
     * There is no point creating this manually as it's used in {@link Items}
     *
     * @param name The display name of the item.
     * Should have spaces between words and camel cased.
     * For example: Diamond Sword
     * @param type The material type
     * @param data The material data
     * @param aliases Array with aliases
     * Aliases should be all lowercased and not contain any spaces!
     * May be null or empty.
     */
    public ItemData(String name, Material type, short data, String... aliases) {
        this.name = name;
        this.type = type;
        this.data = data;
        if (aliases != null) {
            this.aliases.addAll(Arrays.asList(aliases));
        }
        this.id = type.getId();

        matchStrings.add(name.toLowerCase().replace(" ", ""));
        matchStrings.add(name.toLowerCase().replace(" ", "_"));
        matchStrings.addAll(Arrays.asList(aliases));
        matchStrings.add(type.toString().toLowerCase());
        matchStrings.add(type.toString().toLowerCase().replace("_", ""));
    }

    /**
     * Get the display name for the item.
     * Each word is capitalized and each word is seperated with a space.
     * For example: 'Diamond Sword'
     *
     * @return The display name for the item.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the display name for the item.
     * This should only be called to override the default value or if it doesn't have a default.
     * Use {@link Items#register(String, Material, short, String...)} for registering/updating items!
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the key for the item which is used for looking up items.
     * The key has the material followed by a dash and then the data.
     * For example: 'DIAMOND_SWORD-0' or 'WOOL-9'
     *
     * @return The key used to identify the item with {material}-{data}
     */
    public String getKey() {
        return type.toString().toLowerCase() + "-" + data;
    }

    /**
     * Get the {@link Material} type for this item.
     *
     * @return The {@link Material}
     */
    public Material getType() {
        return type;
    }

    /**
     * Get the material ID for this item.
     * For example for STONE this would be 1 and AIR 0.
     * <b>Note that material ID's are deprecated in the API so you should avoid using ID's!</b>
     *
     * @return The material ID.
     * @see Material#getId()
     */
    public int getId() {
        return id;
    }

    /**
     * Get the data for this item.
     * Also known as durability or damage values.
     * For example to get cyan wool the data value would be 9.
     *
     * @return The data value
     */
    public short getData() {
        return data;
    }

    /**
     * Get the set with aliases.
     * This set does not contain the material name, display name etc.
     * It only contains the custom aliases.
     *
     * @return Set with strings for item aliases. (May be empty for some items)
     */
    public Set<String> getAliases() {
        return aliases;
    }

    /**
     * Add the specified aliases to this item.
     * These aliases don't get registered in the {@link Items} class for alias matching!
     * Use {@link Items#register(String, Material, short, String...)}
     * This just add the specified aliases if the item is already registered.
     *
     * @param aliases Array with aliases
     * Aliases should be all lowercased and not contain any spaces!
     * May be null or empty.
     */
    public void addAliases(String... aliases) {
        if (aliases != null) {
            this.aliases.addAll(Arrays.asList(aliases));
            matchStrings.addAll(Arrays.asList(aliases));
        }
    }

    /**
     * Get the set with all the strings that will match this item.
     * This contains all the aliases, the display name and the material string.
     * For the display name and material string it contains a string without underscores and a string with underscores.
     * There are no spaces in match strings and they are all lowercase.
     *
     * @return Set with all the strings that match this item.
     */
    public Set<String> getMatchStrings() {
        return matchStrings;
    }

    /**
     * Check whether or not this item matched with the specified match string.
     *
     * @param matchString The string to search for to check for a match.
     * @return True when there is a match and false if not.
     * @see #getMatchStrings()
     */
    public boolean matches(String matchString) {
        return matchStrings.contains(matchString.toLowerCase());
    }
}
