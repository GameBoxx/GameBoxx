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
import org.bukkit.enchantments.Enchantment;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Enchantments extends AliasMap<Enchantment> {

    private Enchantments() {
        super("Enchantments", new File(ALIASES_FOLDER, "Enchantments.yml"), "enchantment", "enchants", "enchant", "ench");
    }

    @Override
    public void onLoad() {
        add(Enchantment.PROTECTION_ENVIRONMENTAL, "Protection", "prot", "pr");
        add(Enchantment.PROTECTION_FIRE, "Fire Protection", "fprotection", "fprot", "fireprot", "firep", "fp");
        add(Enchantment.PROTECTION_FALL, "Feather Falling", "ffalling", "ffall", "featherfall", "feeatherf", "ff");
        add(Enchantment.PROTECTION_EXPLOSIONS, "Blast Protection", "bprotection", "bprot", "blastprot", "blastp", "bp");
        add(Enchantment.PROTECTION_PROJECTILE, "Projectile Protection", "pprotection", "pprot", "projectileprot", "projectilep", "projprotection", "projprot", "projp", "pp");
        add(Enchantment.OXYGEN, "Respiration", "resp", "re", "oxy");
        add(Enchantment.WATER_WORKER, "Aqua Affinity", "waterw", "wworker", "aquaaff", "aquaa", "aaffinity", "ww", "aa");
        add(Enchantment.THORNS, "Thorns", "thorn", "spikes", "th");
        add(Enchantment.DEPTH_STRIDER, "Depth Strider", "depths", "dstrider", "ds");
        add(Enchantment.FROST_WALKER, "Frost Walker", "frostw", "fwalker", "icewalker", "icew", "iwalker", "fw", "iw");
        add(Enchantment.DAMAGE_ALL, "Sharpness", "sharp", "dmgall", "dall", "sh");
        add(Enchantment.DAMAGE_UNDEAD, "Smite", "dmgundead", "dundead", "sm");
        add(Enchantment.DAMAGE_ARTHROPODS, "Bane of Arthropods", "arthropods", "art", "banearthropods", "dmgarthropods", "darthropods", "barthropods", "bofa", "boa", "ba");
        add(Enchantment.KNOCKBACK, "Knockback", "knockb", "kb", "kn");
        add(Enchantment.FIRE_ASPECT, "Fire Aspect", "firea", "faspect", "fa");
        add(Enchantment.LOOT_BONUS_MOBS, "Looting", "loot", "lootm", "lootmobs", "lmobs");
        add(Enchantment.DIG_SPEED, "Efficiency", "eff", "haste", "ef");
        add(Enchantment.SILK_TOUCH, "Silk touch", "silk", "silkt", "stouch", "st");
        add(Enchantment.DURABILITY, "Unbreaking", "dura", "dur", "unbreakable", "unbreak", "unbr");
        add(Enchantment.LOOT_BONUS_BLOCKS, "Fortune", "fort", "for", "lootblocks", "lootb", "lblocks");
        add(Enchantment.ARROW_DAMAGE, "Power", "pwr", "pow");
        add(Enchantment.ARROW_KNOCKBACK, "Punch", "pun", "arrowknockb", "bowknockb", "aknockback", "bowknockback", "bknockback", "an", "bn");
        add(Enchantment.ARROW_FIRE, "Flame", "fla", "fl", "arrowf", "firearrow", "farrow", "afire", "firea");
        add(Enchantment.ARROW_INFINITE, "Infinity", "infin", "inf");
        add(Enchantment.LUCK, "Luck of the Sea", "lots", "lucksea", "luckofsea");
        add(Enchantment.LURE, "Lure", "luring", "lur");
        add(Enchantment.MENDING, "Mending", "mend", "repair", "autorepair", "rep");
    }

    @Override
    public String getKey(Enchantment key) {
        return key.getName();
    }

    public static Enchantment get(String string) {
        return instance()._get(string);
    }

    public static String getName(Enchantment key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(Enchantment key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(Enchantment key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static Enchantments instance() {
        if (getMap(Enchantments.class) == null) {
            aliasMaps.put(Enchantments.class, new Enchantments());
        }
        return (Enchantments)getMap(Enchantments.class);
    }
}
