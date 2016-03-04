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
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.options.single.*;
import info.gameboxx.gameboxx.util.Parse;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.*;

public class EntityTag {

    private static Map<EntityType, List<EntityTag>> BY_ENTITY = new HashMap<>();
    private static Map<String, EntityTag> BY_NAME = new HashMap<>();

    private final SingleOption option;
    private final EntityTagCallback callback;
    private final String setMethod;
    private final String getMethod;
    private final Class<? extends Entity>[] entities;

    private EntityTag(SingleOption option, String setMethod, String getMethod, Class... entities) {
        this.option = option;
        this.callback = null;
        this.setMethod = setMethod;
        this.getMethod = getMethod;
        this.entities = entities;
    }

    private EntityTag(SingleOption option, EntityTagCallback callback, Class... entities) {
        this.option = option;
        this.callback = callback;
        this.setMethod = null;
        this.getMethod = null;
        this.entities = entities;
    }


    public String getName() {
        return option.getName();
    }

    public Class<? extends Entity>[] getEntities() {
        return entities;
    }

    public SingleOption getOption() {
        return option;
    }

    public String setMethod() {
        return setMethod;
    }

    public String getMethod() {
        return getMethod;
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

    public static Collection<EntityTag> values() {
        return BY_NAME.values();
    }


    public static EntityTag register(SingleOption option, EntityTagCallback executeCallback, Class... entities) {
        return register(new EntityTag(option, executeCallback, entities));
    }

    private static EntityTag register(SingleOption option, String setMethod, String getMethod, Class... entities) {
        return register(new EntityTag(option, setMethod, getMethod, entities));
    }

    private static EntityTag register(EntityTag tag) {
        String key = tag.getName().toUpperCase().replace("_","").replace(" ", "");
        if (BY_NAME.containsKey(key)) {
            throw new IllegalArgumentException("There is already an EntityTag registered with the name '" + key + "'!");
        }
        BY_NAME.put(key, tag);

        for (EntityType entityType : EntityType.values()) {
            if (entityType == EntityType.UNKNOWN) {
                continue;
            }
            for (Class clazz : tag.getEntities()) {
                if (clazz.isAssignableFrom(entityType.getEntityClass())) {
                    List<EntityTag> tags;
                    if (BY_ENTITY.containsKey(entityType)) {
                        tags = BY_ENTITY.get(entityType);
                    } else {
                        tags = new ArrayList<>();
                    }
                    tags.add(tag);
                    BY_ENTITY.put(entityType, tags);
                }
            }
        }
        return tag;
    }


    //region Tags
    //TODO: More modfiers for all options like min/max values etc.
    //TODO: More custom tags,
    //TODO: Attributes

    //All entities
    public static final EntityTag VELOCITY = EntityTag.register(new VectorOption("VELOCITY"), "setVelocity", null, Entity.class);
    public static final EntityTag FALLDISTANCE = EntityTag.register(new IntOption("FALLDISTANCE"), "setFallDistance", null, Entity.class);
    public static final EntityTag FIRETICKS = EntityTag.register(new IntOption("FIRETICKS", -1), "setFireTicks", "getFireTicks", Entity.class);
    public static final EntityTag LIVED = EntityTag.register(new IntOption("LIVED"), "setTicksLived", null, Entity.class);
    public static final EntityTag NAME = EntityTag.register(new StringOption("NAME"), "setCustomName", "getCustomName", Entity.class);
    public static final EntityTag NAMEVISIBLE = EntityTag.register(new BoolOption("NAMEVISIBLE", false), "setCustomNameVisible", "isCustomNameVisible", Entity.class);
    public static final EntityTag META = EntityTag.register(new StringOption("META").matchRegex("([^:]*?):([^:]*?)", "String must have the syntax key:value"), new EntityTagCallback() {
        @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
            String[] split = ((StringOption)result).getValue().split(":");
            if (split.length < 2) {
                return false;
            }
            entity.setMetadata(split[0], new FixedMetadataValue(GameBoxx.get(), split[1]));
            return true;
        }
        @Override String onGet(EEntity entity) {return null;}
    }, Entity.class);
    public static final EntityTag PLAYEFFECT = EntityTag.register(new StringOption("PLAYEFFECT").match(Parse.StringArray(EntityEffect.values())), new EntityTagCallback() {
        @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
            entity.playEffect(EntityEffect.valueOf(((StringOption)result).getValue().toUpperCase()));
            return true;
        }
        @Override String onGet(EEntity entity) {return null;}
    }, Entity.class); //TODO: Aliases

    //Damagable
    public static final EntityTag MAXHEALTH = EntityTag.register(new DoubleOption("maxhealth"), "setMaxHealth", "getMaxHealth", Damageable.class);
    public static final EntityTag HEALTH = EntityTag.register(new DoubleOption("health"), "setHealth", "getHealth", Damageable.class);

    //Living
    public static final EntityTag AIR = EntityTag.register(new IntOption("AIR", 300), "setRemainingAir", "getRemainingAir", LivingEntity.class);
    public static final EntityTag MAXNODMGTICKS = EntityTag.register(new IntOption("MAXNODMGTICKS", 20), "setMaximumNoDamageTicks", "getMaximumNoDamageTicks", LivingEntity.class);
    public static final EntityTag MAXAIR = EntityTag.register(new IntOption("MAXAIR", 300), "setMaximumAir", "getMaximumAir", LivingEntity.class);
    public static final EntityTag NODMGTICKS = EntityTag.register(new IntOption("NODMGTICKS", 0), "setNoDamageTicks", "getNoDamageTicks", LivingEntity.class);
    public static final EntityTag HANDDROP = EntityTag.register(new DoubleOption("HANDDROP"), "setItemInHandDropChance", null, LivingEntity.class);
    public static final EntityTag HELMETDROP = EntityTag.register(new DoubleOption("HELMETDROP"), "setHelmetDropChance", null, LivingEntity.class);
    public static final EntityTag CHESTPLATEDROP = EntityTag.register(new DoubleOption("CHESTPLATEDROP"), "setChestplateDropChance", null, LivingEntity.class);
    public static final EntityTag LEGGINGSDROP = EntityTag.register(new DoubleOption("LEGGINGSDROP"), "setLeggingsDropChance", null, LivingEntity.class);
    public static final EntityTag BOOTSDROP = EntityTag.register(new DoubleOption("BOOTSDROP"), "setBootsDropChance", null, LivingEntity.class);
    public static final EntityTag PICKUP = EntityTag.register(new BoolOption("PICKUP", false), "setCanPickupItems", "getCanPickupItems", LivingEntity.class);
    public static final EntityTag REMOVEFAR = EntityTag.register(new BoolOption("REMOVEFAR", false), "setRemoveWhenFarAway", "getRemoveWhenFarAway", LivingEntity.class);
    //public static final EntityTag LEASH = EntityTag.register(new EntityOption("LEASH"), "setLeashHolder", "getLeashHolder", LivingEntity.class);

    //Ageable
    public static final EntityTag AGE = EntityTag.register(new IntOption("AGE", 0), "setAge", "getAge", Ageable.class);
    public static final EntityTag AGELOCK = EntityTag.register(new BoolOption("AGELOCK", false), "setAgeLock", "getAgeLock", Ageable.class);
    public static final EntityTag BABY = EntityTag.register(new BoolOption("BABY", false), "setBaby", "isBaby", Ageable.class, Zombie.class);
    public static final EntityTag BREED = EntityTag.register(new BoolOption("BREED", true), "setBreed", "canBreed", Ageable.class);

    //Tamable
    public static final EntityTag TAMED = EntityTag.register(new BoolOption("TAMED", false), "setTamed", "isTamed", Tameable.class);
    public static final EntityTag OWNER = EntityTag.register(new PlayerOption("OWNER"), "setOwner", "getOwner", Tameable.class); //TODO: OfflinePlayerOption?

    //Creature
    //public static final EntityTag TARGET = EntityTag.register(new EntityOption("TARGET"), "setTarget", Creature.class);

    //Projectile
    //public static final EntityTag SHOOTER = EntityTag.register(new EntityOption("SHOOTER"), "setShooter", Projectile.class);
    public static final EntityTag BOUNCE = EntityTag.register(new BoolOption("BOUNCE", true), "setBounce", null, Projectile.class);

    //Hanging
    public static final EntityTag DIR = EntityTag.register(new StringOption("DIR").match(Arrays.asList("north", "east", "south", "west", "up", "down")), new EntityTagCallback() {
        @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
            entity.setFacingDirection(BlockFace.valueOf(((StringOption)result).getValue().toUpperCase()), true);
            return true;
        }
        @Override String onGet(EEntity entity) {return null;}
    }, Hanging.class);

    //Mixed Entities
    //public static final EntityTag MAINHAND = EntityTag.register(new ItemOption("MAINHAND"), "setItemInMainHand", ArmorStand.class, LivingEntity.class);
    //public static final EntityTag OFFHAND = EntityTag.register(new ItemOption("OFFHAND"), "setItemInOffHand", ArmorStand.class, LivingEntity.class);
    //public static final EntityTag BOOTS = EntityTag.register(new ItemOption("BOOTS"), "setBoots", ArmorStand.class, LivingEntity.class);
    //public static final EntityTag LEGGINGS = EntityTag.register(new ItemOption("LEGGINGS"), "setLeggings", ArmorStand.class, LivingEntity.class);
    //public static final EntityTag CHESTPLATE = EntityTag.register(new ItemOption("CHESTPLATE"), "setChestplate", ArmorStand.class, LivingEntity.class);
    //public static final EntityTag HELMET = EntityTag.register(new ItemOption("HELMET"), "setHelmet", ArmorStand.class, LivingEntity.class);
    //public static final EntityTag ITEM = EntityTag.register(new ItemOption("ITEM"), "setItem", Item.class, ItemFrame.class, ThrownPotion.class);
    public static final EntityTag SITTING = EntityTag.register(new BoolOption("SITTING", false), "setSitting", "isSitting", Ocelot.class, Wolf.class);
    public static final EntityTag ANGRY = EntityTag.register(new BoolOption("ANGRY", false), "setAngry", "isAngry", Wolf.class, PigZombie.class);
    public static final EntityTag SADDLE = EntityTag.register(new BoolOption("SADDLE", false), "setSaddle", "hasSaddle", Horse.class, Pig.class);
    //public static final EntityTag SADDLEITEM = EntityTag.register(new ItemOption("SADDLEITEM", true), "setSaddle", Horse.class, Pig.class);
    public static final EntityTag COLOR = EntityTag.register(new StringOption("COLOR").match(Parse.StringArray(DyeColor.values())), new EntityTagCallback() {
        @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
            entity.setColor(DyeColor.valueOf(((StringOption)result).getValue().toUpperCase()));
            return true;
        }
        @Override String onGet(EEntity entity) {
            return entity.getColor().toString();
        }
    }, Colorable.class, Wolf.class); //TODO: Aliases
    //public static final EntityTag EFFECT = EntityTag.register(new PotionOption("EFFECT"), "addEffect", LivingEntity.class, AreaEffectCloud.class);
    public static final EntityTag PROFESSION = EntityTag.register(new StringOption("PROFESSION").match(Parse.StringArray(Villager.Profession.values())), new EntityTagCallback() {
        @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
            entity.setProfession(Villager.Profession.valueOf(((StringOption)result).getValue().toUpperCase()));
            return true;
        }
        @Override String onGet(EEntity entity) {
            return entity.getProfession() == null ? null : entity.getProfession().toString();
        }
    }, Villager.class, Zombie.class); //TODO: Aliases

    //ArmorStand
    public static final EntityTag POSE = EntityTag.register(new VectorOption("POSE", new Vector(0,0,0)), "setBodyPose", "getBodyPose", ArmorStand.class);
    public static final EntityTag HEAD = EntityTag.register(new VectorOption("HEAD", new Vector(0,0,0)), "setHeadPose", "getHeadPose", ArmorStand.class);
    public static final EntityTag LARM = EntityTag.register(new VectorOption("LARM", new Vector(0,0,0)), "setLeftArmPose", "getLeftArmPose", ArmorStand.class);
    public static final EntityTag RARM = EntityTag.register(new VectorOption("RARM", new Vector(0,0,0)), "setRightArmPose", "getRightArmPose", ArmorStand.class);
    public static final EntityTag LLEG = EntityTag.register(new VectorOption("LLEG", new Vector(0,0,0)), "setLeftLegPose", "getLeftLegPose", ArmorStand.class);
    public static final EntityTag RLEG = EntityTag.register(new VectorOption("RLEG", new Vector(0,0,0)), "setRightLegPose", "getRightLegPose", ArmorStand.class);
    public static final EntityTag BASEPLATE = EntityTag.register(new BoolOption("BASEPLATE", true), "setBasePlate", "hasBasePlate", ArmorStand.class);
    public static final EntityTag GRAVITY = EntityTag.register(new BoolOption("GRAVITY", true), "setGravity", "hasGravity", ArmorStand.class);
    public static final EntityTag VISIBLE = EntityTag.register(new BoolOption("VISIBLE", true), "setVisible", "isVisible", ArmorStand.class);
    public static final EntityTag ARMS = EntityTag.register(new BoolOption("ARMS", false), "setArms", "hasArms", ArmorStand.class);
    public static final EntityTag SMALL = EntityTag.register(new BoolOption("SMALL", false), "setSmall", "isSmall", ArmorStand.class);
    public static final EntityTag MARKER = EntityTag.register(new BoolOption("MARKER", false), "setMarker", "isMarker", ArmorStand.class);

    //Arrow
    public static final EntityTag KNOCKBACK = EntityTag.register(new IntOption("KNOCKBACK"), "setKnockbackStrength", "getKnockbackStrength", Arrow.class);
    public static final EntityTag CRITICAL = EntityTag.register(new BoolOption("CRITICAL"), "setCritical", null, Arrow.class);

    //AreaEffectCloud
    public static final EntityTag DURATION = EntityTag.register(new IntOption("DURATION"), "setDuration", null, AreaEffectCloud.class);
    public static final EntityTag WAITTIME = EntityTag.register(new IntOption("WAITTIME"), "setWaitTime", null, AreaEffectCloud.class);
    public static final EntityTag DELAYTICKS = EntityTag.register(new IntOption("DELAYTICKS"), "setReapplicationDelay", null, AreaEffectCloud.class);
    public static final EntityTag USETICKS = EntityTag.register(new IntOption("USETICKS"), "setDurationOnUse", null, AreaEffectCloud.class);
    public static final EntityTag RADIUS = EntityTag.register(new DoubleOption("RADIUS"), "setRadius", null, AreaEffectCloud.class);
    public static final EntityTag USERADIUS = EntityTag.register(new DoubleOption("USERADIUS"), "setRadiusOnUse", null, AreaEffectCloud.class);
    public static final EntityTag RADIUSDECAY = EntityTag.register(new DoubleOption("RADIUSDECAY"), "setRadiusPerTick", null, AreaEffectCloud.class);
    public static final EntityTag PARTICLE = EntityTag.register(new IntOption("PARTICLE"), "setParticle", null, AreaEffectCloud.class);
    //public static final EntityTag EFFECTCOLOR = EntityTag.register(new ColorOption("COLOR"), "setEffectColor", null, AreaEffectCloud.class);

    //Minecart
    public static final EntityTag MAXSPEED = EntityTag.register(new DoubleOption("MAXSPEED", 0.4), "setMaxSpeed", "getMaxSpeed", Minecart.class);
    public static final EntityTag SLOWEMPTY = EntityTag.register(new BoolOption("SLOWEMPTY", true), "setSlowWhenEmpty", "isSlowWhenEmpty", Minecart.class);
    public static final EntityTag FLYVELOCITY = EntityTag.register(new VectorOption("FLYVELOCITY", new Vector(0.95, 0.95, 0.95)), "setFlyingVelocityMod", "getFlyingVelocityMod", Minecart.class);
    public static final EntityTag DERAILVELOCITY = EntityTag.register(new VectorOption("DERAILVELOCITY", new Vector(0.5, 0.5, 0.5)), "setDerailedVelocityMod", "getDerailedVelocityMod", Minecart.class);
    public static final EntityTag DISPLAYBLOCK = EntityTag.register(new MaterialOption("DISPLAYBLOCK", new MaterialData(Material.AIR)), "setDisplayBlock", "getDisplayBlock", Minecart.class); //TODO: Block modifier
    public static final EntityTag BLOCKOFFSET = EntityTag.register(new IntOption("BLOCKOFFSET", 6), "setDisplayBlockOffset", "getDisplayBlockOffset", Minecart.class);

    //Cmdblock minecart
    public static final EntityTag CMD = EntityTag.register(new StringOption("CMD"), "setCommand", "getCommand", CommandMinecart.class);
    public static final EntityTag SENDER = EntityTag.register(new StringOption("SENDER"), "setName", "getName", CommandMinecart.class);

    //Experience
    public static final EntityTag EXP = EntityTag.register(new IntOption("EXP"), "setExperience", null, ExperienceOrb.class);

    //Falling block
    public static final EntityTag DROPITEM = EntityTag.register(new BoolOption("DROPITEM", true), "setDropItem", null, FallingBlock.class);
    public static final EntityTag HURTENTITIES = EntityTag.register(new BoolOption("HURTENTITIES", true), "setHurtEntities", null, FallingBlock.class);

    //Firework
    public static final EntityTag DETONATE = EntityTag.register(new BoolOption("DETONATE", true), "detonate", null, Firework.class);
    //public static final EntityTag FIREWORK = EntityTag.register(new FireworkOption("FIREWORK"), "setFireworkMeta", Firework.class);

    //Item
    public static final EntityTag PICKUPDELAY = EntityTag.register(new IntOption("PICKUPDELAY"), "setPickupDelay", null, Item.class);

    //Itemframe
    public static final EntityTag ROTATION = EntityTag.register(new StringOption("ROTATION").match(Arrays.asList("0","45","90","135","180","225","270","315","360")), "setRotation", null, ItemFrame.class);

    //Painting
    public static final EntityTag ART = EntityTag.register(new StringOption("ART").match(Parse.StringArray(Art.values())), new EntityTagCallback() {
        @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
            entity.setArt(Art.valueOf(((StringOption)result).getValue().toUpperCase()));
            return true;
        }
        @Override String onGet(EEntity entity) {
            return entity.getArt().toString();
        }
    }, Painting.class); //TODO: Aliases

    //FishHook
    public static final EntityTag BITECHANCE = EntityTag.register(new DoubleOption("BITECHANCE"), "setBiteChance", null, FishHook.class);

    //Primed TnT
    public static final EntityTag FUSETICKS = EntityTag.register(new IntOption("FUSETICKS"), "setFuseTicks", null, TNTPrimed.class);

    //Explosive
    public static final EntityTag YIELD = EntityTag.register(new DoubleOption("YIELD"), "setYield", null, Explosive.class);
    public static final EntityTag FIRE = EntityTag.register(new BoolOption("FIRE", true), "setIsIncendiary", null, Explosive.class);

    //Witherskull
    public static final EntityTag CHARGED = EntityTag.register(new BoolOption("CHARGED", true), "setCharged", "isCharged", WitherSkull.class);

    //Bat
    public static final EntityTag AWAKE = EntityTag.register(new BoolOption("AWAKE", true), "setAwake", "isAwake", Bat.class);

    //Creeper
    public static final EntityTag POWERED = EntityTag.register(new BoolOption("POWERED", true), "setPowered", "isPowered", Creeper.class);

    //Enderman
    public static final EntityTag HOLDING = EntityTag.register(new MaterialOption("holding"), "setCarriedMaterial", "getCarriedMaterial", Enderman.class); //TODO: Block modifier

    //Guardian
    public static final EntityTag ELDER = EntityTag.register(new BoolOption("ELDER", false), "setElder", "isElder", Guardian.class);

    //Horse
    public static final EntityTag HORSEVARIANT = EntityTag.register(new StringOption("HORSEVARIANT", "HORSE").match(Parse.StringArray(Horse.Variant.values())), new EntityTagCallback() {
        @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
            entity.setHorseVariant(Horse.Variant.valueOf(((StringOption)result).getValue().toUpperCase()));
            return true;
        }
        @Override String onGet(EEntity entity) {
            return entity.getHorseVariant().toString();
        }
    }, Horse.class); //TODO: Aliases
    public static final EntityTag HORSECOLOR = EntityTag.register(new StringOption("HORSECOLOR").match(Parse.StringArray(Horse.Color.values())), new EntityTagCallback() {
        @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
            entity.setHorseColor(Horse.Color.valueOf(((StringOption)result).getValue().toUpperCase()));
            return true;
        }
        @Override String onGet(EEntity entity) {
            return entity.getHorseColor().toString();
        }
    }, Horse.class); //TODO: Aliases
    public static final EntityTag HORSESTYLE = EntityTag.register(new StringOption("HORSESTYLE").match(Parse.StringArray(Horse.Style.values())), new EntityTagCallback() {
        @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
            entity.setHorseStyle(Horse.Style.valueOf(((StringOption)result).getValue().toUpperCase()));
            return true;
        }
        @Override String onGet(EEntity entity) {
            return entity.getHorseStyle().toString();
        }
    }, Horse.class); //TODO: Aliases
    public static final EntityTag CHEST = EntityTag.register(new BoolOption("CHEST", false), "setCarryingChest", "isCarryingChest", Horse.class);
    public static final EntityTag DOMESTICATION = EntityTag.register(new IntOption("DOMESTICATION", 0), "setDomestication", "getDomestication", Horse.class);
    public static final EntityTag MAXDOMESTICATION = EntityTag.register(new IntOption("MAXDOMESTICATION", 100), "setMaxDomestication", "getMaxDomestication", Horse.class);
    public static final EntityTag JUMPSTRENGTH = EntityTag.register(new DoubleOption("JUMPSTRENGTH"), "setJumpStrength", "getJumpStrength", Horse.class);
    //public static final EntityTag ARMOR = EntityTag.register(new ItemOption("ARMOR"), "setHorseArmor", "getHorseArmor", Horse.class);

    //Ocelot
    public static final EntityTag CATTYPE = EntityTag.register(new StringOption("CATTYPE").match(Parse.StringArray(Ocelot.Type.values())), new EntityTagCallback() {
        @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
            entity.setCatType(Ocelot.Type.valueOf(((StringOption)result).getValue().toUpperCase()));
            return true;
        }
        @Override String onGet(EEntity entity) {
            return entity.getCatType().toString();
        }
    }, Ocelot.class); //TODO: Aliases

    //Rabbit
    public static final EntityTag RABBITTYPE = EntityTag.register(new StringOption("RABBITTYPE").match(Parse.StringArray(Rabbit.Type.values())), new EntityTagCallback() {
        @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
            entity.setRabbitType(Rabbit.Type.valueOf(((StringOption)result).getValue().toUpperCase()));
            return true;
        }
        @Override String onGet(EEntity entity) {
            return entity.getRabbitType().toString();
        }
    }, Rabbit.class); //TODO: Aliases

    //Pigman
    public static final EntityTag ANGER = EntityTag.register(new IntOption("ANGER", 0), "setAnger", "getAnger", PigZombie.class);

    //Sheep
    public static final EntityTag SHEARED = EntityTag.register(new BoolOption("SHEARED", false), "setSheared", "isSheared", Sheep.class);

    //Skeleton
    public static final EntityTag WITHER = EntityTag.register(new BoolOption("WITHER", false), "setWitherSkeleton", "isWitherSkeleton", Skeleton.class);

    //Slime
    public static final EntityTag SIZE = EntityTag.register(new IntOption("SIZE"), "setSize", "getSize", Slime.class);

    //IronGolem
    public static final EntityTag PLAYERCREATED = EntityTag.register(new BoolOption("PLAYERCREATED", false), "setPlayerCreated", "isPlayerCreated", IronGolem.class);

    //Tags
    public static final EntityTag NOAI = EntityTag.register(new BoolOption("NOAI", false), "setNoAI", "hasNoAI", Entity.class);
    public static final EntityTag INVULNERABLE = EntityTag.register(new BoolOption("INVULNERABLE", false), "setInvulnerable", "isInvulnerable", Entity.class);
    public static final EntityTag SILENT = EntityTag.register(new BoolOption("SILENT", false), "setSilent", "isSilent", Entity.class);
    public static final EntityTag PERSISTENT = EntityTag.register(new BoolOption("PERSISTENT", false), "setPersistent", "isPersistent", Entity.class);
    //endregion
}
