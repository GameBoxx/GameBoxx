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

    private final String tag;
    private final SingleOption option;
    private final EntityTagCallback callback;
    private final String setMethod;
    private final String getMethod;
    private final Class<? extends Entity>[] entities;

    private EntityTag(String tag, SingleOption option, String setMethod, String getMethod, Class... entities) {
        this.tag = tag;
        option.name(tag);
        this.option = option;
        this.callback = null;
        this.setMethod = setMethod;
        this.getMethod = getMethod;
        this.entities = entities;
    }

    private EntityTag(String tag, SingleOption option, EntityTagCallback callback, Class... entities) {
        this.tag = tag;
        option.name(tag);
        this.option = option;
        this.callback = callback;
        this.setMethod = null;
        this.getMethod = null;
        this.entities = entities;
    }


    public String getTag() {
        return tag;
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


    public static EntityTag register(String tag, SingleOption option, EntityTagCallback executeCallback, Class... entities) {
        return register(new EntityTag(tag, option, executeCallback, entities));
    }

    private static EntityTag register(String tag, SingleOption option, String setMethod, String getMethod, Class... entities) {
        return register(new EntityTag(tag, option, setMethod, getMethod, entities));
    }

    private static EntityTag register(EntityTag tag) {
        String key = tag.getTag().toUpperCase().replace("_", "").replace(" ", "");
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


    public static void registerDefaults() {
        //TODO: More modfiers for all options like min/max values etc.
        //TODO: More custom tags,
        //TODO: Attributes
    
        //All entities
        EntityTag.register("VELOCITY", new VectorOption(), "setVelocity", null, Entity.class);
        EntityTag.register("FALLDISTANCE", new IntOption(), "setFallDistance", null, Entity.class);
        EntityTag.register("FIRETICKS", new IntOption().def(-1), "setFireTicks", "getFireTicks", Entity.class);
        EntityTag.register("LIVED", new IntOption(), "setTicksLived", null, Entity.class);
        EntityTag.register("NAME", new StringOption(), "setCustomName", "getCustomName", Entity.class);
        EntityTag.register("NAMEVISIBLE", new BoolOption().def(false), "setCustomNameVisible", "isCustomNameVisible", Entity.class);
        EntityTag.register("META", new StringOption().matchRegex("([^:]*?):([^:]*?)", "String must have the syntax key:value"), new EntityTagCallback() {
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
        EntityTag.register("PLAYEFFECT", new StringOption().match(Parse.StringArray(EntityEffect.values())), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.playEffect(EntityEffect.valueOf(((StringOption)result).getValue().toUpperCase()));
                return true;
            }
    
            @Override String onGet(EEntity entity) {return null;}
        }, Entity.class); //TODO: Aliases
    
        //Damagable
        EntityTag.register("MAXHEALTH", new DoubleOption(), "setMaxHealth", "getMaxHealth", Damageable.class);
        EntityTag.register("HEALTH", new DoubleOption(), "setHealth", "getHealth", Damageable.class);
    
        //Living
        EntityTag.register("AIR", new IntOption().def(300), "setRemainingAir", "getRemainingAir", LivingEntity.class);
        EntityTag.register("MAXNODMGTICKS", new IntOption().def(20), "setMaximumNoDamageTicks", "getMaximumNoDamageTicks", LivingEntity.class);
        EntityTag.register("MAXAIR", new IntOption().def(300), "setMaximumAir", "getMaximumAir", LivingEntity.class);
        EntityTag.register("NODMGTICKS", new IntOption().def(0), "setNoDamageTicks", "getNoDamageTicks", LivingEntity.class);
        EntityTag.register("HANDDROP", new DoubleOption(), "setItemInHandDropChance", null, LivingEntity.class);
        EntityTag.register("HELMETDROP", new DoubleOption(), "setHelmetDropChance", null, LivingEntity.class);
        EntityTag.register("CHESTPLATEDROP", new DoubleOption(), "setChestplateDropChance", null, LivingEntity.class);
        EntityTag.register("LEGGINGSDROP", new DoubleOption(), "setLeggingsDropChance", null, LivingEntity.class);
        EntityTag.register("BOOTSDROP", new DoubleOption(), "setBootsDropChance", null, LivingEntity.class);
        EntityTag.register("PICKUP", new BoolOption().def(false), "setCanPickupItems", "getCanPickupItems", LivingEntity.class);
        EntityTag.register("REMOVEFAR", new BoolOption().def(false), "setRemoveWhenFarAway", "getRemoveWhenFarAway", LivingEntity.class);
        //EntityTag.register(new EntityOption("LEASH"), "setLeashHolder", "getLeashHolder", LivingEntity.class);
    
        //Ageable
        EntityTag.register("AGE", new IntOption().def(0), "setAge", "getAge", Ageable.class);
        EntityTag.register("AGELOCK", new BoolOption().def(false), "setAgeLock", "getAgeLock", Ageable.class);
        EntityTag.register("BABY", new BoolOption().def(false), "setBaby", "isBaby", Ageable.class, Zombie.class);
        EntityTag.register("BREED", new BoolOption().def(true), "setBreed", "canBreed", Ageable.class);
    
        //Tamable
        EntityTag.register("TAMED", new BoolOption().def(false), "setTamed", "isTamed", Tameable.class);
        EntityTag.register("OWNER", new PlayerOption(), "setOwner", "getOwner", Tameable.class); //TODO: OfflinePlayerOption?
    
        //Creature
        //EntityTag.register("TARGET", new EntityOption("TARGET"), "setTarget", Creature.class);
    
        //Projectile
        //EntityTag.register("SHOOTER", new EntityOption("SHOOTER"), "setShooter", Projectile.class);
        EntityTag.register("BOUNCE", new BoolOption().def(true), "setBounce", null, Projectile.class);
    
        //Hanging
        EntityTag.register("DIR", new StringOption().match(Arrays.asList("north", "east", "south", "west", "up", "down")), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setFacingDirection(BlockFace.valueOf(((StringOption)result).getValue().toUpperCase()), true);
                return true;
            }
    
            @Override String onGet(EEntity entity) {return null;}
        }, Hanging.class);
    
        //Mixed Entities
        //EntityTag.register("MAINHAND", new ItemOption(), "setItemInMainHand", ArmorStand.class, LivingEntity.class);
        //EntityTag.register("OFFHAND", new ItemOption(), "setItemInOffHand", ArmorStand.class, LivingEntity.class);
        //EntityTag.register("BOOTS", new ItemOption(), "setBoots", ArmorStand.class, LivingEntity.class);
        //EntityTag.register("LEGGINGS", new ItemOption(), "setLeggings", ArmorStand.class, LivingEntity.class);
        //EntityTag.register("CHESTPLATE", new ItemOption(), "setChestplate", ArmorStand.class, LivingEntity.class);
        //EntityTag.register("HELMET", new ItemOption(), "setHelmet", ArmorStand.class, LivingEntity.class);
        // EntityTag.register("ITEM", new ItemOption(), "setItem", Item.class, ItemFrame.class, ThrownPotion.class);
        EntityTag.register("SITTING", new BoolOption().def(false), "setSitting", "isSitting", Ocelot.class, Wolf.class);
        EntityTag.register("ANGRY", new BoolOption().def(false), "setAngry", "isAngry", Wolf.class, PigZombie.class);
        EntityTag.register("SADDLE", new BoolOption().def(false), "setSaddle", "hasSaddle", Horse.class, Pig.class);
        //EntityTag.register("SADDLEITEM", new ItemOption(), "setSaddle", Horse.class, Pig.class);
        EntityTag.register("COLOR", new StringOption().match(Parse.StringArray(DyeColor.values())), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setColor(DyeColor.valueOf(((StringOption)result).getValue().toUpperCase()));
                return true;
            }
    
            @Override String onGet(EEntity entity) {
                return entity.getColor().toString();
            }
        }, Colorable.class, Wolf.class); //TODO: Aliases
        //EntityTag.register("EFFECT", new PotionOption(), "addEffect", LivingEntity.class, AreaEffectCloud.class);
        EntityTag.register("PROFESSION", new StringOption().match(Parse.StringArray(Villager.Profession.values())), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setProfession(Villager.Profession.valueOf(((StringOption)result).getValue().toUpperCase()));
                return true;
            }
    
            @Override String onGet(EEntity entity) {
                return entity.getProfession() == null ? null : entity.getProfession().toString();
            }
        }, Villager.class, Zombie.class); //TODO: Aliases
    
        //ArmorStand
        EntityTag.register("POSE", new VectorOption().def(new Vector(0, 0, 0)), "setBodyPose", "getBodyPose", ArmorStand.class);
        EntityTag.register("HEAD", new VectorOption().def(new Vector(0, 0, 0)), "setHeadPose", "getHeadPose", ArmorStand.class);
        EntityTag.register("LARM", new VectorOption().def(new Vector(0, 0, 0)), "setLeftArmPose", "getLeftArmPose", ArmorStand.class);
        EntityTag.register("RARM", new VectorOption().def(new Vector(0, 0, 0)), "setRightArmPose", "getRightArmPose", ArmorStand.class);
        EntityTag.register("LLEG", new VectorOption().def(new Vector(0, 0, 0)), "setLeftLegPose", "getLeftLegPose", ArmorStand.class);
        EntityTag.register("RLEG", new VectorOption().def(new Vector(0, 0, 0)), "setRightLegPose", "getRightLegPose", ArmorStand.class);
        EntityTag.register("BASEPLATE", new BoolOption().def(true), "setBasePlate", "hasBasePlate", ArmorStand.class);
        EntityTag.register("GRAVITY", new BoolOption().def(true), "setGravity", "hasGravity", ArmorStand.class);
        EntityTag.register("VISIBLE", new BoolOption().def(true), "setVisible", "isVisible", ArmorStand.class);
        EntityTag.register("ARMS", new BoolOption().def(false), "setArms", "hasArms", ArmorStand.class);
        EntityTag.register("SMALL", new BoolOption().def(false), "setSmall", "isSmall", ArmorStand.class);
        EntityTag.register("MARKER", new BoolOption().def(false), "setMarker", "isMarker", ArmorStand.class);
    
        //Arrow
        EntityTag.register("KNOCKBACK", new IntOption(), "setKnockbackStrength", "getKnockbackStrength", Arrow.class);
        EntityTag.register("CRITICAL", new BoolOption(), "setCritical", null, Arrow.class);
    
        //AreaEffectCloud
        EntityTag.register("DURATION", new IntOption(), "setDuration", null, AreaEffectCloud.class);
        EntityTag.register("WAITTIME", new IntOption(), "setWaitTime", null, AreaEffectCloud.class);
        EntityTag.register("DELAYTICKS", new IntOption(), "setReapplicationDelay", null, AreaEffectCloud.class);
        EntityTag.register("USETICKS", new IntOption(), "setDurationOnUse", null, AreaEffectCloud.class);
        EntityTag.register("RADIUS", new DoubleOption(), "setRadius", null, AreaEffectCloud.class);
        EntityTag.register("USERADIUS", new DoubleOption(), "setRadiusOnUse", null, AreaEffectCloud.class);
        EntityTag.register("RADIUSDECAY", new DoubleOption(), "setRadiusPerTick", null, AreaEffectCloud.class);
        EntityTag.register("PARTICLE", new IntOption(), "setParticle", null, AreaEffectCloud.class);
        //EntityTag.register("COLOR", new ColorOption(), "setEffectColor", null, AreaEffectCloud.class);
    
        //Minecart
        EntityTag.register("MAXSPEED", new DoubleOption().def(0.4), "setMaxSpeed", "getMaxSpeed", Minecart.class);
        EntityTag.register("SLOWEMPTY", new BoolOption().def(true), "setSlowWhenEmpty", "isSlowWhenEmpty", Minecart.class);
        EntityTag.register("FLYVELOCITY", new VectorOption().def(new Vector(0.95, 0.95, 0.95)), "setFlyingVelocityMod", "getFlyingVelocityMod", Minecart.class);
        EntityTag.register("DERAILVELOCITY", new VectorOption().def(new Vector(0.5, 0.5, 0.5)), "setDerailedVelocityMod", "getDerailedVelocityMod", Minecart.class);
        EntityTag.register("DISPLAYBLOCK", new MaterialOption().def(new MaterialData(Material.AIR)), "setDisplayBlock", "getDisplayBlock", Minecart.class); //TODO: Block modifier
        EntityTag.register("BLOCKOFFSET", new IntOption().def(6), "setDisplayBlockOffset", "getDisplayBlockOffset", Minecart.class);

        //Cmdblock minecart
        EntityTag.register("CMD", new StringOption(), "setCommand", "getCommand", CommandMinecart.class);
        EntityTag.register("SENDER", new StringOption(), "setName", "getTag", CommandMinecart.class);

        //Experience
        EntityTag.register("EXP", new IntOption(), "setExperience", null, ExperienceOrb.class);

        //Falling block
        EntityTag.register("DROPITEM", new BoolOption().def(true), "setDropItem", null, FallingBlock.class);
        EntityTag.register("HURTENTITIES", new BoolOption().def(true), "setHurtEntities", null, FallingBlock.class);

        //Firework
        EntityTag.register("DETONATE", new BoolOption().def(true), "detonate", null, Firework.class);
        //EntityTag.register("FIREWORK", new FireworkOption(), "setFireworkMeta", Firework.class);

        //Item
        EntityTag.register("PICKUPDELAY", new IntOption(), "setPickupDelay", null, Item.class);

        //Itemframe
        EntityTag.register("ROTATION", new StringOption().match(Arrays.asList("0", "45", "90", "135", "180", "225", "270", "315", "360")), "setRotation", null, ItemFrame.class);

        //Painting
        EntityTag.register("ART", new StringOption().match(Parse.StringArray(Art.values())), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setArt(Art.valueOf(((StringOption)result).getValue().toUpperCase()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return entity.getArt().toString();
            }
        }, Painting.class); //TODO: Aliases

        //FishHook
        EntityTag.register("BITECHANCE", new DoubleOption(), "setBiteChance", null, FishHook.class);

        //Primed TnT
        EntityTag.register("FUSETICKS", new IntOption(), "setFuseTicks", null, TNTPrimed.class);

        //Explosive
        EntityTag.register("YIELD", new DoubleOption(), "setYield", null, Explosive.class);
        EntityTag.register("FIRE", new BoolOption().def(true), "setIsIncendiary", null, Explosive.class);

        //Witherskull
        EntityTag.register("CHARGED", new BoolOption().def(true), "setCharged", "isCharged", WitherSkull.class);

        //Bat
        EntityTag.register("AWAKE", new BoolOption().def(true), "setAwake", "isAwake", Bat.class);

        //Creeper
        EntityTag.register("POWERED", new BoolOption().def(true), "setPowered", "isPowered", Creeper.class);

        //Enderman
        EntityTag.register("holding", new MaterialOption(), "setCarriedMaterial", "getCarriedMaterial", Enderman.class); //TODO: Block modifier

        //Guardian
        EntityTag.register("ELDER", new BoolOption().def(false), "setElder", "isElder", Guardian.class);

        //Horse
        EntityTag.register("HORSEVARIANT", new StringOption().def("HORSE").match(Parse.StringArray(Horse.Variant.values())), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setHorseVariant(Horse.Variant.valueOf(((StringOption)result).getValue().toUpperCase()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return entity.getHorseVariant().toString();
            }
        }, Horse.class); //TODO: Aliases
        EntityTag.register("HORSECOLOR", new StringOption().match(Parse.StringArray(Horse.Color.values())), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setHorseColor(Horse.Color.valueOf(((StringOption)result).getValue().toUpperCase()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return entity.getHorseColor().toString();
            }
        }, Horse.class); //TODO: Aliases
        EntityTag.register("HORSESTYLE", new StringOption().match(Parse.StringArray(Horse.Style.values())), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setHorseStyle(Horse.Style.valueOf(((StringOption)result).getValue().toUpperCase()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return entity.getHorseStyle().toString();
            }
        }, Horse.class); //TODO: Aliases
        EntityTag.register("CHEST", new BoolOption().def(false), "setCarryingChest", "isCarryingChest", Horse.class);
        EntityTag.register("DOMESTICATION", new IntOption().def(0), "setDomestication", "getDomestication", Horse.class);
        EntityTag.register("MAXDOMESTICATION", new IntOption().def(100), "setMaxDomestication", "getMaxDomestication", Horse.class);
        EntityTag.register("JUMPSTRENGTH", new DoubleOption(), "setJumpStrength", "getJumpStrength", Horse.class);
        //ARMOR = EntityTag.register("ARMOR", new ItemOption(), "setHorseArmor", "getHorseArmor", Horse.class);

        //Ocelot
        EntityTag.register("CATTYPE", new StringOption().match(Parse.StringArray(Ocelot.Type.values())), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setCatType(Ocelot.Type.valueOf(((StringOption)result).getValue().toUpperCase()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return entity.getCatType().toString();
            }
        }, Ocelot.class); //TODO: Aliases

        //Rabbit
        EntityTag.register("RABBITTYPE", new StringOption().match(Parse.StringArray(Rabbit.Type.values())), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setRabbitType(Rabbit.Type.valueOf(((StringOption)result).getValue().toUpperCase()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return entity.getRabbitType().toString();
            }
        }, Rabbit.class); //TODO: Aliases

        //Pigman
        EntityTag.register("ANGER", new IntOption().def(0), "setAnger", "getAnger", PigZombie.class);

        //Sheep
        EntityTag.register("SHEARED", new BoolOption().def(false), "setSheared", "isSheared", Sheep.class);

        //Skeleton
        EntityTag.register("WITHER", new BoolOption().def(false), "setWitherSkeleton", "isWitherSkeleton", Skeleton.class);

        //Slime
        EntityTag.register("SIZE", new IntOption(), "setSize", "getSize", Slime.class);

        //IronGolem
        EntityTag.register("PLAYERCREATED", new BoolOption().def(false), "setPlayerCreated", "isPlayerCreated", IronGolem.class);

        //Tags
        EntityTag.register("NOAI", new BoolOption().def(false), "setNoAI", "hasNoAI", Entity.class);
        EntityTag.register("INVULNERABLE", new BoolOption().def(false), "setInvulnerable", "isInvulnerable", Entity.class);
        EntityTag.register("SILENT", new BoolOption().def(false), "setSilent", "isSilent", Entity.class);
        EntityTag.register("PERSISTENT", new BoolOption().def(false), "setPersistent", "isPersistent", Entity.class);
    }
}
