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
import org.bukkit.EntityEffect;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.material.Colorable;

import java.util.*;

public enum EntityTag {
    //TODO: More modfiers for all options like min/max values etc.
    //TODO: More custom tags,
    //TODO: Attributes

    //All entities
    VELOCITY(new VectorOption("VELOCITY"), "setVelocity", Entity.class),
    FALLDISTANCE(new IntOption("FALLDISTANCE"), "setFallDistance", Entity.class),
    FIRETICKS(new IntOption("FIRETICKS"), "setFireTicks", Entity.class),
    LIVED(new IntOption("LIVED"), "setTicksLived", Entity.class),
    NAME(new StringOption("NAME"), "setCustomName", Entity.class),
    NAMEVISIBLE(new BoolOption("NAMEVISIBLE", true), "setCustomNameVisible", Entity.class),
    META(new StringOption("META").matchRegex("(.*?).(.*?)", "String must have the syntax name.value"), "CUSTOM", Entity.class), //TODO: Test regex and add translatable message.
    PLAYEFFECT(new StringOption("PLAYEFFECT").match(Parse.Array(EntityEffect.values())), "playEffect", Entity.class), //TODO: Aliases
    RIDE(new PlayerOption("RIDE"), "CUSTOM", Entity.class),

    //Damagable
    HEALTH(new DoubleOption("health"), "setHealth", Damageable.class),
    MAXHEALTH(new DoubleOption("maxhealth"), "setMaxHealth", Damageable.class),

    //Living
    AIR(new IntOption("AIR"), "setRemainingAir", LivingEntity.class),
    MAXAIR(new IntOption("MAXAIR"), "setMaximumAir", LivingEntity.class),
    MAXNODMGTICKS(new IntOption("MAXNODMGTICKS"), "setMaximumNoDamageTicks", LivingEntity.class),
    NODMGTICKS(new IntOption("NODMGTICKS"), "setNoDamageTicks", LivingEntity.class),
    //MAINHAND(new ItemOption("MAINHAND"), "setItemInMainHand", LivingEntity.class),
    //OFFHAND(new ItemOption("OFFHAND"), "setItemInOffHand", LivingEntity.class),
    HANDDROP(new DoubleOption("HANDDROP"), "setItemInHandDropChance", LivingEntity.class),
    HELMETDROP(new DoubleOption("HELMETDROP"), "setHelmetDropChance", LivingEntity.class),
    CHESTPLATEDROP(new DoubleOption("CHESTPLATEDROP"), "setChestplateDropChance", LivingEntity.class),
    LEGGINGSDROP(new DoubleOption("LEGGINGSDROP"), "setLeggingsDropChance", LivingEntity.class),
    BOOTSDROP(new DoubleOption("BOOTSDROP"), "setBootsDropChance", LivingEntity.class),
    PICKUP(new BoolOption("PICKUP", true), "setCanPickupItems", LivingEntity.class),
    REMOVEFAR(new BoolOption("REMOVEFAR", true), "setRemoveWhenFarAway", LivingEntity.class),
    //LEASH(new EntityOption("LEASH"), "setLeashHolder", LivingEntity.class),

    //Ageable
    AGE(new IntOption("AGE"), "setAge", Ageable.class),
    AGELOCK(new BoolOption("AGELOCK", true), "setAgeLock", Ageable.class),
    BABY(new BoolOption("BABY", true), "setBaby", Ageable.class),
    BREED(new BoolOption("BREED", true), "setBreed", Ageable.class),

    //Tamable
    TAMED(new BoolOption("TAMED", true), "setTamed", Tameable.class),
    OWNER(new PlayerOption("OWNER"), "setOwner", Tameable.class), //TODO: OfflinePlayerOption?

    //Creature
    //TARGET(new EntityOption("TARGET"), "setTarget", Creature.class),

    //Projectile
    //SHOOTER(new EntityOption("SHOOTER"), "setShooter", Projectile.class),
    BOUNCE(new BoolOption("BOUNCE", true), "setBounce", Projectile.class),

    //Hanging
    DIR(new StringOption("DIR").match(Arrays.asList("north","east","south","west","up","down")), "CUSTOM", Hanging.class),

