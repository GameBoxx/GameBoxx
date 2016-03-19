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

public class MultiO extends SingleOption<SingleOption[], MultiO> {

    private final String splitString;
    private final String format;
    private final SingleOption[] options;

    public MultiO(String splitString, String format, SingleOption... options) {
        this.splitString = splitString;
        this.format = format;
        this.options = options;
    }

    @Override
    public boolean parse(CommandSender sender, String input) {
        error = "";
        value = null;

        String[] split = input.split(splitString, options.length);
        if (split.length < options.length) {
            error = Msg.getString("mixed.invalid", Param.P("format", format));
            return false;
        }

        for (int i = 0; i < options.length; i++) {
            if (!options[i].parse(sender, split[i])) {
                error = options[i].getError();
                return false;
            }
        }

        value = options;
        return true;
    }

    @Override
    public String serialize() {
        return serialize(splitString, getValue());
    }

    public static String serialize(String glue, SingleOption[] val) {
        if (val == null || val.length < 1) {
            return null;
        }
        String result = "";
        for (SingleOption option : val) {
            result += glue + option.getDisplayValue();
        }
        return result.substring(glue.length());
    }

    @Override
    public String getTypeName() {
        return "Multi";
    }

    @Override
    public MultiO clone() {
        return super.cloneData(new MultiO(splitString, format, options));
    }
}
