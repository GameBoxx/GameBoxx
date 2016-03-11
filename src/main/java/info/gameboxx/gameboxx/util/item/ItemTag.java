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

import info.gameboxx.gameboxx.aliases.BannerPatterns;
import info.gameboxx.gameboxx.aliases.DyeColors;
import info.gameboxx.gameboxx.aliases.Enchantments;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.options.single.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.*;

import java.util.*;

public class ItemTag {

    private static Map<String, ItemTag> BY_NAME = new HashMap<>();

    private final String tag;
    private final SingleOption option;
    private final ItemTagCallback callback;
    private final String setMethod;
    private final String getMethod;
    private final Class<? extends ItemMeta>[] metaClasses;

    private ItemTag(String tag, SingleOption option, String setMethod, String getMethod, Class... metaClasses) {
        this.tag = tag;
        option.name(tag);
        this.option = option;
        this.callback = null;
        this.setMethod = setMethod;
        this.getMethod = getMethod;
        this.metaClasses = metaClasses;
    }

    private ItemTag(String tag, SingleOption option, ItemTagCallback callback, Class... metaClasses) {
        this.tag = tag;
        option.name(tag);
        this.option = option;
        this.callback = callback;
        this.setMethod = null;
        this.getMethod = null;
        this.metaClasses = metaClasses;
    }


    public String getTag() {
        return tag;
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
                    tags.add(tag);
                }
            }
        }
        return tags;
    }

    public static Collection<ItemTag> values() {
        return BY_NAME.values();
    }


    public static ItemTag register(String tag, SingleOption option, ItemTagCallback executeCallback, Class... entities) {
        return register(new ItemTag(tag, option, executeCallback, entities));
    }

    private static ItemTag register(String tag, SingleOption option, String setMethod, String getMethod, Class... entities) {
        return register(new ItemTag(tag, option, setMethod, getMethod, entities));
    }

    private static ItemTag register(ItemTag tag) {
        String key = tag.getTag().toUpperCase().replace("_", "").replace(" ", "");
        if (BY_NAME.containsKey(key)) {
            throw new IllegalArgumentException("There is already an ItemTag registered with the name '" + key + "'!");
        }
        BY_NAME.put(key, tag);
        return tag;
    }


    public static void registerDefaults() {

        //All items
        ItemTag.register("NAME", new StringO(), "setName", "getName", ItemMeta.class);
        ItemTag.register("LORE", new StringO(), "setLore", "getLore", ItemMeta.class);
        ItemTag.register("ENCHANT", new EnchantO(), new ItemTagCallback() {
            @Override boolean onSet(CommandSender sender, EItem item, SingleOption result) {
                item.addEnchant(((EnchantO)result).getValue());
                return true;
            }

            @Override String onGet(EItem item) {
                String result = "";
                for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
                    result += "enchant:" + Enchantments.getName(entry.getKey()) + ":" + entry.getValue();
                }
                return result.substring(8);
            }
        }, ItemMeta.class);

        //Leather Color
        ItemTag.register("COLOR", new ColorO(), "setColor", "getColor", LeatherArmorMeta.class);

        //Skull
        ItemTag.register("OWNER", new StringO(), "setSkull", "getSkull", SkullMeta.class);
        //TODO: Skull texture

        //Banners
        ItemTag.register("BASE", new StringO().match(DyeColors.getAliasMap()), new ItemTagCallback() {
            @Override boolean onSet(CommandSender sender, EItem item, SingleOption result) {
                item.setBaseColor(DyeColors.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EItem item) {
                return DyeColors.getName(item.getBaseColor());
            }
        }, BannerMeta.class);
        ItemTag.register("PATTERN", new MultiO(":", "{pattern}:{dyecolor}", new StringO().match(BannerPatterns.getAliasMap()), new StringO().match(DyeColors.getAliasMap())), new ItemTagCallback() {
            @Override boolean onSet(CommandSender sender, EItem item, SingleOption result) {
                item.addPattern(BannerPatterns.get((String)((MultiO)result).getValue()[0].getValue()), DyeColors.get((String)((MultiO)result).getValue()[1].getValue()));
                return true;
            }

            @Override String onGet(EItem item) {
                String result = "";
                for (Pattern pattern : item.getPatterns()) {
                    result += "pattern:" + BannerPatterns.getName(pattern.getPattern()) + ":" + DyeColors.getName(pattern.getColor());
                }
                return result.substring(8);
            }
        }, BannerMeta.class);

        //Firework


        //Potions


        //Books


        //Custom
        ItemTag.register("GLOW", new BoolO().def(false), "setGlowing", "isGlowing", ItemMeta.class);

    }
}
