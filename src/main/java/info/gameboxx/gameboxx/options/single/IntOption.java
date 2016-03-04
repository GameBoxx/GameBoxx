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

import info.gameboxx.gameboxx.options.SingleOption;
import org.bukkit.entity.Player;

public class IntOption extends SingleOption<Integer, IntOption> {

    private Integer min = null;
    private Integer max = null;

    public IntOption min(Integer min) {
        this.min = min;
        return this;
    }

    public IntOption max(Integer max) {
        this.max = max;
        return this;
    }

    @Override
    public boolean parse(Player player, String input) {
        try {
            value = Integer.parseInt(input);
        } catch (Exception e) {
            error = "Input string is not a number.";
            return false;
        }

        if (min != null && (Integer)value < min) {
            error = "Number is too low, can't be lower than " + min + ".";
            return false;
        }

        if (max != null && (Integer)value > max) {
            error = "Number is too high, can't be higher than " + max + ".";
            return false;
        }

        return true;
    }

    @Override
    public IntOption clone() {
        return super.cloneData(new IntOption().min(min).max(max));
    }
}
