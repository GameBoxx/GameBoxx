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
import org.bukkit.Art;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Paintings extends AliasMap<Art> {

    private Paintings() {
        super("Paintings", new File(ALIASES_FOLDER, "Paintings.yml"), "painting", "pain", "art", "motives", "motive");
    }

    @Override
    public void onLoad() {
        add(Art.KEBAB, "Kebab", "1x1_1", "Keb", "Food");
        add(Art.AZTEC, "Aztec", "1x1_2", "de_aztec", "Azt");
        add(Art.ALBAN, "Alban", "1x1_3", "Albanian", "Albania", "Alb", "Fez");
        add(Art.AZTEC2, "Aztec2", "1x1_4", "de_aztec2", "Azt2");
        add(Art.BOMB, "Bomb", "1x1_5", "Bombed", "Bo", "de_dust2");
        add(Art.PLANT, "Plant", "1x1_6", "Pl", "Plants", "Pots", "Pot");
        add(Art.WASTELAND, "Wasteland", "1x1_7", "Waste", "Was", "Rabbit");
        add(Art.POOL, "pool", "2x1_1", "Po", "Skinny Dip", "Swimming", "Swim");
        add(Art.COURBET, "Courbet", "2x1_2", "Cou", "Hikers", "Hiker", "Beards");
        add(Art.SEA, "Sea", "2x1_3", "Seaside");
        add(Art.SUNSET, "Sunset", "2x1_41", "sunset_dense", "Sun", "Mountains", "Mountain");
        add(Art.CREEBET, "Creebet", "2x1_5", "Seaside2", "Creeper", "Cre");
        add(Art.WANDERER, "Wanderer", "1x2_1", "Wander", "Wan", "Man");
        add(Art.GRAHAM, "Graham", "1x2_2", "Gra", "King");
        add(Art.MATCH, "Match", "2x2_1", "Mat", "Matches", "Lighter", "Fire");
        add(Art.BUST, "Bust", "2x2_2", "Bus", "Statue", "");
        add(Art.STAGE, "Stage", "2x2_3", "Sta", "Spider", "Scenery", "Scene");
        add(Art.VOID, "Void", "2x2_3", "Angel", "Pray");
        add(Art.SKULL_AND_ROSES, "SkullAndRoses", "2x2_4", "Moonlight", "SR", "SaR");
        add(Art.WITHER, "Wither", "2x2_5", "Withers", "Wit", "Wither Skulls", "WS");
        add(Art.FIGHTERS, "Fighters", "4x2_1", "Fight", "Karate", "Karate+");
        add(Art.POINTER, "Pointer", "4x4_1", "Point", "Hand", "Finger", "Karateka");
        add(Art.PIGSCENE, "Pigscene", "4x4_2", "RGB", "Cavas", "Pig");
        add(Art.BURNINGSKULL, "BurningSkull", "4x4_3", "Skull on Fire");
        add(Art.SKELETON, "Skeleton", "4x3_1", "Mortal Coil");
        add(Art.DONKEYKONG, "DonkeyKong", "4x3_2", "Donkey", "Kong");
    }

    public static Art get(String string) {
        return instance()._get(string);
    }

    public static String getName(Art key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(Art key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(Art key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static Paintings instance() {
        if (getMap(Paintings.class) == null) {
            aliasMaps.put(Paintings.class, new Paintings());
        }
        return (Paintings)getMap(Paintings.class);
    }
}
