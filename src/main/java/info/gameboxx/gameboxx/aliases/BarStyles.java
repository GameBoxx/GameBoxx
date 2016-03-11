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

package info.gameboxx.gameboxx.aliases;

import info.gameboxx.gameboxx.aliases.internal.AliasMap;
import org.bukkit.boss.BarStyle;

import java.io.File;
import java.util.List;
import java.util.Map;

public class BarStyles extends AliasMap<BarStyle> {

    private BarStyles() {
        super("BarStyles", new File(ALIASES_FOLDER, "BossBars.yml"), "barstyle", "bossbarstyles", "bossbarstyle");
    }

    @Override
    public void onLoad() {
        add(BarStyle.SOLID, "Solid", "0", "None", "Blank", "Default", "Def", "D", "S");
        add(BarStyle.SEGMENTED_6, "6 Segments", "6", "6S", "S6", "1", "S1");
        add(BarStyle.SEGMENTED_10, "10 Segments", "10", "10S", "S10", "2", "S2");
        add(BarStyle.SEGMENTED_12, "12 Segments", "12", "12S", "S12", "3", "S3");
        add(BarStyle.SEGMENTED_20, "20 Segments", "20", "20S", "S20", "4", "S4");
    }

    public static BarStyle get(String string) {
        return instance()._get(string);
    }

    public static String getName(BarStyle key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(BarStyle key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(BarStyle key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static BarStyles instance() {
        if (getMap(BarStyles.class) == null) {
            aliasMaps.put(BarStyles.class, new BarStyles());
        }
        return (BarStyles)getMap(BarStyles.class);
    }
}
