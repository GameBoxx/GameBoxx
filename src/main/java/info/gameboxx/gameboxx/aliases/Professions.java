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
import org.bukkit.entity.Villager;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Professions extends AliasMap<Villager.Profession> {

    private Professions() {
        super("Professions", new File(ALIASES_FOLDER, "EntityData.yml"), "profession", "villagers", "villager");
    }

    @Override
    public void onLoad() {
        add(Villager.Profession.FARMER, "Farmer", "Default", "Def", "Normal", "None", "Farm", "FA", "F");
        add(Villager.Profession.LIBRARIAN, "Librarian", "Library", "Lib", "LI", "L");
        add(Villager.Profession.PRIEST, "Priest", "PR", "P");
        add(Villager.Profession.BLACKSMITH, "Blacksmith", "Smith", "BSmith", "BlackS", "BS", "BL");
        add(Villager.Profession.BUTCHER, "Butcher", "BU", "B");
    }

    public static Villager.Profession get(String string) {
        return instance()._get(string);
    }

    public static String getName(Villager.Profession key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(Villager.Profession key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(Villager.Profession key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static Professions instance() {
        if (getMap(Professions.class) == null) {
            aliasMaps.put(Professions.class, new Professions());
        }
        return (Professions)getMap(Professions.class);
    }
}
