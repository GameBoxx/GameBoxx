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

public class EnchantO extends SingleOption<Enchant, EnchantO> {

    private boolean ignoreMax = true;

    public EnchantO ignoreMax(boolean ignoreMax) {
        this.ignoreMax = ignoreMax;
        return this;
    }

    @Override
    public boolean parse(CommandSender sender, String input) {
        String[] split = input.split(":");
        Integer level = 0;

        Enchantment enchant = Enchantments.get(split[0]);
        if (enchant == null) {
            Msg.getString("enchant.invalid-type", Param.P("input", split[0]), Param.P("types", Utils.getAliasesString("enchant.entry", Enchantments.getAliasMap())));
            return false;
        }

        if (split.length > 1) {
            level = Parse.Int(split[1]);
            if (level == null) {
                Msg.getString("enchant.invalid-level", Param.P("input", split[0]));
                return false;
            }
            if (!ignoreMax && level > enchant.getMaxLevel()) {
                Msg.getString("enchant.level-too-high", Param.P("input", split[0]), Param.P("max", enchant.getMaxLevel()));
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
        return enchant == null ? null : enchant.getType().toString() + ":" + enchant.getLevel();
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(Enchant enchant) {
        return enchant == null ? null : Msg.getString("enchant.display", Param.P("type", Enchantments.getDisplayName(enchant.getType())), Param.P("level", enchant.getLevel()));
    }

    @Override
    public EnchantO clone() {
        return super.cloneData(new EnchantO().ignoreMax(ignoreMax));
    }
}
