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
import org.bukkit.entity.Rabbit;

import java.io.File;
import java.util.List;
import java.util.Map;

public class RabbitTypes extends AliasMap<Rabbit.Type> {

    private RabbitTypes() {
        super("RabbitTypes", new File(ALIASES_FOLDER, "EntityData.yml"), "rabbittype", "rabbits", "rabbit");
    }

    @Override
    public void onLoad() {
        add(Rabbit.Type.BROWN, "Brown", "BR");
        add(Rabbit.Type.WHITE, "White", "WH", "W");
        add(Rabbit.Type.BLACK, "Black", "BL", "B");
        add(Rabbit.Type.BLACK_AND_WHITE, "Black and White", "BW", "Black White");
        add(Rabbit.Type.GOLD, "Gold", "GO", "G");
        add(Rabbit.Type.SALT_AND_PEPPER, "Salt and Pepper", "SP", "Salt", "Pepper", "Salt Pepper");
        add(Rabbit.Type.THE_KILLER_BUNNY, "The Killer Bunny", "TKB", "KB", "K", "Killer", "Killer Bunny", "KillerB");
    }

    public static Rabbit.Type get(String string) {
        return instance()._get(string);
    }

    public static String getName(Rabbit.Type key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(Rabbit.Type key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(Rabbit.Type key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static RabbitTypes instance() {
        if (getMap(RabbitTypes.class) == null) {
            aliasMaps.put(RabbitTypes.class, new RabbitTypes());
        }
        return (RabbitTypes)getMap(RabbitTypes.class);
    }
}
