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

package info.gameboxx.gameboxx.util.item;

import info.gameboxx.gameboxx.aliases.items.Items;
import info.gameboxx.gameboxx.nms.NMS;
import info.gameboxx.gameboxx.util.Enchant;
import info.gameboxx.gameboxx.util.Str;
import info.gameboxx.gameboxx.util.text.BookParser;
import info.gameboxx.gameboxx.util.text.TextParser;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * Custom {@link ItemStack} which allows building entities easily.
 * It's mainly used for the {@link ItemParser}
 * <p/>
 * It has methods for all item meta in a single class so you don't have to cast items all the time.
 * Calling a method on an invalid item just does nothing.
 * For example if you try to add a firework effect a diamond item nothing will happen.
 * <p/>
 * <b>There are not a ton of checks for all the methods!</b>
 * It assumes the correct input for most method like non null values and such.
 * It will just throw NPE's and other exceptions when using invalid input.
 * <p/>
 * Most of the javadoc comments are copied from the Bukkit/Spigot javadocs.
 * Not all the methods have javadoc comments but the implementation is pretty much the same as the methods in the Bukkit API.
 */
public class EItem extends ItemStack {

    /** ItemStack of type {@link Material#AIR} */
    public static EItem AIR = new EItem(Material.AIR);


    //region Construct

    /**
     * Create a new EItem from a Bukkit {@link ItemStack}
     *
     * @param itemStack A Bukkit {@link ItemStack}
     */
    public EItem(ItemStack itemStack) {
        super(itemStack == null ? new ItemStack(Material.AIR) : itemStack);
    }

    /**
     * Create a new EItem of the specified {@link Material}
     *
     * @param material The {@link Material} for the item.
     */
    public EItem(Material material) {
        this(material, (short)0, 1);
    }

    /**
     * Create a new EItem of the specified {@link Material}
     *
     * @param material The {@link Material} for the item.
     */
    public EItem(Material material, short data) {
        this(material, data, 1);
    }

    /**
     * Create a new EItem of the specified {@link Material} with the set amount.
     *
     * @param material The {@link Material} for the item.
     * @param amount The amount of items this EItem stack will have. (can be more than {@link Material#getMaxStackSize()})
     */
    public EItem(Material material, int amount) {
        this(material, (short)0, amount);
    }

    /**
     * Create a new EItem of the specified {@link Material}/data with the set amount.
     *
     * @param material The {@link Material} for the item.
     * @param data The material data value. (Also known as durability)
     * @param amount The amount of items this EItem stack will have. (can be more than {@link Material#getMaxStackSize()})
     */
    public EItem(Material material, short data, int amount) {
        super(material, amount, data);
    }

    /**
     * Create a new EItem of the specified {@link MaterialData}.
     *
     * @param materialData The materialData value.
     */
    public EItem(MaterialData materialData) {
        this(materialData.getItemType(), materialData.getData(), 1);
    }

    /**
     * Create a new EItem of the specified {@link MaterialData}.
     *
     * @param materialData The materialData value.
     * @param amount The amount of items this EItem stack will have. (can be more than {@link Material#getMaxStackSize()})
     */
    public EItem(MaterialData materialData, int amount) {
        this(materialData.getItemType(), materialData.getData(), amount);
    }
    //endregion


    /**
     * Create a clone of the EItem stack.
     *
     * @return Cloned EItem
     */
    public EItem clone() {
        return (EItem)super.clone();
    }


    // ##################################################
    // ################## UTILS/CUSTOM ##################
    // ##################################################
    //region Utils/Custom


    /**
     * Set if the item should be glowing or not.
     * Either adds or removes a custom {@link GlowEnchant} to/from the item.
     *
     * @param glow True to add the glow enchant and false to remove the glow enchant.
     * @return this instance
     */
    public EItem setGlowing(boolean glow) {
        if (glow) {
            if (!isGlowing()) {
                addUnsafeEnchantment(GlowEnchant.get(), 1);
            }
        } else {
            if (isGlowing()) {
                removeEnchantment(GlowEnchant.get());
            }
        }
        return this;
    }

    /**
     * Checks whether or not the item is glowing or not.
     * This checks if the item has the custom {@link GlowEnchant}
     *
     * @return Whether or not the item is glowing.
     */
    public boolean isGlowing() {
        return containsEnchantment(GlowEnchant.get());
    }

    //endregion



    // ##################################################
    // ##################### FLAGS ######################
    // ##################################################
    //region Flags

    /**
     * Get the set with {@link ItemFlag}s this item has.
     *
     * @return The set with {@link ItemFlag}s
     */
    public Set<ItemFlag> getItemFlags() {
        return getItemMeta().getItemFlags();
    }

    /**
     * Checks whether or not the item has the specified {@link ItemFlag}
     *
     * @param flag The {@link ItemFlag} to check for.
     * @return Whether or not he item has the specified flag.
     */
    public boolean hasItemFlag(ItemFlag flag) {
        return getItemMeta().hasItemFlag(flag);
    }

    /**
     * Add the specified {@link ItemFlag}s to the item.
     * Item flags will hide the attributes from the item for the user.
     *
     * @param flags The flags that needed to be added.
     * @return this instance.
     */
    public EItem addItemFlags(ItemFlag... flags) {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(flags);
        setItemMeta(meta);
        return this;
    }

    /**
     * Remove the specified {@link ItemFlag}s from the item.
     *
     * @param flags The flags that needed to be removed.
     * @return this instance.
     */
    public EItem removeItemFlags(ItemFlag... flags) {
        ItemMeta meta = getItemMeta();
        meta.removeItemFlags(flags);
        setItemMeta(meta);
        return this;
    }