    //Mixed Entities
    //BOOTS(new ItemOption("BOOTS"), "setBoots", ArmorStand.class, LivingEntity.class),
    //LEGGINGS(new ItemOption("LEGGINGS"), "setLeggings", ArmorStand.class, LivingEntity.class),
    //CHESTPLATE(new ItemOption("CHESTPLATE"), "setChestplate", ArmorStand.class, LivingEntity.class),
    //HELMET(new ItemOption("HELMET"), "setHelmet", ArmorStand.class, LivingEntity.class),
    //ITEM(new ItemOption("ITEM"), "setItem", Item.class, ItemFrame.class, ThrownPotion.class),
    SITTING(new BoolOption("SITTING", true), "setSitting", Ocelot.class, Wolf.class),
    ANGRY(new BoolOption("ANGRY", true), "setAngry", Wolf.class, PigZombie.class),
    SADDLE(new BoolOption("SADDLE", true), "setSaddle", Horse.class, Pig.class),
    //SADDLEITEM(new ItemOption("SADDLEITEM", true), "setSaddle", Horse.class, Pig.class),
    COLOR(new StringOption("COLOR").match(Parse.Array(DyeColor.values())), "setColor", Colorable.class, Wolf.class), //TODO: Aliases
    //EFFECT(new PotionOption("EFFECT"), "addEffect", LivingEntity.class, AreaEffectCloud.class),
    PROFESSION(new StringOption("PROFESSION").match(Parse.Array(Villager.Profession.values())), "setProfession", Villager.class, Zombie.class), //TODO: Aliases

    //ArmorStand
    //HAND(new ItemOption("HAND"), "setItemInHand", ArmorStand.class),
    POSE(new VectorOption("POSE"), "setBodyPose", ArmorStand.class),
    HEAD(new VectorOption("HEAD"), "setHeadPose", ArmorStand.class),
    LARM(new VectorOption("LARM"), "setLeftArmPose", ArmorStand.class),
    RARM(new VectorOption("RARM"), "setRightArmPose", ArmorStand.class),
    LLEG(new VectorOption("LLEG"), "setLeftLegPose", ArmorStand.class),
    RLEG(new VectorOption("RLEG"), "setRightLegPose", ArmorStand.class),
    BASEPLATE(new BoolOption("BASEPLATE", true), "setBasePlate", ArmorStand.class),
    GRAVITY(new BoolOption("GRAVITY", true), "setGravity", ArmorStand.class),
    VISIBLE(new BoolOption("VISIBLE", true), "setVisible", ArmorStand.class),
    ARMS(new BoolOption("ARMS", true), "setArms", ArmorStand.class),
    SMALL(new BoolOption("SMALL", true), "setSmall", ArmorStand.class),
    MARKER(new BoolOption("MARKER", true), "setMarker", ArmorStand.class),

    //Arrow
    KNOCKBACK(new IntOption("KNOCKBACK"), "setKnockbackStrength", Arrow.class),
    CRITICAL(new BoolOption("CRITICAL", true), "setCritical", Arrow.class),

    //AreaEffectCloud
    DURATION(new IntOption("DURATION"), "setDuration", AreaEffectCloud.class),
    WAITTIME(new IntOption("WAITTIME"), "setWaitTime", AreaEffectCloud.class),
    DELAYTICKS(new IntOption("DELAYTICKS"), "setReapplicationDelay", AreaEffectCloud.class),
    USETICKS(new IntOption("USETICKS"), "setDurationOnUse", AreaEffectCloud.class),
    RADIUS(new DoubleOption("RADIUS"), "setRadius", AreaEffectCloud.class),
    USERADIUS(new DoubleOption("USERADIUS"), "setRadiusOnUse", AreaEffectCloud.class),
    RADIUSDECAY(new DoubleOption("RADIUSDECAY"), "setRadiusPerTick", AreaEffectCloud.class),
    PARTICLE(new IntOption("PARTICLE"), "setParticle", AreaEffectCloud.class),
    //EFFECTCOLOR(new ColorOption("COLOR"), "setEffectColor", AreaEffectCloud.class),

    //Minecart
    MAXSPEED(new DoubleOption("MAXSPEED"), "setMaxSpeed", Minecart.class),
    SLOWEMPTY(new BoolOption("SLOWEMPTY", true), "setSlowWhenEmpty", Minecart.class),
    FLYVELOCITY(new VectorOption("FLYVELOCITY"), "setFlyingVelocityMod", Minecart.class),
    DERAILVELOCITY(new VectorOption("DERAILVELOCITY"), "setDerailedVelocityMod", Minecart.class),
    DISPLAYBLOCK(new MaterialOption("DISPLAYBLOCK"), "setDisplayBlock", Minecart.class), //TODO: Block modifier
    BLOCKOFFSET(new IntOption("BLOCKOFFSET"), "setDisplayBlockOffset", Minecart.class),

