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

public class IntO extends SingleOption<Integer, IntO> {

    private Integer min = null;
    private Integer max = null;

    public IntO min(Integer min) {
        this.min = min;
        return this;
    }

    public IntO max(Integer max) {
        this.max = max;
        return this;
    }

    @Override
    public boolean parse(CommandSender sender, String input) {
        try {
            value = Integer.parseInt(input);
        } catch (Exception e) {
            error = Msg.getString("integer.invalid", Param.P("input", input));
            return false;
        }

        if (min != null && (Integer)value < min) {
            error = Msg.getString("number-too-low", Param.P("input", input), Param.P("min", min));
            return false;
        }

        if (max != null && (Integer)value > max) {
            error = Msg.getString("number-too-high", Param.P("input", input), Param.P("max", max));
            return false;
        }

        return true;
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(Integer val) {
        return val == null ? null : Msg.getString("integer.display", Param.P("val", val));
    }

    @Override
    public String getTypeName() {
        return "Integer";
    }

    @Override
    public IntO clone() {
        return super.cloneData(new IntO().min(min).max(max));
    }
}