    /**
     * Add all the {@link ItemFlag}s to the item.
     * This will hide all the attributes from the item for the user.
     *
     * @return this instance.
     */
    public EItem addItemFlags() {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(ItemFlag.values());
        setItemMeta(meta);
        return this;
    }

    /**
     * Remove all the {@link ItemFlag}s from the item.
     *
     * @return this instance.
     */
    public EItem removeItemFlags() {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(ItemFlag.values());
        setItemMeta(meta);
        return this;
    }
    //endregion



    // ##################################################
    // ###################### NAME ######################
    // ##################################################
    //region Name

    /**
     * Set the display name of the item.
     * Color codes will be formatted automatically.
     *
     * @param name The name to set
     * @return this instance
     */
    public EItem setName(String name) {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(Str.color(name));
        setItemMeta(meta);
        return this;
    }

    /**
     * Get the name of the item.
     * Unlike {@link #getDisplayName()} this value will never be {@code null}
     * <p/>
     * If the item has a display name set with {@link #setName(String)} it will return the display name.
     * <p/>
     * If not, it will return the name from the {@link Items} aliases value.
     * If the item is not registered the name will just be the material type.
     *
     * @return The name of the item. (display name or item name)
     */
    public String getName() {
        if (hasDisplayName()) {
            return getDisplayName();
        }
        return Items.getName(getData());
    }

    /**
     * Checks if the item has a custom display name set with {@link #setName(String)}
     *
     * @return Whether or not the item has a custom display name.
     */
    public boolean hasDisplayName() {
        return getItemMeta().hasDisplayName();
    }

    /**
     * Get the display name of the item.
     * <p/>
     * If the item has no custom name set this will return {@code null}
     * Use {@link #getName()} to get the item name even when it doesn't have a custom display name.
     *
     * @return The display name of the item set with {@link #setName(String)}
     */
    public String getDisplayName() {
        return getItemMeta().getDisplayName();
    }
    //endregion



    // ##################################################
    // ###################### LORE ######################
    // ##################################################
    //region Lore

    /**
     * Get the list of lore strings of the item.
     * <p/>
     * If the item doesn't have lore this will return an empty list.
     *
     * @return List with lore lines. (Empty list when no lore)
     */
    public List<String> getLore() {
        ItemMeta meta = getItemMeta();
        if (!meta.hasLore()) {
            return new ArrayList<>();
        }
        return meta.getLore();
    }

    /**
     * Get lore of the item as a string..
     * <p/>
     * If the item doesn't have lore this will return an empty string.
     * <p/>
     * Lore lines are combined with \n.
     *
     * @return List with lore lines. (Empty list when no lore)
     */
    public String getLoreString() {
        ItemMeta meta = getItemMeta();
        if (!meta.hasLore()) {
            return "";
        }
        return Str.implode(meta.getLore(), "\\n");
    }

    /**
     * Get the item lore from the specified line.
     * <p/>
     * If the item doesn't have lore or if the line number is invalid this will be null.
     *
     * @param line The line to retrieve.
     * @return The lore string from the specified line. (May be {@code null})
     */
    public String getLore(int line) {
        ItemMeta meta = getItemMeta();
        if (!meta.hasLore() || meta.getLore().size() <= line || line < 0) {
            return null;
        }
        return meta.getLore().get(line);
    }

    /**
     * Check whether or not the item has lore set.
     *
     * @return True when the item has lore.
     */
    public boolean hasLore() {
        return getItemMeta().hasLore();
    }

    /**
     * Checks if the lore of the item contains the specified string.
     * <p/>
     * Casing will be ignored and color codes too. {@link #containsLore(String, boolean, boolean)}
     *
     * @param string The string to search for in the lore.
     * @return True when the lore contains the specified string.
     */
    public boolean containsLore(String string) {
        return containsLore(string, true, true);
    }

    /**
     * Checks if the lore of the item contains the specified string.
     * <p/>
     * Color codes will be ignored. {@link #containsLore(String, boolean, boolean)}
     *
     * @param string The string to search for in the lore.
     * @param ignoreCase When set to true casing won't have to match.
     * @return True when the lore contains the specified string.
     */
    public boolean containsLore(String string, boolean ignoreCase) {
        return containsLore(string, ignoreCase, true);
    }

    /**
     * Checks if the lore of the item contains the specified string.
     * <p/>
     * Lines will be separated with '\n'
     * So, to check multiple lines you'd specify a string like 'line1\nline2'
     *
     * @param string The string to search for in the lore.
     * @param ignoreCase When set to true casing won't have to match.
     * @param ignoreColor When set to true color codes will be removed before checking.
     * @return True when the lore contains the specified string.
     */
    public boolean containsLore(String string, boolean ignoreCase, boolean ignoreColor) {
        String loreStr = Str.implode(getLore(), "\n");
        if (ignoreCase) {
            string = string.toLowerCase();
            loreStr = loreStr.toLowerCase();
        }
        if (ignoreColor) {
            string = Str.stripColor(string);
            loreStr = Str.stripColor(loreStr);
        }
        return loreStr.contains(string);
    }


    /**
     * Set the lore on the item.
     * <p/>
     * If the item already had lore this will be overwritten.
     * <p/>
     * Colors codes will be formatted for all the lines.
     * New lines (\n or \r) will be split for all lines too.
     *
     * @param lore The lore string to set.
     * @return this instance
     */
    public EItem setLore(String lore) {
        return setLore(Arrays.asList(lore));
    }

