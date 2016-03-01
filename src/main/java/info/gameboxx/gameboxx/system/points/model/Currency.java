/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2016 GameBoxx <http://gameboxx.info>
 *  Copyright (c) 2016 contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package info.gameboxx.gameboxx.system.points.model;


import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;

public class Currency {

    private static Map<String, Currency> forName = Maps.newHashMap();

    static {
        // TODO: 2/28/2016 LOAD CURRENCIES FROM FILE
    }

    public static Currency forName(String name) {
        return forName.get(name);
    }

    public static Iterator<Currency> iterator() {
        return forName.values().iterator();
    }

    private String name;
    private String singular;
    private String plural;
    private String syntax;

    public Currency(String name, String singular, String plural, String syntax) {
        this.name = name;
        this.singular = singular;
        this.plural = plural;
        this.syntax = syntax;
    }

    public String getName() {
        return name;
    }

    public String getPlural() {
        return plural;
    }

    public String getSingular() {
        return singular;
    }

    public String getSyntax() {
        return syntax;
    }
}