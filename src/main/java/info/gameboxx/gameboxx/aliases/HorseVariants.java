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

public class HorseVariants extends AliasMap<Horse.Variant> {

    private HorseVariants() {
        super("HorseVariants", new File(ALIASES_FOLDER, "Horses.yml"), "horsevariant", "hvariants", "hvariant");
    }

    @Override
    public void onLoad() {
        add(Horse.Variant.HORSE, "Horse", "HO", "H", "Default", "Def");
        add(Horse.Variant.DONKEY, "Donkey", "DO", "D");
        add(Horse.Variant.MULE, "Mule", "MU", "M");
        add(Horse.Variant.UNDEAD_HORSE, "Undead Horse", "UH", "U", "Undead", "Zombie", "UHorse");
        add(Horse.Variant.SKELETON_HORSE, "Skeleton Horse", "SH", "S", "Skeleton", "Skeli", "SHorse");
    }

    public static Horse.Variant get(String string) {
        return instance()._get(string);
    }

    public static String getName(Horse.Variant key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(Horse.Variant key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(Horse.Variant key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static HorseVariants instance() {
        if (getMap(HorseVariants.class) == null) {
            aliasMaps.put(HorseVariants.class, new HorseVariants());
        }
        return (HorseVariants)getMap(HorseVariants.class);
    }
}
