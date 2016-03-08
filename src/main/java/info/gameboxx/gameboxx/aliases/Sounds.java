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
import org.bukkit.Sound;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Sounds extends AliasMap<Sound> {

    private Sounds() {
        super("Sounds", new File(ALIASES_FOLDER, "Sounds.yml"), "sound");
    }

    @Override
    public void onLoad() {
        add(Sound.AMBIENT_CAVE, "Cave");
        add(Sound.BLOCK_ANVIL_BREAK, "Anvil Break");
        add(Sound.BLOCK_ANVIL_DESTROY, "Anvil Destroy");
        add(Sound.BLOCK_ANVIL_FALL, "Anvil Fall");
        add(Sound.BLOCK_ANVIL_HIT, "Anvil Hit");
        add(Sound.BLOCK_ANVIL_LAND, "Anvil Land");
        add(Sound.BLOCK_ANVIL_PLACE, "Anvil Place");
        add(Sound.BLOCK_ANVIL_STEP, "Anvil Step");
        add(Sound.BLOCK_ANVIL_USE, "Anvil Use");
        add(Sound.BLOCK_BREWING_STAND_BREW, "Brew");
        add(Sound.BLOCK_CHEST_CLOSE, "Chest Close");
        add(Sound.BLOCK_CHEST_LOCKED, "Chest Locked");
        add(Sound.BLOCK_CHEST_OPEN, "Chest Open");
        add(Sound.BLOCK_CHORUS_FLOWER_DEATH, "Chorus Death");
        add(Sound.BLOCK_CHORUS_FLOWER_GROW, "Chorus Grow");
        add(Sound.BLOCK_CLOTH_BREAK, "Cloth Break", "Wool Break");
        add(Sound.BLOCK_CLOTH_FALL, "Cloth Fall", "Wool Fall");
        add(Sound.BLOCK_CLOTH_HIT, "Cloth Hit", "Wool Hit");
        add(Sound.BLOCK_CLOTH_PLACE, "Cloth Place", "Wool Place");
        add(Sound.BLOCK_CLOTH_STEP, "Cloth Step", "Wool Step");
        add(Sound.BLOCK_COMPARATOR_CLICK, "Comparator Click");
        add(Sound.BLOCK_DISPENSER_DISPENSE, "Dispenser Dispense", "Dispense");
        add(Sound.BLOCK_DISPENSER_FAIL, "Dispenser Fail");
        add(Sound.BLOCK_DISPENSER_LAUNCH, "Dispenser Launch");
        add(Sound.BLOCK_ENDERCHEST_CLOSE, "Enderchest Close");
        add(Sound.BLOCK_ENDERCHEST_OPEN, "Enderchest Open");
        add(Sound.BLOCK_END_GATEWAY_SPAWN, "Gateway spawn");
        add(Sound.BLOCK_FENCE_GATE_CLOSE, "Fence Close");
        add(Sound.BLOCK_FENCE_GATE_OPEN, "Fence Open");
        add(Sound.BLOCK_FIRE_AMBIENT, "Fire");
        add(Sound.BLOCK_FIRE_EXTINGUISH, "Fire Extinguish");
        add(Sound.BLOCK_FURNACE_FIRE_CRACKLE, "Fire Crackle", "Crackle");
        add(Sound.BLOCK_GLASS_BREAK, "Glass Break");
        add(Sound.BLOCK_GLASS_FALL, "Glass Fall");
        add(Sound.BLOCK_GLASS_HIT, "Glass Hit");
        add(Sound.BLOCK_GLASS_PLACE, "Glass Place");
        add(Sound.BLOCK_GLASS_STEP, "Glass Step");
        add(Sound.BLOCK_GRASS_BREAK, "Glass Break");
        add(Sound.BLOCK_GRASS_FALL, "Grass Fall");
        add(Sound.BLOCK_GRASS_HIT, "Grass Hit");
        add(Sound.BLOCK_GRASS_PLACE, "Grass Place");
        add(Sound.BLOCK_GRASS_STEP, "Grass Step");
        add(Sound.BLOCK_GRAVEL_BREAK, "Gravel Break");
        add(Sound.BLOCK_GRAVEL_FALL, "Gravel Fall");
        add(Sound.BLOCK_GRAVEL_HIT, "Gravel Hit");
        add(Sound.BLOCK_GRAVEL_PLACE, "Gravel Place");
        add(Sound.BLOCK_GRAVEL_STEP, "Gravel Step");
        add(Sound.BLOCK_IRON_DOOR_CLOSE, "Iron Door Close", "IDoor Close");
        add(Sound.BLOCK_IRON_DOOR_OPEN, "Iron Door Open", "IDoor Open");
        add(Sound.BLOCK_IRON_TRAPDOOR_CLOSE, "Iron Trapdoor Close", "ITrapdoor Close");
        add(Sound.BLOCK_IRON_TRAPDOOR_OPEN, "Iron Trapdoor Open", "ITrapdoor Open");
        add(Sound.BLOCK_LADDER_BREAK, "Ladder Break");
        add(Sound.BLOCK_LADDER_FALL, "Ladder Fall");
        add(Sound.BLOCK_LADDER_HIT, "Ladder Hit");
        add(Sound.BLOCK_LADDER_PLACE, "Ladder Place");
        add(Sound.BLOCK_LADDER_STEP, "Ladder Step");
        add(Sound.BLOCK_LAVA_AMBIENT, "Lava");
        add(Sound.BLOCK_LAVA_EXTINGUISH, "Lava Extinguish");
        add(Sound.BLOCK_LAVA_POP, "Lava Pop", "Pop");
        add(Sound.BLOCK_LEVER_CLICK, "Lever");
        add(Sound.BLOCK_METAL_BREAK, "Metal Break");
        add(Sound.BLOCK_METAL_FALL, "Metal Fall");
        add(Sound.BLOCK_METAL_HIT, "Metal Hit");
        add(Sound.BLOCK_METAL_PLACE, "Metal Place");
        add(Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_OFF, "Metal Pressureplate Off", "MPressureplate Off", "Metal Plate Off");
        add(Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, "Metal Pressureplate On", "MPressureplate On", "Metal Plate On");
        add(Sound.BLOCK_METAL_STEP, "Metal Step");
        add(Sound.BLOCK_NOTE_BASEDRUM, "Note Drum");
        add(Sound.BLOCK_NOTE_BASS, "Note Bass");
        add(Sound.BLOCK_NOTE_HARP, "Note Harp");
        add(Sound.BLOCK_NOTE_HAT, "Note Hat");
        add(Sound.BLOCK_NOTE_PLING, "Note Pling");
        add(Sound.BLOCK_NOTE_SNARE, "Note Snare");
        add(Sound.BLOCK_PISTON_CONTRACT, "Piston Contract");
        add(Sound.BLOCK_PISTON_EXTEND, "Piston Extend");
        add(Sound.BLOCK_PORTAL_AMBIENT, "Portal");
        add(Sound.BLOCK_PORTAL_TRAVEL, "Portal Travel");
        add(Sound.BLOCK_PORTAL_TRIGGER, "Portal Trigger");
        add(Sound.BLOCK_REDSTONE_TORCH_BURNOUT, "Redstone Burnout", "Burnout");
        add(Sound.BLOCK_SAND_BREAK, "Sand Break");
        add(Sound.BLOCK_SAND_FALL, "Sand Fall");
        add(Sound.BLOCK_SAND_HIT, "Sand Hit");
        add(Sound.BLOCK_SAND_PLACE, "Sand Place");
        add(Sound.BLOCK_SAND_STEP, "Sand Step");
        add(Sound.BLOCK_SLIME_BREAK, "Slime Break");
        add(Sound.BLOCK_SLIME_FALL, "Slime Fall");
        add(Sound.BLOCK_SLIME_HIT, "Slime Hit");
        add(Sound.BLOCK_SLIME_PLACE, "Slime Place");
        add(Sound.BLOCK_SLIME_STEP, "Slime Step");
        add(Sound.BLOCK_SNOW_BREAK, "Snow Break");
        add(Sound.BLOCK_SNOW_FALL, "Snow Fall");
        add(Sound.BLOCK_SNOW_HIT, "Snow Hit");
        add(Sound.BLOCK_SNOW_PLACE, "Snow Place");
        add(Sound.BLOCK_SNOW_STEP, "Snow Step");
        add(Sound.BLOCK_STONE_BREAK, "Stone Break");
        add(Sound.BLOCK_STONE_BUTTON_CLICK_OFF, "Stone Button Off", "SButton Off");
        add(Sound.BLOCK_STONE_BUTTON_CLICK_ON, "Stone Button On", "SButton On");
        add(Sound.BLOCK_STONE_FALL, "Stone Fall");
        add(Sound.BLOCK_STONE_HIT, "Stone Hit");
        add(Sound.BLOCK_STONE_PLACE, "Stone Place");
        add(Sound.BLOCK_STONE_PRESSUREPLATE_CLICK_OFF, "Stone Pressureplate Off", "SPressureplate Off", "Stone Plate Off");
        add(Sound.BLOCK_STONE_PRESSUREPLATE_CLICK_ON, "Stone Pressureplate On", "SPressureplate On", "Stone Plate On");
        add(Sound.BLOCK_STONE_STEP, "Stone Step");
        add(Sound.BLOCK_TRIPWIRE_ATTACH, "Tripwire Attach");
        add(Sound.BLOCK_TRIPWIRE_CLICK_OFF, "Tripwire Off");
        add(Sound.BLOCK_TRIPWIRE_CLICK_ON, "Tripwire On");
        add(Sound.BLOCK_TRIPWIRE_DETACH, "Tripwire Detach");
        add(Sound.BLOCK_WATERLILY_PLACE, "Waterlily Place");
        add(Sound.BLOCK_WATER_AMBIENT, "Water");
        add(Sound.BLOCK_WOODEN_DOOR_CLOSE, "Wooden Door Close");
        add(Sound.BLOCK_WOODEN_DOOR_OPEN, "Wooden Door Open");
        add(Sound.BLOCK_WOODEN_TRAPDOOR_CLOSE, "Wooden Trapdoor Close", "WTrapdoor Close");
        add(Sound.BLOCK_WOODEN_TRAPDOOR_OPEN, "Wooden Trapdoor Open", "WTrapdoor Open");
        add(Sound.BLOCK_WOOD_BREAK, "Wood Break");
        add(Sound.BLOCK_WOOD_BUTTON_CLICK_OFF, "Wood Button Off", "WButton Off");
        add(Sound.BLOCK_WOOD_BUTTON_CLICK_ON, "Wood Button On", "WButton On");
        add(Sound.BLOCK_WOOD_FALL, "Wood Fall");
        add(Sound.BLOCK_WOOD_HIT, "Wood Hit");
        add(Sound.BLOCK_WOOD_PLACE, "Wood Place");
        add(Sound.BLOCK_WOOD_PRESSUREPLATE_CLICK_OFF, "Wood Pressureplate Off", "WPressureplate Off", "Wood Plate Off");
        add(Sound.BLOCK_WOOD_PRESSUREPLATE_CLICK_ON, "Wood Pressureplate On", "WPressureplate On", "Wood Plate On");
        add(Sound.BLOCK_WOOD_STEP, "Wood Step");
        add(Sound.ENCHANT_THORNS_HIT, "Thorns");
        add(Sound.ENTITY_ARMORSTAND_BREAK, "Armorstand Break");
        add(Sound.ENTITY_ARMORSTAND_FALL, "Armorstand Fall");
        add(Sound.ENTITY_ARMORSTAND_HIT, "Armorstand Hit");
        add(Sound.ENTITY_ARMORSTAND_PLACE, "Armorstand Place");
        add(Sound.ENTITY_ARROW_HIT, "Arrow Hit");
        add(Sound.ENTITY_ARROW_HIT_PLAYER, "Arrow Hit Player");
        add(Sound.ENTITY_ARROW_SHOOT, "Arrow Shoot");
        add(Sound.ENTITY_BAT_AMBIENT, "Bat");
        add(Sound.ENTITY_BAT_DEATH, "Bat Death");
        add(Sound.ENTITY_BAT_HURT, "Bat Hurt");
        add(Sound.ENTITY_BAT_LOOP, "Bat Loop");
        add(Sound.ENTITY_BAT_TAKEOFF, "Bat Takeoff");
        add(Sound.ENTITY_BLAZE_AMBIENT, "Blaze");
        add(Sound.ENTITY_BLAZE_BURN, "Blaze Burn");
        add(Sound.ENTITY_BLAZE_DEATH, "Blaze Death");
        add(Sound.ENTITY_BLAZE_HURT, "Blaze Hurt");
        add(Sound.ENTITY_BLAZE_SHOOT, "Blaze Shoot");
        add(Sound.ENTITY_BOBBER_SPLASH, "Bobber Splash");
        add(Sound.ENTITY_BOBBER_THROW, "Bobber Throw");
        add(Sound.ENTITY_CAT_AMBIENT, "Cat");
        add(Sound.ENTITY_CAT_DEATH, "Cat Death");
        add(Sound.ENTITY_CAT_HISS, "Cat Hiss");
        add(Sound.ENTITY_CAT_HURT, "Cat Hurt");
        add(Sound.ENTITY_CAT_PURR, "Cat Purr");
        add(Sound.ENTITY_CAT_PURREOW, "Cat Purreow");
        add(Sound.ENTITY_CHICKEN_AMBIENT, "Chicken");
        add(Sound.ENTITY_CHICKEN_DEATH, "Chicken Death");
        add(Sound.ENTITY_CHICKEN_EGG, "Chicken Egg");
        add(Sound.ENTITY_CHICKEN_HURT, "Chicken Hurt");
        add(Sound.ENTITY_CHICKEN_STEP, "Chicken Step");
        add(Sound.ENTITY_COW_AMBIENT, "Cow");
        add(Sound.ENTITY_COW_DEATH, "Cow Death");
        add(Sound.ENTITY_COW_HURT, "Cow Hurt");
        add(Sound.ENTITY_COW_MILK, "Cow Milk", "Milk");
        add(Sound.ENTITY_COW_STEP, "Cow Step");
        add(Sound.ENTITY_CREEPER_DEATH, "Creeper Death");
        add(Sound.ENTITY_CREEPER_HURT, "Creeper Hurt");
        add(Sound.ENTITY_CREEPER_PRIMED, "Creeper Primed");
        add(Sound.ENTITY_DONKEY_AMBIENT, "Donkey");
        add(Sound.ENTITY_DONKEY_ANGRY, "Donkey Angry");
        add(Sound.ENTITY_DONKEY_CHEST, "Donkey Chest");
        add(Sound.ENTITY_DONKEY_DEATH, "Donkey Death");
        add(Sound.ENTITY_DONKEY_HURT, "Donkey Hurt");
        add(Sound.ENTITY_EGG_THROW, "Egg Throw", "Egg");
        add(Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, "Elder Guardian");
        add(Sound.ENTITY_ELDER_GUARDIAN_AMBIENT_LAND, "Elder Guardian Land");
        add(Sound.ENTITY_ELDER_GUARDIAN_CURSE, "Elder Guardian Curse");
        add(Sound.ENTITY_ELDER_GUARDIAN_DEATH, "Elder Guardian Death");
        add(Sound.ENTITY_ELDER_GUARDIAN_DEATH_LAND, "Elder Guardian Death Land");
        add(Sound.ENTITY_ELDER_GUARDIAN_HURT, "Elder Guardian Hurt");
        add(Sound.ENTITY_ELDER_GUARDIAN_HURT_LAND, "Elder Guardian Hurt Land");
        add(Sound.ENTITY_ENDERDRAGON_AMBIENT, "Enderdragon", "Dragon");
        add(Sound.ENTITY_ENDERDRAGON_DEATH, "Enderdragon Death", "Dragon Death");
        add(Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, "Enderdragon Fireball", "Dragon Fireball");
        add(Sound.ENTITY_ENDERDRAGON_FLAP, "Enderdragon Flap", "Dragon Flap");
        add(Sound.ENTITY_ENDERDRAGON_GROWL, "Enderdragon Growl", "Dragon Growl", "Growl");
        add(Sound.ENTITY_ENDERDRAGON_HURT, "Enderdragon Hurt", "Dragon Hurt");
        add(Sound.ENTITY_ENDERDRAGON_SHOOT, "Enderdragon Shoot", "Dragon Shoot");
        add(Sound.ENTITY_ENDEREYE_LAUNCH, "Endereye");
        add(Sound.ENTITY_ENDERMEN_AMBIENT, "Enderman");
        add(Sound.ENTITY_ENDERMEN_DEATH, "Enderman Death");
        add(Sound.ENTITY_ENDERMEN_HURT, "Enderman Hurt");
        add(Sound.ENTITY_ENDERMEN_SCREAM, "Enderman Scream");
        add(Sound.ENTITY_ENDERMEN_STARE, "Enderman Stare");
        add(Sound.ENTITY_ENDERMEN_TELEPORT, "Enderman Teleport");
        add(Sound.ENTITY_ENDERMITE_AMBIENT, "Endermite");
        add(Sound.ENTITY_ENDERMITE_DEATH, "Endermite Death");
        add(Sound.ENTITY_ENDERMITE_HURT, "Endermite Hurt");
        add(Sound.ENTITY_ENDERMITE_STEP, "Endermite Step");
        add(Sound.ENTITY_ENDERPEARL_THROW, "Enderpearl");
        add(Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, "Experience Bottle");
        add(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, "Experience Pickup", "XP Pickup", "Exp Pickup");
        add(Sound.ENTITY_EXPERIENCE_ORB_TOUCH, "Experience Touch", "XP Touch", "Exp Touch");
        add(Sound.ENTITY_FIREWORK_BLAST, "Firework Blast");
        add(Sound.ENTITY_FIREWORK_BLAST_FAR, "Firework Blast Far");
        add(Sound.ENTITY_FIREWORK_LARGE_BLAST, "Firework Large Blast");
        add(Sound.ENTITY_FIREWORK_LARGE_BLAST_FAR, "Firework Large Blast Far");
        add(Sound.ENTITY_FIREWORK_LAUNCH, "Firework Launch");
        add(Sound.ENTITY_FIREWORK_SHOOT, "Firework Shoot");
        add(Sound.ENTITY_FIREWORK_TWINKLE, "Firework Twinkle");
        add(Sound.ENTITY_FIREWORK_TWINKLE_FAR, "Firework Twinkle Far");
        add(Sound.ENTITY_GENERIC_BIG_FALL, "Big Fall");
        add(Sound.ENTITY_GENERIC_BURN, "Burn");
        add(Sound.ENTITY_GENERIC_DEATH, "Death");
        add(Sound.ENTITY_GENERIC_DRINK, "Drink");
        add(Sound.ENTITY_GENERIC_EAT, "Eat");
        add(Sound.ENTITY_GENERIC_EXPLODE, "Explode");
        add(Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, "Extinguish");
        add(Sound.ENTITY_GENERIC_HURT, "Hurt");
        add(Sound.ENTITY_GENERIC_SMALL_FALL, "Small Fall");
        add(Sound.ENTITY_GENERIC_SPLASH, "Splash");
        add(Sound.ENTITY_GENERIC_SWIM, "Swim");
        add(Sound.ENTITY_GHAST_AMBIENT, "Ghast");
        add(Sound.ENTITY_GHAST_DEATH, "Ghast Death");
        add(Sound.ENTITY_GHAST_HURT, "Ghast Hurt");
        add(Sound.ENTITY_GHAST_SCREAM, "Ghast Scream");
        add(Sound.ENTITY_GHAST_SHOOT, "Ghast Shoot");
        add(Sound.ENTITY_GHAST_WARN, "Ghast Warn");
        add(Sound.ENTITY_GUARDIAN_AMBIENT, "Guardian");
        add(Sound.ENTITY_GUARDIAN_AMBIENT_LAND, "Guardian Land");
        add(Sound.ENTITY_GUARDIAN_ATTACK, "Guardian Attack");
        add(Sound.ENTITY_GUARDIAN_DEATH, "Guardian Death");
        add(Sound.ENTITY_GUARDIAN_DEATH_LAND, "Guardian Death Land");
        add(Sound.ENTITY_GUARDIAN_FLOP, "Guardian Flop");
        add(Sound.ENTITY_GUARDIAN_HURT, "Guardian Hurt");
        add(Sound.ENTITY_GUARDIAN_HURT_LAND, "Guardian Hurt Land");
        add(Sound.ENTITY_HORSE_AMBIENT, "Horse");
        add(Sound.ENTITY_HORSE_ANGRY, "Horse Angry");
        add(Sound.ENTITY_HORSE_ARMOR, "Horse Armor");
        add(Sound.ENTITY_HORSE_BREATHE, "Horse Breathe");
        add(Sound.ENTITY_HORSE_DEATH, "Horse Death");
        add(Sound.ENTITY_HORSE_EAT, "Horse Eat");
        add(Sound.ENTITY_HORSE_GALLOP, "Horse Gallop");
        add(Sound.ENTITY_HORSE_HURT, "Horse Hurt");
        add(Sound.ENTITY_HORSE_JUMP, "Horse Jump");
        add(Sound.ENTITY_HORSE_LAND, "Horse Land");
        add(Sound.ENTITY_HORSE_SADDLE, "Horse Saddle");
        add(Sound.ENTITY_HORSE_STEP, "Horse Step");
        add(Sound.ENTITY_HORSE_STEP_WOOD, "Horse Step Wood");
        add(Sound.ENTITY_HOSTILE_BIG_FALL, "Hostile Big Fall");
        add(Sound.ENTITY_HOSTILE_DEATH, "Hostile Death");
        add(Sound.ENTITY_HOSTILE_HURT, "Hostile Hurt");
        add(Sound.ENTITY_HOSTILE_SMALL_FALL, "Hostile Small Fall");
        add(Sound.ENTITY_HOSTILE_SPLASH, "Hostile Splash");
        add(Sound.ENTITY_HOSTILE_SWIM, "Hostile Swim");
        add(Sound.ENTITY_IRONGOLEM_ATTACK, "Irongolem Attack", "Golem Attack");
        add(Sound.ENTITY_IRONGOLEM_DEATH, "Irongolem Death", "Golem Death");
        add(Sound.ENTITY_IRONGOLEM_HURT, "Irongolem Hurt", "Golem Hurt");
        add(Sound.ENTITY_IRONGOLEM_STEP, "Irongolem Step", "Golem Step");
        add(Sound.ENTITY_ITEMFRAME_ADD_ITEM, "Itemframe Add", "Itemframe");
        add(Sound.ENTITY_ITEMFRAME_BREAK, "Itembreak Break");
        add(Sound.ENTITY_ITEMFRAME_PLACE, "Itemframe Place");
        add(Sound.ENTITY_ITEMFRAME_REMOVE_ITEM, "Itemframe Remove");
        add(Sound.ENTITY_ITEMFRAME_ROTATE_ITEM, "Itemframe Rotate");
        add(Sound.ENTITY_ITEM_BREAK, "Item Break");
        add(Sound.ENTITY_ITEM_PICKUP, "item Pickup");
        add(Sound.ENTITY_LEASHKNOT_BREAK, "Leash Break");
        add(Sound.ENTITY_LEASHKNOT_PLACE, "Leash Place");
        add(Sound.ENTITY_LIGHTNING_IMPACT, "Lightning");
        add(Sound.ENTITY_LIGHTNING_THUNDER, "Thunder");
        add(Sound.ENTITY_LINGERINGPOTION_THROW, "Lingering Throw");
        add(Sound.ENTITY_MAGMACUBE_DEATH, "Magmacube Death");
        add(Sound.ENTITY_MAGMACUBE_HURT, "Magmacube Hurt");
        add(Sound.ENTITY_MAGMACUBE_JUMP, "Magmacube Jump");
        add(Sound.ENTITY_MAGMACUBE_SQUISH, "Magmacube Squish");
        add(Sound.ENTITY_MINECART_INSIDE, "Minecart");
        add(Sound.ENTITY_MINECART_RIDING, "Minecart Riding");
        add(Sound.ENTITY_MOOSHROOM_SHEAR, "Mooshroom Shear");
        add(Sound.ENTITY_MULE_AMBIENT, "Mule");
        add(Sound.ENTITY_MULE_DEATH, "Mule Death");
        add(Sound.ENTITY_MULE_HURT, "Mule Hurt");
        add(Sound.ENTITY_PAINTING_BREAK, "Painting Break");
        add(Sound.ENTITY_PAINTING_PLACE, "Painting Place");
        add(Sound.ENTITY_PIG_AMBIENT, "Pig");
        add(Sound.ENTITY_PIG_DEATH, "Pig Death");
        add(Sound.ENTITY_PIG_HURT, "Pig Hurt");
        add(Sound.ENTITY_PIG_SADDLE, "Pig Saddle");
        add(Sound.ENTITY_PIG_STEP, "Pig Step");
        add(Sound.ENTITY_PLAYER_ATTACK_CRIT, "Attack Crit");
        add(Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, "Attack Knockback");
        add(Sound.ENTITY_PLAYER_ATTACK_NODAMAGE, "Attack Nodamage");
        add(Sound.ENTITY_PLAYER_ATTACK_STRONG, "Attack Strong");
        add(Sound.ENTITY_PLAYER_ATTACK_SWEEP, "Attack Sweep");
        add(Sound.ENTITY_PLAYER_ATTACK_WEAK, "Attack Weak");
        add(Sound.ENTITY_PLAYER_BIG_FALL, "Player Big Fall");
        add(Sound.ENTITY_PLAYER_BREATH, "Breath");
        add(Sound.ENTITY_PLAYER_BURP, "Burp");
        add(Sound.ENTITY_PLAYER_DEATH, "Player Death");
        add(Sound.ENTITY_PLAYER_HURT, "Player Hurt");
        add(Sound.ENTITY_PLAYER_LEVELUP, "LevelUp");
        add(Sound.ENTITY_PLAYER_SMALL_FALL, "Player Small Fall");
        add(Sound.ENTITY_PLAYER_SPLASH, "Player Splash");
        add(Sound.ENTITY_PLAYER_SWIM, "Player Swim");
        add(Sound.ENTITY_RABBIT_AMBIENT, "Rabbit");
        add(Sound.ENTITY_RABBIT_ATTACK, "Rabbit Attack");
        add(Sound.ENTITY_RABBIT_DEATH, "Rabbit Death");
        add(Sound.ENTITY_RABBIT_HURT, "Rabbit Hurt");
        add(Sound.ENTITY_RABBIT_JUMP, "Rabbit Jump");
        add(Sound.ENTITY_SHEEP_AMBIENT, "Sheep");
        add(Sound.ENTITY_SHEEP_DEATH, "Sheep Death");
        add(Sound.ENTITY_SHEEP_HURT, "Sheep hurt");
        add(Sound.ENTITY_SHEEP_SHEAR, "Sheep Shear");
        add(Sound.ENTITY_SHEEP_STEP, "Sheep Step");
        add(Sound.ENTITY_SHULKER_AMBIENT, "Shulker");
        add(Sound.ENTITY_SHULKER_BULLET_HIT, "Shulker Bullet Hit");
        add(Sound.ENTITY_SHULKER_BULLET_HURT, "Shulker Bullet Hurt");
        add(Sound.ENTITY_SHULKER_CLOSE, "Shulker Close");
        add(Sound.ENTITY_SHULKER_DEATH, "Shulker Death");
        add(Sound.ENTITY_SHULKER_HURT, "Shulker Hurt");
        add(Sound.ENTITY_SHULKER_HURT_CLOSED, "Shulker Hurt Closed");
        add(Sound.ENTITY_SHULKER_OPEN, "Shulker Open");
        add(Sound.ENTITY_SHULKER_SHOOT, "Shulker Shoot");
        add(Sound.ENTITY_SHULKER_TELEPORT, "Shulker Teleport");
        add(Sound.ENTITY_SILVERFISH_AMBIENT, "Silverfish");
        add(Sound.ENTITY_SILVERFISH_DEATH, "Silverfish Death");
        add(Sound.ENTITY_SILVERFISH_HURT, "Silverfish Hurt");
        add(Sound.ENTITY_SILVERFISH_STEP, "Silverfish Step");
        add(Sound.ENTITY_SKELETON_AMBIENT, "Skeleton");
        add(Sound.ENTITY_SKELETON_DEATH, "Skeleton Death");
        add(Sound.ENTITY_SKELETON_HORSE_AMBIENT, "Skeleton Horse");
        add(Sound.ENTITY_SKELETON_HORSE_DEATH, "Skeleton Horse Death");
        add(Sound.ENTITY_SKELETON_HORSE_HURT, "Skeleton Horse Hurt");
        add(Sound.ENTITY_SKELETON_HURT, "Skeleton Hurt");
        add(Sound.ENTITY_SKELETON_SHOOT, "Skeleton Shoot");
        add(Sound.ENTITY_SKELETON_STEP, "Skeleton Step");
        add(Sound.ENTITY_SLIME_ATTACK, "Slime Attack");
        add(Sound.ENTITY_SLIME_DEATH, "Slime Death");
        add(Sound.ENTITY_SLIME_HURT, "Slime Hurt");
        add(Sound.ENTITY_SLIME_JUMP, "Slime Jump");
        add(Sound.ENTITY_SLIME_SQUISH, "Slime Squish");
        add(Sound.ENTITY_SMALL_MAGMACUBE_DEATH, "Small Magmacube Death", "SMagmacube Death");
        add(Sound.ENTITY_SMALL_MAGMACUBE_HURT, "Small Magmacube Hurt", "SMagmacube Hurt");
        add(Sound.ENTITY_SMALL_MAGMACUBE_SQUISH, "Small Magmacube Squish", "SMagmacube Squish");
        add(Sound.ENTITY_SMALL_SLIME_DEATH, "Small Slime Death", "SSlime Death");
        add(Sound.ENTITY_SMALL_SLIME_HURT, "Small Slime Hurt", "SSlime Hurt");
        add(Sound.ENTITY_SMALL_SLIME_JUMP, "Small Slime Jump", "SSlime Jump");
        add(Sound.ENTITY_SMALL_SLIME_SQUISH, "Small Slime Squish", "SS Squish");
        add(Sound.ENTITY_SNOWBALL_THROW, "Snowball Throw", "Snowball");
        add(Sound.ENTITY_SNOWMAN_AMBIENT, "Snowman", "Snowgolem");
        add(Sound.ENTITY_SNOWMAN_DEATH, "Snowman Death", "Snowgolem Death");
        add(Sound.ENTITY_SNOWMAN_HURT, "Snowman Hurt", "Snowgolem Hurt");
        add(Sound.ENTITY_SNOWMAN_SHOOT, "Snowman Shoot", "Snowgolem Shoot");
        add(Sound.ENTITY_SPIDER_AMBIENT, "Spider");
        add(Sound.ENTITY_SPIDER_DEATH, "Spider Death");
        add(Sound.ENTITY_SPIDER_HURT, "Spider Hurt");
        add(Sound.ENTITY_SPIDER_STEP, "Spider Step");
        add(Sound.ENTITY_SPLASH_POTION_BREAK, "Potion Break");
        add(Sound.ENTITY_SPLASH_POTION_THROW, "Potion Throw");
        add(Sound.ENTITY_SQUID_AMBIENT, "Squid");
        add(Sound.ENTITY_SQUID_DEATH, "Squid Death");
        add(Sound.ENTITY_SQUID_HURT, "Squid hurt");
        add(Sound.ENTITY_TNT_PRIMED, "TnT");
        add(Sound.ENTITY_VILLAGER_AMBIENT, "Villager");
        add(Sound.ENTITY_VILLAGER_DEATH, "Villager Death");
        add(Sound.ENTITY_VILLAGER_HURT, "Villager Hurt");
        add(Sound.ENTITY_VILLAGER_NO, "Villager No");
        add(Sound.ENTITY_VILLAGER_TRADING, "Villager Trade");
        add(Sound.ENTITY_VILLAGER_YES, "Villager Yes");
        add(Sound.ENTITY_WITCH_AMBIENT, "Witch");
        add(Sound.ENTITY_WITCH_DEATH, "Witch Death");
        add(Sound.ENTITY_WITCH_DRINK, "Witch Drink");
        add(Sound.ENTITY_WITCH_HURT, "Witch Hurt");
        add(Sound.ENTITY_WITCH_THROW, "Witch Throw");
        add(Sound.ENTITY_WITHER_AMBIENT, "Wither");
        add(Sound.ENTITY_WITHER_BREAK_BLOCK, "Wither Break Block");
        add(Sound.ENTITY_WITHER_DEATH, "Wither Death");
        add(Sound.ENTITY_WITHER_HURT, "Wither Hurt");
        add(Sound.ENTITY_WITHER_SHOOT, "Wither Shoot");
        add(Sound.ENTITY_WITHER_SPAWN, "Wither Spawn");
        add(Sound.ENTITY_WOLF_AMBIENT, "Wolf");
        add(Sound.ENTITY_WOLF_DEATH, "Wolf Death");
        add(Sound.ENTITY_WOLF_GROWL, "Wolf Growl");
        add(Sound.ENTITY_WOLF_HOWL, "Wolf Howl");
        add(Sound.ENTITY_WOLF_HURT, "Wolf Hurt");
        add(Sound.ENTITY_WOLF_PANT, "Wolf Pant");
        add(Sound.ENTITY_WOLF_SHAKE, "Wolf Shake");
        add(Sound.ENTITY_WOLF_STEP, "Wolf Step");
        add(Sound.ENTITY_WOLF_WHINE, "Wolf Whine");
        add(Sound.ENTITY_ZOMBIE_AMBIENT, "Zombie");
        add(Sound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, "Zombie Attack Wood");
        add(Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, "Zombie Attack Iron");
        add(Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, "Zombie Break Wood");
        add(Sound.ENTITY_ZOMBIE_DEATH, "Zombie Death");
        add(Sound.ENTITY_ZOMBIE_HORSE_AMBIENT, "Zombie Horse");
        add(Sound.ENTITY_ZOMBIE_HORSE_DEATH, "Zombie Horse Death");
        add(Sound.ENTITY_ZOMBIE_HORSE_HURT, "Zombie Horse Hurt");
        add(Sound.ENTITY_ZOMBIE_HURT, "Zombie Hurt");
        add(Sound.ENTITY_ZOMBIE_INFECT, "Zombie Infect");
        add(Sound.ENTITY_ZOMBIE_PIG_AMBIENT, "Zombie Pig Ambient");
        add(Sound.ENTITY_ZOMBIE_PIG_ANGRY, "Zombie Pig Angry");
        add(Sound.ENTITY_ZOMBIE_PIG_DEATH, "Zombie Pig Death");
        add(Sound.ENTITY_ZOMBIE_PIG_HURT, "Zombie Pig Hurt");
        add(Sound.ENTITY_ZOMBIE_STEP, "Zombie Step");
        add(Sound.ENTITY_ZOMBIE_VILLAGER_AMBIENT, "Zombie Villager");
        add(Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, "Zombie Villager Converted", "Zombie Converted");
        add(Sound.ENTITY_ZOMBIE_VILLAGER_CURE, "Zombie Villager Cure", "Zombie Cure");
        add(Sound.ENTITY_ZOMBIE_VILLAGER_DEATH, "Zombie Villager Death");
        add(Sound.ENTITY_ZOMBIE_VILLAGER_HURT, "Zombie Villager Hurt");
        add(Sound.ENTITY_ZOMBIE_VILLAGER_STEP, "Zombie Villager Step");
        add(Sound.ITEM_ARMOR_EQUIP_CHAIN, "Armor Equip Chain");
        add(Sound.ITEM_ARMOR_EQUIP_DIAMOND, "Armor Equip Diamond", "Armor Diamond");
        add(Sound.ITEM_ARMOR_EQUIP_GENERIC, "Armor Equip Generic", "Armor Generic");
        add(Sound.ITEM_ARMOR_EQUIP_GOLD, "Armor Equip Gold", "Armor Gold");
        add(Sound.ITEM_ARMOR_EQUIP_IRON, "Armor Equip Iron", "Armor Iron");
        add(Sound.ITEM_ARMOR_EQUIP_LEATHER, "Armor Equip Leather", "Armor Leather");
        add(Sound.ITEM_BOTTLE_FILL, "Bottle Fill", "Bottle");
        add(Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, "Bottle Fill Dragonbreath", "Bottle Dragonbreath");
        add(Sound.ITEM_BUCKET_EMPTY, "Bucket Empty");
        add(Sound.ITEM_BUCKET_EMPTY_LAVA, "Bucket Empty Lava");
        add(Sound.ITEM_BUCKET_FILL, "Bucket Fill");
        add(Sound.ITEM_BUCKET_FILL_LAVA, "Bucket Fill Lava");
        add(Sound.ITEM_CHORUS_FRUIT_TELEPORT, "Chorus Teleport");
        add(Sound.ITEM_FIRECHARGE_USE, "Firecharge");
        add(Sound.ITEM_FLINTANDSTEEL_USE, "Flint and Steel");
        add(Sound.ITEM_HOE_TILL, "Hoe Till", "Till");
        add(Sound.ITEM_SHIELD_BLOCK, "Shield Block");
        add(Sound.ITEM_SHIELD_BREAK, "Shield Break");
        add(Sound.ITEM_SHOVEL_FLATTEN, "Shovel Flatten", "Flatten");
        add(Sound.MUSIC_CREATIVE, "Music Creative");
        add(Sound.MUSIC_CREDITS, "Music Credits");
        add(Sound.MUSIC_DRAGON, "Music Dragon");
        add(Sound.MUSIC_END, "Music End");
        add(Sound.MUSIC_GAME, "Music Game");
        add(Sound.MUSIC_MENU, "Music Menu");
        add(Sound.MUSIC_NETHER, "Music Nether");
        add(Sound.RECORD_11, "Record 11");
        add(Sound.RECORD_13, "Record 13");
        add(Sound.RECORD_BLOCKS, "Record Blocks");
        add(Sound.RECORD_CAT, "Record Cat");
        add(Sound.RECORD_CHIRP, "Record Chirp");
        add(Sound.RECORD_FAR, "Record Far");
        add(Sound.RECORD_MALL, "Record Mall");
        add(Sound.RECORD_MELLOHI, "Record Mellohi");
        add(Sound.RECORD_STAL, "Record Stal");
        add(Sound.RECORD_STRAD, "Record Strad");
        add(Sound.RECORD_WAIT, "Record Wait");
        add(Sound.RECORD_WARD, "Record Ward");
        add(Sound.UI_BUTTON_CLICK, "UI Click", "Button Click", "Click");
        add(Sound.WEATHER_RAIN, "Rain");
        add(Sound.WEATHER_RAIN_ABOVE, "Rain Above");
    }

    public static Sound get(String string) {
        return instance()._get(string);
    }

    public static String getName(Sound key) {
        return instance()._getName(key);
    }

    public static List<String> getAliases(Sound key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static Sounds instance() {
        if (getMap(Sounds.class) == null) {
            aliasMaps.put(Sounds.class, new Sounds());
        }
        return (Sounds)getMap(Sounds.class);
    }
}
