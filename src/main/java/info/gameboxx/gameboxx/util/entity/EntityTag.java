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

package info.gameboxx.gameboxx.util.entity;

import info.gameboxx.gameboxx.options.Option;
import info.gameboxx.gameboxx.options.single.*;
import info.gameboxx.gameboxx.util.Parse;
import org.bukkit.Art;
import org.bukkit.DyeColor;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.CommandMinecart;

import java.util.*;

public enum EntityTag {
    //TODO: More modfiers for all options like min/max values etc.
    //TODO: More custom tags,
    //TODO: Check for missing tags.

    //All entities
    VELOCITY(new VectorOption("velocity"), "setVelocity", Entity.class),
    FIRETICKS(new IntOption("fireticks"), "setFireTicks", Entity.class),
    TICKSLIVED(new IntOption("tickslived"), "setTicksLived", Entity.class),
    NAME(new StringOption("name"), "setCustomName", Entity.class),
    NAMEVISIBLE(new BoolOption("namevisible", true), "setCustomNameVisible", Entity.class),

    //Damagable/Living
    HEALTH(new DoubleOption("health"), "setHealth", Damageable.class),
    MAXHEALTH(new DoubleOption("maxhealth"), "setMaxHealth", Damageable.class),
    REMOVEFAR(new BoolOption("removefar", true), "setRemoveWhenFarAway", LivingEntity.class),
    PICKUPITEMS(new BoolOption("pickupitems", true), "setCanPickupItems", LivingEntity.class),
    AIR(new IntOption("air"), "setAir", LivingEntity.class),

    //Ageable
    AGE(new IntOption("age"), "setAge", Ageable.class),
    AGELOCK(new BoolOption("agelock", true), "setAgeLock", Ageable.class),
    BABY(new BoolOption("baby", true), "setBaby", Ageable.class),
    BREED(new BoolOption("breed", true), "setBreed", Ageable.class),

    //Tamable
    TAMED(new BoolOption("tamed", true), "setTamed", Tameable.class),
    OWNER(new PlayerOption("owner"), "setOwner", Tameable.class),

    //Projectile
    BOUNCE(new BoolOption("bounce", true), "setBounce", Projectile.class),

    //Hanging
    DIR(new StringOption("dir").match(Arrays.asList("north","east","south","west","up","down")), "CUSTOM", Hanging.class),

    //ArmorStand
    POSE(new VectorOption("pose"), "setBodyPose", ArmorStand.class),
    HEAD(new VectorOption("head"), "setHeadPose", ArmorStand.class),
    LARM(new VectorOption("larm"), "setLeftArmPose", ArmorStand.class),
    RARM(new VectorOption("rarm"), "setRightArmPose", ArmorStand.class),
    LLEG(new VectorOption("lleg"), "setLeftLegPose", ArmorStand.class),
    RLEG(new VectorOption("rleg"), "setRightLegPose", ArmorStand.class),
    BASEPLATE(new BoolOption("baseplate", true), "setBasePlate", ArmorStand.class),
    GRAVITY(new BoolOption("gravity", true), "setGravity", ArmorStand.class),
    VISIBLE(new BoolOption("visible", true), "setVisible", ArmorStand.class),
    ARMS(new BoolOption("arms", true), "setArms", ArmorStand.class),
    SMALL(new BoolOption("small", true), "setSmall", ArmorStand.class),
    MARKER(new BoolOption("marker", true), "setMarker", ArmorStand.class),

    //Arrow
    CRITICAL(new BoolOption("critical", true), "setCritical", Arrow.class),
    KNOCKBACK(new IntOption("knockback"), "setKnockbackStrength", Arrow.class),

    //Boat
    HOVER(new BoolOption("hover", true), "setWorkOnLand", Boat.class),
    EMPTYDECEL(new DoubleOption("emptydecel"), "setUnoccupiedDeceleration", Boat.class),
    DECEL(new DoubleOption("decel"), "setOccupiedDeceleration", Boat.class),

    //Cmdblock minecart
    CMD(new StringOption("cmd"), "setCommand", CommandMinecart.class),
    SENDER(new StringOption("sender"), "setName", CommandMinecart.class),

    //Experience
    EXP(new IntOption("exp"), "setExperience", ExperienceOrb.class),

    //Falling block
    DROPITEM(new BoolOption("dropitem", true), "setDropItem", FallingBlock.class),
    HURTENTITIES(new BoolOption("hurtentities", true), "setHurtEntities", FallingBlock.class),

    //Firework
    DETONATE(new BoolOption("detonate", true), "detonate", Firework.class),

    //Item
    PICKUPDELAY(new IntOption("pickupdelay"), "setPickupDelay", Item.class),

    //Itemframe
    ROTATION(new StringOption("rotation").match(Arrays.asList("0","45","90","135","180","225","270","315","360")), "setRotation", ItemFrame.class),

    //Minecart
    SLOWEMPTY(new BoolOption("slowempty", true), "setSlowWhenEmpty", Minecart.class),
    FLYVELOCITY(new VectorOption("flyvelocity"), "setFlyingVelocityMod", Minecart.class),
    DERAILVELOCITY(new VectorOption("derailvelocity"), "setDerailedVelocityMod", Minecart.class),
    DISPLAYBLOCK(new MaterialOption("displayblock"), "setDisplayBlock", Minecart.class),
    BLOCKOFFSET(new IntOption("blockoffset"), "setDisplayBlockOffset", Minecart.class),

    //Painting
    //TODO: Aliases
    ART(new StringOption("art").match(Parse.Array(Art.values())), "setArt", Painting.class),

