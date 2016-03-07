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
import org.bukkit.entity.Player;

public class BoolOption extends SingleOption<Boolean, BoolOption> {

    @Override
    public boolean parse(Player player, String input) {
        input = input.toLowerCase();
        switch (input) {
            case "true":
            case "t":
            case "yes":
            case "y":
            case "+":
            case "v":
            case "1":
                value = true;
                break;
            case "false":
            case "f":
            case "no":
            case "n":
            case "-":
            case "x":
            case "0":
                value = false;
                break;
            default:
                error = Msg.getString("boolean.invalid", Param.P("input", input));
                return false;
        }
        return true;
    }

    @Override
    public BoolOption clone() {
        return super.cloneData(new BoolOption());
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(Boolean bool) {
        return bool == null ? null : (bool ? Msg.getString("boolean.display.true") : Msg.getString("boolean.display.false"));
    }
}
