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
import org.bukkit.potion.PotionType;

import java.io.File;
import java.util.List;
import java.util.Map;

public class PotionTypes extends AliasMap<PotionType> {

    private PotionTypes() {
        super("PotionTypes", new File(ALIASES_FOLDER, "PotionTypes.yml"), "potiontype", "potions", "ptypes", "pottypes");
    }

    @Override
    public void onLoad() {
        add(PotionType.UNCRAFTABLE, "Uncraftable", "None", "Default", "Def", "Uncraft", "UC");
        add(PotionType.WATER, "Water", "Wat", "WA");
        add(PotionType.MUNDANE, "Mundane", "Mun", "MU", "MD");
        add(PotionType.THICK, "Thick", "Thi", "TH");
        add(PotionType.AWKWARD, "Awkward", "Awk", "AW");
        add(PotionType.NIGHT_VISION, "Night Vision", "NV", "NVision", "NightV", "Vision", "Night Vis", "NVis");
        add(PotionType.INVISIBILITY, "Invisibility", "Invis", "Inv", "IN");
        add(PotionType.JUMP, "Jump", "Jumping", "Leap", "Leaping", "JU", "LE");
        add(PotionType.FIRE_RESISTANCE, "Fire Resistance", "FR", "FResistance", "Fire Resist", "Fire Res", "FResist", "FRes", "FireR");
        add(PotionType.SPEED, "Speed", "SP");
        add(PotionType.SLOWNESS, "Slowness", "SL", "Slow");
        add(PotionType.WATER_BREATHING, "Water Breathing", "WBreathing", "WaterBreathing", "Water Breath", "WBreath", "WB");
        add(PotionType.INSTANT_HEAL, "Instant Health", "IHealth", "Instant Heal", "IHeal", "Instant HP", "IHP", "IH");
        add(PotionType.INSTANT_DAMAGE, "Instant Damage", "IDamage", "Instant Dmg", "IDmg", "Harming", "Harm", "ID");
        add(PotionType.POISON, "Poison", "Poi", "Toxic", "PO");
        add(PotionType.REGEN, "Regeneration", "Regen", "Reg", "RE");
        add(PotionType.STRENGTH, "Strength", "Str", "ST");
        add(PotionType.WEAKNESS, "Weakness", "Weak", "WE");
        add(PotionType.LUCK, "Luck", "Lucky", "LU");
    }

    public static PotionType get(String string) {
        return instance()._get(string);
    }

    public static String getName(PotionType key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(PotionType key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(PotionType key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static PotionTypes instance() {
        if (getMap(PotionTypes.class) == null) {
            aliasMaps.put(PotionTypes.class, new PotionTypes());
        }
        return (PotionTypes)getMap(PotionTypes.class);
    }
}
