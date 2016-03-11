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
import org.bukkit.Particle;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Particles extends AliasMap<Particle> {

    private Particles() {
        super("Particles", new File(ALIASES_FOLDER, "Particles.yml"), "particle");
    }

    @Override
    public void onLoad() {
        add(Particle.EXPLOSION_NORMAL, "Normal Explosion", "Normal Explode", "NExplosion", "NExplode", "Explosion1", "Explode1", "NE", "EX1", "Mob Death");
        add(Particle.EXPLOSION_LARGE, "Large Explosion", "Large Explode", "Explosion", "Explode", "LExplosion", "LExplode", "Explosion2", "Explode2", "LE", "EX2", "Fireball");
        add(Particle.EXPLOSION_HUGE, "Huge Explosion", "Huge Explode", "HExplosion", "HExplode", "Explosion3", "Explode3", "HE", "EX3", "TnT");
        add(Particle.FIREWORKS_SPARK, "Firework Spark", "Firework", "Spark", "Firework Rocket", "Rocket");
        add(Particle.WATER_BUBBLE, "Bubble", "Air");
        add(Particle.WATER_SPLASH, "Splash", "Boat", "Wolf");
        add(Particle.WATER_WAKE, "Wake", "Fishing", "Fish");
        add(Particle.SUSPENDED, "Suspended", "Underwater");
        add(Particle.SUSPENDED_DEPTH, "Suspended Depth", "Void");
        add(Particle.CRIT, "Crit", "Arrow", "Bow");
        add(Particle.CRIT_MAGIC, "Magic Crit", "Sharpness", "Smite", "MCrit", "CritM");
        add(Particle.SMOKE_NORMAL, "Normal Smoke", "Smoke", "Small Smoke", "SSmoke", "SmokeS", "NSmoke", "SmokeN");
        add(Particle.SMOKE_LARGE, "Large Smoke", "LSmoke", "SmokeL");
        add(Particle.SPELL, "Spell", "Potion", "Effect");
        add(Particle.SPELL_INSTANT, "Instant Spell", "ISpell", "SpellI", "Splash Potion", "SPotion", "SplashEffect");
        add(Particle.SPELL_MOB, "Mob Spell", "MSpell", "Mob Potion", "MPotion", "Mob Effect", "MEffect");
        add(Particle.SPELL_MOB_AMBIENT, "Ambient Mob Spell", "AMSpell", "AMob Spell", "Ambient Mob Potion", "AMob Potion", "AMPotion", "Ambient Mob Effect", "AMob Effect", "AMEffect", "Beacon");
        add(Particle.SPELL_WITCH, "Witch", "Witch Magic", "Magic", "Purple");
        add(Particle.DRIP_WATER, "Water Drip", "Sponge", "WDrip");
        add(Particle.DRIP_LAVA, "Lava Drip", "Lava", "LavaD", "LDrip");
        add(Particle.VILLAGER_ANGRY, "Angry Villager", "Angry", "AVillager", "AngryV");
        add(Particle.VILLAGER_HAPPY, "Happy Villager", "Happy", "Green", "Feed", "Bonemeal", "Grow", "HVillager", "VillagerH");
        add(Particle.TOWN_AURA, "Town Aura", "Aura", "Town", "Mycelium", "Mycel");
        add(Particle.NOTE, "Note", "Music", "Note Block");
        add(Particle.PORTAL, "Portal", "Enderman", "Endermite", "Enderpearl", "Ender");
        add(Particle.ENCHANTMENT_TABLE, "Enchantment Table", "Enchant", "Enchantment", "Enchant Table", "ETable", "EnchantmentT", "EnchantT", "Ench Table", "Ench");
        add(Particle.FLAME, "Flame", "Fire", "Torch", "Furnace", "Spawner");
        add(Particle.LAVA, "Lava", "Lava Pop", "Pop", "Fire Spark", "Magma");
        add(Particle.FOOTSTEP, "Footstep", "Squares", "Path");
        add(Particle.CLOUD, "Cloud", "White Smoke", "WSmoke", "SmokeW");
        add(Particle.REDSTONE, "Redstone", "Dust", "RStone");
        add(Particle.SNOWBALL, "Snowball", "Snowball Poof", "S");
        add(Particle.SNOW_SHOVEL, "Snow Shovel", "Snowman", "Snow Golem", "Snow");
        add(Particle.SLIME, "Slime", "Slimes", "Slimeball");
        add(Particle.HEART, "Heart", "Hearts", "Love", "Breed", "Tame");
        add(Particle.BARRIER, "Barrier", "Blockade", "Stop");
        add(Particle.ITEM_CRACK, "Item Crack", "Icon Crack", "Eat", "Egg", "Item Break", "Item", "IC", "IB", "ICrack", "IBreak");
        add(Particle.BLOCK_CRACK, "Block Crack", "Block", "Block Break", "BC", "BCrack", "BB", "BBreak");
        add(Particle.BLOCK_DUST, "Block Dust", "Fall", "BDus", "BD");
        add(Particle.WATER_DROP, "Water Drop", "Rain", "WaterD", "WDrop", "Droplet", "Drop");
        add(Particle.ITEM_TAKE, "Item Take", "ITake", "ItemT", "Take");
        add(Particle.MOB_APPEARANCE, "Mob Appearance", "Elder Guardian", "EGuardian", "Guardian", "Mob Appear", "Guardian Appear");
        add(Particle.DRAGON_BREATH, "Dragon Breath", "DBreath", "DragonB", "DB", "Acid");
        add(Particle.END_ROD, "End Rod", "Rod", "Shulker Bullet", "Shulker", "Bullet");
        add(Particle.DAMAGE_INDICATOR, "Damage Indicator", "Hurt", "Damage", "Damage Heart", "Dark Heart");
        add(Particle.SWEEP_ATTACK, "Sweep Attack", "Sweep", "Attack", "SA");
    }

    public static Particle get(String string) {
        return instance()._get(string);
    }

    public static String getName(Particle key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(Particle key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(Particle key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static Particles instance() {
        if (getMap(Particles.class) == null) {
            aliasMaps.put(Particles.class, new Particles());
        }
        return (Particles)getMap(Particles.class);
    }
}
