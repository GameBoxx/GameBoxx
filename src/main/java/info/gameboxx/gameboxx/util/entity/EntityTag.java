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

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.options.Option;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.options.single.*;
import info.gameboxx.gameboxx.util.Parse;
import org.bukkit.Art;
import org.bukkit.DyeColor;
import org.bukkit.EntityEffect;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.material.Colorable;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class EntityTag {
    //TODO: More modfiers for all options like min/max values etc.
    //TODO: More custom tags,
    //TODO: Attributes

    //All entities
    public static final EntityTag VELOCITY = EntityTag.register(new VectorOption("VELOCITY"), "setVelocity", Entity.class);
    public static final EntityTag FALLDISTANCE = EntityTag.register(new IntOption("FALLDISTANCE"), "setFallDistance", Entity.class);
    public static final EntityTag FIRETICKS = EntityTag.register(new IntOption("FIRETICKS"), "setFireTicks", Entity.class);
    public static final EntityTag LIVED = EntityTag.register(new IntOption("LIVED"), "setTicksLived", Entity.class);
    public static final EntityTag NAME = EntityTag.register(new StringOption("NAME"), "setCustomName", Entity.class);
    public static final EntityTag NAMEVISIBLE = EntityTag.register(new BoolOption("NAMEVISIBLE", true), "setCustomNameVisible", Entity.class);
    public static final EntityTag META = EntityTag.register(new StringOption("META").matchRegex("([^:]*?):([^:]*?)", "String must have the syntax key:value"), new EntityTagCallback() {
        @Override boolean execute(CommandSender sender, EEntity entity, SingleOption result) {
            String[] split = ((StringOption)result).getValue().split(":");
            if (split.length < 2) {
                return false;
            }
            entity.setMetadata(split[0], new FixedMetadataValue(GameBoxx.get(), split[1]));
            return true;
        }
    }, Entity.class); //TODO: Test regex and add translatable message.
    public static final EntityTag PLAYEFFECT = EntityTag.register(new StringOption("PLAYEFFECT").match(Parse.Array(EntityEffect.values())), "playEffect", Entity.class); //TODO: Aliases
    public static final EntityTag RIDE = EntityTag.register(new PlayerOption("RIDE"), new EntityTagCallback() {
        @Override boolean execute(CommandSender sender, EEntity entity, SingleOption result) {
            Player player = ((PlayerOption)result).getValue();
            if (player == null && sender instanceof Player) {
                player = (Player)sender;
            } else if (player == null) {
                return false;
            }
            entity.setVehicle(player);
            return true;
        }
    }, Entity.class);

    //Damagable
    public static final EntityTag HEALTH = EntityTag.register(new DoubleOption("health"), "setHealth", Damageable.class);
    public static final EntityTag MAXHEALTH = EntityTag.register(new DoubleOption("maxhealth"), "setMaxHealth", Damageable.class);

    //Living
    public static final EntityTag AIR = EntityTag.register(new IntOption("AIR"), "setRemainingAir", LivingEntity.class);
    public static final EntityTag MAXAIR = EntityTag.register(new IntOption("MAXAIR"), "setMaximumAir", LivingEntity.class);
    public static final EntityTag MAXNODMGTICKS = EntityTag.register(new IntOption("MAXNODMGTICKS"), "setMaximumNoDamageTicks", LivingEntity.class);
    public static final EntityTag NODMGTICKS = EntityTag.register(new IntOption("NODMGTICKS"), "setNoDamageTicks", LivingEntity.class);
    //public static final EntityTag MAINHAND = EntityTag.register(new ItemOption("MAINHAND"), "setItemInMainHand", LivingEntity.class);
    //public static final EntityTag OFFHAND = EntityTag.register(new ItemOption("OFFHAND"), "setItemInOffHand", LivingEntity.class);
    public static final EntityTag HANDDROP = EntityTag.register(new DoubleOption("HANDDROP"), "setItemInHandDropChance", LivingEntity.class);
    public static final EntityTag HELMETDROP = EntityTag.register(new DoubleOption("HELMETDROP"), "setHelmetDropChance", LivingEntity.class);
    public static final EntityTag CHESTPLATEDROP = EntityTag.register(new DoubleOption("CHESTPLATEDROP"), "setChestplateDropChance", LivingEntity.class);
    public static final EntityTag LEGGINGSDROP = EntityTag.register(new DoubleOption("LEGGINGSDROP"), "setLeggingsDropChance", LivingEntity.class);
    public static final EntityTag BOOTSDROP = EntityTag.register(new DoubleOption("BOOTSDROP"), "setBootsDropChance", LivingEntity.class);
    public static final EntityTag PICKUP = EntityTag.register(new BoolOption("PICKUP", true), "setCanPickupItems", LivingEntity.class);
    public static final EntityTag REMOVEFAR = EntityTag.register(new BoolOption("REMOVEFAR", true), "setRemoveWhenFarAway", LivingEntity.class);
    //public static final EntityTag public static final EntityTag LEASH = EntityTag.register(new EntityOption("LEASH"), "setLeashHolder", LivingEntity.class);

    //Ageable
    public static final EntityTag AGE = EntityTag.register(new IntOption("AGE"), "setAge", Ageable.class);
    public static final EntityTag AGELOCK = EntityTag.register(new BoolOption("AGELOCK", true), "setAgeLock", Ageable.class);
    public static final EntityTag BABY = EntityTag.register(new BoolOption("BABY", true), "setBaby", Ageable.class);
    public static final EntityTag BREED = EntityTag.register(new BoolOption("BREED", true), "setBreed", Ageable.class);

    //Tamable
    public static final EntityTag TAMED = EntityTag.register(new BoolOption("TAMED", true), "setTamed", Tameable.class);
    public static final EntityTag OWNER = EntityTag.register(new PlayerOption("OWNER"), "setOwner", Tameable.class); //TODO: OfflinePlayerOption?

    //Creature
    //public static final EntityTag TARGET = EntityTag.register(new EntityOption("TARGET"), "setTarget", Creature.class);

    //Projectile
    //public static final EntityTag SHOOTER = EntityTag.register(new EntityOption("SHOOTER"), "setShooter", Projectile.class);
    public static final EntityTag BOUNCE = EntityTag.register(new BoolOption("BOUNCE", true), "setBounce", Projectile.class);

    //Hanging
    public static final EntityTag DIR = EntityTag.register(new StringOption("DIR").match(Arrays.asList("north", "east", "south", "west", "up", "down")), new EntityTagCallback() {
        @Override boolean execute(CommandSender sender, EEntity entity, SingleOption result) {
            entity.setFacingDirection(BlockFace.valueOf(((StringOption)result).getValue()), true);
            return true;
        }
    }, Hanging.class);

    //Mixed Entities
    //public static final EntityTag BOOTS = EntityTag.register(new ItemOption("BOOTS"), "setBoots", ArmorStand.class, LivingEntity.class);
    //public static final EntityTag LEGGINGS = EntityTag.register(new ItemOption("LEGGINGS"), "setLeggings", ArmorStand.class, LivingEntity.class);
    //public static final EntityTag CHESTPLATE = EntityTag.register(new ItemOption("CHESTPLATE"), "setChestplate", ArmorStand.class, LivingEntity.class);
    //public static final EntityTag HELMET = EntityTag.register(new ItemOption("HELMET"), "setHelmet", ArmorStand.class, LivingEntity.class);
    //public static final EntityTag ITEM = EntityTag.register(new ItemOption("ITEM"), "setItem", Item.class, ItemFrame.class, ThrownPotion.class);
    public static final EntityTag SITTING = EntityTag.register(new BoolOption("SITTING", true), "setSitting", Ocelot.class, Wolf.class);
    public static final EntityTag ANGRY = EntityTag.register(new BoolOption("ANGRY", true), "setAngry", Wolf.class, PigZombie.class);
    public static final EntityTag SADDLE = EntityTag.register(new BoolOption("SADDLE", true), "setSaddle", Horse.class, Pig.class);
    //public static final EntityTag SADDLEITEM = EntityTag.register(new ItemOption("SADDLEITEM", true), "setSaddle", Horse.class, Pig.class);
    public static final EntityTag COLOR = EntityTag.register(new StringOption("COLOR").match(Parse.Array(DyeColor.values())), "setColor", Colorable.class, Wolf.class); //TODO: Aliases
    //public static final EntityTag EFFECT = EntityTag.register(new PotionOption("EFFECT"), "addEffect", LivingEntity.class, AreaEffectCloud.class);
    public static final EntityTag PROFESSION = EntityTag.register(new StringOption("PROFESSION").match(Parse.Array(Villager.Profession.values())), "setProfession", Villager.class, Zombie.class); //TODO: Aliases

    //ArmorStand
    //public static final EntityTag HAND = EntityTag.register(new ItemOption("HAND"), "setItemInHand", ArmorStand.class);
    public static final EntityTag POSE = EntityTag.register(new VectorOption("POSE"), "setBodyPose", ArmorStand.class);
    public static final EntityTag HEAD = EntityTag.register(new VectorOption("HEAD"), "setHeadPose", ArmorStand.class);
    public static final EntityTag LARM = EntityTag.register(new VectorOption("LARM"), "setLeftArmPose", ArmorStand.class);
    public static final EntityTag RARM = EntityTag.register(new VectorOption("RARM"), "setRightArmPose", ArmorStand.class);
    public static final EntityTag LLEG = EntityTag.register(new VectorOption("LLEG"), "setLeftLegPose", ArmorStand.class);
    public static final EntityTag RLEG = EntityTag.register(new VectorOption("RLEG"), "setRightLegPose", ArmorStand.class);
    public static final EntityTag BASEPLATE = EntityTag.register(new BoolOption("BASEPLATE", true), "setBasePlate", ArmorStand.class);
    public static final EntityTag GRAVITY = EntityTag.register(new BoolOption("GRAVITY", true), "setGravity", ArmorStand.class);
    public static final EntityTag VISIBLE = EntityTag.register(new BoolOption("VISIBLE", true), "setVisible", ArmorStand.class);
    public static final EntityTag ARMS = EntityTag.register(new BoolOption("ARMS", true), "setArms", ArmorStand.class);
    public static final EntityTag SMALL = EntityTag.register(new BoolOption("SMALL", true), "setSmall", ArmorStand.class);
    public static final EntityTag MARKER = EntityTag.register(new BoolOption("MARKER", true), "setMarker", ArmorStand.class);

    //Arrow
    public static final EntityTag KNOCKBACK = EntityTag.register(new IntOption("KNOCKBACK"), "setKnockbackStrength", Arrow.class);
    public static final EntityTag CRITICAL = EntityTag.register(new BoolOption("CRITICAL", true), "setCritical", Arrow.class);

    //AreaEffectCloud
    public static final EntityTag DURATION = EntityTag.register(new IntOption("DURATION"), "setDuration", AreaEffectCloud.class);
    public static final EntityTag WAITTIME = EntityTag.register(new IntOption("WAITTIME"), "setWaitTime", AreaEffectCloud.class);
    public static final EntityTag DELAYTICKS = EntityTag.register(new IntOption("DELAYTICKS"), "setReapplicationDelay", AreaEffectCloud.class);
    public static final EntityTag USETICKS = EntityTag.register(new IntOption("USETICKS"), "setDurationOnUse", AreaEffectCloud.class);
    public static final EntityTag RADIUS = EntityTag.register(new DoubleOption("RADIUS"), "setRadius", AreaEffectCloud.class);
    public static final EntityTag USERADIUS = EntityTag.register(new DoubleOption("USERADIUS"), "setRadiusOnUse", AreaEffectCloud.class);
    public static final EntityTag RADIUSDECAY = EntityTag.register(new DoubleOption("RADIUSDECAY"), "setRadiusPerTick", AreaEffectCloud.class);
    public static final EntityTag PARTICLE = EntityTag.register(new IntOption("PARTICLE"), "setParticle", AreaEffectCloud.class);
    //public static final EntityTag EFFECTCOLOR = EntityTag.register(new ColorOption("COLOR"), "setEffectColor", AreaEffectCloud.class);

    //Minecart
    public static final EntityTag MAXSPEED = EntityTag.register(new DoubleOption("MAXSPEED"), "setMaxSpeed", Minecart.class);
    public static final EntityTag SLOWEMPTY = EntityTag.register(new BoolOption("SLOWEMPTY", true), "setSlowWhenEmpty", Minecart.class);
    public static final EntityTag FLYVELOCITY = EntityTag.register(new VectorOption("FLYVELOCITY"), "setFlyingVelocityMod", Minecart.class);
    public static final EntityTag DERAILVELOCITY = EntityTag.register(new VectorOption("DERAILVELOCITY"), "setDerailedVelocityMod", Minecart.class);
    public static final EntityTag DISPLAYBLOCK = EntityTag.register(new MaterialOption("DISPLAYBLOCK"), "setDisplayBlock", Minecart.class); //TODO: Block modifier
    public static final EntityTag BLOCKOFFSET = EntityTag.register(new IntOption("BLOCKOFFSET"), "setDisplayBlockOffset", Minecart.class);

    //Cmdblock minecart
    public static final EntityTag CMD = EntityTag.register(new StringOption("CMD"), "setCommand", CommandMinecart.class);
    public static final EntityTag SENDER = EntityTag.register(new StringOption("SENDER"), "setName", CommandMinecart.class);

    //Experience
    public static final EntityTag EXP = EntityTag.register(new IntOption("EXP"), "setExperience", ExperienceOrb.class);

    //Falling block
    public static final EntityTag DROPITEM = EntityTag.register(new BoolOption("DROPITEM", true), "setDropItem", FallingBlock.class);
    public static final EntityTag HURTENTITIES = EntityTag.register(new BoolOption("HURTENTITIES", true), "setHurtEntities", FallingBlock.class);

    //Firework
    public static final EntityTag DETONATE = EntityTag.register(new BoolOption("DETONATE", true), "detonate", Firework.class);
    //public static final EntityTag FIREWORK = EntityTag.register(new FireworkOption("FIREWORK"), "setFireworkMeta", Firework.class);

    //Item
    //public static final EntityTag PICKUPDELAY = EntityTag.register(new IntOption("PICKUPDELAY"), "setPickupDelay", Item.class);

    //Itemframe
    public static final EntityTag ROTATION = EntityTag.register(new StringOption("ROTATION").match(Arrays.asList("0","45","90","135","180","225","270","315","360")), "setRotation", ItemFrame.class);

    //Painting
    public static final EntityTag ART = EntityTag.register(new StringOption("ART").match(Parse.Array(Art.values())), "setArt", Painting.class); //TODO: Aliases

    //FishHook
    public static final EntityTag BITECHANCE = EntityTag.register(new DoubleOption("BITECHANCE"), "setBiteChance", FishHook.class);

    //Primed TnT
    public static final EntityTag FUSETICKS = EntityTag.register(new IntOption("FUSETICKS"), "setFuseTicks", TNTPrimed.class);

    //Explosive
    public static final EntityTag YIELD = EntityTag.register(new DoubleOption("YIELD"), "setYield", Explosive.class);
    public static final EntityTag FIRE = EntityTag.register(new BoolOption("FIRE", true), "setIsIncendiary", Explosive.class);

    //Witherskull
    public static final EntityTag CHARGED = EntityTag.register(new BoolOption("CHARGED", true), "setCharged", WitherSkull.class);

    //Bat
    public static final EntityTag AWAKE = EntityTag.register(new BoolOption("AWAKE", true), "setAwake", Bat.class);

    //Creeper
    public static final EntityTag POWERED = EntityTag.register(new BoolOption("POWERED", true), "setPowered", Creeper.class);

    //Enderman
    public static final EntityTag HOLDING = EntityTag.register(new MaterialOption("holding"), "setCarriedMaterial", Enderman.class); //TODO: Block modifier

    //Guardian
    public static final EntityTag ELDER = EntityTag.register(new BoolOption("ELDER", true), "setElder", Guardian.class);

    //Horse
    public static final EntityTag HORSEVARIANT = EntityTag.register(new StringOption("HORSEVARIANT").match(Parse.Array(Horse.Variant.values())), "setHorseVariant", Horse.class); //TODO: Aliases
    public static final EntityTag HORSECOLOR = EntityTag.register(new StringOption("HORSECOLOR").match(Parse.Array(Horse.Color.values())), "setHorseColor", Horse.class); //TODO: Aliases
    public static final EntityTag HORSESTYLE = EntityTag.register(new StringOption("HORSESTYLE").match(Parse.Array(Horse.Style.values())), "setHorseStyle", Horse.class); //TODO: Aliases
    public static final EntityTag CHEST = EntityTag.register(new BoolOption("CHEST", true), "setCarryingChest", Horse.class);
    public static final EntityTag DOMESTICATION = EntityTag.register(new IntOption("DOMESTICATION"), "setDomestication", Horse.class);
    public static final EntityTag MAXDOMESTICATION = EntityTag.register(new IntOption("MAXDOMESTICATION"), "setMaxDomestication", Horse.class);
    public static final EntityTag JUMPSTRENGTH = EntityTag.register(new DoubleOption("JUMPSTRENGTH"), "setJumpStrength", Horse.class);
    //public static final EntityTag ARMOR = EntityTag.register(new ItemOption("ARMOR"), "setHorseArmor", Horse.class);

    //Ocelot
    public static final EntityTag CATTYPE = EntityTag.register(new StringOption("CATTYPE").match(Parse.Array(Ocelot.Type.values())), "setCatType", Ocelot.class); //TODO: Aliases

    //Rabbit
    public static final EntityTag RABITTYPE = EntityTag.register(new StringOption("RABITTYPE").match(Parse.Array(Rabbit.Type.values())), "setRabbitType", Rabbit.class); //TODO: Aliases

    //Pigman
    public static final EntityTag ANGER = EntityTag.register(new IntOption("ANGER"), "setAnger", PigZombie.class);

    //Sheep
    public static final EntityTag SHEARED = EntityTag.register(new BoolOption("SHEARED", true), "setSheared", Sheep.class);

    //Skeleton
    public static final EntityTag WITHER = EntityTag.register(new BoolOption("WITHER", true), "setWitherSkeleton", Skeleton.class);

    //Slime
    public static final EntityTag SIZE = EntityTag.register(new IntOption("SIZE"), "setSize", Slime.class);

    //IronGolem
    public static final EntityTag PLAYERCREATED = EntityTag.register(new BoolOption("PLAYERCREATED", true), "setPlayerCreated", IronGolem.class);

    //Tags
    public static final EntityTag NOAI = EntityTag.register(new BoolOption("NOAI", true), "setAI", Entity.class);
    public static final EntityTag INVULNERABLE = EntityTag.register(new BoolOption("INVULNERABLE", true), "setInvulnerable", Entity.class);
    public static final EntityTag SILENT = EntityTag.register(new BoolOption("SILENT", true), "setSilent", Entity.class);
    public static final EntityTag INVISIBLE = EntityTag.register(new BoolOption("INVISIBLE", true), "setInvisible", Entity.class);



    private static final Map<EntityType, List<EntityTag>> BY_ENTITY = new HashMap<>();
    private static final Map<String, EntityTag> BY_NAME = new HashMap<>();

    private SingleOption option;
    private EntityTagCallback callback = null;
    private String entityMethod = null;
    private Class<? extends Entity>[] entities;


    private EntityTag(SingleOption option, String entityMethod, Class... entities) {
        this.option = option;
        this.entityMethod = entityMethod;
        this.entities = entities;
    }

    private EntityTag(SingleOption option, EntityTagCallback callback, Class... entities) {
        this.option = option;
        this.callback = callback;
        this.entities = entities;
    }


    public String getName() {
        return option.getName();
    }

    public Class<? extends Entity>[] getEntities() {
        return entities;
    }

    public Option getOption() {
        return option;
    }

    public String getEntityMethod() {
        return entityMethod;
    }

    public EntityTagCallback getCallback() {
        return callback;
    }

    public boolean hasCallback() {
        return callback != null;
    }


    public static EntityTag fromString(String name) {
        return BY_NAME.get(name.toUpperCase().replace("_","").replace(" ", ""));
    }

    public static List<EntityTag> getTags(EntityType type) {
        return BY_ENTITY.get(type);
    }


    public static EntityTag register(SingleOption option, EntityTagCallback executeCallback, Class... entities) {
        return register(new EntityTag(option, executeCallback, entities));
    }

    private static EntityTag register(SingleOption option, String entityMethod, Class... entities) {
        return register(new EntityTag(option, entityMethod, entities));
    }

    private static EntityTag register(EntityTag tag) {
        String key = tag.getName().toUpperCase().replace("_","").replace(" ", "");
        if (BY_NAME.containsKey(key)) {
            throw new IllegalArgumentException("There is already an EntityTag registered with the name '" + key + "'!");
        }
        BY_NAME.put(key, tag);

        for (EntityType entityType : EntityType.values()) {
            for (Class clazz : tag.getEntities()) {
                if (clazz.isAssignableFrom(entityType.getEntityClass())) {
                    if (BY_ENTITY.containsKey(entityType)) {
                        List<EntityTag> tags = BY_ENTITY.get(entityType);
                        tags.add(tag);
                        BY_ENTITY.put(entityType, tags);
                    } else {
                        BY_ENTITY.put(entityType, Arrays.asList(tag));
                    }
                }
            }
        }
        return tag;
    }
}
