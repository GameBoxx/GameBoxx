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

import info.gameboxx.gameboxx.aliases.*;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.options.single.*;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class ItemTag {

    private static Map<String, ItemTag> BY_NAME = new HashMap<>();

    private final String tag;
    private final String[] aliases;
    private final SingleOption option;
    private final ItemTagCallback callback;
    private final String setMethod;
    private final String getMethod;
    private final Class<? extends ItemMeta>[] metaClasses;

    private ItemTag(String tag, String[] aliases, SingleOption option, String setMethod, String getMethod, Class... metaClasses) {
        this.tag = tag;
        option.name(tag);
        this.aliases = aliases;
        this.option = option;
        this.callback = null;
        this.setMethod = setMethod;
        this.getMethod = getMethod;
        this.metaClasses = metaClasses;
    }

    private ItemTag(String tag, String[] aliases, SingleOption option, ItemTagCallback callback, Class... metaClasses) {
        this.tag = tag;
        option.name(tag);
        this.aliases = aliases;
        this.option = option;
        this.callback = callback;
        this.setMethod = null;
        this.getMethod = null;
        this.metaClasses = metaClasses;
    }


    public String getTag() {
        return tag;
    }

    public String[] getAliases() {
        return aliases;
    }

    public Class<? extends ItemMeta>[] getMeta() {
        return metaClasses;
    }

    public SingleOption getOption() {
        return option;
    }

    public String setMethod() {
        return setMethod;
    }

    public String getMethod() {
        return getMethod;
    }

    public ItemTagCallback getCallback() {
        return callback;
    }

    public boolean hasCallback() {
        return callback != null;
    }


    public static ItemTag fromString(String name) {
        return BY_NAME.get(name.toUpperCase().replace("_", "").replace(" ", ""));
    }

    public static List<ItemTag> getTags(ItemMeta meta) {
        List<ItemTag> tags = new ArrayList<ItemTag>();
        for (ItemTag tag : ItemTag.values()) {
            for (Class<? extends ItemMeta> clazz : tag.getMeta()) {
                if (clazz.isAssignableFrom(meta.getClass())) {
                    if (!tags.contains(tag)) {
                        tags.add(tag);
                    }
                }
            }
        }
        return tags;
    }

    public static Collection<ItemTag> values() {
        return BY_NAME.values();
    }

    public static Map<String, List<String>> getTagsMap(ItemMeta meta) {
        Map<String, List<String>> tagMap = new HashMap<>();
        Collection<ItemTag> tagList = meta == null ? values() : getTags(meta);
        for (ItemTag tag : tagList) {
            tagMap.put(tag.getTag(), tag.getAliases() == null || tag.getAliases().length < 1 ? new ArrayList<String>() : Arrays.asList(tag.getAliases()));
        }
        return tagMap;
    }


    public static ItemTag register(String tag, String[] aliases, SingleOption option, ItemTagCallback executeCallback, Class... entities) {
        return register(new ItemTag(tag, aliases, option, executeCallback, entities));
    }

    private static ItemTag register(String tag, String[] aliases, SingleOption option, String setMethod, String getMethod, Class... entities) {
        return register(new ItemTag(tag, aliases, option, setMethod, getMethod, entities));
    }

    private static ItemTag register(ItemTag tag) {
        String key = tag.getTag().toUpperCase().replace("_", "").replace(" ", "");
        if (BY_NAME.containsKey(key)) {
            throw new IllegalArgumentException("There is already an ItemTag registered with the name '" + key + "'!");
        }
        BY_NAME.put(key, tag);

        if (tag.getAliases() != null) {
            for (String alias : tag.getAliases()) {
                alias = alias.toUpperCase().replace("_", "").replace(" ", "");
                if (!BY_NAME.containsKey(alias)) {
                    BY_NAME.put(alias, tag);
                }
            }
        }
        return tag;
    }


    public static void registerDefaults() {

        //All items
        ItemTag.register("Name", new String[] {"DisplayName", "DName"}, new StringO(), "setName", "getDisplayName", ItemMeta.class);
        ItemTag.register("Lore", new String[] {"Description", "Desc"}, new StringO().def(""), "setLore", "getLoreString", ItemMeta.class);
        ItemTag.register("Enchant", new String[] {"Enchantment", "Ench", "E"}, new EnchantO(), new ItemTagCallback() {
            @Override boolean onSet(CommandSender sender, EItem item, SingleOption result) {
                item.enchant(((EnchantO)result).getValue());
                return true;
            }

            @Override String onGet(EItem item) {
                String result = "";
                for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
                    result += "enchant:" + Enchantments.getName(entry.getKey()) + ":" + entry.getValue();
                }
                return result.length() >= 8 ? result.substring(8) : "";
            }
        }, ItemMeta.class, EnchantmentStorageMeta.class);

        //Leather Color
        ItemTag.register("Color", new String[] {"Clr", "Leather", "LColor", "LClr"}, new ColorO(), "setColor", "getColor", LeatherArmorMeta.class);

        //Skull
        ItemTag.register("Owner", new String[] {"Skull", "Player"}, new StringO().maxChars(16), "setOwner", "getOwner", SkullMeta.class);
        ItemTag.register("Texture", new String[] {"Tex", "SkullTexture", "SkullT", "SkullTex", "Skin", "SkullURL"}, new StringO(), "setTexture", "getTexture", SkullMeta.class);

        //Banners
        ItemTag.register("Base", new String[] {"BaseColor", "BaseClr", "BColor", "BClr", "BC"}, new StringO().match(DyeColors.getAliasMap()), new ItemTagCallback() {
            @Override boolean onSet(CommandSender sender, EItem item, SingleOption result) {
                item.setBaseColor(DyeColors.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EItem item) {
                return DyeColors.getName(item.getBaseColor());
            }
        }, BannerMeta.class);
        ItemTag.register("Pattern", new String[] {"Pat", "P"}, new MultiO(":", "{pattern}:{dyecolor}",
                new StringO().match(BannerPatterns.getAliasMap()), new StringO().match(DyeColors.getAliasMap())), new ItemTagCallback() {
            @Override boolean onSet(CommandSender sender, EItem item, SingleOption result) {
                item.addPattern(BannerPatterns.get((String)((MultiO)result).getValue()[0].getValue()), DyeColors.get((String)((MultiO)result).getValue()[1].getValue()));
                return true;
            }

            @Override String onGet(EItem item) {
                String result = "";
                for (Pattern pattern : item.getPatterns()) {
                    result += "pattern:" + BannerPatterns.getName(pattern.getPattern()) + ":" + DyeColors.getName(pattern.getColor());
                }
                return result.length() >= 8 ? result.substring(8) : "";
            }
        }, BannerMeta.class);

        //Firework
        ItemTag.register("FEffect", new String[] {"Firework", "FW", "FWEffect", "FE", "FWE"}, new FireworkO(), new ItemTagCallback() {
            @Override boolean onSet(CommandSender sender, EItem item, SingleOption result) {
                item.addEffect(((FireworkO)result).getValue());
                return true;
            }

            @Override String onGet(EItem item) {
                if (item.getType() == Material.FIREWORK_CHARGE) {
                    return FireworkO.serialize(item.getEffect());
                } else {
                    String result = "";
                    for (FireworkEffect effect : item.getEffects()) {
                        result += "feffect:" + FireworkO.serialize(effect);
                    }
                    return result.length() >= 8 ? result.substring(8) : "";
                }
            }
        }, FireworkMeta.class, FireworkEffectMeta.class);
        ItemTag.register("Power", new String[] {"Pwr", "Pow"}, new IntO(), "setPower", "getPower", FireworkMeta.class);

        //Potions
        ItemTag.register("Potion", new String[] {"Pot", "PotionType", "PotType", "PType"}, new StringO().match(PotionTypes.getAliasMap()), new ItemTagCallback() {
            @Override boolean onSet(CommandSender sender, EItem item, SingleOption result) {
                item.setPotion(new PotionData(PotionTypes.get(((StringO)result).getValue())));
                return true;
            }

            @Override String onGet(EItem item) {
                return PotionTypes.getName(item.getPotion().getType());
            }
        }, PotionMeta.class);
        ItemTag.register("PEffect", new String[] {"PotionE", "PE", "PotEffect", "PotE"}, new PotionO(), new ItemTagCallback() {
            @Override boolean onSet(CommandSender sender, EItem item, SingleOption result) {
                item.addPotionEffect(((PotionO)result).getValue());
                return true;
            }

            @Override String onGet(EItem item) {
                String result = "";
                for (PotionEffect effect : item.getPotionEffects()) {
                    result += "peffect:" + PotionO.serialize(effect);
                }
                return result.length() >= 8 ? result.substring(8) : "";
            }
        }, PotionMeta.class);

        //Books
        ItemTag.register("Author", new String[] {"BookAuthor", "Auth", "AU"}, new StringO(), "setAuthor", "getAuthor", BookMeta.class);
        ItemTag.register("Title", new String[] {"BookTitle", "Tit"}, new StringO(), "setTitle", "getTitle", BookMeta.class);
        ItemTag.register("Content", new String[] {"BookContent", "Cont", "CT", "Pages", "Page", "PA"}, new StringO(), new ItemTagCallback() {
            @Override boolean onSet(CommandSender sender, EItem item, SingleOption result) {
                item.setContent(((StringO)result).getValue());
                return true;
            }

            @Override String onGet(EItem item) {
                return item.getContent();
            }
        }, BookMeta.class);

        //Flags
        ItemTag.register("Flag", new String[] {"ItemFlag", "IFlag", "ItemF", "F", "HideFlag", "HFlag", "HideF", "Hide"}, new StringO().match(ItemFlags.getAliasMap()), new ItemTagCallback() {
            @Override boolean onSet(CommandSender sender, EItem item, SingleOption result) {
                item.addItemFlags(ItemFlags.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EItem item) {
                String result = "";
                for (ItemFlag flag : item.getItemFlags()) {
                    result += "flag:" + ItemFlags.getName(flag);
                }
                return result.length() >= 5 ? result.substring(5) : "";
            }
        }, ItemMeta.class);
        ItemTag.register("AllFlags", new String[] {"AllItemFlags", "AllIFlags", "AllItemF", "AllF", "AllHideFlags", "AllHFlags", "AllHideF", "HideAll"}, new BoolO().def(false), new ItemTagCallback() {
            @Override boolean onSet(CommandSender sender, EItem item, SingleOption result) {
                if (((BoolO)result).getValue()) {
                    item.addItemFlags();
                } else {
                    item.removeItemFlags();
                }
                return true;
            }

            @Override String onGet(EItem item) {return null;}
        }, ItemMeta.class);

        //Custom
        ItemTag.register("Glow", new String[] {"Glowing"}, new BoolO().def(false), "setGlowing", "isGlowing", ItemMeta.class);

    }
}
