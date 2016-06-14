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
import info.gameboxx.gameboxx.util.Random;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;

import java.util.*;

public class ColorO extends SingleOption<Color, ColorO> {

    public static final Map<String, Color> PRESETS = new HashMap<>();

    static {
        PRESETS.put("black", Color.fromRGB(0,0,0));
        PRESETS.put("darkblue", Color.fromRGB(0,0,170));
        PRESETS.put("darkgreen", Color.fromRGB(0,170,0));
        PRESETS.put("darkaqua", Color.fromRGB(0,170,170));
        PRESETS.put("darkred", Color.fromRGB(170,0,0));
        PRESETS.put("darkpurple", Color.fromRGB(170,0,0));
        PRESETS.put("gold", Color.fromRGB(255,170,0));
        PRESETS.put("gray", Color.fromRGB(170,170,170));
        PRESETS.put("darkgray", Color.fromRGB(85,85,85));
        PRESETS.put("blue", Color.fromRGB(85,85,255));
        PRESETS.put("green", Color.fromRGB(85,255,85));
        PRESETS.put("aqua", Color.fromRGB(85,255,255));
        PRESETS.put("red", Color.fromRGB(255,85,85));
        PRESETS.put("pink", Color.fromRGB(255,85,255));
        PRESETS.put("yellow", Color.fromRGB(255,255,85));
        PRESETS.put("white", Color.fromRGB(255,255,255));
    }

    @Override
    public boolean parse(CommandSender sender, String input) {
        //Truly random
        if (input.equals("**")) {
            value = Color.fromRGB(Random.Int(255), Random.Int(255), Random.Int(255));
        }

        //Saturated/Bright random
        if (input.equals("*")) {
            value = Parse.HSLColor(Random.Int(255),Random.Int(150, 255), Random.Int(120, 160));
        }

        if (input.startsWith("#")) {
            input = input.substring(1);
        }

        boolean hsb = false;
        if (input.startsWith("^")) {
            input = input.substring(1);
            hsb = true;
        }

        String[] split = input.split(",");
        if (split.length > 2) {
            Map<String, Integer> clrs = new HashMap<>();
            clrs.put("red", getColorValue(split[0]));
            clrs.put("green", getColorValue(split[1]));
            clrs.put("blue", getColorValue(split[2]));
            for (Map.Entry<String, Integer> clr : clrs.entrySet()) {
                if (clr.getValue() == null) {
                    error = Msg.getString("color.not-number", Param.P("input", input), Param.P("color", clr.getKey()));
                    return false;
                }
                if (clr.getValue() < 0 || clr.getValue() > 255) {
                    error = Msg.getString("color.minmax", Param.P("input", clr.getValue()), Param.P("color", clr.getKey()));
                }
            }
            if (hsb) {
                value = Parse.HSLColor(clrs.get("red"), clrs.get("green"), clrs.get("blue"));
            } else {
                value = Color.fromRGB(clrs.get("red"), clrs.get("green"), clrs.get("blue"));
            }
        } else if (input.matches("[0-9A-Fa-f]+")) {
            value = Color.fromRGB(Integer.parseInt(input, 16));
        } else if (PRESETS.containsKey(input.toLowerCase())) {
            value = Color.fromRGB(PRESETS.get(input.toLowerCase()).asRGB());
        }

        if (value == null) {
            error = Msg.getString("color.invalid", Param.P("input", input));
            return false;
        }
        return true;
    }

    private Integer getColorValue(String input) {
        String[] split = input.split("-");
        if (split.length > 1) {
            Integer val1 = Parse.Int(split[0]);
            Integer val2 = Parse.Int(split[1]);
            if (val1 == null || val2 == null) {
                return null;
            }
            return Random.Int(Math.min(val1, val2), Math.max(val1, val2));
        } else {
            return Parse.Int(input);
        }
    }

    @Override
    public String serialize() {
        return serialize(getValue());
    }

    public static String serialize(Color color) {
        return color == null ? null : color.getRed() + "," + color.getGreen() + "," + color.getBlue();
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(Color color) {
        return color == null ? null : Msg.getString("color.display", Param.P("r", color.getRed()), Param.P("g", color.getGreen()), Param.P("b", color.getBlue()));
    }

    @Override
    public String getTypeName() {
        return "Color";
    }

    @Override
    public List<String> onComplete(CommandSender sender, String prefix, String input) {
        List<String> suggestions = new ArrayList<>();

        for (String preset : PRESETS.keySet()) {
            if (input.isEmpty() || preset.toLowerCase().startsWith(input)) {
                suggestions.add(prefix + preset);
            }
        }

        if (input.isEmpty() || input.startsWith("*")) {
            suggestions.add(prefix + "*");
            suggestions.add(prefix + "**");
        }

        Collections.sort(suggestions);
        return suggestions;
    }

    @Override
    public ColorO clone() {
        return super.cloneData(new ColorO());
    }
}
