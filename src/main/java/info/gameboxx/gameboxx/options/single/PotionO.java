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

import info.gameboxx.gameboxx.aliases.PotionEffects;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.util.Parse;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PotionO extends SingleOption<PotionEffect, PotionO> {

    @Override
    public boolean parse(CommandSender sender, String input) {
        boolean ambient = false;
        if (input.startsWith("!")) {
            ambient = true;
            input = input.substring(1);
        }

        String[] split = input.split(":");
        Color color = null;
        Integer amplifier = 0;
        Integer duration = 600; //30 seconds default

        PotionEffectType type = PotionEffects.get(split[0]);
        if (type == null) {
            error = Msg.getString("potion.invalid-type", Param.P("input", split[0]), Param.P("types", Utils.getAliasesString("potion.entry", PotionEffects.getAliasMap())));
            return false;
        }

        if (split.length > 1 && !split[1].isEmpty()) {
            amplifier = Parse.Int(split[1]);
            if (amplifier == null) {
                error = Msg.getString("potion.invalid-amplifier", Param.P("input", split[0]));
                return false;
            }
        }

        if (split.length > 2 && !split[2].isEmpty()) {
            duration = Parse.Int(split[2]);
            if (duration == null) {
                error = Msg.getString("potion.invalid-duration", Param.P("input", split[1]));
                return false;
            }
            if (duration < 0) {
                duration = Integer.MAX_VALUE;
            }
        }

        if (split.length > 3 && !split[3].isEmpty()) {
            ColorO colorOption = new ColorO();
            if (!colorOption.parse(split[3])) {
                error = colorOption.getError();
                return false;
            }
            color = colorOption.getValue();
        }

        value = color == null ? new PotionEffect(type, duration, amplifier, ambient) : new PotionEffect(type, duration, amplifier, ambient, ambient, color);
        return true;
    }

    @Override
    public String serialize() {
        return serialize(getValue());
    }

    public static String serialize(PotionEffect potion) {
        return potion == null ? null : (potion.isAmbient() ? "!" : "") + PotionEffects.getName(potion.getType()) + ":" +
                potion.getAmplifier() + "." + potion.getDuration() + (potion.getColor() == null ? "" : "." + ColorO.serialize(potion.getColor()));
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(PotionEffect potion) {
        return potion == null ? null : Msg.getString("potion.display", Param.P("type", PotionEffects.getDisplayName(potion.getType())),
                Param.P("amplifier", potion.getAmplifier()), Param.P("duration", potion.getDuration()));
    }

    @Override
    public String getTypeName() {
        return "Potion";
    }

    @Override
    public List<String> onComplete(CommandSender sender, String prefix, String input) {
        List<String> suggestions = new ArrayList<>();

        String[] data = input.split(":", -1);
        if (data.length <= 1) {
            for (String key : PotionEffects.getAliasMap().keySet()) {
                String name = key.replace(" ", "");
                if (name.toLowerCase().startsWith(data[0].toLowerCase())) {
                    suggestions.add(prefix + name);
                }
            }
            if (data[0].length() > 0) {
                for (List<String> aliases : PotionEffects.getAliasMap().values()) {
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
                for (int i = 0; i < 3; i++) {
                    suggestions.add(prefix + data[0] + ":" + i);
                }
            }
        } else if (data.length <= 3) {
            int[] times = new int[] {-1, 60, 100, 200, 600, 1200, 3600, 6000};
            for (int ticks : times) {
                if (Integer.toString(ticks).startsWith(data[2].toLowerCase())) {
                    suggestions.add(prefix + data[0] + ":" + data[1] + ":" + ticks);
                }
            }
        } else if (data.length <= 4) {
            ColorO color = new ColorO();
            suggestions.addAll(color.onComplete(sender, prefix + data[0] + ":" + data[1] + ":" + data[2] + ":", data[3]));
        }

        Collections.sort(suggestions);
        return suggestions;
    }

    @Override
    public PotionO clone() {
        return super.cloneData(new PotionO());
    }
}
