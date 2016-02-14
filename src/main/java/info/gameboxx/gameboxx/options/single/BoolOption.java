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

public class BoolOption extends SingleOption {

    public BoolOption() {
        super();
    }

    public BoolOption(String name) {
        super(name);
    }

    public BoolOption(String name, Boolean defaultValue) {
        super(name, defaultValue);
    }


    @Override
    public boolean parse(Object input) {
        if (!parseObject(input)) {
            if (input instanceof Number) {
                if (((Number)input) == 0) {
                    value = false;
                    return true;
                } else if (((Number)input) == 1) {
                    value = true;
                    return true;
                }
            }
            return false;
        }
        if (value != null) {
            return true;
        }
        return parse((String)input);
    }

    @Override
    public boolean parse(String input) {
        input = input.toLowerCase();
        if (input.equals("true") || input.equals("t") || input.equals("yes") || input.equals("y") || input.equals("+") || input.equals("v") || input.equals("1")) {
            value = true;
        } else if (input.equals("false") || input.equals("f") || input.equals("no") || input.equals("n") || input.equals("-") || input.equals("x") || input.equals("0")) {
            value = false;
        } else {
            error = "Input string is not a boolean.";
            return false;
        }
        return true;
    }

    @Override
    public boolean parse(Player player, String input) {
        return parse(input);
    }

    @Override
    public Boolean getValue() {
        return (Boolean)getValueOrDefault();
    }

    @Override
    public String getTypeName() {
        return "boolean";
    }

    @Override
    public Class getRawClass() {
        return Boolean.class;
    }

    @Override
    public BoolOption clone() {
        return new BoolOption(name, (Boolean)defaultValue);
    }
}
