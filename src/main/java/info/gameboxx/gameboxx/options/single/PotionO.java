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

import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.util.Parse;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionO extends SingleOption<PotionEffect, PotionO> {

    @Override
    public boolean parse(Player player, String input) {
        boolean ambient = false;
        if (input.startsWith("!")) {
            ambient = true;
            input = input.substring(1);
        }

        String[] split = input.split(":");
        Color color = null;
        Integer amplifier = 0;
        Integer duration = Integer.MAX_VALUE;

        //TODO: Get type from alias and display aliases.
        PotionEffectType type = PotionEffectType.getByName(split[0]);
        if (type == null) {
            Msg.getString("potion.invalid-type", Param.P("input", split[0]), Param.P("types", Parse.Array(PotionEffectType.values())));
            return false;
        }

        if (split.length > 1) {
            split = split[1].split("\\.");

            if (split.length > 0 && !split[0].isEmpty()) {
                amplifier = Parse.Int(split[0]);
                if (amplifier == null) {
                    Msg.getString("potion.invalid-amplifier", Param.P("input", split[0]));
                    return false;
                }
            }

            if (split.length > 1 && !split[1].isEmpty()) {
                duration = Parse.Int(split[1]);
                if (duration == null) {
                    Msg.getString("potion.invalid-duration", Param.P("input", split[1]));
                    return false;
                }
                if (duration < 0) {
                    duration = Integer.MAX_VALUE;
                }
            }

            if (split.length > 2 && !split[2].isEmpty()) {
                ColorO colorOption = new ColorO();
                if (!colorOption.parse(split[2])) {
                    error = colorOption.getError();
                    return false;
                }
                color = colorOption.getValue();
            }
        }

        value = color == null ? new PotionEffect(type, duration, amplifier, ambient) : new PotionEffect(type, duration, amplifier, ambient, ambient, color);
        return true;
    }

    @Override
    public String serialize() {
        return serialize(getValue());
    }

    public static String serialize(PotionEffect potion) {
        return potion == null ? null : (potion.isAmbient() ? "!" : "") + potion.getType().toString() + ":" + potion.getAmplifier() + "." + potion.getDuration() + "." + ColorO.serialize(potion.getColor());
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(PotionEffect potion) {
        return potion == null ? null : Msg.getString("potion.display", Param.P("type", potion.getType().toString()), Param.P("amplifier", potion.getAmplifier()), Param.P("duration", potion.getDuration()));
    }

    @Override
    public PotionO clone() {
        return super.cloneData(new PotionO());
    }
}