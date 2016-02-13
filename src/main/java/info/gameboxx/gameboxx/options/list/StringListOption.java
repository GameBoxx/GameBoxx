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

package info.gameboxx.gameboxx.options.list;

import info.gameboxx.gameboxx.options.ListOption;
import info.gameboxx.gameboxx.options.single.StringOption;
import info.gameboxx.gameboxx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringListOption extends ListOption {

    private Integer minChars = null;
    private Integer maxChars = null;

    private String regexError = "";
    private String regex = null;

    private List<String> matchList = null;
    private Map<String, List<String>> matchMap = null;


    public StringListOption() {
        super();
    }

    public StringListOption(String name) {
        super(name);
    }

    public StringListOption(String name, String defaultValue) {
        super(name, defaultValue);
    }


    public StringListOption minChars(Integer minChars) {
        this.minChars = minChars;
        return this;
    }

    public StringListOption maxChars(Integer maxChars) {
        this.maxChars = maxChars;
        return this;
    }


    public StringListOption matchRegex(String regex) {
        this.regex = regex;
        return this;
    }

    public StringListOption matchRegex(String regex, String regexError) {
        this.regex = regex;
        this.regexError = regexError;
        return this;
    }


    public StringListOption match(String... strings) {
        matchList = new ArrayList<>();
        for (String str : strings) {
            matchList.add(str.toLowerCase());
        }
        return this;
    }

    public StringListOption match(List<String> strings) {
        for (String str : strings) {
            str.toLowerCase();
        }
        matchList = strings;
        return this;
    }

    public StringListOption match(Pair<String, List<String>>... strings) {
        matchMap = new HashMap<>();
        for (Pair<String, List<String>> pair : strings) {
            for (String str : pair.second) {
                str.toLowerCase();
            }
            matchMap.put(pair.first.toLowerCase(), pair.second);
        }
        return this;
    }

    public StringListOption match(Map<String, List<String>> strings) {
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
    public StringOption getSingleOption() {
        return new StringOption(name, (String)defaultValue).minChars(minChars).maxChars(maxChars).matchRegex(regex, regexError).match(matchList).match(matchMap);
    }

    @Override
    public List<String> getValues() {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            values.add(getValue(i));
        }
        return values;
    }

    @Override
    public String getValue(int index) {
        return (String)getValueOrDefault(index);
    }

    public String getMatchResult(int index) {
        if (index >= value.size()) {
            throw new IndexOutOfBoundsException();
        }
        if (value.get(index) == null) {
            return "";
        }
        return ((StringOption)value.get(index)).getMatchResult();
    }
}
