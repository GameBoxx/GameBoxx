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

import info.gameboxx.gameboxx.aliases.FireworkEffects;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.options.list.ColorLO;
import info.gameboxx.gameboxx.util.Random;
import info.gameboxx.gameboxx.util.Str;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.command.CommandSender;

import java.util.List;

public class FireworkO extends SingleOption<FireworkEffect, FireworkO> {

    @Override
    public boolean parse(CommandSender sender, String input) {
        FireworkEffect.Builder builder = FireworkEffect.builder();
        String[] split = input.split(":");

        if (split.length > 0 && !split[0].isEmpty()) {
            FireworkEffect.Type type;
            if (split[0].equalsIgnoreCase("*")) {
                type = Random.Item(FireworkEffect.Type.values());
            } else {
                type = FireworkEffects.get(split[0]);
            }
            if (type == null) {
                Msg.getString("firework.invalid-type", Param.P("input", split[0]), Param.P("types", Utils.getAliasesString("firework.entry", FireworkEffects.getAliasMap())));
                return false;
            }
            builder.with(type);
        }

        if (split.length < 2 || split[1].isEmpty()) {
            Msg.getString("firework.no-color");
            return false;
        } else {
            String[] splitColors = split[1].split(";");
            for (String colorString : splitColors) {
                ColorO color = new ColorO();
                if (!color.parse(sender, colorString)) {
                    error = color.getError();
                    return false;
                }
                builder.withColor(color.getValue());
            }
        }

        if (split.length > 2 && !split[2].isEmpty()) {
            String[] splitColors = split[2].split(";");
            for (String colorString : splitColors) {
                ColorO color = new ColorO();
                if (!color.parse(sender, colorString)) {
                    error = color.getError();
                    return false;
                }
                builder.withFade(color.getValue());
            }
        }

        if (split.length > 3 && !split[3].isEmpty()) {
            BoolO bool = new BoolO();
            if (!bool.parse(sender, split[3])) {
                error = bool.getError();
                return false;
            }
            builder.trail(bool.getValue());
        }

        if (split.length > 4 && !split[4].isEmpty()) {
            BoolO bool = new BoolO();
            if (!bool.parse(sender, split[4])) {
                error = bool.getError();
                return false;
            }
            builder.flicker(bool.getValue());
        }

        value = builder.build();
        return true;
    }

    @Override
    public String serialize() {
        return serialize(getValue());
    }

    public static String serialize(FireworkEffect effect) {
        return effect == null ? null : effect.getType().toString() + ":" + getColorsString(effect.getColors(), false) + ":" +
                getColorsString(effect.getFadeColors(), false) + ":" + effect.hasTrail() + ":" + effect.hasFlicker();
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(FireworkEffect effect) {
        return effect == null ? null : Msg.getString("firework.display", Param.P("type", FireworkEffects.getDisplayName(effect.getType())), Param.P("colors", getColorsString(effect.getColors(), false)),
                Param.P("fadecolors", getColorsString(effect.getFadeColors(), false)), Param.P("trail", effect.hasTrail()), Param.P("flicker", effect.hasFlicker()));
    }

    private static String getColorsString(List<Color> colors, boolean serialize) {
        ColorLO colorList = new ColorLO();
        colorList.parse(true, colors.toArray(new Color[colors.size()]));
        return Str.implode(serialize ? colorList.serialize() : colorList.getDisplayValues(), ";");
    }

    @Override
    public FireworkO clone() {
        return super.cloneData(new FireworkO());
    }
}
