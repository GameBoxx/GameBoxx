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
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionData;
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

    public static Map<String, List<String>> getTagsMap(EntityType type) {
        Map<String, List<String>> tagMap = new HashMap<>();
        Collection<EntityTag> tagList = type == null ? values() : getTags(type);
        for (EntityTag tag : tagList) {
            tagMap.put(tag.getTag(), tag.getAliases() == null || tag.getAliases().length < 1 ? new ArrayList<String>() : Arrays.asList(tag.getAliases()));
        }
        return tagMap;
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
        EntityTag.register("Velocity", new String[] {"Vel"}, new VectorO(), "setVelocity", null, Entity.class);
        EntityTag.register("FallDistance", new String[] {"Fall", "FallD", "FDistance", "FD"}, new IntO(), "setFallDistance", null, Entity.class);
        EntityTag.register("FireTicks", new String[] {"FTicks", "FireT", "FT"}, new IntO().def(-1), "setFireTicks", "getFireTicks", Entity.class);
        EntityTag.register("Lived", new String[] {"TicksLived", "TLived", "TL"}, new IntO(), "setTicksLived", null, Entity.class);
        EntityTag.register("Name", new String[] {"DisplayName", "DN", "CustomName", "CName", "DName", "CN"}, new StringO(), "setCustomName", "getCustomName", Entity.class);
        EntityTag.register("NameVisible", new String[] {"NameV", "NVisible", "NameVis", "NVis"}, new BoolO().def(false), "setCustomNameVisible", "isCustomNameVisible", Entity.class);
        EntityTag.register("Meta", null, new StringO().matchRegex("([^:]*?):([^:]*?)", "key:value"), new EntityTagCallback() {
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
        EntityTag.register("MaxHealth", new String[] {"MaxHP", "MHealth", "MHP"}, new DoubleO(), "setMaxHealth", "getMaxHealth", Damageable.class);
        EntityTag.register("Health", new String[] {"HP"}, new DoubleO(), "setHealth", "getHealth", Damageable.class);

        //Living
        EntityTag.register("Air", new String[] {"Oxygen", "Oxy"}, new IntO().def(300), "setRemainingAir", "getRemainingAir", LivingEntity.class);
        EntityTag.register("MaxAir", new String[] {"MaxOxygen", "MaxOxy", "MAir", "MOxygen", "MOxy"}, new IntO().def(300), "setMaximumAir", "getMaximumAir", LivingEntity.class);
        EntityTag.register("NoDmgTicks", new String[] {"NoDmg", "NoDamageTicks"}, new IntO().def(0), "setNoDamageTicks", "getNoDamageTicks", LivingEntity.class);
        EntityTag.register("MaxNoDmgTicks", new String[] {"MaxNoDamage", "MaxNoDmg", "MNoDmgTicks", "MNoDmg"}, new IntO().def(20), "setMaximumNoDamageTicks", "getMaximumNoDamageTicks", LivingEntity.class);
        EntityTag.register("MainHandDrop", new String[] {"MainHandD", "MainHandDropChance", "MainHandDC", "MainHandC", "MHandD", "MHandDC", "MHandC", "MainHD", "MainHDC", "MainHC", "MHandDrop"},
                new DoubleO(), "setItemInMainHandDropChance", null, LivingEntity.class);
        EntityTag.register("OffHandDrop", new String[] {"OffHandD", "OffHandDropChance", "OffHandDC", "OffHandC", "OHandD", "OHandDC", "OHandC", "OffHD", "OffHDC", "OffHC", "OHandDrop"},
                new DoubleO(), "setItemInOffHandDropChance", null, LivingEntity.class);
        EntityTag.register("HelmetDrop", new String[] {"HelmetD", "HelmetDropChance", "HelmetDC", "HelmetC",
                "HelmDrop", "HelmD", "HelmetDropChance", "HelmDC", "HelmC"}, new DoubleO(), "setHelmetDropChance", null, LivingEntity.class);
        EntityTag.register("ChestplateDrop", new String[] {"ChestplateD", "ChestplateDropChance", "ChestplateDC", "ChestplateC",
                "ChestDrop", "ChestD", "ChestDropChance", "ChestDC", "ChestC"}, new DoubleO(), "setChestplateDropChance", null, LivingEntity.class);
        EntityTag.register("LeggingsDrop", new String[] {"LeggingsD", "LeggingsDropChance", "LEggingsDC", "LeggingsC",
                "LegDrop", "LegD", "LegDropChance", "LegDC", "LegC"}, new DoubleO(), "setLeggingsDropChance", null, LivingEntity.class);
        EntityTag.register("BootsDrop", new String[] {"BootsD", "BootsDropChance", "BootsDC", "BootsC",
                "BootDrop", "BootD", "BootDropChance", "BootDC", "BootC"}, new DoubleO(), "setBootsDropChance", null, LivingEntity.class);
        EntityTag.register("Pickup", new String[] {"ItemPickup", "CanPickup", "IPickup", "CPickup"}, new BoolO().def(false), "setCanPickupItems", "getCanPickupItems", LivingEntity.class);
        EntityTag.register("RemoveFar", new String[] {"RemoveFarAway"}, new BoolO().def(false), "setRemoveWhenFarAway", "getRemoveWhenFarAway", LivingEntity.class);
        EntityTag.register("LeashHolder", new String[] {"Leash"}, new EntityO(), "setLeashHolder", null, LivingEntity.class);
        //TODO: Fix leashes (Maybe also make it so you can create a leash between two locations)

        //Ageable
        EntityTag.register("Age", null, new IntO().def(0), "setAge", "getAge", Ageable.class);
        EntityTag.register("Agelock", new String[] {"AgeL"}, new BoolO().def(false), "setAgeLock", "getAgeLock", Ageable.class);
        EntityTag.register("Baby", null, new BoolO().def(false), "setBaby", "isBaby", Ageable.class, Zombie.class);
        EntityTag.register("CanBreed", new String[] {"Breed"}, new BoolO().def(true), "setBreed", "canBreed", Ageable.class);

        //Tamable
        EntityTag.register("Tamed", new String[] {"Tame"}, new BoolO().def(false), "setTamed", "isTamed", Tameable.class);
        EntityTag.register("Owner", null, new OfflinePlayerO(), "setOwner", "getOwner", Tameable.class);

        //Creature
        EntityTag.register("Target", null, new EntityO(), "setTarget", null, Creature.class, ShulkerBullet.class);

        //Projectile
        EntityTag.register("Shooter", null, new EntityO(), "setShooter", null, Projectile.class, ShulkerBullet.class);
        EntityTag.register("Bounce", null, new BoolO().def(true), "setBounce", null, Projectile.class);

        //Hanging
        EntityTag.register("Dir", null, new StringO().match(Directions.getAliasMap(Directions.Type.SIX_SELF)), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setFacingDirection(Directions.get(((StringO)result).getValue()), true);
                return true;
            }

            @Override String onGet(EEntity entity) {return null;}
        }, Hanging.class);

        //Mixed Entities
        EntityTag.register("MainHand", new String[] {"MHand", "Hand", "MainH"}, new ItemO(), "setItemInMainHand", "getItemInMainHand", ArmorStand.class, LivingEntity.class);
        EntityTag.register("OffHand", new String[] {"OHand", "OffH"}, new ItemO(), "setItemInOffHand", "getItemInOffHand", ArmorStand.class, LivingEntity.class);
        EntityTag.register("Boots", new String[] {"Boot"}, new ItemO(), "setBoots", "getBoots", ArmorStand.class, LivingEntity.class);
        EntityTag.register("Leggings", new String[] {"Legging", "Leg"}, new ItemO(), "setLeggings", "getLeggings", ArmorStand.class, LivingEntity.class);
        EntityTag.register("Chestplate", new String[] {"CPlate", "ChestP"}, new ItemO(), "setChestplate", "getChestplate", ArmorStand.class, LivingEntity.class);
        EntityTag.register("Helmet", new String[] {"Helm"}, new ItemO(), "setHelmet", "getHelmet", ArmorStand.class, LivingEntity.class);
        EntityTag.register("Item", null, new ItemO(), "setItem", "getItem", Item.class, ItemFrame.class, ThrownPotion.class);
        EntityTag.register("Sitting", new String[] {"Sit"}, new BoolO().def(false), "setSitting", "isSitting", Ocelot.class, Wolf.class);
        EntityTag.register("Angry", null, new BoolO().def(false), "setAngry", "isAngry", Wolf.class, PigZombie.class);
        EntityTag.register("Saddle", null, new BoolO().def(false), "setSaddle", "hasSaddle", Horse.class, Pig.class);
        EntityTag.register("SaddleItem", new String[] {"SaddleI"}, new ItemO(), "setSaddle", "getSaddle", Horse.class, Pig.class);
        EntityTag.register("Color", new String[] {"Clr", "DyeColor", "DyeClr"}, new StringO().match(DyeColors.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setColor(DyeColors.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return DyeColors.getName(entity.getColor());
            }
        }, Colorable.class, Wolf.class);
        EntityTag.register("Potion", new String[] {"Pot", "PotionType", "PotType", "PType", "PotT", "PotionT", "PT"}, new StringO().match(PotionTypes.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setBasePotionData(new PotionData(PotionTypes.get(((StringO)result).getValue())));
                return true;
            }

            @Override String onGet(EEntity entity) {return null;}
        }, TippedArrow.class, AreaEffectCloud.class);
        EntityTag.register("Effect", new String[] {"Eff", "E", "PEffect", "PotionEffect", "PotEffect", "PotionE", "PotE", "PE"},
                new PotionO(), "addEffect", null, LivingEntity.class, AreaEffectCloud.class, ThrownPotion.class, TippedArrow.class);
        EntityTag.register("Profession", new String[] {"Prof", "Villager"}, new StringO().match(Professions.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setProfession(Professions.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return Professions.getName(entity.getProfession());
            }
        }, Villager.class, Zombie.class);

        //ArmorStand
        EntityTag.register("Pose", new String[] {"BodyPose", "BPose"}, new VectorO().def(new Vector(0, 0, 0)), "setBodyPose", "getBodyPose", ArmorStand.class);
        EntityTag.register("Head", new String[] {"HeadPose", "HPose"}, new VectorO().def(new Vector(0, 0, 0)), "setHeadPose", "getHeadPose", ArmorStand.class);
        EntityTag.register("LArm", new String[] {"LeftArm", "LeftArmPose", "LArmPose"}, new VectorO().def(new Vector(0, 0, 0)), "setLeftArmPose", "getLeftArmPose", ArmorStand.class);
        EntityTag.register("RArm", new String[] {"RightArm", "RightArmPose", "RArmPose"}, new VectorO().def(new Vector(0, 0, 0)), "setRightArmPose", "getRightArmPose", ArmorStand.class);
        EntityTag.register("LLeg", new String[] {"LeftLeg", "LeftLegPose", "LLegPose"}, new VectorO().def(new Vector(0, 0, 0)), "setLeftLegPose", "getLeftLegPose", ArmorStand.class);
        EntityTag.register("RLeg", new String[] {"RightLeg", "RightLegPose", "RLegPose"}, new VectorO().def(new Vector(0, 0, 0)), "setRightLegPose", "getRightLegPose", ArmorStand.class);
        EntityTag.register("Baseplate", new String[] {"Plate"}, new BoolO().def(true), "setBasePlate", "hasBasePlate", ArmorStand.class);
        EntityTag.register("Gravity", new String[] {"Grav"}, new BoolO().def(true), "setGravity", "hasGravity", ArmorStand.class);
        EntityTag.register("Visible", new String[] {"Vis"}, new BoolO().def(true), "setVisible", "isVisible", ArmorStand.class);
        EntityTag.register("Arms", new String[] {"Arm"}, new BoolO().def(false), "setArms", "hasArms", ArmorStand.class);
        EntityTag.register("Small", null, new BoolO().def(false), "setSmall", "isSmall", ArmorStand.class);
        EntityTag.register("Marker", null, new BoolO().def(false), "setMarker", "isMarker", ArmorStand.class);

        //Arrow
        EntityTag.register("Knockback", new String[] {"KnockB", "KBack"}, new IntO(), "setKnockbackStrength", "getKnockbackStrength", Arrow.class);
        EntityTag.register("Critical", new String[] {"Crit"}, new BoolO(), "setCritical", null, Arrow.class);

        //AreaEffectCloud
        EntityTag.register("Duration", new String[] {"Dur"}, new IntO(), "setDuration", null, AreaEffectCloud.class);
        EntityTag.register("WaitTime", new String[] {"Wait", "WaitDelay", "StartDelay"}, new IntO(), "setWaitTime", null, AreaEffectCloud.class);
        EntityTag.register("DelayTicks", new String[] {"DelayT", "ReapplicationDelay", "UseDelay", "Cooldown", "CD"}, new IntO(), "setReapplicationDelay", null, AreaEffectCloud.class);
        EntityTag.register("UseTicks", new String[] {"UseDuration", "UseT"}, new IntO(), "setDurationOnUse", null, AreaEffectCloud.class);
        EntityTag.register("Radius", new String[] {"Rad"}, new DoubleO(), "setRadius", null, AreaEffectCloud.class);
        EntityTag.register("UseRadius", new String[] {"UseR"}, new DoubleO(), "setRadiusOnUse", null, AreaEffectCloud.class);
        EntityTag.register("RadiusDecay", new String[] {"DecayR", "Decay", "RadiusPerTick", "RadDecay", "RadPerTick"}, new DoubleO(), "setRadiusPerTick", null, AreaEffectCloud.class);
        EntityTag.register("Particle", null, new StringO().match(Particles.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setParticle(Particles.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {return null;}
        }, AreaEffectCloud.class);
        EntityTag.register("ParticleColor", new String[] {"PColor", "ParticleC"}, new ColorO(), "setEffectColor", null, AreaEffectCloud.class);

        //Minecart
        EntityTag.register("MaxSpeed", new String[] {"MSpeed"}, new DoubleO().def(0.4), "setMaxSpeed", "getMaxSpeed", Minecart.class);
        EntityTag.register("SlowEmpty", new String[] {"SlowWhenEmpty", "SlowDecel", "Decel", "Decelerate"}, new BoolO().def(true), "setSlowWhenEmpty", "isSlowWhenEmpty", Minecart.class);
        EntityTag.register("FlyVelocity", new String[] {"FlyVel", "FlyVelocity", "FVel"}, new VectorO().def(new Vector(0.95, 0.95, 0.95)), "setFlyingVelocityMod", "getFlyingVelocityMod", Minecart.class);
        EntityTag.register("DerailVelocity", new String[] {"DerailVel", "DVelocity", "DVel"}, new VectorO().def(new Vector(0.5, 0.5, 0.5)), "setDerailedVelocityMod", "getDerailedVelocityMod", Minecart.class);
        EntityTag.register("DisplayBlock", new String[] {"DBlock"}, new MaterialO().blocks(true).def(new MaterialData(Material.AIR)), "setDisplayBlock", "getDisplayBlock", Minecart.class);
        EntityTag.register("BlockOffset", new String[] {"BOffset"}, new IntO().def(6), "setDisplayBlockOffset", "getDisplayBlockOffset", Minecart.class);

        //Cmdblock minecart
        EntityTag.register("Cmd", new String[] {"Command"}, new StringO(), "setCommand", "getCommand", CommandMinecart.class);
        EntityTag.register("Sender", new String[] {"CmdSender", "CommandSender"}, new StringO(), "setName", "getTag", CommandMinecart.class);

        //Experience
        EntityTag.register("Exp", new String[] {"Experience", "XP"}, new IntO(), "setExperience", null, ExperienceOrb.class);

        //Falling block
        EntityTag.register("DropItem", null, new BoolO().def(true), "setDropItem", null, FallingBlock.class);
        EntityTag.register("HurtEntities", new String[] {"Hurt"}, new BoolO().def(true), "setHurtEntities", null, FallingBlock.class);

        //Firework
        EntityTag.register("Detonate", new String[] {"Det"}, new BoolO().def(true), "detonate", null, Firework.class);
        EntityTag.register("Firework", new String[] {"FW", "FEffect", "FireworkMeta", "FMeta", "FWEffect", "FWMeta"}, new FireworkO(), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                FireworkMeta meta = entity.getFireworkMeta();
                meta.addEffect(((FireworkO)result).getValue());
                entity.setFireworkMeta(meta);
                return true;
            }

            @Override String onGet(EEntity entity) {return null;}
        }, Firework.class);

        //Item
        EntityTag.register("PickupDelay", new String[] {"PDelay", "Delay", "PickupD"}, new IntO(), "setPickupDelay", null, Item.class);

        //Itemframe
        EntityTag.register("Rotation", new String[] {"Rot"}, new StringO().match(Rotations.getAliasMap()),new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setRotation(Rotations.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {return null;}
        }, ItemFrame.class);

        //Painting
        EntityTag.register("Art", new String[] {"Painting", "Paint", "Canvas", "Motive"}, new StringO().match(Paintings.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setArt(Paintings.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return Paintings.getName(entity.getArt());
            }
        }, Painting.class);

        //FishHook
        EntityTag.register("BiteChance", new String[] {"Bite", "BiteC", "BChance"}, new DoubleO(), "setBiteChance", null, FishHook.class);

        //Primed TnT
        EntityTag.register("FuseTicks", new String[] {"FuseT", "Fuse"}, new IntO(), "setFuseTicks", null, TNTPrimed.class);

        //Explosive
        EntityTag.register("Yield", new String[] {"ExplosionSize", "ExplosionRadius", "ExplodeSize", "ExplodeRadius", "ExplosionS", "ExplosionR"}, new DoubleO(), "setYield", null, Explosive.class);
        EntityTag.register("FIRE", new String[] {"ExplodeFire", "ExplosionFire"}, new BoolO().def(true), "setIsIncendiary", null, Explosive.class);

        //Witherskull
        EntityTag.register("Charged", new String[] {"Charge"}, new BoolO().def(true), "setCharged", "isCharged", WitherSkull.class);

        //Bat
        EntityTag.register("Awake", null, new BoolO().def(true), "setAwake", "isAwake", Bat.class);

        //Creeper
        EntityTag.register("Powered", new String[] {"Lightning"}, new BoolO().def(true), "setPowered", "isPowered", Creeper.class);

        //Enderman
        EntityTag.register("Holding", new String[] {"Hold", "Carrying", "Cary"}, new MaterialO(), "setCarriedMaterial", "getCarriedMaterial", Enderman.class); //TODO: Block modifier

        //Guardian
        EntityTag.register("Elder", new String[] {"Eldar"}, new BoolO().def(false), "setElder", "isElder", Guardian.class);

        //Horse
        EntityTag.register("HorseVariant", new String[] {"HVariant", "Variant", "HorseV", "HorseType", "HType", "HorseT"},
                new StringO().def("HORSE").match(HorseVariants.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setHorseVariant(HorseVariants.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return HorseVariants.getName(entity.getHorseVariant());
            }
        }, Horse.class);
        EntityTag.register("HorseColor", new String[] {"HColor", "HorseC"}, new StringO().match(HorseColors.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setHorseColor(HorseColors.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return HorseColors.getName(entity.getHorseColor());
            }
        }, Horse.class);
        EntityTag.register("HorseStyle", new String[] {"HStyle", "HorseS", "HorsePattern", "HorseP", "HPattern"}, new StringO().match(HorseStyles.getAliasMap()), new EntityTagCallback() {
            @Override boolean onSet(CommandSender sender, EEntity entity, SingleOption result) {
                entity.setHorseStyle(HorseStyles.get(((StringO)result).getValue()));
                return true;
            }

            @Override String onGet(EEntity entity) {
                return HorseStyles.getName(entity.getHorseStyle());
            }
        }, Horse.class);
        EntityTag.register("CaryingChest", new String[] {"Chest"}, new BoolO().def(false), "setCarryingChest", "isCarryingChest", Horse.class);
        EntityTag.register("Domestication", new String[] {"Domesticate", "Domest", "Dom"}, new IntO().def(0), "setDomestication", "getDomestication", Horse.class);
        EntityTag.register("MaxDomestication", new String[] {"MaxDomesticate", "MaxDomest", "MaxDom", "MDomestication", "MDomesticate", "MDomest", "MDom"},
                new IntO().def(100), "setMaxDomestication", "getMaxDomestication", Horse.class);
        EntityTag.register("JumpStrength", new String[] {"JumpStr", "Jump", "JStrength", "JumpS", "JSTr"}, new DoubleO(), "setJumpStrength", "getJumpStrength", Horse.class);
        EntityTag.register("Armor", new String[] {"HorseArmor", "HArmor", "HorseA"}, new ItemO(), "setHorseArmor", "getHorseArmor", Horse.class);

        //Ocelot
        EntityTag.register("CatType", new String[] {"OcelotType", "CatT", "OcelotT", "CatVariant", "OcelotVariant", "CatV", "OcelotV"},
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
        EntityTag.register("RabbitType", new String[] {"BunnyType", "BunnyT", "RabbitT", "BunnyVariant", "RabbitVariant", "BunnyV", "RabbitV"},
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
        EntityTag.register("Anger", new String[] {"AngerLevel"}, new IntO().def(0), "setAnger", "getAnger", PigZombie.class);

        //Sheep
        EntityTag.register("Sheared", new String[] {"Shear"}, new BoolO().def(false), "setSheared", "isSheared", Sheep.class);

        //Skeleton
        EntityTag.register("Wither", new String[] {"WitherSkeleton", "WSkeleton", "Withers", "WitherSkeli", "WSkeli"}, new BoolO().def(false), "setWitherSkeleton", "isWitherSkeleton", Skeleton.class);

        //Slime
        EntityTag.register("Size", new String[] {"SlimeSize"}, new IntO(), "setSize", "getSize", Slime.class);

        //IronGolem
        EntityTag.register("PlayerCreated", new String[] {"PCreated", "Created", "PlayerC"}, new BoolO().def(false), "setPlayerCreated", "isPlayerCreated", IronGolem.class);

        //Tags
        EntityTag.register("NoAI", new String[] {"RemoveAI"}, new BoolO().def(false), "setNoAI", "hasNoAI", Entity.class);
        EntityTag.register("Invulnerable", new String[] {"GodMode", "God", "Invul"}, new BoolO().def(false), "setInvulnerable", "isInvulnerable", Entity.class);
        EntityTag.register("Silent", new String[] {"NoSound", "RemoveSound"}, new BoolO().def(false), "setSilent", "isSilent", Entity.class);
        EntityTag.register("Persistent", new String[] {"Persist"}, new BoolO().def(false), "setPersistent", "isPersistent", Entity.class);
    }
}
