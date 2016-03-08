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
import info.gameboxx.gameboxx.options.single.StringO;
import info.gameboxx.gameboxx.util.Pair;

import java.util.*;

public class StringLO extends ListOption<String, StringLO, StringO> {

    private Integer minChars = null;
    private Integer maxChars = null;

    private String regexFormat = "";
    private String regex = null;

    private List<String> matchList = null;
    private Map<String, List<String>> matchMap = null;

    public StringLO minChars(Integer minChars) {
        this.minChars = minChars;
        return this;
    }

    public StringLO maxChars(Integer maxChars) {
        this.maxChars = maxChars;
        return this;
    }


    public StringLO matchRegex(String regex) {
        this.regex = regex;
        return this;
    }

    public StringLO matchRegex(String regex, String regexFormat) {
        this.regex = regex;
        this.regexFormat = regexFormat;
        return this;
    }


    public StringLO match(String... strings) {
        matchList = new ArrayList<>();
        for (String str : strings) {
            matchList.add(str.toLowerCase());
        }
        return this;
    }

    public StringLO match(List<String> strings) {
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
    public final StringLO match(Pair<String, List<String>>... strings) {
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

    public StringLO match(Map<String, List<String>> strings) {
        if (strings == null) {
            return this;
        }
        matchMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : strings.entrySet()) {
            int i = 0;
            for (String str : entry.getValue()) {
                entry.getValue().set(i++, str.toLowerCase());
            }
            matchMap.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        return this;
    }


    @Override
    public StringO getSingleOption() {
        return new StringO().minChars(minChars).maxChars(maxChars).match(matchList).match(matchMap).matchRegex(regex, regexFormat);
    }

    @Override
    public StringLO clone() {
        return cloneData(new StringLO().minChars(minChars).maxChars(maxChars).match(matchList).match(matchMap).matchRegex(regex, regexFormat));
    }
}
