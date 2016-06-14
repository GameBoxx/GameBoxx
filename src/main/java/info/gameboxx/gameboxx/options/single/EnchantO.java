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

package info.gameboxx.gameboxx.options.single;

import info.gameboxx.gameboxx.aliases.Enchantments;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.util.Enchant;
import info.gameboxx.gameboxx.util.Parse;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnchantO extends SingleOption<Enchant, EnchantO> {

    private boolean ignoreMax = true;

    public EnchantO ignoreMax(boolean ignoreMax) {
        this.ignoreMax = ignoreMax;
        return this;
    }

    @Override
    public boolean parse(CommandSender sender, String input) {
        String[] split = input.split(":");
        Integer level = 1;

        Enchantment enchant = Enchantments.get(split[0]);
        if (enchant == null) {
            error = Msg.getString("enchant.invalid-type", Param.P("input", split[0]), Param.P("types", Utils.getAliasesString("enchant.entry", Enchantments.getAliasMap())));
            return false;
        }

        if (split.length > 1) {
            level = Parse.Int(split[1]);
            if (level == null) {
                error = Msg.getString("enchant.invalid-level", Param.P("input", split[0]));
                return false;
            }
            if (!ignoreMax && level > enchant.getMaxLevel()) {
                error = Msg.getString("enchant.level-too-high", Param.P("input", split[0]), Param.P("max", enchant.getMaxLevel()));
                return false;
            }
        }

        value = new Enchant(enchant, level);
        return true;
    }

    @Override
    public String serialize() {
        return serialize(getValue());
    }

    public static String serialize(Enchant enchant) {
        return enchant == null ? null : Enchantments.getName(enchant.getType()) + ":" + enchant.getLevel();
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(Enchant enchant) {
        return enchant == null ? null : Msg.getString("enchant.display", Param.P("type", Enchantments.getDisplayName(enchant.getType())), Param.P("level", enchant.getLevel()));
    }

    @Override
    public String getTypeName() {
        return "Enchantment";
    }

    @Override
    public List<String> onComplete(CommandSender sender, String prefix, String input) {
        List<String> suggestions = new ArrayList<>();

        String[] data = input.split(":", -1);
        if (data.length <= 1) {
            for (String key : Enchantments.getAliasMap().keySet()) {
                String name = key.replace(" ", "");
                if (name.toLowerCase().startsWith(data[0].toLowerCase())) {
                    suggestions.add(prefix + name);
                }
            }
            if (data[0].length() > 0) {
                for (List<String> aliases : Enchantments.getAliasMap().values()) {
                    for (String alias : aliases) {
                        String name = alias.replace(" ", "");
                        if (name.toLowerCase().startsWith(data[0].toLowerCase())) {
                            suggestions.add(prefix + name);
                        }
                    }
                }
            }
        } else if (data.length <= 2) {
            if (data[1].isEmpty()) {
                for (int i = 1; i <= 5; i++) {
                    suggestions.add(prefix + data[0] + ":" + i);
                }
            }
        }

        Collections.sort(suggestions);
        return suggestions;
    }


    @Override
    public EnchantO clone() {
        return super.cloneData(new EnchantO().ignoreMax(ignoreMax));
    }
}
