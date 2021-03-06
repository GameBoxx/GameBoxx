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

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.aliases.items.ItemData;
import info.gameboxx.gameboxx.aliases.items.Items;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.options.single.BoolO;
import info.gameboxx.gameboxx.util.Parse;
import info.gameboxx.gameboxx.util.Str;
import info.gameboxx.gameboxx.util.Utils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ItemParser {

    private String string = null;
    private EItem item = null;

    private String error = null;

    /**
     * Parse the given string in to a item.
     *
     * @param string String with item format.
     * @param ignoreErrors If this is true it will continue parsing even if there are non breaking errors.
     */
    public ItemParser(String string, CommandSender sender, boolean ignoreErrors) {
        this.string = string;
        if (string == null || string.isEmpty()) {
            error = Msg.getString("itemparser.no-input");
            return;
        }

        List<String> sections = Str.splitQuotes(string, ' ', false);

        //Go through all the sections.
        for (int i = 0; i < sections.size(); i++) {
            String section = sections.get(i);

            //Get ItemData from first section
            if (i == 0) {
                ItemData item = Items.getItem(section);
                if (item == null) {
                    error = Msg.getString("itemparser.invalid-item", Param.P("input", section));
                    return;
                }
                this.item = new EItem(item.getType(), item.getData());
                continue;
            }

            //Set amount if it's a number
            Integer amount = Parse.Int(section);
            if (amount != null) {
                item.setAmount(amount);
                continue;
            }

            String[] split = section.split(":", 2);
            String key = split[0].toUpperCase().replace("_", "").replace(" ", "");
            String value = split.length > 1 ? split[1] : ""; //Allow key only

            //Try to parse ItemTag
            ItemTag tag = ItemTag.fromString(key);
            if (tag == null) {
                error = Msg.getString("parser.invalid-tag", Param.P("tag", key), Param.P("value", value), Param.P("type", Msg.getString("itemparser.type")),
                        Param.P("tags", Utils.getAliasesString("parser.tag-entry", ItemTag.getTagsMap(item.getItemMeta()))));
                if (!ignoreErrors) {
                    return;
                }
                continue;
            }

            //Make sure the item tag can be used for the meta of the item.
            if (!ItemTag.getTags(item.getItemMeta()).contains(tag)) {
                error = Msg.getString("parser.unusable-tag", Param.P("tag", key), Param.P("type", Msg.getString("itemparser.type")),
                        Param.P("tags", Utils.getAliasesString("parser.tag-entry", ItemTag.getTagsMap(item.getItemMeta()))));
                System.out.println(Msg.fromString(error).get());
                if (!ignoreErrors) {
                    return;
                }
                continue;
            }

            //Parse the value for the tag
            SingleOption option = (SingleOption)tag.getOption().clone();
            if (option instanceof BoolO && value.isEmpty()) {
                value = "true"; //Allow empty tags for booleans like 'glow' instead of 'glow:true'
            }
            if (!option.parse(sender, value)) {
                error = option.getError();
                if (!ignoreErrors) {
                    return;
                }
                continue;
            }

            //Apply the tag to the item
            if (tag.hasCallback()) {
                if (!tag.getCallback().onSet(sender, item, option)) {
                    error = Msg.getString("parser.tag-fail", Param.P("tag", key), Param.P("value", value));
                    if (!ignoreErrors) {
                        return;
                    }
                    continue;
                }
            } else {
                boolean success = false;
                try {
                    MethodUtils.invokeMethod(item, tag.setMethod(), option.getValue());
                    success = true;
                } catch (NoSuchMethodException e) {
                    error = Msg.getString("parser.missing-method", Param.P("tag", key), Param.P("value", value),
                            Param.P("method", tag.setMethod() + "(" + option.getValue().getClass().getSimpleName() + ")"));
                } catch (IllegalAccessException e) {
                    error = Msg.getString("parser.inaccessible-method", Param.P("tag", key), Param.P("value", value),
                            Param.P("method", tag.setMethod() + "(" + option.getValue().getClass().getSimpleName() + ")"));
                } catch (InvocationTargetException e) {
                    error = Msg.getString("parser.non-invokable-method", Param.P("tag", key), Param.P("value", value),
                            Param.P("method", tag.setMethod() + "(" + option.getValue().getClass().getSimpleName() + ")"));
                }
                if (!success && !ignoreErrors) {
                    return;
                }
            }
        }
    }

    /**
     * Parse the given item in to a string.
     *
     * @param itemStack item which needs to be parsed.
     */
    public ItemParser(ItemStack itemStack) {
        this(new EItem(itemStack));
    }

    public ItemParser(EItem item) {
        this.item = item;

        if (item == null) {
            return;
        }

        //Air can't have any meta.
        if (item.getType() == Material.AIR) {
            this.string = Items.getName(Material.AIR, (byte)0);
            return;
        }

        List<String> sections = new ArrayList<>();

        sections.add(Items.getName(item.getData()).replaceAll(" ", ""));
        if (item.getAmount() > 1) {
            sections.add(Integer.toString(item.getAmount()));
        }

        for (ItemTag tag : ItemTag.getTags(item.getItemMeta())) {
            if (tag.hasCallback()) {
                String result = tag.getCallback().onGet(item);
                if (result == null || result.isEmpty()) {
                    continue;
                }
                if (!result.toLowerCase().contains(tag.getTag().toLowerCase() + ":")) {
                    result = Str.escapeWords(Str.escapeQuotes(result));
                } else {
                    result = Str.escapeQuotes(result);
                }
                sections.add(tag.getTag().toLowerCase() + ":" + result);
            } else {
                if (tag.getMethod() == null || tag.getMethod().isEmpty() || tag.getMethod().equalsIgnoreCase("null")) {
                    continue;
                }
                try {
                    Object result = MethodUtils.invokeMethod(item, tag.getMethod(), new Class[0]);
                    if (result == null) {
                        continue;
                    }
                    SingleOption option = (SingleOption)tag.getOption().clone();
                    if (!option.parse(result)) {
                        GameBoxx.get().warn("Failed to parse entity data! [tag=" + tag.getTag() + " value=" + result.toString() + " error='" + option.getError() + "']");
                        continue;
                    }
                    if (option.getValue().equals(option.getDefault())) {
                        continue;
                    }
                    sections.add(tag.getTag().toLowerCase() + ":" + Str.escapeWords(Str.escapeQuotes(option.serialize())));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        this.string = Str.implode(sections, " ");
    }


    /**
     * Get the parsed string.
     * Will return null if the parser failed.
     *
     * @return Parsed string with all meta and such.
     */
    public String getString() {
        return string;
    }

    /**
     * Get the parsed item.
     * Will return null if the parser failed.
     *
     * @return Parsed EItem with all meta and such.
     */
    public EItem getItem() {
        return item;
    }

    /**
     * Check if the parsing was successful or not.
     * If not you can call getError to get the error message.
     *
     * @return Whether or not the parsing was successful.
     */
    public boolean isValid() {
        return item != null && string != null && error == null;
    }

    /**
     * If the parsing wasn't successful this will return the error message.
     *
     * @return Error message if there is one otherwise an empty string.
     */
    public String getError() {
        return error;
    }
}