    //Cmdblock minecart
    CMD(new StringOption("CMD"), "setCommand", CommandMinecart.class),
    SENDER(new StringOption("SENDER"), "setName", CommandMinecart.class),

    //Experience
    EXP(new IntOption("EXP"), "setExperience", ExperienceOrb.class),

    //Falling block
    DROPITEM(new BoolOption("DROPITEM", true), "setDropItem", FallingBlock.class),
    HURTENTITIES(new BoolOption("HURTENTITIES", true), "setHurtEntities", FallingBlock.class),

    //Firework
    DETONATE(new BoolOption("DETONATE", true), "detonate", Firework.class),
    //FIREWORK(new FireworkOption("FIREWORK"), "setFireworkMeta", Firework.class),

    //Item
    //PICKUPDELAY(new IntOption("PICKUPDELAY"), "setPickupDelay", Item.class),

    //Itemframe
    ROTATION(new StringOption("ROTATION").match(Arrays.asList("0","45","90","135","180","225","270","315","360")), "setRotation", ItemFrame.class),

    //Painting
    ART(new StringOption("ART").match(Parse.Array(Art.values())), "setArt", Painting.class), //TODO: Aliases

    //FishHook
    BITECHANCE(new DoubleOption("BITECHANCE"), "setBiteChance", FishHook.class),

    //Primed TnT
    FUSETICKS(new IntOption("FUSETICKS"), "setFuseTicks", TNTPrimed.class),

    //Explosive
    YIELD(new DoubleOption("YIELD"), "setYield", Explosive.class),
    FIRE(new BoolOption("FIRE", true), "setIsIncendiary", Explosive.class),

    //Witherskull
    CHARGED(new BoolOption("CHARGED", true), "setCharged", WitherSkull.class),

    //Bat
    AWAKE(new BoolOption("AWAKE", true), "setAwake", Bat.class),

    //Creeper
    POWERED(new BoolOption("POWERED", true), "setPowered", Creeper.class),

    //Enderman
    HOLDING(new MaterialOption("holding"), "setCarriedMaterial", Enderman.class), //TODO: Block modifier

    //Guardian
    ELDER(new BoolOption("ELDER", true), "setElder", Guardian.class),

    //Horse
    HORSEVARIANT(new StringOption("HORSEVARIANT").match(Parse.Array(Horse.Variant.values())), "setHorseVariant", Horse.class), //TODO: Aliases
    HORSECOLOR(new StringOption("HORSECOLOR").match(Parse.Array(Horse.Color.values())), "setHorseColor", Horse.class), //TODO: Aliases
    HORSESTYLE(new StringOption("HORSESTYLE").match(Parse.Array(Horse.Style.values())), "setHorseStyle", Horse.class), //TODO: Aliases
    CHEST(new BoolOption("CHEST", true), "setCarryingChest", Horse.class),
    DOMESTICATION(new IntOption("DOMESTICATION"), "setDomestication", Horse.class),
    MAXDOMESTICATION(new IntOption("MAXDOMESTICATION"), "setMaxDomestication", Horse.class),
    JUMPSTRENGTH(new DoubleOption("JUMPSTRENGTH"), "setJumpStrength", Horse.class),
    //ARMOR(new ItemOption("ARMOR"), "setHorseArmor", Horse.class),

    //Ocelot
    CATTYPE(new StringOption("CATTYPE").match(Parse.Array(Ocelot.Type.values())), "setCatType", Ocelot.class), //TODO: Aliases

    //Rabbit
    RABITTYPE(new StringOption("RABITTYPE").match(Parse.Array(Rabbit.Type.values())), "setRabbitType", Rabbit.class), //TODO: Aliases

    //Pigman
    ANGER(new IntOption("ANGER"), "setAnger", PigZombie.class),

    //Sheep
    SHEARED(new BoolOption("SHEARED", true), "setSheared", Sheep.class),

    //Skeleton
    WITHER(new BoolOption("WITHER", true), "setWitherSkeleton", Skeleton.class),

    //Slime
    SIZE(new IntOption("SIZE"), "setSize", Slime.class),

    //IronGolem
    PLAYERCREATED(new BoolOption("PLAYERCREATED", true), "setPlayerCreated", IronGolem.class),

    //Tags
    NOAI(new BoolOption("NOAI", true), "setAI", Entity.class),
    INVULNERABLE(new BoolOption("INVULNERABLE", true), "setInvulnerable", Entity.class),
    SILENT(new BoolOption("SILENT", true), "setSilent", Entity.class),
    INVISIBLE(new BoolOption("INVISIBLE", true), "setInvisible", Entity.class),
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
