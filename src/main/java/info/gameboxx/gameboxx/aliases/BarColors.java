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
import org.bukkit.boss.BarColor;

import java.io.File;
import java.util.List;
import java.util.Map;

public class BarColors extends AliasMap<BarColor> {

    private BarColors() {
        super("BarColors", new File(ALIASES_FOLDER, "BossBars.yml"), "barcolor", "bossbarcolors", "bossbarcolor");
    }

    @Override
    public void onLoad() {
        add(BarColor.PINK, "Pink", "PI", "P", "Default", "Def", "D");
        add(BarColor.BLUE, "Blue", "BL", "B");
        add(BarColor.RED, "Red", "RE", "R");
        add(BarColor.GREEN, "Green", "GR", "G");
        add(BarColor.YELLOW, "Yellow", "YE", "Y");
        add(BarColor.PURPLE, "Purple", "PU", "P");
        add(BarColor.WHITE, "White", "WH", "W");
    }

    public static BarColor get(String string) {
        return instance()._get(string);
    }

    public static String getName(BarColor key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(BarColor key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(BarColor key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static BarColors instance() {
        if (getMap(BarColors.class) == null) {
            aliasMaps.put(BarColors.class, new BarColors());
        }
        return (BarColors)getMap(BarColors.class);
    }
}