    /**
     * Set the lore on the item.
     * <p/>
     * If the item already had lore this will be overwritten.
     * <p/>
     * Colors codes will be formatted for all the lines.
     * New lines (\n or \r) will be split for all lines too.
     *
     * @param lore The lore string(s) to set.
     * @return this instance
     */
    public EItem setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    /**
     * Set the lore on the item.
     * <p/>
     * If the item already had lore this will be overwritten.
     * <p/>
     * Colors codes will be formatted for all the lines.
     * New lines (\n or \r) will be split for all lines too.
     *
     * @param lore The lore strings to set.
     * @return this instance
     */
    public EItem setLore(List<String> lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(Str.color(Str.splitLines(lore)));
        setItemMeta(meta);
        return this;
    }


    /**
     * Add lore on the item.
     * <p/>
     * If the item already had lore it will append the lines on that lore.
     * <p/>
     * Colors codes will be formatted for all the lines.
     * New lines (\n or \r) will be split for all lines too.
     *
     * @param lore The lore string(s) to add.
     * @return this instance
     */
    public EItem addLore(String... lore) {
        return addLore(Arrays.asList(lore));
    }

    /**
     * Add lore on the item.
     * <p/>
     * If the item already had lore it will append the lines on that lore.
     * <p/>
     * Colors codes will be formatted for all the lines.
     * New lines (\n or \r) will be split for all lines too.
     *
     * @param lore The lore strings to add.
     * @return this instance
     */
    public EItem addLore(List<String> lore) {
        ItemMeta meta = getItemMeta();
        List<String> prevLore = getLore();
        prevLore.addAll(Str.color(Str.splitLines(lore)));
        meta.setLore(prevLore);
        setItemMeta(meta);
        return this;
    }


    /**
     * Add lore on the item.
     * <p/>
     * If the item already had lore it will insert the lines at the specified index.
     * If the index is too high blank lines will be inserted to fill the gap.
     * <p/>
     * Colors codes will be formatted for all the lines.
     * New lines (\n or \r) will be split for all lines too.
     *
     * @param line The line number to insert the lore strings at.
     * @param lore The lore string(s) to insert.
     * @return this instance
     */
    public EItem addLore(int line, String... lore) {
        return addLore(line, Arrays.asList(lore));
    }

    /**
     * Add lore on the item.
     * <p/>
     * If the item already had lore it will insert the lines at the specified index.
     * If the index is too high blank lines will be inserted to fill the gap.
     * <p/>
     * Colors codes will be formatted for all the lines.
     * New lines (\n or \r) will be split for all lines too.
     *
     * @param line The line number to insert the lore strings at.
     * @param lore The lore strings to insert.
     * @return this instance
     */
    public EItem addLore(int line, List<String> lore) {
        if (line < 0) {
            return this;
        }
        ItemMeta meta = getItemMeta();
        List<String> prevLore = getLore();
        for (int i = prevLore.size(); i < line; i++) {
            prevLore.add("");
        }
        prevLore.addAll(line, Str.color(Str.splitLines(lore)));
        meta.setLore(prevLore);
        setItemMeta(meta);
        return this;
    }


    /**
     * Set a lore line on the item.
     * <p/>
     * If the item already had lore at the specified line it will be overwritten.
     * If the index is too high blank lines will be inserted to fill the gap.
     * <p/>
     * Colors codes will be formatted for all the lines.
     *
     * @param line The line number to set the lore string at.
     * @param lore The lore string to set.
     * @return this instance
     */
    public EItem setLore(int line, String lore) {
        if (line < 0) {
            return this;
        }
        ItemMeta meta = getItemMeta();
        List<String> prevLore = getLore();
        for (int i = prevLore.size(); i < line; i++) {
            prevLore.add("");
        }
        prevLore.set(line, Str.color(lore));
        meta.setLore(prevLore);
        setItemMeta(meta);
        return this;
    }


    /**
     * Remove all the lore from the item.
     *
     * @return this instance
     */
    public EItem removeLore() {
        ItemMeta meta = getItemMeta();
        meta.setLore(new ArrayList<String>());
        setItemMeta(meta);
        return this;
    }

    /**
     * Remove a specific line of lore from the item.
     * This will shift all other lines to fill up the gap.
     * <p/>
     * If the line number is too high nothing will happen.
     *
     * @param line The line number to remove.
     * @return this instance
     */
    public EItem removeLore(int line) {
        ItemMeta meta = getItemMeta();
        List<String> prevLore = getLore();
        if (line >= 0 && line < prevLore.size()) {
            prevLore.remove(line);
            meta.setLore(prevLore);
            setItemMeta(meta);
        }
        return this;
    }
    //endregion



    // ##################################################
    // #################### ENCHANTS ####################
    // ##################################################
    //region Enchants

    /**
     * Get a list of {@link Enchant}s
     * Use {@link #getEnchantments()} to get the map with {@link Enchantment}s
     *
     * @return List with {@link Enchant}s
     */
    public List<Enchant> getEnchants() {
        List<Enchant> enchants = new ArrayList<>();
        ItemMeta meta = getItemMeta();
        if (meta instanceof EnchantmentStorageMeta) {
            for (Map.Entry<Enchantment, Integer> entry : ((EnchantmentStorageMeta)meta).getStoredEnchants().entrySet()) {
                enchants.add(new Enchant(entry.getKey(), entry.getValue()));
            }
        } else {
            for (Map.Entry<Enchantment, Integer> entry : getEnchantments().entrySet()) {
                enchants.add(new Enchant(entry.getKey(), entry.getValue()));
            }
        }
        return enchants;
    }

