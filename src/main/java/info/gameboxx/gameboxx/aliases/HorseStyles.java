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
import org.bukkit.entity.Horse;

import java.io.File;
import java.util.List;
import java.util.Map;

public class HorseStyles extends AliasMap<Horse.Style> {

    private HorseStyles() {
        super("HorseStyles", new File(ALIASES_FOLDER, "Horses.yml"), "horsestyle", "hstyles", "hstyle");
    }

    @Override
    public void onLoad() {
        add(Horse.Style.NONE, "None", "NO", "N", "X", "Default", "Def", "Blank", "BL");
        add(Horse.Style.WHITE, "White", "WH", "W");
        add(Horse.Style.WHITEFIELD, "White Field", "WF", "F", "WField", "Field");
        add(Horse.Style.WHITE_DOTS, "White Dots", "WD", "WDots");
        add(Horse.Style.BLACK_DOTS, "Black Dots", "BD", "BlackD", "BDots");
    }

    public static Horse.Style get(String string) {
        return instance()._get(string);
    }

    public static String getName(Horse.Style key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(Horse.Style key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(Horse.Style key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static HorseStyles instance() {
        if (getMap(HorseStyles.class) == null) {
            aliasMaps.put(HorseStyles.class, new HorseStyles());
        }
        return (HorseStyles)getMap(HorseStyles.class);
    }
}
