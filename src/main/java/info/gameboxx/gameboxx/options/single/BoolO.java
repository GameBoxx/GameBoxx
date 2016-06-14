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
import org.bukkit.command.CommandSender;

import java.util.*;

public class BoolO extends SingleOption<Boolean, BoolO> {

    public static List<String> TRUE_VALUES = new ArrayList<>(Arrays.asList("true", "yes", "t", "y", "1", "+", "v"));
    public static List<String> FALSE_VALUES = new ArrayList<>(Arrays.asList("false", "no", "f", "n", "0", "-", "x"));

    @Override
    public boolean parse(CommandSender sender, String input) {
        input = input.toLowerCase();

        if (TRUE_VALUES.contains(input.toLowerCase())) {
            value = true;
            return true;
        }

        if (FALSE_VALUES.contains(input.toLowerCase())) {
            value = false;
            return true;
        }

        error = Msg.getString("boolean.invalid", Param.P("input", input));
        return false;
    }

    @Override
    public BoolO clone() {
        return super.cloneData(new BoolO());
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    @Override
    public String getTypeName() {
        return "Boolean";
    }

    @Override
    public List<String> onComplete(CommandSender sender, String prefix, String input) {
        List<String> suggestions = new ArrayList<>();

        for (String string : TRUE_VALUES) {
            if (input.isEmpty() || string.startsWith(input)) {
                suggestions.add(prefix + string);
            }
        }
        for (String string : FALSE_VALUES) {
            if (input.isEmpty() || string.startsWith(input)) {
                suggestions.add(prefix + string);
            }
        }

        Collections.sort(suggestions);
        return suggestions;
    }

    public static String display(Boolean bool) {
        return bool == null ? null : (bool ? Msg.getString("boolean.display.true") : Msg.getString("boolean.display.false"));
    }
}
