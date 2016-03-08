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
import org.bukkit.entity.Ocelot;

import java.io.File;
import java.util.List;
import java.util.Map;

public class OcelotTypes extends AliasMap<Ocelot.Type> {

    private OcelotTypes() {
        super("OcelotTypes", new File(ALIASES_FOLDER, "EntityData.yml"), "ocelottype", "cattypes", "cattype", "ocelots", "ocelot", "cats", "cat");
    }

    @Override
    public void onLoad() {
        add(Ocelot.Type.WILD_OCELOT, "Wild Ocelot", "Wild", "WI", "W", "Untamed", "Default", "Def", "None");
        add(Ocelot.Type.BLACK_CAT, "Black Cat", "Black", "BL", "B");
        add(Ocelot.Type.RED_CAT, "Red Cat", "Red", "RE", "R");
        add(Ocelot.Type.SIAMESE_CAT, "Siamese Cat", "Siamese", "SI", "S", "Siam");
    }

    public static Ocelot.Type get(String string) {
        return instance()._get(string);
    }

    public static String getName(Ocelot.Type key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(Ocelot.Type key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(Ocelot.Type key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static OcelotTypes instance() {
        if (getMap(OcelotTypes.class) == null) {
            aliasMaps.put(OcelotTypes.class, new OcelotTypes());
        }
        return (OcelotTypes)getMap(OcelotTypes.class);
    }
}
