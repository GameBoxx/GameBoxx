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

import info.gameboxx.gameboxx.util.Parse;
import info.gameboxx.gameboxx.util.Str;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * Custom ItemStack that supports item building.
 * For example: new EItem(Material.STONE).setName("&amp;atest").setLore("lore", "more lore")
 */
public class EItem extends ItemStack {

    public static EItem AIR = new EItem(Material.AIR);

    // Constructors

    public EItem(ItemStack itemStack) {
        super(itemStack == null ? new ItemStack(Material.AIR) : itemStack);
    }

    public EItem(Material material) {
        super(material, 1);
    }

    public EItem(Material material, Integer amount) {
        super(material, amount);
    }

    public EItem(Material material, Integer amount, Short durability) {
        super(material, amount, durability);
    }


    // ##################################################
    // #################### GENERAL #####################
    // ##################################################

    /** Clone the EItem. */
    public EItem clone() {
        return (EItem)super.clone();
    }

    /**
     * Add all ItemFlags to the item.
     * It will hide all tooltips like enchants, attributes, effects etc.
     */
    public EItem addAllFlags(Boolean add) {
        ItemMeta meta = getItemMeta();
        for (ItemFlag itemFlag : ItemFlag.values()) {
            if (add) {
                if (!meta.hasItemFlag(itemFlag)) {
                    meta.addItemFlags(itemFlag);
                }
            } else {
                if (meta.hasItemFlag(itemFlag)) {
                    meta.removeItemFlags(itemFlag);
                }
            }
        }
        setItemMeta(meta);
        return this;
    }

    /** Add a specific ItemFlag to the item to hide a certain tooltip. */
    public EItem addFlags(ItemFlag... flags) {
        ItemMeta meta = getItemMeta();
        for (ItemFlag itemFlag : flags) {
            if (!meta.hasItemFlag(itemFlag)) {
                meta.addItemFlags(itemFlag);
            }
        }
        setItemMeta(meta);
        return this;
    }