    /**
     * Check whether or not the item has the specified {@link Enchantment}
     * @see {@link #containsEnchantment(Enchantment)}
     *
     * @param enchant The enchantment to check.
     * @return True when the item has the specified enchantment.
     */
    public boolean hasEnchant(Enchantment enchant) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof EnchantmentStorageMeta) {
            return ((EnchantmentStorageMeta)meta).hasStoredEnchant(enchant);
        }
        return containsEnchantment(enchant);
    }

    /**
     * Get the level of the specified {@link Enchantment}
     *
     * @param enchant The enchantment to get the level from.
     * @return The level for the specified enchantment. (0 when the item doesn't have the specified enchantment)
     */
    public int getEnchantLevel(Enchantment enchant) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof EnchantmentStorageMeta) {
            return ((EnchantmentStorageMeta)meta).getStoredEnchantLevel(enchant);
        }
        return getEnchantmentLevel(enchant);
    }

    /**
     * Check whether or not the item is enchanted.
     *
     * @return True when the item has one or more enchantments.
     */
    public boolean isEnchanted() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof EnchantmentStorageMeta) {
            return ((EnchantmentStorageMeta)meta).hasStoredEnchants();
        }
        return getItemMeta().hasEnchants();
    }

    /**
     * Enchant the item with the specified {@link Enchant}
     * <p/>
     * This will ignore max levels and application restrictions for enchantments.
     *
     * @param enchant The enchantment to add to the item.
     * @return this instance
     */
    public EItem enchant(Enchant enchant) {
        return enchant(enchant.getType(), enchant.getLevel(), true);
    }

    /**
     * Enchant the item with the specified {@link Enchant}
     * <p/>
     * When allowUnsafe is set to false and the enchantment can not be added nothing will happen!
     * Use {@link #addEnchantment(Enchantment, int)} if you want an exception.
     *
     * @param enchant The enchantment to add to the item.
     * @param allowUnsafe Whether or not to allow enchant levels above the limit and enchantments on invalid items.
     * @return this instance
     */
    public EItem enchant(Enchant enchant, boolean allowUnsafe) {
        return enchant(enchant.getType(), enchant.getLevel(), allowUnsafe);
    }

    /**
     * Enchant the item with the specified {@link Enchantment} and level.
     * <p/>
     * This will ignore max levels and application restrictions for enchantments.
     *
     * @param enchantment The enchantment type to add to the item.
     * @param level The enchantment level
     * @return this instance
     */
    public EItem enchant(Enchantment enchantment, Integer level) {
        return enchant(enchantment, level, true);
    }

    /**
     * Enchant the item with the specified {@link Enchantment} and level.
     * <p/>
     * When allowUnsafe is set to false and the enchantment can not be added nothing will happen!
     * Use {@link #addEnchantment(Enchantment, int)} if you want an exception.
     *
     * @param enchantment The enchantment type to add to the item.
     * @param level The enchantment level
     * @param allowUnsafe Whether or not to allow enchant levels above the limit and enchantments on invalid items.
     * @return this instance
     */
    public EItem enchant(Enchantment enchantment, Integer level, boolean allowUnsafe) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta)meta).addStoredEnchant(enchantment, level, allowUnsafe);
            return this;
        }
        if (allowUnsafe) {
            super.addUnsafeEnchantment(enchantment, level);
        } else {
            try {
                super.addEnchantment(enchantment, level);
            } catch(Exception e) {}
        }
        return this;
    }

    /**
     * Enchant the item with the specified {@link Enchantment}s/levels.
     * <p/>
     * This will ignore max levels and application restrictions for enchantments.
     *
     * @param enchants Map with {@link Enchantment} types and levels to add to the item.
     * @return this instance
     */
    public EItem enchant(Map<Enchantment, Integer> enchants) {
        return enchant(enchants, true);
    }

    /**
     * Enchant the item with the specified {@link Enchantment}s/levels.
     * <p/>
     * When allowUnsafe is set to false and the enchantments can not be added nothing will happen!
     * Use {@link #addEnchantments(Map)} if you want an exception.
     *
     * @param enchants Map with {@link Enchantment} types and levels to add to the item.
     * @param allowUnsafe Whether or not to allow enchant levels above the limit and enchantments on invalid items.
     * @return this instance
     */
    public EItem enchant(Map<Enchantment, Integer> enchants, Boolean allowUnsafe) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof EnchantmentStorageMeta) {
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                ((EnchantmentStorageMeta)meta).addStoredEnchant(entry.getKey(), entry.getValue(), allowUnsafe);
            }
            return this;
        }
        if (allowUnsafe) {
            super.addUnsafeEnchantments(enchants);
        } else {
            try {
                super.addEnchantments(enchants);
            } catch(Exception e) {}
        }
        return this;
    }

    /**
     * Remove the specified {@link Enchantment} from the item.
     * <p/>
     * If the item doesn't have the specified enchantment nothing will happen.
     *
     * @param enchant The enchantment to remove.
     * @return this instance
     */
    public EItem unEnchant(Enchantment enchant) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta)meta).removeStoredEnchant(enchant);
            return this;
        }
        removeEnchantment(enchant);
        return this;
    }

    /**
     * Remove all enchants from the item if it has any.
     *
     * @return this instance.
     */
    public EItem unEnchant() {
        List<Enchantment> enchants;
        ItemMeta meta = getItemMeta();
        if (meta instanceof EnchantmentStorageMeta) {
            enchants = new ArrayList<>(((EnchantmentStorageMeta)meta).getStoredEnchants().keySet());
        } else {
            enchants = new ArrayList<>(getEnchantments().keySet());
        }
        for (Enchantment enchantment : enchants) {
            unEnchant(enchantment);
        }
        return this;
    }
    //endregion



    // ##################################################
    // #################### POTIONS #####################
    // ##################################################
    //region Potions

    /**
     * Get the {@link PotionData} from the potion item.
     * <p/>
     * Items: {@link Material#POTION}, {@link Material#SPLASH_POTION}, {@link Material#LINGERING_POTION}
     *
     * @return {@link PotionData}. ({@code null} when no potion or no data)
     */
    public PotionData getPotion() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof PotionMeta) {
            return ((PotionMeta)meta).getBasePotionData();
        }
        return null;
    }

    /**
     * Set the base {@link PotionData} on the potion item.
     * <p/>
     * Items: {@link Material#POTION}, {@link Material#SPLASH_POTION}, {@link Material#LINGERING_POTION}
     *
     * @param data The {@link PotionData} to set.
     * @return this instance
     */
    public EItem setPotion(PotionData data) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof PotionMeta) {
            ((PotionMeta)meta).setBasePotionData(data);
            setItemMeta(meta);
        }
        return this;
    }


    /**
     * Get the list of custom {@link PotionEffect}s from the potion item.
     * <p/>
     * Items: {@link Material#POTION}, {@link Material#SPLASH_POTION}, {@link Material#LINGERING_POTION}
     *
     * @return List of {@link PotionEffect}s. (Empty list when no potion or no effects)
     */
    public List<PotionEffect> getPotionEffects() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof PotionMeta) {
            return ((PotionMeta)meta).getCustomEffects();
        }
        return new ArrayList<>();
    }

    /**
     * Check whether or not the item has the specified custom {@link PotionEffectType}
     * <p/>
     * Items: {@link Material#POTION}, {@link Material#SPLASH_POTION}, {@link Material#LINGERING_POTION}
     *
     * @param effect The {@link PotionEffectType} to check for.
     * @return True when the item has the specified effect. (false when no potion)
     */
    public boolean hasPotionEffect(PotionEffectType effect) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof PotionMeta) {
            return ((PotionMeta)meta).hasCustomEffect(effect);
        }
        return false;
    }

    /**
     * Check whether or not the item has custom potion effects.
     * <p/>
     * Items: {@link Material#POTION}, {@link Material#SPLASH_POTION}, {@link Material#LINGERING_POTION}
     *
     * @return True when the item has one or more custom potion effects. (false when no potion)
     */
    public boolean hasPotionEffects() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof PotionMeta) {
            return ((PotionMeta)meta).hasCustomEffects();
        }
        return false;
    }

    /**
     * Add a custom {@link PotionEffect} to the item.
     * <p/>
     * This will overwrite any previous custom effects that have been added.
     * See {@link #addPotionEffect(PotionEffect, boolean)}
     * <p/>
     * Items: {@link Material#POTION}, {@link Material#SPLASH_POTION}, {@link Material#LINGERING_POTION}
     *
     * @param effect The {@link PotionEffect} to add.
     * @return this instance
     */
    public EItem addPotionEffect(PotionEffect effect) {
        return addPotionEffect(effect, true);
    }

    /**
     * Add a custom {@link PotionEffect} to the item.
     * <p/>
     * If overwrite is false and the effect can't be applied nothing will happen.
     * <p/>
     * Items: {@link Material#POTION}, {@link Material#SPLASH_POTION}, {@link Material#LINGERING_POTION}
     *
     * @param effect The {@link PotionEffect} to add.
     * @param overwrite When true and the item has a effect of the same type it will be overwritten. If not nothing will happen.
     * @return this instance
     */
    public EItem addPotionEffect(PotionEffect effect, boolean overwrite) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof PotionMeta) {
            ((PotionMeta)meta).addCustomEffect(effect, overwrite);
            setItemMeta(meta);
        }
        return this;
    }

    /**
     * Remove the specified custom {@link PotionEffectType} from the item.
     * <p/>
     * If the item doesn't have a custom potion effect of the specified type nothing will happen.
     * <p/>
     * Items: {@link Material#POTION}, {@link Material#SPLASH_POTION}, {@link Material#LINGERING_POTION}
     *
     * @param effect The {@link PotionEffectType} to remove.
     * @return this instance.
     */
    public EItem removePotionEffect(PotionEffectType effect) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof PotionMeta) {
            ((PotionMeta)meta).removeCustomEffect(effect);
            setItemMeta(meta);
        }
        return this;
    }

    /**
     * Clear all the custom potion effects from the item if it has any.
     *
     * @return this instance
     */
    public EItem clearPotionEffects() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof PotionMeta) {
            ((PotionMeta)meta).clearCustomEffects();
            setItemMeta(meta);
        }
        return this;
    }
    //endregion



    // ##################################################
    // ##################### LEATHER ####################
    // ##################################################
    //region Leather Colors

    /**
     * Get the leather {@link Color} from the leather item.
     * <p/>
     * Items: {@link Material#LEATHER_HELMET}, {@link Material#LEATHER_CHESTPLATE}, {@link Material#LEATHER_LEGGINGS}, {@link Material#LEATHER_BOOTS}
     *
     * @return The leather {@link Color} of the item. ({@code null} when not leather)
     */
    public Color getColor() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof LeatherArmorMeta) {
            return ((LeatherArmorMeta)meta).getColor();
        }
        return null;
    }

    /**
     * Set the leather {@link Color} on the leather item.
     * <p/>
     * Items: {@link Material#LEATHER_HELMET}, {@link Material#LEATHER_CHESTPLATE}, {@link Material#LEATHER_LEGGINGS}, {@link Material#LEATHER_BOOTS}
     *
     * @param color The {@link Color} to set.
     * @return this instance
     */
    public EItem setColor(Color color) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta)meta).setColor(color);
            setItemMeta(meta);
        }
        return this;
    }
    //endregion



    // ##################################################
    // ##################### SKULLS #####################
    // ##################################################
    //region Skulls

    /**
     * Get the skull owner of the skull item.
     * <p/>
     * Items: {@link Material#SKULL_ITEM}
     *
     * @return The skull owner name. ({@code null} when no skull)
     */
    public String getOwner() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof SkullMeta) {
            return ((SkullMeta)meta).getOwner();
        }
        return null;
    }

    /**
     * Check whether or not the skull has a custom skull owner set with {@link #getOwner()}
     * <p/>
     * Items: {@link Material#SKULL_ITEM}
     *
     * @return True when the skull has a owner. (false when no skull)
     */
    public boolean hasOwner() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof SkullMeta) {
            return ((SkullMeta)meta).hasOwner();
        }
        return false;
    }

    /**
     * Set the name of the skull owner on the skull item.
     * <p/>
     * If the item is a {@link Material#SKULL_ITEM} and the data is not 3 it will be set to 3 (player skull).
     * If the name is more than 16 characters nothing will happen.
     * <p/>
     * Items: {@link Material#SKULL_ITEM}
     *
     * @param name The name of the skull owner.
     * @return
     */
    public EItem setOwner(String name) {
        if (getType() == Material.SKULL_ITEM && getDurability() != 3) {
            setDurability((short)3);
        }
        ItemMeta meta = getItemMeta();
        if (meta instanceof SkullMeta) {
            ((SkullMeta)meta).setOwner(name);
            setItemMeta(meta);
        }
        return this;
    }

    /**
     * Get the skull texture of the skull item.
     * <p/>
     * Items: {@link Material#SKULL_ITEM}
     *
     * @return The skull texture code. ({@code null} when no skull)
     */
    public String getTexture() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof SkullMeta) {
            return NMS.get().getItemUtils().getSkullTexture((SkullMeta)meta);
        }
        return null;
    }

    /**
     * Set the skull texture on the skull item.
     * <p/>
     * If the item is a {@link Material#SKULL_ITEM} and the data is not 3 it will be set to 3 (player skull).
     * <p/>
     * The texture string can have 3 different formats.
     * <ol>
     *     <li>Base64 encoded string like eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWVhMjMxNjhjNmM5NDk5ZDI2ZDM0MTZlNzVjNDFiNTZkNjY5YTcyMGQ1ZWViYTdlNTJhYjJiNDViMjRhOCJ9fX0=</li>
     *     <li>Full skin URL from the Minecraft site like http://textures.minecraft.net/texture/955d611a878e821231749b2965708cad942650672db09e26847a88e2fac2946</li>
     *     <li>Just the texture code from the Minecraft site like 955d611a878e821231749b2965708cad942650672db09e26847a88e2fac2946</li>
     * </ol>
     * <p/>
     * <b>Head databases:</b>
     * <ul>
     *     <li><a href="http://www.minecraft-heads.com/database">minecraft-heads.com</a></li>
     *     <li><a href="http://heads.freshcoal.com/heads.php">heads.freshcoal.com</a></li>
     *     <li><a href="https://headmc.net/view/blocks">headmc.net</a></li>
     * </ul>
     * <p/>
     * Items: {@link Material#SKULL_ITEM}
     *
     * @param texture The name of the skull owner.
     * @return
     */
    public EItem setTexture(String texture) {
        if (getType() == Material.SKULL_ITEM && getDurability() != 3) {
            setDurability((short)3);
        }
        ItemMeta meta = getItemMeta();
        if (meta instanceof SkullMeta) {
            NMS.get().getItemUtils().setSkullTexture((SkullMeta)meta, texture);
            setItemMeta(meta);
        }
        return this;
    }
    //endregion



    // ##################################################
    // #################### BANNERS #####################
    // ##################################################
    //region Banners

    /**
     * Get the base {@link DyeColor} from the banner item.
     * <p/>
     * Items: {@link Material#BANNER}
     *
     * @return Base {@link DyeColor} of the item. ({@code null} when no banner)
     */
    public DyeColor getBaseColor() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BannerMeta) {
            return ((BannerMeta)meta).getBaseColor();
        }
        return null;
    }

    /**
     * Set the base {@link DyeColor} on the banner item.
     * <p/>
     * Items: {@link Material#BANNER}
     *
     * @param color The base dye color to set.
     * @return this instance.
     */
    public EItem setBaseColor(DyeColor color) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BannerMeta) {
            ((BannerMeta)meta).setBaseColor(color);
            setItemMeta(meta);
        }
        return this;
    }


    /**
     * Get the list of banner {@link Pattern}s from the banner item.
     * <p/>
     * Items: {@link Material#BANNER}
     *
     * @return List of {@link Pattern}s (Empty list when no banner)
     */
    public List<Pattern> getPatterns() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BannerMeta) {
            return ((BannerMeta)meta).getPatterns();
        }
        return new ArrayList<>();
    }

    /**
     * Get the {@link Pattern} from the banner item at the specified index.
     * <p/>
     * If the index is below 0 or too high this will return null.
     * <p/>
     * Items: {@link Material#BANNER}
     *
     * @param index The index of the pattern to get.
     * @return The {@link Pattern} at the specified index from the banner item. ({@code null} when no banner or invalid index)
     */
    public Pattern getPattern(int index) {
        if (index < 0) {
            return null;
        }
        ItemMeta meta = getItemMeta();
        if (meta instanceof BannerMeta) {
            if (index >= ((BannerMeta)meta).numberOfPatterns()) {
                return null;
            }
            return ((BannerMeta)meta).getPattern(index);
        }
        return null;
    }


    /**
     * Add a {@link Pattern} on the banner item.
     * <p/>
     * Items: {@link Material#BANNER}
     *
     * @param type The {@link PatternType} to add on the banner.
     * @param color The {@link DyeColor} for the pattern.
     * @return this instance.
     */
    public EItem addPattern(PatternType type, DyeColor color) {
        return addPattern(new Pattern(color, type));
    }

    /**
     * Add a {@link Pattern} on the banner item.
     * <p/>
     * Items: {@link Material#BANNER}
     *
     * @param pattern Pattern to add on the banner.
     * @return this instance.
     */
    public EItem addPattern(Pattern pattern) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BannerMeta) {
            ((BannerMeta)meta).addPattern(pattern);
            setItemMeta(meta);
        }
        return this;
    }


    /**
     * Set the {@link Pattern}s on the banner item.
     * <p/>
     * Items: {@link Material#BANNER}
     *
     * @param patterns Patterns to set on the banner.
     * @return this instance.
     */
    public EItem setPatterns(Pattern... patterns) {
        return setPatterns(Arrays.asList(patterns));
    }

    /**
     * Set the {@link Pattern}s on the banner item.
     * <p/>
     * Items: {@link Material#BANNER}
     *
     * @param patterns Patterns to set on the banner.
     * @return this instance.
     */
    public EItem setPatterns(List<Pattern> patterns) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BannerMeta) {
            ((BannerMeta)meta).setPatterns(patterns);
            setItemMeta(meta);
        }
        return this;
    }


    /**
     * Set a {@link Pattern} on the banner item at the specified index.
     * <p/>
     * If the index is below 0 nothing will happen,
     * if it's too high the pattern will be added on the end instead.
     * <p/>
     * Items: {@link Material#BANNER}
     *
     * @param index The index to set the pattern at.
     * @param type The {@link PatternType} to set on the banner.
     * @param color The {@link DyeColor} for the pattern.
     * @return this instance.
     */
    public EItem setPattern(Integer index, PatternType type, DyeColor color) {
        return setPattern(index, new Pattern(color, type));
    }

    /**
     * Set a {@link Pattern} on the banner item at the specified index.
     * <p/>
     * If the index is below 0 nothing will happen,
     * if it's too high the pattern will be added on the end instead.
     * <p/>
     * Items: {@link Material#BANNER}
     *
     * @param index The index to set the pattern at.
     * @param pattern Pattern to set on the banner.
     * @return this instance.
     */
    public EItem setPattern(Integer index, Pattern pattern) {
        if (index < 0) {
            return this;
        }
        ItemMeta meta = getItemMeta();
        if (meta instanceof BannerMeta) {
            if (((BannerMeta)meta).getPatterns().size() > index) {
                ((BannerMeta)meta).setPattern(index, pattern);
            } else {
                ((BannerMeta)meta).addPattern(pattern);
            }
            setItemMeta(meta);
        }
        return this;
    }
    //endregion



    // ##################################################
    // #################### FIREWORK ####################
    // ##################################################
    //region Firework

    /**
     * Get the power of the firework rocket item.
     * <p/>
     * Items: {@link Material#FIREWORK}
     *
     * @return The power. (0 when no firework)
     */
    public Integer getPower() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkMeta) {
            return ((FireworkMeta)meta).getPower();
        }
        return 0;
    }

    /**
     * Set the power on the firework rocket item.
     * <p/>
     * The default power is 0,1 or 2 but it can be set above 2.
     * <p/>
     * Items: {@link Material#FIREWORK}
     *
     * @return this instance
     */
    public EItem setPower(Integer power) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkMeta) {
            ((FireworkMeta)meta).setPower(power);
            setItemMeta(meta);
        }
        return this;
    }


    /**
     * Get a list of {@link FireworkEffect}s on the firework rocket item.
     * <p/>
     * Items: {@link Material#FIREWORK}
     *
     * @return List of firework effects on the item. (Empty list when no firework)
     */
    public List<FireworkEffect> getEffects() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkMeta) {
            return ((FireworkMeta)meta).getEffects();
        }
        return new ArrayList<>();
    }

    /**
     * Get the {@link FireworkEffect} fom a firework charge item.
     * <p/>
     * Items: {@link Material#FIREWORK_CHARGE}
     *
     * @return The {@link FireworkEffect} on  the firework charge item. ({@code null} when no firework charge or no effect)
     */
    public FireworkEffect getEffect() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkEffectMeta) {
            return ((FireworkEffectMeta)meta).getEffect();
        }
        return null;
    }

    /**
     * Get the {@link FireworkEffect} at the specified index from a firework (charge) item.
     * <p/>
     * If the index is below 0 or too high this will return null.
     * <p/>
     * If it's a firework charge the index doesn't matter as long as it's not below 0.
     * You can also use {@link #getEffect()}
     * <p/>
     * Items: {@link Material#FIREWORK}, {@link Material#FIREWORK_CHARGE}
     *
     * @param index the index of the effect to get. (must be above 0 and below the amount of effects)
     * @return The {@link FireworkEffect} at the specified index.
     */
    public FireworkEffect getEffect(int index) {
        if (index < 0) {
            return null;
        }
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkMeta) {
            List<FireworkEffect> effects = ((FireworkMeta)meta).getEffects();
            if (effects.size() > index) {
                return effects.get(index);
            }
        } else if (meta instanceof FireworkEffectMeta) {
            return ((FireworkEffectMeta)meta).getEffect();
        }
        return null;
    }

    /**
     * Checks whether or not the firework (charge) item has effect(s).
     * <p/>
     * Items: {@link Material#FIREWORK}, {@link Material#FIREWORK_CHARGE}
     *
     * @return True when the item has one or more firework effects.
     */
    public boolean hasEffects() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkMeta) {
            return ((FireworkMeta)meta).hasEffects();
        } else if (meta instanceof FireworkEffectMeta) {
            return ((FireworkEffectMeta)meta).hasEffect();
        }
        return false;
    }

    /**
     * Add a {@link FireworkEffect} to the firework (charge) item.
     * <p/>
     * If the item is a firework charge the previous effect will be overwritten as it can only have one effect.
     * <p/>
     * Items: {@link Material#FIREWORK}, {@link Material#FIREWORK_CHARGE}
     *
     * @param effect The effect to add/set on the item.
     * @return this instance
     */
    public EItem addEffect(FireworkEffect effect) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkMeta) {
            ((FireworkMeta)meta).addEffect(effect);
            setItemMeta(meta);
        } else if (meta instanceof FireworkEffectMeta) {
            ((FireworkEffectMeta)meta).setEffect(effect);
            setItemMeta(meta);
        }
        return this;
    }

    /**
     * Remove a {@link FireworkEffect} from the firework (charge) item.
     * <p/>
     * If the index is below 0 or too high this won't do anything.
     * <p/>
     * If it's a firework charge the index doesn't matter as long as it's not below 0.
     * You can also use {@link #clearEffects()}
     * <p/>
     * Items: {@link Material#FIREWORK}, {@link Material#FIREWORK_CHARGE}
     *
     * @param index The index of the effect to remove.
     * @return this instance
     */
    public EItem removeEffect(int index) {
        if (index < 0) {
            return this;
        }
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkMeta) {
            if (index >= ((FireworkMeta)meta).getEffectsSize()) {
                return this;
            }
            ((FireworkMeta)meta).removeEffect(index);
            setItemMeta(meta);
        } else if (meta instanceof FireworkEffectMeta) {
            ((FireworkEffectMeta)meta).setEffect(null);
            setItemMeta(meta);
        }
        return this;
    }

    /**
     * Remove all firework effects from the firework (charge) item.
     * <p/>
     * Items: {@link Material#FIREWORK}, {@link Material#FIREWORK_CHARGE}
     *
     * @return this instance
     */
    public EItem clearEffects() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkMeta) {
            ((FireworkMeta)meta).clearEffects();
            setItemMeta(meta);
        } else if (meta instanceof FireworkEffectMeta) {
            ((FireworkEffectMeta)meta).setEffect(null);
            setItemMeta(meta);
        }
        return this;
    }
    //endregion



    // ##################################################
    // ##################### BOOKS ######################
    // ##################################################
    //region Books

    /**
     * Get the author of the book item.
     * <p/>
     * Items: {@link Material#WRITTEN_BOOK}
     *
     * @return The author of the book. ({@code null} when no written book)
     */
    public String getAuthor() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            return ((BookMeta)meta).getAuthor();
        }
        return null;
    }

    /**
     * Check whether or not the book item has an author.
     * <p/>
     * Items: {@link Material#WRITTEN_BOOK}
     *
     * @return True when the book has an author. (false when no written book)
     */
    public boolean hasAuthor() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            return ((BookMeta)meta).hasAuthor();
        }
        return false;
    }

    /**
     * Set the author of the book item.
     * <p/>
     * Items: {@link Material#WRITTEN_BOOK}
     *
     * @param author The author name.
     * @return this instance
     */
    public EItem setAuthor(String author) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            ((BookMeta)meta).setAuthor(Str.color(author));
            setItemMeta(meta);
        }
        return this;
    }


    /**
     * Get the title of the book item.
     * <p/>
     * Items: {@link Material#WRITTEN_BOOK}
     *
     * @return The title of the book. ({@code null} when no written book)
     */
    public String getTitle() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            return ((BookMeta)meta).getTitle();
        }
        return null;
    }

    /**
     * Check whether or not the book item has a title.
     * <p/>
     * Items: {@link Material#WRITTEN_BOOK}
     *
     * @return True when the book has a title. (false when no written book)
     */
    public boolean hasTitle() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            return ((BookMeta)meta).hasTitle();
        }
        return false;
    }

    /**
     * Set the title of the book item.
     * <p/>
     * Items: {@link Material#WRITTEN_BOOK}
     *
     * @param title The book title.
     * @return this instance
     */
    public EItem setTitle(String title) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            ((BookMeta)meta).setTitle(Str.color(title));
            setItemMeta(meta);
        }
        return this;
    }


    /**
     * Get contents of the book item.
     * <p/>
     * Pages are separated with \p
     * Line breaks are converted to literal \n text. (\\\\n)
     * <p/>
     * Currently the output string will be JSON formatted. (no fancy formatting yet)
     * This string can be passed back to {@link #setContent(String...)} though.
     * TODO: Format JSON to custom syntax.
     *
     * @return The content of the book as a string.
     */
    public String getContent() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {

            return Str.implode(NMS.get().getItemUtils().getBookPages((BookMeta)meta), "\\p").replaceAll("\n", "\\\\n");
        }
        return null;
    }

    /**
     * Set the contents of the book item.
     * <p/>
     * The book format can have custom JSON formatting supported by the {@link TextParser}
     * <p/>
     * Each string passed in will be a page in the book.
     * And each string on it's own can have multiple pages as well by separating the text with \p.
     * <p/>
     * New lines can be added with \n (\\\\n) and color codes will be formatted.
     *
     * @param content The content to put in the book.
     * @return this instance
     */
    public EItem setContent(String... content) {
        return setContent(Arrays.asList(content));
    }

    /**
     * Set the contents of the book item.
     * <p/>
     * The book format can have custom JSON formatting supported by the {@link TextParser}
     * <p/>
     * Each string passed in will be a page in the book.
     * And each string on it's own can have multiple pages as well by separating the text with \p.
     * <p/>
     * New lines can be added with \n (\\\\n) and color codes will be formatted.
     *
     * @param content The content to put in the book.
     * @return this instance
     */
    public EItem setContent(List<String> content) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            BookParser bookParser = new BookParser(Str.color(content));
            setItemMeta(NMS.get().getItemUtils().setBookPages((BookMeta)meta, bookParser.getJSONPages()));
        }
        return this;
    }
    //endregion
}

