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
import org.bukkit.DyeColor;

import java.io.File;
import java.util.List;
import java.util.Map;

public class DyeColors extends AliasMap<DyeColor> {

    private DyeColors() {
        super("DyeColors", new File(ALIASES_FOLDER, "DyeColors.yml"), "colors", "dyecolor");
    }

    @Override
    public void onLoad() {
        add(DyeColor.WHITE, "White", "0", "WH");
        add(DyeColor.ORANGE, "Orange", "1", "OR", "DY", "Dark Yellow", "DYellow");
        add(DyeColor.MAGENTA, "Magenta", "2", "LP", "MA", "Light Purple", "LPurple");
        add(DyeColor.LIGHT_BLUE, "Light Blue", "LB", "3", "LBlue");
        add(DyeColor.YELLOW, "Yellow", "4", "YE", "Light Yellow", "LYellow");
        add(DyeColor.LIME, "Lime", "5", "LI", "LG", "Light Green", "LGreen");
        add(DyeColor.PINK, "Pink", "6", "PI");
        add(DyeColor.GRAY, "Gray", "7", "GR", "Dark Gray", "DGray");
        add(DyeColor.SILVER, "Silver", "8", "SI", "Light Gray", "LGray");
        add(DyeColor.CYAN, "Cyan", "9", "DA", "CY", "Dark Aqua", "DAqua");
        add(DyeColor.PURPLE, "Purple", "10", "DP", "Dark Purple", "DPurple");
        add(DyeColor.BLUE, "Blue", "11", "DB", "Dark Blue", "DBlue");
        add(DyeColor.BROWN, "Brown", "BR", "12");
        add(DyeColor.GREEN, "Green", "13", "DG", "Dark Green", "DGreen");
        add(DyeColor.RED, "Red", "14", "DR", "RE", "Dark Red", "DRed");
        add(DyeColor.BLACK, "Black", "15", "BL");
    }

    public static DyeColor get(String string) {
        return instance()._get(string);
    }

    public static String getName(DyeColor key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(DyeColor key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(DyeColor key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static DyeColors instance() {
        if (getMap(DyeColors.class) == null) {
            aliasMaps.put(DyeColors.class, new DyeColors());
        }
        return (DyeColors)getMap(DyeColors.class);
    }
}
