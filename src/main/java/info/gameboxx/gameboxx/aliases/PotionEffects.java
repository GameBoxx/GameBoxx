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
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.List;
import java.util.Map;

public class PotionEffects extends AliasMap<PotionEffectType> {

    private PotionEffects() {
        super("PotionEffects", new File(ALIASES_FOLDER, "PotionEffects.yml"), "potioneffect", "peffects", "poteffects");
    }

    @Override
    public void onLoad() {
        add(PotionEffectType.SPEED, "Speed", "rush", "spd", "sp");
        add(PotionEffectType.SLOW, "Slowness", "slo", "sl");
        add(PotionEffectType.FAST_DIGGING, "Haste", "fastdig", "digfast", "fdigging", "digf", "fastd", "fd", "has");
        add(PotionEffectType.SLOW_DIGGING, "Mining Fatigue", "miningfat", "minefatigue", "mfatigue", "miningf", "minef", "slowdig", "digslow", "digs", "slowd", "sd", "mf");
        add(PotionEffectType.INCREASE_DAMAGE, "Strength", "str", "incdamage", "dmgincreease", "damageincrease", "dmginc", "incdmg");
        add(PotionEffectType.HEAL, "Instant Health", "health", "hp", "healing", "instahealth", "instaheal", "instanth", "instah");
        add(PotionEffectType.HARM, "Instant Damage", "damage", "dmg", "harming", "instadamage", "instadmg", "instantd", "instad");
        add(PotionEffectType.JUMP, "Jump Boost", "jumpb", "jboost", "bounce");
        add(PotionEffectType.CONFUSION, "Nausea", "dizzy", "naus", "conf", "confuse");
        add(PotionEffectType.REGENERATION, "Regeneration", "regen", "reg", "hpregen");
        add(PotionEffectType.DAMAGE_RESISTANCE, "Resistance", "resist", "res", "dmgresistance");
        add(PotionEffectType.FIRE_RESISTANCE, "Fire Resistance", "fireresist", "fresistance", "fresist", "firer", "fr", "fireprotection", "fireprot", "fprot");
        add(PotionEffectType.WATER_BREATHING, "Water Breathing", "waterbreath", "wbreathing", "breathing", "breath", "air", "oxygen", "oxy", "wb");
        add(PotionEffectType.INVISIBILITY, "Invisibility", "invis", "inv");
        add(PotionEffectType.BLINDNESS, "Blindness", "blind", "blinding", "fog");
        add(PotionEffectType.NIGHT_VISION, "Night Vision", "vision", "nightv", "nvision");
        add(PotionEffectType.HUNGER, "Hunger", "hun", "starving", "starve", "star");
        add(PotionEffectType.WEAKNESS, "Weakness", "weak", "weakening", "weaken", "bonusdamage", "extradamage", "bonusdmg", "extradmg");
        add(PotionEffectType.POISON, "Poison", "toxic", "dot", "poi");
        add(PotionEffectType.WITHER, "Wither", "wit", "blackpoison", "bpoison");
        add(PotionEffectType.HEALTH_BOOST, "Health Boost", "hboost", "hpboost", "hp", "healboost","healthb", "healb", "hpb", "bonushealth", "bonushp", "extrahealth", "extrahp");
        add(PotionEffectType.ABSORPTION, "Absorption", "absorb", "abs");
        add(PotionEffectType.SATURATION, "Saturation", "sat", "food", "feed", "feeding");
        add(PotionEffectType.GLOWING, "Glowing", "glow", "outline", "highlight");
        add(PotionEffectType.LEVITATION, "Levitation", "fly", "flying", "levitate", "lev", "hovering", "hover");
        add(PotionEffectType.LUCK, "Luck", "lucky");
        add(PotionEffectType.UNLUCK, "Bad Luck", "bluck", "badl", "unlucky");
    }

    @Override
    public String getKey(PotionEffectType key) {
        return key.getName();
    }

    public static PotionEffectType get(String string) {
        return instance()._get(string);
    }

    public static String getName(PotionEffectType key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(PotionEffectType key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(PotionEffectType key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static PotionEffects instance() {
        if (getMap(PotionEffects.class) == null) {
            aliasMaps.put(PotionEffects.class, new PotionEffects());
        }
        return (PotionEffects)getMap(PotionEffects.class);
    }
}
