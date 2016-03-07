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
import info.gameboxx.gameboxx.util.Pair;
import info.gameboxx.gameboxx.util.Str;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.entity.Player;

import java.util.*;

public class StringOption extends SingleOption<String, StringOption> {

    private Integer minChars = null;
    private Integer maxChars = null;

    private String regexFormat = null;
    private String regex = null;

    private List<String> matchList = null;
    private Map<String, List<String>> matchMap = null;

    public StringOption minChars(Integer minChars) {
        this.minChars = minChars;
        return this;
    }

    public StringOption maxChars(Integer maxChars) {
        this.maxChars = maxChars;
        return this;
    }


    public StringOption matchRegex(String regex) {
        this.regex = regex;
        return this;
    }

    public StringOption matchRegex(String regex, String regexFormat) {
        this.regex = regex;
        this.regexFormat = regexFormat;
        return this;
    }


    public StringOption match(String... strings) {
        matchList = new ArrayList<>();
        for (String str : strings) {
            matchList.add(str.toLowerCase());
        }
        return this;
    }

    public StringOption match(List<String> strings) {
        if (strings == null) {
            return this;
        }
        int i = 0;
        for (String str : strings) {
            strings.set(i++, str.toLowerCase());
        }
        matchList = strings;
        return this;
    }

    @SafeVarargs
    public final StringOption match(Pair<String, List<String>>... strings) {
        matchMap = new HashMap<>();
        for (Pair<String, List<String>> pair : strings) {
            int i = 0;
            for (String str : pair.second) {
                pair.second.set(i++, str.toLowerCase());
            }
            matchMap.put(pair.first.toLowerCase(), pair.second);
        }
        return this;
    }

    public StringOption match(Map<String, List<String>> strings) {
        if (strings == null) {
            return this;
        }
        for (Map.Entry<String, List<String>> entry : strings.entrySet()) {
            int i = 0;
            for (String str : entry.getValue()) {
                entry.getValue().set(i++, str.toLowerCase());
            }
            strings.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        matchMap = strings;
        return this;
    }

    @Override
    public boolean parse(Object input) {
        if (!parseObject(input)) {
            return false;
        }
        return parse((String) input);
    }

    @Override
    public boolean parse(Player player, String input) {
        if (matchList != null && !matchList.isEmpty()) {
            if (!matchList.contains(input.toLowerCase())) {
                error = Msg.getString("string.match-list", Param.P("input", input), Param.P("values", Str.implode(matchList)));
                return false;
            }
            value = input;
            return true;
        } else if (matchMap != null && !matchMap.isEmpty()) {
            if (!matchMap.containsKey(input.toLowerCase())) {
                for (Map.Entry<String, List<String>> entry : matchMap.entrySet()) {
                    if (entry.getValue().contains(input)) {
                        value = entry.getKey();
                        return true;
                    }
                }
                error = Msg.getString("string.match-map", Param.P("input", input), Param.P("values", Utils.getAliasesString("string.match-map-entry", matchMap)));
                return false;
            }
            value = input;
            return true;
        } else {
            if (regex != null && !input.matches(regex)) {
                if (regexFormat == null || regexFormat.isEmpty()) {
                    error = Msg.getString("string.match-regex", Param.P("input", input), Param.P("regex", regex));
                } else {
                    error = Msg.getString("string.match-regex-format", Param.P("input", input), Param.P("format", regexFormat));
                }
                return false;
            }

            if (minChars != null && input.length() < minChars) {
                error = Msg.getString("string.chars-min", Param.P("input", input), Param.P("chars", minChars));
                return false;
            }

            if (maxChars != null && input.length() > maxChars) {
                error = Msg.getString("string.chars-max", Param.P("input", input), Param.P("chars", maxChars));
                return false;
            }
            value = input;
        }
        return true;
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(String val) {
        return val == null ? null : Msg.getString("string.display", Param.P("val", val));
    }

    @Override
    public StringOption clone() {
        return super.cloneData(new StringOption().minChars(minChars).maxChars(maxChars).match(matchList).match(matchMap).matchRegex(regex));
    }
}