    //Primed TnT
    FUSETICKS(new IntOption("fuseticks"), "setFuseTicks", TNTPrimed.class),

    //Witherskull
    CHARGED(new BoolOption("charged", true), "setCharged", WitherSkull.class),

    //Bat
    AWAKE(new BoolOption("awake", true), "setAwake", Bat.class),

    //Creeper
    POWERED(new BoolOption("powered", true), "setPowered", Creeper.class),

    //Enderman
    //TODO: Block modifier
    HOLDING(new MaterialOption("holding"), "setCarriedMaterial", Enderman.class),

    //Guardian
    ELDER(new BoolOption("elder", true), "setElder", Guardian.class),

    //Horse
    //TODO: Aliases for VARIANT, COLOR & STYLE
    VARIANT(new StringOption("variant").match(Parse.Array(Horse.Variant.values())), "setVariant", Horse.class),
    COLOR(new StringOption("color").match(Parse.Array(Horse.Color.values())), "setColor", Horse.class),
    STYLE(new StringOption("style").match(Parse.Array(Horse.Style.values())), "setStyle", Horse.class),
    CHEST(new BoolOption("chest", true), "setCarryingChest", Horse.class),
    DOMESTICATION(new IntOption("domestication"), "setDomestication", Horse.class),
    MAXDOMESTICATION(new IntOption("maxdomestication"), "setMaxDomestication", Horse.class),
    JUMPSTRENGTH(new DoubleOption("jumpstrength"), "setJumpStrength", Horse.class),
    //ARMOR(new ItemOption("armor"), "setArmor", Horse.class),
    //SADDLEITEM(new ItemOption("saddleitem"), "setSaddle", Horse.class),

    //Ocelot
    //TODO: Aliases
    CATTYPE(new StringOption("cattype").match(Parse.Array(Ocelot.Type.values())), "setCatType", Ocelot.class),

    //Rabbit
    //TODO: Aliases
    RABITTYPE(new StringOption("rabbittype").match(Parse.Array(Rabbit.Type.values())), "setRabbitType", Rabbit.class),

    //Pigman
    ANGER(new IntOption("anger"), "setAnger", PigZombie.class),

    //Sheep
    SHEARED(new BoolOption("sheared", true), "setSheared", Sheep.class),

    //Skeleton
    WITHER(new BoolOption("wither", true), "makeWitherSkeleton", Wither.class),

    //Slime
    SIZE(new IntOption("size"), "setSize", Slime.class),

    //Villager
    //TODO: Aliases
    PROFESSION(new StringOption("profession").match(Parse.Array(Villager.Profession.values())), "setProfession", Villager.class),

    //Wolf
    //TODO: Aliases
    COLLAR(new StringOption("collar").match(Parse.Array(DyeColor.values())), "setCollarColor", Wolf.class),

    //Zombie
    VILLAGER(new BoolOption("villager", true), "setVillager", Zombie.class),

    //Mixed
    SADDLE(new BoolOption("saddle", true), "setSaddle", Horse.class, Pig.class),
    ANGRY(new BoolOption("angry", true), "setAngry", Wolf.class, PigZombie.class),
    SITTING(new BoolOption("sitting", true), "setSitting", Ocelot.class, Wolf.class),
    MAXSPEED(new BoolOption("maxspeed"), "setMaxSpeed", Minecart.class, Boat.class),
    //ITEM(new ItemOption("item"), "setItem", Item.class, ItemFrame.class, ThrownPotion.class),
    //HAND(new ItemOption("hand"), "setItemInHand", ArmorStand.class, LivingEntity.class),
    //HELMET(new ItemOption("helmet"), "setHelmet", ArmorStand.class, LivingEntity.class),
    //CHESTPLATE(new ItemOption("chestplate"), "setChestplate", ArmorStand.class, LivingEntity.class),
    //LEGGINGS(new ItemOption("leggings"), "setLeggings", ArmorStand.class, LivingEntity.class),
    //BOOTS(new ItemOption("boots"), "setBoots", ArmorStand.class, LivingEntity.class),

    //Tags
    NOAI(new BoolOption("noai", true), "setAI", Entity.class),
    INVULNERABLE(new BoolOption("setInvulnerable", true), "invulnerable", Entity.class),
    SILENT(new BoolOption("silent", true), "setSilent", Entity.class),
    INVISIBLE(new BoolOption("invisible", true), "setInvisible", Entity.class),
    ;

    private static Map<EntityType, List<EntityTag>> BY_ENTITY = new HashMap<>();
    private static Map<String, EntityTag> BY_NAME = new HashMap<>();

    static {
        for (EntityType entityType : EntityType.values()) {
            List<EntityTag> tags = new ArrayList<>();
            for (EntityTag tag : values()) {
                for (Class<? extends Entity> clazz : tag.getClasses()) {
                    if (clazz.isAssignableFrom(entityType.getEntityClass())) {
                        tags.add(tag);
                    }
                }
            }
            BY_ENTITY.put(entityType, tags);
        }

        for (EntityTag tag : values()) {
            BY_NAME.put(tag.toString().toLowerCase(), tag);
        }
    }

    private Option option;
    private String method;
    private Class<? extends Entity>[] classes;

    EntityTag(Option option, String method, Class... classes) {
        this.option = option;
        this.method = method;
        this.classes = classes;
    }

    public Class[] getClasses() {
        return classes;
    }

    public Option getOption() {
        return option;
    }

    public String getMethod() {
        return method;
    }

    public static EntityTag fromString(String name) {
        return BY_NAME.get(name.toLowerCase().replace("_",""));
    }

    public static List<EntityTag> getTags(EntityType type) {
        return BY_ENTITY.get(type);
    }
}
