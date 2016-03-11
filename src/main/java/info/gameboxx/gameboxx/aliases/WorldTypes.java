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
import org.bukkit.WorldType;

import java.io.File;
import java.util.List;
import java.util.Map;

public class WorldTypes extends AliasMap<WorldType> {

    private WorldTypes() {
        super("WorldTypes", new File(ALIASES_FOLDER, "WorldData.yml"), "worldtype", "worldt");
    }

    @Override
    public void onLoad() {
        add(WorldType.NORMAL, "Normal", "Default", "Def", "Norm", "DE", "D", "NO", "N");
        add(WorldType.FLAT, "Flat", "Super Flat", "SFlat", "Creative", "FL", "F");
        add(WorldType.VERSION_1_1, "Version 1.1", "Old", "Old Version", "Beta", "1.1", "1_1", "V1.1", "V1_1");
        add(WorldType.LARGE_BIOMES, "Large Biomes", "LBiomes", "Large", "LB", "Big Biomes", "Big", "BB");
        add(WorldType.AMPLIFIED, "Amplified", "Amp", "AM", "A");
        add(WorldType.CUSTOMIZED, "Customized", "Custom", "CU", "C");
    }

    public static WorldType get(String string) {
        return instance()._get(string);
    }

    public static String getName(WorldType key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(WorldType key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(WorldType key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static WorldTypes instance() {
        if (getMap(WorldTypes.class) == null) {
            aliasMaps.put(WorldTypes.class, new WorldTypes());
        }
        return (WorldTypes)getMap(WorldTypes.class);
    }
}
