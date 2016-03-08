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
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.List;
import java.util.Map;

public class EntityTypes extends AliasMap<EntityType> {

    private EntityTypes() {
        super("EntityTypes", new File(ALIASES_FOLDER, "EntityTypes.yml"), "entities", "entity", "mobs");
    }

    @Override
    public void onLoad() {
        add(EntityType.DROPPED_ITEM, "Item", "Item Drop", "Items");
        add(EntityType.EXPERIENCE_ORB, "Experience Orb", "XP Orb", "Exp Orb", "XP", "Exp", "Orb");
        add(EntityType.LEASH_HITCH, "Lead Knot", "Leash Knot", "Lead", "Leash", "Lead Hitch");
        add(EntityType.PAINTING, "Painting", "Art", "Paint", "Picture");
        add(EntityType.ARROW, "Arrow");
        add(EntityType.SNOWBALL, "Snowball", "SnowB");
        add(EntityType.FIREBALL, "Fireball", "FireB", "Large Fireball");
        add(EntityType.SMALL_FIREBALL, "Small Fireball", "SFireball", "SFireB");
        add(EntityType.ENDER_PEARL, "Enderpearl", "Pearl");
        add(EntityType.ENDER_SIGNAL, "Endereye", "Eye", "Eye of Ender", "Signal", "EEye", "EyeE");
        add(EntityType.THROWN_EXP_BOTTLE, "Thrown Experience Bottle", "XP Bottle", "Exp Bottle", "Thrown XP Bottle", "Thrown Exp Bottle", "XPB", "ExpB", "Thrown Exp", "Thrown XP");
        add(EntityType.ITEM_FRAME, "Item Frame", "Frame");
        add(EntityType.WITHER_SKULL, "Wither Skull", "Skull", "WSkull", "Wither Head", "Head", "WHead");
        add(EntityType.PRIMED_TNT, "Primed TNT", "TNT", "Explosive", "Prime TNT");
        add(EntityType.FALLING_BLOCK, "Falling Block", "Falling Sand", "FallingB", "Falling", "FBlock", "FSand");
        add(EntityType.FIREWORK, "Firework Rocket", "Firework", "Rocket", "FRocket", "FireworkR");
        add(EntityType.TIPPED_ARROW, "Tipped Arrow", "TArrow", "Tip Arrow");
        add(EntityType.SPECTRAL_ARROW, "Spectral Arrow", "SArrow", "Spec Arrow");
        add(EntityType.SHULKER_BULLET, "Shulker Bullet", "ShulkerB", "SBullet");
        add(EntityType.DRAGON_FIREBALL, "Dragon Fireball", "DFireball", "Purple Fireball", "Acid Fireball");
        add(EntityType.ARMOR_STAND, "Armor Stand", "Stand", "AS", "ArmorS", "AStand");
        add(EntityType.MINECART_COMMAND, "Command Minecart", "Cmd Minecart", "CMinecart", "Command Cart", "Cmd Cart");
        add(EntityType.BOAT, "Boat");
        add(EntityType.MINECART, "Minecart", "Cart");
        add(EntityType.MINECART_CHEST, "Storage Minecart", "Storage Cart", "Chest Minecart", "Chest Cart", "CMinecart");
        add(EntityType.MINECART_FURNACE, "Powered Minecart", "Powered Cart", "Furnace Minecart", "Furnace Cart", "Power Minecart", "Power Cart", "FMinecart");
        add(EntityType.MINECART_TNT, "Explosive Minecart", "Explosive Cart", "TNT Minecart", "TNT Cart");
        add(EntityType.MINECART_HOPPER, "Hopper Minecart", "Hopper Cart");
        add(EntityType.MINECART_MOB_SPAWNER, "Spawner Minecart", "Spawner Cart");
        add(EntityType.CREEPER, "Creeper");
        add(EntityType.SKELETON, "Skeleton", "Skeli");
        add(EntityType.SPIDER, "Spider");
        add(EntityType.GIANT, "Giant");
        add(EntityType.ZOMBIE, "Zombie");
        add(EntityType.SLIME, "Slime");
        add(EntityType.GHAST, "Ghast");
        add(EntityType.PIG_ZOMBIE, "Pigman", "Pigzombie", "Zombie Pigman", "Zombie Pig");
        add(EntityType.ENDERMAN, "Enderman", "EMan");
        add(EntityType.CAVE_SPIDER, "Cave Spider", "CSpider", "Poison Spider", "Small Spider");
        add(EntityType.SILVERFISH, "Silverfish", "Bug");
        add(EntityType.BLAZE, "Blaze");
        add(EntityType.MAGMA_CUBE, "Magma Cube", "Magma Slime", "Lava Slime", "Magma");
        add(EntityType.ENDER_DRAGON, "Ender Dragon", "Dragon", "EDragon");
        add(EntityType.WITHER, "Wither");
        add(EntityType.BAT, "Bat");
        add(EntityType.WITCH, "Witch");
        add(EntityType.ENDERMITE, "Endermite");
        add(EntityType.GUARDIAN, "Guardian");
        add(EntityType.SHULKER, "Shulker");
        add(EntityType.PIG, "Pig");
        add(EntityType.SHEEP, "Sheep");
        add(EntityType.COW, "Cow");
        add(EntityType.CHICKEN, "Chicken");
        add(EntityType.SQUID, "Squid");
        add(EntityType.WOLF, "Wolf");
        add(EntityType.MUSHROOM_COW, "Mushroom Cow", "Mooshroom", "Shroom Cow", "MCow", "SCow");
        add(EntityType.SNOWMAN, "Snowman", "Snow Golem");
        add(EntityType.OCELOT, "Ocelot", "Cat");
        add(EntityType.IRON_GOLEM, "Iron Golem", "Golem", "Villager Golem");
        add(EntityType.HORSE, "Horse");
        add(EntityType.RABBIT, "Rabbit");
        add(EntityType.VILLAGER, "Villager");
        add(EntityType.ENDER_CRYSTAL, "Ender Crystal", "Crystal", "ECrystal", "EnderC", "End Crystal", "EndC");
        add(EntityType.SPLASH_POTION, "Splash Potion", "Thrown Potion", "Potion", "Splash", "Splash Pot", "Thrown Pot", "Pot");
        add(EntityType.AREA_EFFECT_CLOUD, "Area Effect Cloud", "Area Effect", "Area Cloud", "Potion Cloud", "PotionC", "AEC", "Effect Cloud", "EffectC");
        add(EntityType.EGG, "Egg");
        add(EntityType.FISHING_HOOK, "Fishing Hook", "Fishing Bobber", "Fish Hook", "Fish Bobber", "Fish");
        add(EntityType.LIGHTNING, "Lightning", "Lightning Strike", "Bolt");
        add(EntityType.WEATHER, "Weather");
        add(EntityType.PLAYER, "Player", "Human");
        add(EntityType.COMPLEX_PART, "Complex Part", "Dragon Part");
        add(EntityType.UNKNOWN, "Unknown");
    }

    public static EntityType get(String string) {
        return instance()._get(string);
    }

    public static String getName(EntityType key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(EntityType key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(EntityType key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static EntityTypes instance() {
        if (getMap(EntityTypes.class) == null) {
            aliasMaps.put(EntityTypes.class, new EntityTypes());
        }
        return (EntityTypes)getMap(EntityTypes.class);
    }
}
