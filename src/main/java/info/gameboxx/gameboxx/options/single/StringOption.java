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
import info.gameboxx.gameboxx.util.Pair;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringOption extends SingleOption {

    private Integer minChars = null;
    private Integer maxChars = null;

    private String regexError = "";
    private String regex = null;

    private List<String> matchList = null;
    private Map<String, List<String>> matchMap = null;


    public StringOption() {
        super();
    }

    public StringOption(String name) {
        super(name);
    }

    public StringOption(String name, String defaultValue) {
        super(name, defaultValue);
    }


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

    public StringOption matchRegex(String regex, String regexError) {
        this.regex = regex;
        this.regexError = regexError;
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
        for (String str : strings) {
            str.toLowerCase();
        }
        matchList = strings;
        return this;
    }

    public StringOption match(Pair<String, List<String>>... strings) {
        matchMap = new HashMap<>();
        for (Pair<String, List<String>> pair : strings) {
            for (String str : pair.second) {
                str.toLowerCase();
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
            for (String str : entry.getValue()) {
                str.toLowerCase();
            }
            entry.getKey().toLowerCase();
        }
        matchMap = strings;
        return this;
    }


    @Override
    public boolean parse(Object input) {
        if (!parseObject(input)) {
            return false;
        }
        return parse((String)input);
    }

    @Override
    public boolean parse(String input) {
        if (matchList != null && !matchList.isEmpty()) {
            if (!matchList.contains(input.toLowerCase())) {
                error = "String doesn't match with a valid option.";
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
                error = "String doesn't match with a valid option.";
                return false;
            }
            value = input;
            return true;
        } else {
            if (regex != null && !input.matches(regex)) {
                error = regexError.isEmpty() ? "String doesn't match custom regex." : regexError;
                return false;
            }

            if (minChars != null && input.length() < minChars) {
                error = "String is too short, can't be shorter than " + minChars + " chars.";
                return false;
            }

            if (maxChars != null && input.length() > maxChars) {
                error = "String is too long, can't be longer than " + maxChars + " chars.";
                return false;
            }
            value = input;
        }
        return true;
    }

    @Override
    public boolean parse(Player player, String input) {
        return parse(input);
    }

    @Override
    public String getValue() {
        return (String)getValueOrDefault();
    }

    @Override
    public String serialize() {
        return getValue();
    }

    @Override
    public String getTypeName() {
        return "string";
    }

    @Override
    public Class getRawClass() {
        return String.class;
    }

    @Override
    public StringOption clone() {
        return (StringOption) new StringOption(name, (String)defaultValue).minChars(minChars).maxChars(maxChars).match(matchList).match(matchMap).matchRegex(regex).setDescription(description).setFlag(flag);
    }
}
