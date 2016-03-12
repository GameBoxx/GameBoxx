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
import info.gameboxx.gameboxx.aliases.*;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.options.single.*;
import org.bukkit.Material;
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

    private final String tag;
    private final String[] aliases;
    private final SingleOption option;
    private final EntityTagCallback callback;
    private final String setMethod;
    private final String getMethod;
    private final Class<? extends Entity>[] entities;

    private EntityTag(String tag, String[] aliases, SingleOption option, String setMethod, String getMethod, Class... entities) {
        this.tag = tag;
        option.name(tag);
        this.aliases = aliases;
        this.option = option;
        this.callback = null;
        this.setMethod = setMethod;
        this.getMethod = getMethod;
        this.entities = entities;
    }

    private EntityTag(String tag, String[] aliases, SingleOption option, EntityTagCallback callback, Class... entities) {
        this.tag = tag;
        option.name(tag);
        this.aliases = aliases;
        this.option = option;
        this.callback = callback;
        this.setMethod = null;
        this.getMethod = null;
        this.entities = entities;
    }


    public String getTag() {
        return tag;
    }

    public String[] getAliases() {
        return aliases;
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
        return BY_NAME.get(name.toUpperCase().replace("_", "").replace(" ", ""));
    }

    public static List<EntityTag> getTags(EntityType type) {
        return BY_ENTITY.get(type);
    }

    public static Collection<EntityTag> values() {
        return BY_NAME.values();
    }


    public static EntityTag register(String tag, String[] aliases, SingleOption option, EntityTagCallback executeCallback, Class... entities) {
        return register(new EntityTag(tag, aliases, option, executeCallback, entities));
    }

    private static EntityTag register(String tag, String[] aliases, SingleOption option, String setMethod, String getMethod, Class... entities) {
        return register(new EntityTag(tag, aliases, option, setMethod, getMethod, entities));
    }

    private static EntityTag register(EntityTag tag) {
        String key = tag.getTag().toUpperCase().replace("_", "").replace(" ", "");
        if (BY_NAME.containsKey(key)) {
            throw new IllegalArgumentException("There is already an EntityTag registered with the name '" + key + "'!");
        }
        BY_NAME.put(key, tag);

        if (tag.getAliases() != null) {
            for (String alias : tag.getAliases()) {
                alias = alias.toUpperCase().replace("_", "").replace(" ", "");
                if (!BY_NAME.containsKey(alias)) {
                    BY_NAME.put(alias, tag);
                }
            }
        }

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


    public static void registerDefaults() {
        //TODO: More modfiers for all options like min/max values etc.
        //TODO: More custom tags,
        //TODO: Attributes
    
        //All entities
        EntityTag.register("VELOCITY", new String[] {"VEL"}, new VectorO(), "setVelocity", null, Entity.class);
        EntityTag.register("FALLDISTANCE", new String[] {"FALL", "FALLD", "FDISTANCE", "FD"}, new IntO(), "setFallDistance", null, Entity.class);
        EntityTag.register("FIRETICKS", new String[] {"FTICKS", "FIRET", "FT"}, new IntO().def(-1), "setFireTicks", "getFireTicks", Entity.class);
        EntityTag.register("LIVED", new String[] {"TICKSLIVED", "TLIVED", "TL"}, new IntO(), "setTicksLived", null, Entity.class);
        EntityTag.register("NAME", new String[] {"DISPLAYNAME", "DN", "CUSTOMNAME", "CNAME", "DNAME", "CN"}, new StringO(), "setCustomName", "getCustomName", Entity.class);
        EntityTag.register("NAMEVISIBLE", new String[] {"NAMEV", "NVISIBLE", "NAMEVIS", "NVIS"}, new BoolO().def(false), "setCustomNameVisible", "isCustomNameVisible", Entity.class);
        EntityTag.register("META", null, new StringO().matchRegex("([^:]*?):([^:]*?)", "String must have the syntax key:value"), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                String[] split = ((StringO)result).getValue().split(":");
                if (split.length < 2) {
                    return false;
                }
                entity.setMetadata(split[0], new FixedMetadataValue(GameBoxx.get(), split[1]));
                return true;
            }

            @Override String onGet(EEntity entity) {return null;}
        }, Entity.class);

        //Damagable
        EntityTag.register("MAXHEALTH", new String[] {"MAXHP", "MHEALTH", "MHP"}, new DoubleO(), "setMaxHealth", "getMaxHealth", Damageable.class);
        EntityTag.register("HEALTH", new String[] {"HP"}, new DoubleO(), "setHealth", "getHealth", Damageable.class);

        //Living
        EntityTag.register("AIR", new String[] {"OXYGEN", "OXY"}, new IntO().def(300), "setRemainingAir", "getRemainingAir", LivingEntity.class);
        EntityTag.register("MAXNODMGTICKS", new String[] {"MAXNODAMAGE", "MAXNODMG", "MNODMGTICKS", "MNODMG"}, new IntO().def(20), "setMaximumNoDamageTicks", "getMaximumNoDamageTicks", LivingEntity.class);
        EntityTag.register("MAXAIR", new String[] {"MAXOXYGEN", "MAXOXY", "MAIR", "MOXYGEN", "MOXY"}, new IntO().def(300), "setMaximumAir", "getMaximumAir", LivingEntity.class);
        EntityTag.register("NODMGTICKS", new String[] {"NODMG", "NODAMAGETICKS"}, new IntO().def(0), "setNoDamageTicks", "getNoDamageTicks", LivingEntity.class);
        EntityTag.register("MAINHANDDROP", new String[] {"MAINHANDD", "MAINHANDDROPCHANCE", "MAINHANDDC", "MAINHANDC"}, new DoubleO(), "setItemInMainHandDropChance", null, LivingEntity.class);
        EntityTag.register("OFFHANDDROP", new String[] {"OFFHANDD", "OFFHANDDROPCHANCE", "OFFHANDDC", "OFFHANDC"}, new DoubleO(), "setItemInOffHandDropChance", null, LivingEntity.class);
        EntityTag.register("HELMETDROP", new String[] {"HELMETD", "HELMETDROPCHANCE", "HELMETDC", "HELMETC",
                "HELMDROP", "HELMD", "HELMDROPCHANCE", "HELMDC", "HELMC"}, new DoubleO(), "setHelmetDropChance", null, LivingEntity.class);
        EntityTag.register("CHESTPLATEDROP", new String[] {"CHESTPLATED", "CHESTPLATEDROPCHANCE", "CHESTPLATEDC", "CHESTPLATEC",
                "CHESTDROP", "CHESTD", "CHESTDROPCHANCE", "CHESTDC", "CHESTC"}, new DoubleO(), "setChestplateDropChance", null, LivingEntity.class);
        EntityTag.register("LEGGINGSDROP", new String[] {"LEGGINGSD", "LEGGINGSDROPCHANCE", "LEGGINGSDC", "LEGGINGSC",
                "LEGDROP", "LEGD", "LEGDROPCHANCE", "LEGDC", "LEGC"}, new DoubleO(), "setLeggingsDropChance", null, LivingEntity.class);
        EntityTag.register("BOOTSDROP", new String[] {"BOOTSD", "BOOTSDROPCHANCE", "BOOTSDC", "BOOTSC",
                "BOOTDROP", "BOOTD", "BOOTDROPCHANCE", "BOOTDC", "BOOTC"}, new DoubleO(), "setBootsDropChance", null, LivingEntity.class);
        EntityTag.register("PICKUP", new String[] {"ITEMPICKUP", "CANPICKUP", "IPICKUP", "CPICKUP"}, new BoolO().def(false), "setCanPickupItems", "getCanPickupItems", LivingEntity.class);
        EntityTag.register("REMOVEFAR", new String[] {"REMOVEFARAWAY"}, new BoolO().def(false), "setRemoveWhenFarAway", "getRemoveWhenFarAway", LivingEntity.class);
        EntityTag.register("LEASHHOLDER", new String[] {"LEASH"}, new EntityO(), "setLeashHolder", null, LivingEntity.class);
        //TODO: Fix leashes (Maybe also make it so you can create a leash between two locations)

        //Ageable
        EntityTag.register("AGE", null, new IntO().def(0), "setAge", "getAge", Ageable.class);
        EntityTag.register("AGELOCK", new String[] {"AGEL"}, new BoolO().def(false), "setAgeLock", "getAgeLock", Ageable.class);
        EntityTag.register("BABY", null, new BoolO().def(false), "setBaby", "isBaby", Ageable.class, Zombie.class);
        EntityTag.register("CANBREED", new String[] {"BREED"}, new BoolO().def(true), "setBreed", "canBreed", Ageable.class);

        //Tamable
        EntityTag.register("TAMED", new String[] {"TAME"}, new BoolO().def(false), "setTamed", "isTamed", Tameable.class);
        EntityTag.register("OWNER", null, new OfflinePlayerO(), "setOwner", "getOwner", Tameable.class);

        //Creature
        EntityTag.register("TARGET", null, new EntityO(), "setTarget", null, Creature.class, ShulkerBullet.class);

        //Projectile
        EntityTag.register("SHOOTER", null, new EntityO(), "setShooter", null, Projectile.class, ShulkerBullet.class);
        EntityTag.register("BOUNCE", null, new BoolO().def(true), "setBounce", null, Projectile.class);

        //Hanging
        EntityTag.register("DIR", null, new StringO().match(Directions.getAliasMap(Directions.Type.SIX_SELF)), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setFacingDirection(Directions.get(((StringO)result).getValue()), true);
                return true;
            }

            @Override String onGet(EEntity entity) {return null;}
        }, Hanging.class);

        //Mixed Entities
        EntityTag.register("MAINHAND", new String[] {"MHAND", "HAND"}, new ItemO(), "setItemInMainHand", "getItemInMainHand", ArmorStand.class, LivingEntity.class);
        EntityTag.register("OFFHAND", new String[] {"OHAND"}, new ItemO(), "setItemInOffHand", "getItemInOffHand", ArmorStand.class, LivingEntity.class);
        EntityTag.register("BOOTS", new String[] {"BOOT"}, new ItemO(), "setBoots", "getBoots", ArmorStand.class, LivingEntity.class);
        EntityTag.register("LEGGINGS", new String[] {"LEGGING", "LEG"}, new ItemO(), "setLeggings", "getLeggings", ArmorStand.class, LivingEntity.class);
        EntityTag.register("CHESTPLATE", new String[] {"CPLATE", "CHESTP"}, new ItemO(), "setChestplate", "getChestplate", ArmorStand.class, LivingEntity.class);
        EntityTag.register("HELMET", new String[] {"HELM"}, new ItemO(), "setHelmet", "getHelmet", ArmorStand.class, LivingEntity.class);
        EntityTag.register("ITEM", null, new ItemO(), "setItem", "getItem", Item.class, ItemFrame.class, ThrownPotion.class);
        EntityTag.register("SITTING", new String[] {"SIT"}, new BoolO().def(false), "setSitting", "isSitting", Ocelot.class, Wolf.class);
        EntityTag.register("ANGRY", null, new BoolO().def(false), "setAngry", "isAngry", Wolf.class, PigZombie.class);
        EntityTag.register("SADDLE", null, new BoolO().def(false), "setSaddle", "hasSaddle", Horse.class, Pig.class);
        EntityTag.register("SADDLEITEM", new String[] {"SADDLEI"}, new ItemO(), "setSaddle", "getSaddle", Horse.class, Pig.class);
        EntityTag.register("COLOR", new String[] {"CLR", "DYECOLOR", "DYECLR"}, new StringO().match(DyeColors.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setColor(DyeColors.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return DyeColors.getName(entity.getColor());
            }
        }, Colorable.class, Wolf.class);
        EntityTag.register("EFFECT", new String[] {"POTION", "EFF", "E"}, new PotionO(), "addEffect", null, LivingEntity.class, AreaEffectCloud.class);
        EntityTag.register("PROFESSION", new String[] {"PROF", "VILLAGER"}, new StringO().match(Professions.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setProfession(Professions.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return Professions.getName(entity.getProfession());
            }
        }, Villager.class, Zombie.class);

        //ArmorStand
        EntityTag.register("POSE", new String[] {"BODYPOSE", "BPOSE"}, new VectorO().def(new Vector(0, 0, 0)), "setBodyPose", "getBodyPose", ArmorStand.class);
        EntityTag.register("HEAD", new String[] {"HEADPOSE", "HPOSE"}, new VectorO().def(new Vector(0, 0, 0)), "setHeadPose", "getHeadPose", ArmorStand.class);
        EntityTag.register("LARM", new String[] {"LEFTARM", "LEFTARMPOSE", "LARMPOSE"}, new VectorO().def(new Vector(0, 0, 0)), "setLeftArmPose", "getLeftArmPose", ArmorStand.class);
        EntityTag.register("RARM", new String[] {"RIGHTARM", "RIGHTARMPOSE", "RARMPOSE"}, new VectorO().def(new Vector(0, 0, 0)), "setRightArmPose", "getRightArmPose", ArmorStand.class);
        EntityTag.register("LLEG", new String[] {"LEFTLEG", "LEFTLEGPOSE", "LLEGPOSE"}, new VectorO().def(new Vector(0, 0, 0)), "setLeftLegPose", "getLeftLegPose", ArmorStand.class);
        EntityTag.register("RLEG", new String[] {"RIGHTLEG", "RIGHTLEGPOSE", "RLEGPOSE"}, new VectorO().def(new Vector(0, 0, 0)), "setRightLegPose", "getRightLegPose", ArmorStand.class);
        EntityTag.register("BASEPLATE", new String[] {"PLATE"}, new BoolO().def(true), "setBasePlate", "hasBasePlate", ArmorStand.class);
        EntityTag.register("GRAVITY", new String[] {"GRAV"}, new BoolO().def(true), "setGravity", "hasGravity", ArmorStand.class);
        EntityTag.register("VISIBLE", new String[] {"VIS"}, new BoolO().def(true), "setVisible", "isVisible", ArmorStand.class);
        EntityTag.register("ARMS", new String[] {"ARM"}, new BoolO().def(false), "setArms", "hasArms", ArmorStand.class);
        EntityTag.register("SMALL", null, new BoolO().def(false), "setSmall", "isSmall", ArmorStand.class);
        EntityTag.register("MARKER", null, new BoolO().def(false), "setMarker", "isMarker", ArmorStand.class);

        //Arrow
        EntityTag.register("KNOCKBACK", new String[] {"KNOCKB", "KBACK"}, new IntO(), "setKnockbackStrength", "getKnockbackStrength", Arrow.class);
        EntityTag.register("CRITICAL", new String[] {"CRIT"}, new BoolO(), "setCritical", null, Arrow.class);

        //AreaEffectCloud
        EntityTag.register("DURATION", new String[] {"DUR"}, new IntO(), "setDuration", null, AreaEffectCloud.class);
        EntityTag.register("WAITTIME", new String[] {"WAIT", "WAITDELAY", "STARTDELAY"}, new IntO(), "setWaitTime", null, AreaEffectCloud.class);
        EntityTag.register("DELAYTICKS", new String[] {"DELAYT", "REAPPLICATEDELAY", "USEDELAY", "COOLDOWN", "CD"}, new IntO(), "setReapplicationDelay", null, AreaEffectCloud.class);
        EntityTag.register("USETICKS", new String[] {"USEDURATION", "USET"}, new IntO(), "setDurationOnUse", null, AreaEffectCloud.class);
        EntityTag.register("RADIUS", new String[] {"RAD"}, new DoubleO(), "setRadius", null, AreaEffectCloud.class);
        EntityTag.register("USERADIUS", new String[] {"USER"}, new DoubleO(), "setRadiusOnUse", null, AreaEffectCloud.class);
        EntityTag.register("RADIUSDECAY", new String[] {"DECAYR", "DECAY", "RADIUSPERTICK", "RADDECAY", "RADPERTICK"}, new DoubleO(), "setRadiusPerTick", null, AreaEffectCloud.class);
        EntityTag.register("PARTICLE", null, new StringO().match(Particles.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setParticle(Particles.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {return null;}
        }, AreaEffectCloud.class);
        EntityTag.register("PARTICLECOLOR", new String[] {"PCOLOR", "PARTICLEC"}, new ColorO(), "setEffectColor", null, AreaEffectCloud.class);

        //Minecart
        EntityTag.register("MAXSPEED", new String[] {"MSPEED"}, new DoubleO().def(0.4), "setMaxSpeed", "getMaxSpeed", Minecart.class);
        EntityTag.register("SLOWEMPTY", new String[] {"SLOWWHENEMPTY", "SLOWDECEL", "DECEL", "DECELERATE"}, new BoolO().def(true), "setSlowWhenEmpty", "isSlowWhenEmpty", Minecart.class);
        EntityTag.register("FLYVELOCITY", new String[] {"FLYVEL", "FVELOCITY", "FVEL"}, new VectorO().def(new Vector(0.95, 0.95, 0.95)), "setFlyingVelocityMod", "getFlyingVelocityMod", Minecart.class);
        EntityTag.register("DERAILVELOCITY", new String[] {"DERAILVEL", "DVELOCITY", "DVEL"}, new VectorO().def(new Vector(0.5, 0.5, 0.5)), "setDerailedVelocityMod", "getDerailedVelocityMod", Minecart.class);
        EntityTag.register("DISPLAYBLOCK", new String[] {"DBLOCK"}, new MaterialO().blocks(true).def(new MaterialData(Material.AIR)), "setDisplayBlock", "getDisplayBlock", Minecart.class);
        EntityTag.register("BLOCKOFFSET", new String[] {"BOFFSET"}, new IntO().def(6), "setDisplayBlockOffset", "getDisplayBlockOffset", Minecart.class);

        //Cmdblock minecart
        EntityTag.register("CMD", new String[] {"COMMAND"}, new StringO(), "setCommand", "getCommand", CommandMinecart.class);
        EntityTag.register("SENDER", new String[] {"CMDSENDER", "COMMANDSENDER"}, new StringO(), "setName", "getTag", CommandMinecart.class);

        //Experience
        EntityTag.register("EXP", new String[] {"EXPERIENCE", "XP"}, new IntO(), "setExperience", null, ExperienceOrb.class);

        //Falling block
        EntityTag.register("DROPITEM", null, new BoolO().def(true), "setDropItem", null, FallingBlock.class);
        EntityTag.register("HURTENTITIES", new String[] {"HURT"}, new BoolO().def(true), "setHurtEntities", null, FallingBlock.class);

        //Firework
        EntityTag.register("DETONATE", new String[] {"DET"}, new BoolO().def(true), "detonate", null, Firework.class);
        //TODO: Needs firework option
        //EntityTag.register("FIREWORK", new String[] {"FW", "FEFFECT"}, new FireworkOption(), "setFireworkMeta", Firework.class);

        //Item
        EntityTag.register("PICKUPDELAY", new String[] {"PDELAY", "DELAY", "PICKUPD"}, new IntO(), "setPickupDelay", null, Item.class);

        //Itemframe
        EntityTag.register("ROTATION", new String[] {"ROT"}, new StringO().match(Rotations.getAliasMap()),new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setRotation(Rotations.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {return null;}
        }, ItemFrame.class);

        //Painting
        EntityTag.register("ART", new String[] {"PAINTING", "PAINT", "CAVAS", "MOTIVE"}, new StringO().match(Paintings.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setArt(Paintings.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return Paintings.getName(entity.getArt());
            }
        }, Painting.class);

        //FishHook
        EntityTag.register("BITECHANCE", new String[] {"BITE", "BITEC", "BCHANCE"}, new DoubleO(), "setBiteChance", null, FishHook.class);

        //Primed TnT
        EntityTag.register("FUSETICKS", new String[] {"FUSET", "FUSE"}, new IntO(), "setFuseTicks", null, TNTPrimed.class);

        //Explosive
        EntityTag.register("YIELD", new String[] {"EXPLOSIONSIZE", "EXPLOSIONRADIUS", "EXPLODESIZE", "EXPLODERADIUS", "EXPLOSIONS", "EXPLOSIONR"}, new DoubleO(), "setYield", null, Explosive.class);
        EntityTag.register("FIRE", new String[] {"EXPLODEFIRE", "EXPLOSIONFIRE"}, new BoolO().def(true), "setIsIncendiary", null, Explosive.class);

        //Witherskull
        EntityTag.register("CHARGED", new String[] {"CHARGE"}, new BoolO().def(true), "setCharged", "isCharged", WitherSkull.class);

        //Bat
        EntityTag.register("AWAKE", null, new BoolO().def(true), "setAwake", "isAwake", Bat.class);

        //Creeper
        EntityTag.register("POWERED", new String[] {"LIGHTNING"}, new BoolO().def(true), "setPowered", "isPowered", Creeper.class);

        //Enderman
        EntityTag.register("holding", new String[] {"HOLD", "CARYING", "CARY"}, new MaterialO(), "setCarriedMaterial", "getCarriedMaterial", Enderman.class); //TODO: Block modifier

        //Guardian
        EntityTag.register("ELDER", new String[] {"ELDAR"}, new BoolO().def(false), "setElder", "isElder", Guardian.class);

        //Horse
        EntityTag.register("HORSEVARIANT", new String[] {"HVARIANT", "VARIANT", "HORSEV", "HORSETYPE", "HTYPE", "HORSET"}, new StringO().def("HORSE").match(HorseVariants.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setHorseVariant(HorseVariants.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return HorseVariants.getName(entity.getHorseVariant());
            }
        }, Horse.class);
        EntityTag.register("HORSECOLOR", new String[] {"HCOLOR", "HORSEC"}, new StringO().match(HorseColors.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setHorseColor(HorseColors.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return HorseColors.getName(entity.getHorseColor());
            }
        }, Horse.class);
        EntityTag.register("HORSESTYLE", new String[] {"HSTYLE", "HORSES"}, new StringO().match(HorseStyles.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setHorseStyle(HorseStyles.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return HorseStyles.getName(entity.getHorseStyle());
            }
        }, Horse.class);
        EntityTag.register("CARYINGCHEST", new String[] {"CHEST"}, new BoolO().def(false), "setCarryingChest", "isCarryingChest", Horse.class);
        EntityTag.register("DOMESTICATION", new String[] {"DOMESTICATE", "DOMEST", "DOM"}, new IntO().def(0), "setDomestication", "getDomestication", Horse.class);
        EntityTag.register("MAXDOMESTICATION", new String[] {"MAXDOMESTICATE", "MAXDOMEST", "MAXDOM", "MDOMESTICATION", "MDOMESTICATE", "MDOMEST", "MDOM"},
                new IntO().def(100), "setMaxDomestication", "getMaxDomestication", Horse.class);
        EntityTag.register("JUMPSTRENGTH", new String[] {"JUMPSTR", "JUMP", "JSTRENGTH", "JUMPS", "JSTR"}, new DoubleO(), "setJumpStrength", "getJumpStrength", Horse.class);
        EntityTag.register("ARMOR", new String[] {"HORSEARMOR", "HARMOR", "HORSEA"}, new ItemO(), "setHorseArmor", "getHorseArmor", Horse.class);

        //Ocelot
        EntityTag.register("CATTYPE", new String[] {"OCELOTTYPE", "CATT", "OCELOTT", "CATVARIANT", "OCELOTVARIANT", "CATV", "OCELOTV"},
                new StringO().match(OcelotTypes.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setCatType(OcelotTypes.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return OcelotTypes.getName(entity.getCatType());
            }
        }, Ocelot.class);

        //Rabbit
        EntityTag.register("RABBITTYPE", new String[] {"BUNNYTYPE", "BUNNYT", "RABBITT", "BUNNYVARIANT", "RABBITVARIANT", "BUNNYV", "RABBITV"},
                new StringO().match(RabbitTypes.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setRabbitType(RabbitTypes.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return RabbitTypes.getName(entity.getRabbitType());
            }
        }, Rabbit.class);

        //Pigman
        EntityTag.register("ANGER", new String[] {"ANGERLEVEL"}, new IntO().def(0), "setAnger", "getAnger", PigZombie.class);

        //Sheep
        EntityTag.register("SHEARED", new String[] {"SHEAR"}, new BoolO().def(false), "setSheared", "isSheared", Sheep.class);

        //Skeleton
        EntityTag.register("WITHER", new String[] {"WITHERSKELETON", "WSKELETON", "WITHERS", "WITHERSKELI", "WSKELI"}, new BoolO().def(false), "setWitherSkeleton", "isWitherSkeleton", Skeleton.class);

        //Slime
        EntityTag.register("SIZE", new String[] {"SLIMESIZE"}, new IntO(), "setSize", "getSize", Slime.class);

        //IronGolem
        EntityTag.register("PLAYERCREATED", new String[] {"PCREATED", "CREATED", "PLAYERC"}, new BoolO().def(false), "setPlayerCreated", "isPlayerCreated", IronGolem.class);

        //Tags
        EntityTag.register("NOAI", new String[] {"REMOVEAI"}, new BoolO().def(false), "setNoAI", "hasNoAI", Entity.class);
        EntityTag.register("INVULNERABLE", new String[] {"GODMODE", "GOD", "INVUL"}, new BoolO().def(false), "setInvulnerable", "isInvulnerable", Entity.class);
        EntityTag.register("SILENT", new String[] {"NOSOUND", "REMOVESOUND"}, new BoolO().def(false), "setSilent", "isSilent", Entity.class);
        EntityTag.register("PERSISTENT", new String[] {"PERSIST"}, new BoolO().def(false), "setPersistent", "isPersistent", Entity.class);
    }
}