    /** Add the enchantment glow effect on the item. (Doesn't work for all items like skulls for example not) */
    public EItem makeGlowing(Boolean glow) {
        //TODO: Check for protocol lib and if enabled use protocol lib.
        //TODO: Use custom enchantment.
        ItemMeta meta = getItemMeta();
        if (glow) {
            if (!meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            setItemMeta(meta);
            if (getType() == Material.FISHING_ROD) {
                addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 0);
            } else {
                addUnsafeEnchantment(Enchantment.LURE, 0);
            }
        } else {
            if (meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            setItemMeta(meta);
            if (getType() == Material.FISHING_ROD) {
                removeEnchantment(Enchantment.ARROW_INFINITE);
            } else {
                removeEnchantment(Enchantment.LURE);
            }
        }
        return this;
    }


    // ##################################################
    // ###################### NAME ######################
    // ##################################################

    /** Set the display name of the item. It will automatically format colors */
    public EItem setName(String name) {
        return setName(name, true);
    }

    /** Set the display name of the item. If color is set to true it will automatically format colors. */
    public EItem setName(String name, Boolean color) {
        ItemMeta meta = getItemMeta();
        name = name.replaceAll("(?<!_)_(?!_)", " ");
        meta.setDisplayName(color ? Str.color(name) : name);
        setItemMeta(meta);
        return this;
    }

    /** Get the display name of the item. */
    public String getName() {
        if (!getItemMeta().hasDisplayName()) {
            return getType().toString().toLowerCase().replace("_", " ");
        }
        return getItemMeta().getDisplayName();
    }


    // ##################################################
    // ###################### LORE ######################
    // ##################################################

    //Set

    /**
     * Set the lore lines. It will automatically format colors.
     * New lines are split by the | symbol.
     */
    public EItem setLore(String lore) {
        String[] split = lore.split("\\|");
        return setLore(true, split);
    }

    /** Set the lore lines. It will automatically format colors. */
    public EItem setLore(String... lore) {
        return setLore(true, Arrays.asList(lore));
    }

    /** Set the lore lines. If color is set to true it will automatically format colors. */
    public EItem setLore(Boolean color, String... lore) {
        return setLore(color, Arrays.asList(lore));
    }

    /** Set the lore lines. It will automatically format colors. */
    public EItem setLore(List<String> lore) {
        return setLore(true, lore);
    }

    /** Set the lore lines. If color is set to true it will automatically format colors. */
    public EItem setLore(Boolean color, List<String> lore) {
        //lore = Str.splitNewLines(lore);
        for (Integer i = 0; i < lore.size(); i++) {
            String loreStr = lore.get(i);
            loreStr = loreStr.replaceAll("(?<!_)_(?!_)", " ");
            if (color) {
                loreStr = Str.color(loreStr);
            }
            lore.set(i, loreStr);
        }
        ItemMeta meta = getItemMeta();
        meta.setLore(lore);
        setItemMeta(meta);
        return this;
    }


    //Add

    /** Add the given lore lines to the current lore. It will automatically format colors. */
    public EItem addLore(String... lore) {
        return addLore(true, lore);
    }

    /** Add the given lore lines to the current lore. If color is set to true it will automatically format colors. */
    public EItem addLore(Boolean color, String... lore) {
        return addLore(color, Arrays.asList(lore));
    }

    /** Add the given lore lines to the current lore. It will automatically format colors. */
    public EItem addLore(List<String> lore) {
        return addLore(true, lore);
    }

    /** Add the given lore lines to the current lore. If color is set to true it will automatically format colors. */
    public EItem addLore(Boolean color, List<String> lore) {
        //lore = Str.splitNewLines(lore);
        for (Integer i = 0; i < lore.size(); i++) {
            String loreStr = lore.get(i);
            loreStr = loreStr.replaceAll("(?<!_)_(?!_)", " ");
            if (color) {
                loreStr = Str.color(loreStr);
            }
            lore.set(i, loreStr);
        }
        ItemMeta meta = getItemMeta();
        if (meta.hasLore()) {
            List<String> prevLore = meta.getLore();
            prevLore.addAll(lore);
            meta.setLore(prevLore);
        } else {
            meta.setLore(lore);
        }
        setItemMeta(meta);
        return this;
    }


    //Set line

    /**
     * Set the specified line for the lore.
     * It will add empty lines if the line number is above the amount of lore lines.
     * It will automatically format colors.
     */
    public EItem setLore(Integer lineNr, String lore) {
        return setLore(lineNr, lore, true);
    }

    /**
     * Set the specified line for the lore.
     * It will add empty lines if the line number is above the amount of lore lines.
     * If color is set to true it will automatically format colors.
     */
    public EItem setLore(Integer lineNr, String lore, Boolean color) {
        lore = lore.replaceAll("(?<!_)_(?!_)", " ");
        if (color) {
            lore = Str.color(lore);
        }
        ItemMeta meta = getItemMeta();

        List<String> loreList = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
        Integer lines = Math.max(lineNr + 1, loreList.size());
        for (Integer i = 0; i < lines; i++) {
            if (i >= loreList.size()) {
                loreList.add("");
            }
            if (i == lineNr) {
                loreList.set(i, lore);
            }
        }
        meta.setLore(loreList);

        setItemMeta(meta);
        return this;
    }


    //Remove lore
    public EItem clearLore() {
        ItemMeta meta = getItemMeta();
        meta.setLore(new ArrayList<String>());
        setItemMeta(meta);
        return this;
    }

    /** Clear a specific line of lore from the lines. */
    public EItem clearLore(Integer lineNr) {
        ItemMeta meta = getItemMeta();
        List<String> lore = meta.getLore();
        if (lore.size() > lineNr) {
            lore.remove(lineNr);
        }
        meta.setLore(lore);
        setItemMeta(meta);
        return this;
    }


    //Get lore

    /** Get a list of all the lore lines. */
    public List<String> getLore() {
        if (!getItemMeta().hasLore()) {
            return new ArrayList<String>();
        }
        return getItemMeta().getLore();
    }

    /** Get a specific line of lore. */
    public String getLore(Integer lineNr) {
        if (!getItemMeta().hasLore() || getItemMeta().getLore().size() <= lineNr) {
            return "";
        }
        return getItemMeta().getLore().get(lineNr);
    }

    /**
     * Checks if the lore contains the given string.
     * If wholeLine is true the given string need to match a whole line.
     * If it's false it will check if any of the lore lines contains the string.
     */
    public Boolean containsLore(String string, Boolean wholeLine) {
        List<String> lore = getLore();
        for (String str : lore) {
            if (wholeLine && str.equalsIgnoreCase(string)) {
                return true;
            } else if (!wholeLine) {
                if (str.contains(string)) {
                    return true;
                }
            }
        }
        return false;
    }


    // ##################################################
    // ################## ENCHANTMENTS ##################
    // ##################################################

    /** Add the given enchantment and level to the item. */
    public EItem addEnchant(Enchantment enchantment, Integer level) {
        return addEnchant(enchantment, level, true);
    }

    /**
     * Add the given enchantment and level to the item.
     * If allowUnsafe is set to false it will throw an error if the level is too high or if the enchantment can't be applied on this item.
     */
    public EItem addEnchant(Enchantment enchantment, Integer level, Boolean allowUnsafe) {
        if (allowUnsafe) {
            super.addUnsafeEnchantment(enchantment, level);
        } else {
            super.addEnchantment(enchantment, level);
        }
        return this;
    }

    /** Add the given enchantments with their levels to the item. */
    public EItem addEnchants(Map<Enchantment, Integer> enchants) {
        return addEnchants(enchants, true);
    }

    /**
     * Add the given enchantments with their levels to the item.
     * If allowUnsafe is set to false it will throw an error if the level of any enchants is too high or if any of the enchantments can't be applied on this item.
     */
    public EItem addEnchants(Map<Enchantment, Integer> enchants, Boolean allowUnsafe) {
        if (allowUnsafe) {
            super.addUnsafeEnchantments(enchants);
        } else {
            super.addEnchantments(enchants);
        }
        return this;
    }


    // ##################################################
    // #################### POTIONS #####################
    // ##################################################

    public EItem setMainEffect(PotionEffectType effect) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof PotionMeta) {
            ((PotionMeta)meta).setMainEffect(effect);
            setItemMeta(meta);
        }
        return this;
    }

    public EItem addEffect(PotionEffect effect, Boolean overwrite) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof PotionMeta) {
            ((PotionMeta)meta).addCustomEffect(effect, overwrite);
            setItemMeta(meta);
        }
        return this;
    }


    // ##################################################
    // ##################### COLOR ######################
    // ##################################################

    public EItem setColor(String color) {
        return setColor(Parse.Color(color));
    }

    public EItem setColor(Color color) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta)meta).setColor(color);
            setItemMeta(meta);
        }
        return this;
    }

    public Color getColor() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof LeatherArmorMeta) {
            return ((LeatherArmorMeta)meta).getColor();
        }
        return null;
    }


    // ##################################################
    // ##################### BOOKS ######################
    // ##################################################

    /** Set the author of the book */
    public EItem setAuthor(String author) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            ((BookMeta)meta).setAuthor(author);
            setItemMeta(meta);
        }
        return this;
    }

    /** Get the author of the book */
    public String getAuthor() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            return ((BookMeta)meta).getAuthor();
        }
        return null;
    }

    /** Set the title of the book */
    public EItem setTitle(String title) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            ((BookMeta)meta).setTitle(title);
            setItemMeta(meta);
        }
        return this;
    }

    /** Get the title of the book */
    public String getTitle() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            return ((BookMeta)meta).getTitle();
        }
        return null;
    }

    /** Can turn a written book in book and quil and the other way around if editable is set to false. */
    public EItem setEditable(Boolean editable) {
        //TODO: Probably need a better method for this. (needs testing)
        if (editable) {
            setType(Material.BOOK_AND_QUILL);
        } else {
            setType(Material.WRITTEN_BOOK);
        }
        return this;
    }

    /**
     * Adds one or more pages to the end of the book.
     * Each page can have up to 256 characters.
     * And there can only be 50 pages in each book.
     * for several book formatting options. //TODO: Put link for book formatting class.
     */
    public EItem addPage(String... pages) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            ((BookMeta)meta).addPage(pages);
            setItemMeta(meta);
        }
        return this;
    }

    /**
     * Sets a page of the book.
     * The content can have up to 256 characters. (any more will be truncated)
     * for several book formatting options. //TODO: Put link for book formatting class.
     */
    public EItem setPage(Integer page, String content) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            ((BookMeta)meta).setPage(page, content);
            setItemMeta(meta);
        }
        return this;
    }

    /**
     * Clears the book and sets the page(s) specified.
     * Each page can have up to 256 characters.
     * And there can only be 50 pages in each book.
     * for several book formatting options. //TODO: Put link for book formatting class.
     */
    public EItem setPages(String... pages) {
        return setPages(Arrays.asList(pages));
    }

    /**
     * Clears the book and sets the page(s) specified.
     * Each page can have up to 256 characters.
     * And there can only be 50 pages in each book.
     * for several book formatting options. //TODO: Put link for book formatting class.
     */
    public EItem setPages(List<String> pages) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            ((BookMeta)meta).setPages(pages);
            setItemMeta(meta);
        }
        return this;
    }

    /** Get the content of the given page */
    public String getPage(Integer page) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            return ((BookMeta)meta).getPage(page);
        }
        return null;
    }

    /** Get all the pages of the book that have content. */
    public List<String> getPages() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BookMeta) {
            return ((BookMeta)meta).getPages();
        }
        return null;
    }


    // ##################################################
    // #################### BANNERS #####################
    // ##################################################

    /** Set the base color of a banner */
    public EItem setBaseColor(DyeColor color) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BannerMeta) {
            ((BannerMeta)meta).setBaseColor(color);
            setItemMeta(meta);
        }
        return this;
    }

    /** Get the base color of a banner */
    public DyeColor getBaseColor() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BannerMeta) {
            return ((BannerMeta)meta).getBaseColor();
        }
        return null;
    }

    /** Add a pattern to the banner */
    public EItem addPattern(PatternType type, DyeColor color) {
        return addPattern(new Pattern(color, type));
    }

    /** Add a pattern to the banner */
    public EItem addPattern(Pattern pattern) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BannerMeta) {
            ((BannerMeta)meta).addPattern(pattern);
            setItemMeta(meta);
        }
        return this;
    }

    /** Insert a pattern to the banner at the given index. */
    public EItem insertPattern(Integer index, PatternType type, DyeColor color) {
        return insertPattern(index, new Pattern(color, type));
    }

    /** Insert a pattern to the banner at the given index. */
    public EItem insertPattern(Integer index, Pattern pattern) {
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

    /** Set the banner patterns to the given array of patterns. */
    public EItem setPatterns(Pattern... patterns) {
        return setPatterns(Arrays.asList(patterns));
    }

    /** Set the banner patterns to the given array of patterns. */
    public EItem setPatterns(List<Pattern> patterns) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BannerMeta) {
            ((BannerMeta)meta).setPatterns(patterns);
            setItemMeta(meta);
        }
        return this;
    }

    /** Get the banner pattern at the given index. */
    public Pattern getPattern(Integer index) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BannerMeta) {
            return ((BannerMeta)meta).getPattern(index);
        }
        return null;
    }

    /** Get a list with all patterns on the banner */
    public List<Pattern> getPatterns() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof BannerMeta) {
            return ((BannerMeta)meta).getPatterns();
        }
        return new ArrayList<Pattern>();
    }


    // ##################################################
    // ##################### SKULLS #####################
    // ##################################################

    /** Set the skull owner of the skull. */
    public EItem setSkull(String name) {
        setDurability((short)3);
        ItemMeta meta = getItemMeta();
        if (meta instanceof SkullMeta) {
            ((SkullMeta)meta).setOwner(name);
            setItemMeta(meta);
        }
        return this;
    }

    /** Get the skull owner of the skull. */
    public String getSkull() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof SkullMeta) {
            return ((SkullMeta)meta).getOwner();
        }
        return "";
    }


    // ##################################################
    // #################### FIREWORK ####################
    // ##################################################

    /** Set the firework rocket power */
    public EItem setPower(Integer power) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkMeta) {
            ((FireworkMeta)meta).setPower(power);
            setItemMeta(meta);
        }
        return this;
    }

    /** Get the firework rocket power. */
    public Integer getPower() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkMeta) {
            return ((FireworkMeta)meta).getPower();
        }
        return 1;
    }

    /** Add a base firework effect. (White ball) */
    public EItem addBaseEffect() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkMeta) {
            FireworkEffect.Builder builder = FireworkEffect.builder();
            builder.with(FireworkEffect.Type.BALL).withColor(Color.WHITE);
            ((FireworkMeta)meta).addEffect(builder.build());
            setItemMeta(meta);
        }
        return this;
    }

    /** Add a firework effect to the item. */
    public EItem addEffect(FireworkEffect effect) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkMeta) {
            ((FireworkMeta)meta).addEffect(effect);
            setItemMeta(meta);
        }
        return this;
    }

    /** Add a firework effect to the item. */
    public EItem addEffect(FireworkEffect.Type type, List<Color> colors, List<Color> fadeColors, Boolean flicker, Boolean trail) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkMeta) {
            FireworkEffect.Builder builder = FireworkEffect.builder();
            builder.with(type).withColor(colors).withFade(fadeColors);
            if (flicker) {
                builder.withFlicker();
            }
            if (trail) {
                builder.withTrail();
            }
            ((FireworkMeta)meta).addEffect(builder.build());
            setItemMeta(meta);
        }
        return this;
    }

    /** Get a firework effect at the given index. */
    public FireworkEffect getEffect(Integer index) {
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkMeta) {
            List<FireworkEffect> effects = ((FireworkMeta)meta).getEffects();
            if (effects.size() > index) {
                return effects.get(index);
            }
        }
        return null;
    }

    /** Get a list of all the firework effects on the item. */
    public List<FireworkEffect> getEffects() {
        ItemMeta meta = getItemMeta();
        if (meta instanceof FireworkMeta) {
            return ((FireworkMeta)meta).getEffects();
        }
        return null;
    }

}

