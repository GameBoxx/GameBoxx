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
import org.bukkit.FireworkEffect;

import java.io.File;
import java.util.List;
import java.util.Map;

public class FireworkEffects extends AliasMap<FireworkEffect.Type> {

    private FireworkEffects() {
        super("FireworkEffects", new File(ALIASES_FOLDER, "FireworkEffects.yml"), "fireworkeffect", "fireworktypes", "fireworktype");
    }

    @Override
    public void onLoad() {
        add(FireworkEffect.Type.BALL, "Ball", "B", "BA", "Default", "Normal", "Def", "None");
        add(FireworkEffect.Type.BALL_LARGE, "Large Ball", "LB", "LBA", "LBall", "LargeB", "Fire Charge", "FCharge", "Fireball", "FireC");
        add(FireworkEffect.Type.STAR, "Star", "S", "Gold Nugget", "Nugget", "Gold");
        add(FireworkEffect.Type.BURST, "Burst", "BU", "Feather");
        add(FireworkEffect.Type.CREEPER, "Creeper", "C", "Creep", "Creeper Face", "Face", "CFace", "CreeperF", "Head", "Skull");
    }

    public static FireworkEffect.Type get(String string) {
        return instance()._get(string);
    }

    public static String getName(FireworkEffect.Type key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(FireworkEffect.Type key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(FireworkEffect.Type key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static FireworkEffects instance() {
        if (getMap(FireworkEffects.class) == null) {
            aliasMaps.put(FireworkEffects.class, new FireworkEffects());
        }
        return (FireworkEffects)getMap(FireworkEffects.class);
    }
}
