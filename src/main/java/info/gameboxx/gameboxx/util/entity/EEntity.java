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

import info.gameboxx.gameboxx.nms.NMS;
import info.gameboxx.gameboxx.nms.entity.EntityUtils;
import info.gameboxx.gameboxx.util.Str;
import info.gameboxx.gameboxx.util.item.EItem;
import org.bukkit.*;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Custom {@link Entity} which allows building entities easily.
 * It's mainly used for the {@link EntityParser}
 * <p/>
 * It has methods for all entities in a single class so you don't have to cast entities all the time.
 * Calling a method on an invalid entity just does nothing.
 * For example if you try to set the villager type on a chicken nothing will happen.
 * <p/>
 * <b>There are not a ton of checks for all the methods!</b>
 * It assumes the correct input for most method like non null values and such.
 * It will just throw NPE's and other exceptions when using invalid input.
 * <p/>
 * Most of the javadoc comments are copied from the Bukkit/Spigot javadocs.
 */
public class EEntity {

    private final Entity entity;


    //region Construct

    /**
     * Create a new EEntity from a Bukkit {@link Entity}.
     * <p/>
     * <b>This will use the entity provided!</b>
     * It will not spawn a new entity.
     * <p/>
     * <b>Note: If the provided entity is {@oode null} you can't do anything with this entity!</b>
     * Use {@link #isValid()} to validate that the entity is valid or not.
     *
     * @param entity A Bukkit {@link Entity}.
     */
    public EEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * Spawns a <b>new</b> entity at the specified location.
     *
     * @param type The type of the entity.
     * @param location The location where to spawn the entity.
     */
    public EEntity(EntityType type, Location location) {
        this(EEntity.spawn(type, location));
    }

    /**
     * Spawns a <b>new</b> {@link FallingBlock} entity at the specified location.
     *
     * @param location The location where to spawn the entity.
     * @param fallingBlockMaterial MaterialData used for the falling block.
     */
    public EEntity(Location location, MaterialData fallingBlockMaterial) {
        this(location.getWorld().spawnFallingBlock(location, fallingBlockMaterial.getItemType(), fallingBlockMaterial.getData()));
    }

    /**
     * Spawns a <b>new</b> {@link Item} entity at the specified location.
     *
     * @param location The location where to spawn the entity.
     * @param item The item to spawn
     */
    public EEntity(Location location, EItem item) {
        this(location.getWorld().dropItem(location, item));
    }

    private static Entity spawn(EntityType type, Location location) {
        try {
            if (type == EntityType.DROPPED_ITEM) {
                return location.getWorld().dropItem(location, new EItem(Material.STONE));
            } else if (type == EntityType.ITEM_FRAME || type == EntityType.PAINTING) {
                return location.getWorld().spawn(location.getBlock().getLocation(), type.getEntityClass());
            } else {
                return location.getWorld().spawn(location, type.getEntityClass());
            }
        } catch (Exception e) {
            return null;
        }
    }
    //endregion



    // ##################################################
    // ################## UTILS/CUSTOM ##################
    // ##################################################
    //region Utils

    //region Tags

    public EEntity setNoAI(boolean state) {
        NMS.get().getEntityUtils().setTag(bukkit(), EntityUtils.Tag.NOAI, state);
        return this;
    }

    public boolean hasNoAI() {
        return NMS.get().getEntityUtils().getTag(bukkit(), EntityUtils.Tag.NOAI);
    }

    public EEntity setSilent(boolean state) {
        NMS.get().getEntityUtils().setTag(bukkit(), EntityUtils.Tag.SILENT, state);
        return this;
    }

    public boolean isSilent() {
        return NMS.get().getEntityUtils().getTag(bukkit(), EntityUtils.Tag.SILENT);
    }

    public EEntity setInvulnerable(boolean state) {
        NMS.get().getEntityUtils().setTag(bukkit(), EntityUtils.Tag.INVULNERABLE, state);
        return this;
    }

    public boolean isInvulnerable() {
        return NMS.get().getEntityUtils().getTag(bukkit(), EntityUtils.Tag.INVULNERABLE);
    }

    public EEntity setPersistent(boolean state) {
        NMS.get().getEntityUtils().setTag(bukkit(), EntityUtils.Tag.PERSISTENT, state);
        return this;
    }

    public boolean isPersistent() {
        return NMS.get().getEntityUtils().getTag(bukkit(), EntityUtils.Tag.PERSISTENT);
    }
    //endregion


    //endregion



    // ##################################################
    // ################## ALL ENTITIES ##################
    // ##################################################
    //region ALL ENTITIES
    //region General

    /**
     * Get the Bukkit {@link Entity} instance.
     *
     * @return Bukkit {@link Entity} instance.
     */
    public Entity bukkit() {
        return entity;
    }

    /**
     * Gets the {@link Server} that contains this entity.
     *
     * @return server instance running this entity
     */
    public Server getServer() {
        return entity.getServer();
    }

    /**
     * Returns false if the entity has died or been despawned for some other reason.
     * <p/>
     * It also does {@code null} check on the stored Bukkit {@link Entity} used for this {@link EEntity}.
     *
     * @return Whether or not this EEntity has a valid entity.
     */
    public boolean isValid() {
        return entity != null && entity.isValid();
    }

    /**
     * Get the type of the entity.
     *
     * @return the {@link EntityType}
     */
    public EntityType getType() {
        return entity.getType();
    }

    /**
     * Get the unique and persistent id for this entity
     *
     * @return unique persistent {@link UUID}
     */
    public UUID getUUID() {
        return entity.getUniqueId();
    }

    /**
     * Returns a unique id for this entity
     * <p/>
     * <b>This ID is not persistent!</b> {@link #getUUID()}
     *
     * @return entity ID
     */
    public int getID() {
        return entity.getEntityId();
    }

    /**
     * Mark the entity to be removed.
     */
    public void remove() {
        entity.remove();
    }

    /**
     * Returns true if this entity has been marked for removal.
     *
     * @return True if it's dead
     */
    public Boolean isDead() {
        return entity.isDead();
    }

    /**
     * Returns true if the entity is supported by a block.
     * This value is a state updated by the server and is not recalculated unless the entity moves.
     *
     * @return True if the entity is on the ground
     */
    public Boolean isGrounded() {
        return entity.isOnGround();
    }

    /**
     * Perform the specified {@link EntityEffect} for this entity.
     * <p/>
     * This will be viewable for all players nearby the entity.
     *
     * @param effect The {@link EntityEffect} to play
     * @return this instance
     */
    public EEntity playEffect(EntityEffect effect) {
        entity.playEffect(effect);
        return this;
    }
    //endregion


    //region Location

    /**
     * Gets the entity's current position
     *
     * @return a new copy of Location containing the position of this entity
     */
    public Location getLocation() {
        return entity.getLocation();
    }

    /**
     * Stores the entity's current position in the provided Location object.
     * If the provided Location is null this method does nothing and returns null.
     *
     * @param loc the location to copy into
     * @return The Location object provided or null
     */
    public Location getLocation(Location loc) {
        return entity.getLocation(loc);
    }

    /**
     * Gets the current world this entity resides in
     *
     * @return the world the entity resides in
     */
    public World getWorld() {
        return entity.getWorld();
    }

    /**
     * Returns a list of entities within a bounding box centered around the entity.
     *
     * @param x half the size of the box along the x axis.
     * @param y half the size of the box along the y axis.
     * @param z half the size of the box along the z axis.
     * @return list with entities
     */
    public List<EEntity> getNearbyEntities(double x, double y, double z) {
        List<Entity> nearby = entity.getNearbyEntities(x, y, z);
        List<EEntity> converted = new ArrayList<>();
        for (Entity e : nearby) {
            converted.add(new EEntity(e));
        }
        return converted;
    }

    /**
     * Returns a list of entities within a bounding box centered around the entity.
     *
     * @param radius half the size of the box along all axis.
     * @return list with entities
     */
    public List<EEntity> getNearbyEntities(double radius) {
        List<Entity> nearby = entity.getNearbyEntities(radius, radius, radius);
        List<EEntity> converted = new ArrayList<>();
        for (Entity e : nearby) {
            converted.add(new EEntity(e));
        }
        return converted;
    }
    //endregion


    //region Teleportation

    /**
     * Teleports this entity to the given location.
     * If this entity is riding a vehicle, it will be dismounted prior to teleportation.
     *
     * @param target Entity to teleport this entity to
     * @return this instance
     */
    public EEntity teleport(Location target) {
        new EntityStack(this).teleport(target, PlayerTeleportEvent.TeleportCause.PLUGIN);
        return this;
    }

    /**
     * Teleports this entity to the target Entity.
     * <p/>
     * It will teleport the entire stack of entities. {@link EntityStack#teleport(Location, PlayerTeleportEvent.TeleportCause)}
     *
     * @param target Entity to teleport this entity to
     * @param cause The cause of this teleportation
     * @return this instance
     */
    public EEntity teleport(Location target, PlayerTeleportEvent.TeleportCause cause) {
        new EntityStack(this).teleport(target, cause);
        return this;
    }

    /**
     * Teleports this entity to the target Entity.
     * <p/>
     * It will teleport the entire stack of entities. {@link EntityStack#teleport(Location, PlayerTeleportEvent.TeleportCause)}
     *
     * @param target Entity to teleport this entity to
     * @return this instance
     */
    public EEntity teleport(Entity target) {
        new EntityStack(this).teleport(target.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        return this;
    }


    /**
     * Teleports this entity to the target Entity.
     * <p/>
     * It will teleport the entire stack of entities. {@link EntityStack#teleport(Location, PlayerTeleportEvent.TeleportCause)}
     *
     * @param target Entity to teleport this entity to
     * @param cause The cause of this teleportation
     * @return this instance
     */
    public EEntity teleport(Entity target, PlayerTeleportEvent.TeleportCause cause) {
        new EntityStack(this).teleport(target.getLocation(), cause);
        return this;
    }

    /**
     * Teleports this entity to the target Entity.
     * <p/>
     * It will teleport the entire stack of entities. {@link EntityStack#teleport(Location, PlayerTeleportEvent.TeleportCause)}
     *
     * @param target EEntity to teleport this entity to
     * @return this instance
     */
    public EEntity teleport(EEntity target) {
        new EntityStack(this).teleport(target.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        return this;
    }

    /**
     * Teleports this entity to the target Entity.
     * <p/>
     * It will teleport the entire stack of entities. {@link EntityStack#teleport(Location, PlayerTeleportEvent.TeleportCause)}
     *
     * @param target EEntity to teleport this entity to
     * @param cause The cause of this teleportation
     * @return this instance
     */
    public EEntity teleport(EEntity target, PlayerTeleportEvent.TeleportCause cause) {
        new EntityStack(this).teleport(target.getLocation(), cause);
        return this;
    }
    //endregion


    //region Velocity
    //TODO: Method to set relative velocity

    /**
     * Sets the entity's velocity
     * <p/>
     * If the entity is a fireball it will set the direction instead.
     *
     * @param x new velocity x axis value
     * @param y new velocity y axis value
     * @param z new velocity z axis value
     * @return this instance
     */
    public EEntity setVelocity(double x, double y, double z) {
        if (entity instanceof Fireball) {
            ((Fireball)entity).setDirection(new Vector(x, y, z));
        } else {
            entity.setVelocity(new Vector(x, y, z));
        }
        return this;
    }

    /**
     * Gets the entity's current velocity
     *
     * @return {@link Vector} with the current velocity
     */
    public Vector getVelocity() {
        if (entity instanceof Vehicle) {
            return ((Vehicle)entity).getVelocity();
        } else if (entity instanceof Fireball) {
            return ((Fireball)entity).getDirection();
        }
        return entity.getVelocity();
    }

    /**
     * Sets the entity's velocity
     * <p/>
     * If the entity is a fireball it will set the direction instead.
     *
     * @param velocity new velocity for the entity
     * @return this instance
     */
    public EEntity setVelocity(Vector velocity) {
        if (entity instanceof Vehicle) {
            ((Vehicle)entity).setVelocity(velocity);
        } else if (entity instanceof Fireball) {
            ((Fireball)entity).setDirection(velocity);
        } else {
            entity.setVelocity(velocity);
        }
        return this;
    }
    //endregion


    //region Vehicles/Passengers

    /**
     * Gets the entity that is riding this entity.
     * <p/>
     * If the entity has multiple passengers, this will only return the primary passenger.
     *
     * @return entity riding this entity. May be {@code null} if there is no passenger.
     */
    public EEntity getPassenger() {
        if (entity.getPassenger() == null) {
            return null;
        }
        return new EEntity(entity.getPassenger());
    }

    /**
     * Set the passenger entity that will be riding this entity.
     *
     * @param passenger the entity that will riding this entity
     * @return this instance
     */
    public EEntity setPassenger(Entity passenger) {
        entity.setPassenger(passenger);
        return this;
    }

    /**
     * Set the passenger entity that will be riding this entity.
     *
     * @param passenger the entity that will riding this entity
     * @return this instance
     */
    public EEntity setPassenger(EEntity passenger) {
        entity.setPassenger(passenger.bukkit());
        return this;
    }

    /**
     * Get the vehicle that the entity is riding if the entity is riding a vehicle.
     * <p/>
     * A vehicle can be any type of entity!
     *
     * @return vehicle this entity is riding. May be {@code null} if the entity is not riding a vehicle.
     */
    public EEntity getVehicle() {
        if (entity.getVehicle() == null) {
            return null;
        }
        return new EEntity(entity.getVehicle());
    }

    /**
     * Set the entity as passenger of the specified vehicle.
     * <p/>
     * A vehicle can be any type of entity!
     *
     * @param vehicle The vehicle that this entity will be riding.
     * @return this instance
     */
    public EEntity setVehicle(Entity vehicle) {
        vehicle.setPassenger(entity);
        return this;
    }

    /**
     * Set the entity as passenger of the specified vehicle.
     * <p/>
     * A vehicle can be any type of entity!
     *
     * @param vehicle The vehicle that this entity will be riding.
     * @return this instance
     */
    public EEntity setVehicle(EEntity vehicle) {
        vehicle.bukkit().setPassenger(entity);
        return this;
    }

    /**
     * Check if the entity has a passenger riding this entity.
     *
     * @return true when there is a passenger riding this entity.
     */
    public Boolean hasPassenger() {
        return entity.getPassenger() != null;
    }

    /**
     * Check if the entity is riding a vehicle
     * <p/>
     * A vehicle can be any type of entity!
     *
     * @return true when the entity is riding a vehicle.
     */
    public Boolean hasVehicle() {
        return entity.getVehicle() != null;
    }

    /**
     * Leave the vehicle when riding a vehicle.
     *
     * @return this instance
     */
    public EEntity leaveVehicle() {
        entity.leaveVehicle();
        return this;
    }

    /**
     * Eject the passenger if there is one riding this entity.
     *
     * @return this instance
     */
    public EEntity eject() {
        entity.eject();
        return this;
    }
    //endregion


    //region Status

    /**
     * Returns the distance this entity has fallen
     *
     * @return distance the entity has fallen
     */
    public double getFallDistance() {
        return entity.getFallDistance();
    }

    /**
     * Sets the fall distance for this entity
     *
     * @param distance the new distance
     * @return this
     */
    public EEntity setFallDistance(double distance) {
        entity.setFallDistance((float)distance);
        return this;
    }

    /**
     * Returns the entity's current fire ticks (ticks before the entity stops being on fire).
     *
     * @return fire ticks
     */
    public int getFireTicks() {
        return entity.getFireTicks();
    }

    /**
     * Sets the entity's current fire ticks (ticks before the entity stops being on fire).
     *
     * @param ticks ticks remaining
     * @return this instance
     */
    public EEntity setFireTicks(int ticks) {
        entity.setFireTicks(ticks);
        return this;
    }

    /**
     * Returns the entity's maximum fire ticks.
     *
     * @return max fire ticks
     */
    public int getMaxFireTicks() {
        return entity.getMaxFireTicks();
    }

    /**
     * Gets the amount of ticks this entity has lived for.
     * This is the equivalent to age in entities.
     *
     * @return amount of ticks the entity lived
     */
    public int getTicksLived() {
        return entity.getTicksLived();
    }

    /**
     * Sets the amount of ticks this entity has lived for.
     * This is the equivalent to age in entities.
     *
     * @param ticks amount of ticks the entity lived
     * @return this instance
     */
    public EEntity setTicksLived(int ticks) {
        entity.setTicksLived(Math.max(1, ticks));
        return this;
    }
    //endregion


    //region Damage Cause

    /**
     * Retrieve the last {@link EntityDamageEvent} inflicted on this entity.
     * This event may have been cancelled.
     *
     * @return the last known {@link EntityDamageEvent} or {@code null} when not damaged.
     */
    public EntityDamageEvent getLastDamageCause() {
        return entity.getLastDamageCause();
    }

    /**
     * Set the last {@link EntityDamageEvent} to record the last damage inflicted on the entity.
     *
     * @param event a {@link EntityDamageEvent}
     * @return this instance
     */
    public EEntity setLastDamageCause(EntityDamageEvent event) {
        entity.setLastDamageCause(event);
        return this;
    }
    //endregion


    //region Name

    /**
     * Gets the custom name of the entity.
     * If the entity has no custom name this method will return {@code null}.
     *
     * @return the custom name of the entity or {@code null} if the entity has no custom name.
     */
    public String getCustomName() {
        return entity.getCustomName();
    }

    /**
     * Sets the custom name of the entity.
     * <p/>
     * This name will be used for death messages and when customNameVisible is true for the nameplate of the entity.
     * The name will automatically be colored
     *
     * @param name The name to set
     * @return this instance
     */
    public EEntity setCustomName(String name) {
        entity.setCustomName(Str.color(name));
        return this;
    }

    /**
     * Gets whether or not the entity's custom name is displayed client side.
     *
     * @return if the custom name is displayed or not.
     */
    public boolean isCustomNameVisible() {
        return entity.isCustomNameVisible();
    }

    /**
     * Sets whether or not to display the entity's custom name client side.
     * The name will be displayed above the entity similarly to a player.
     *
     * @param state visible or not
     * @return this instance
     */
    public EEntity setCustomNameVisible(Boolean state) {
        entity.setCustomNameVisible(state);
        return this;
    }
    //endregion


    //region Attributes

    public AttributeInstance getAttribute(Attribute attribute) {
        if (entity instanceof Attributable) {
            return ((Attributable)entity).getAttribute(attribute);
        }
        return null;
    }
    //endregion


    //region Metadatable

    public List<MetadataValue> getMetadata(String metadataKey) {
        return entity.getMetadata(metadataKey);
    }

    public EEntity setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        entity.setMetadata(metadataKey, newMetadataValue);
        return this;
    }

    public Boolean hasMetadata(String metadataKey) {
        return entity.hasMetadata(metadataKey);
    }

    public EEntity removeMetadata(String metadataKey, Plugin owningPlugin) {
        entity.removeMetadata(metadataKey, owningPlugin);
        return this;
    }
    //endregion

    //endregion



    // ##################################################
    // ################### DAMAGEABLE ###################
    // ##################################################
    //region Damageable

    /**
     * Deals the given amount of damage to the entity.
     * <p/>
     * <b>Entities: </b> {@link Damageable}
     *
     * @param amount the amount of damage to deal
     * @return this instance
     */
    public EEntity damage(double amount) {
        if (entity instanceof Damageable) {
            ((Damageable)entity).damage(amount);
        }
        return this;
    }

    /**
     * Deals the given amount of damage to the entity, from the specified entity source.
     * <p/>
     * <b>Entities: </b> {@link Damageable}
     *
     * @param amount the amount of damage to deal
     * @param source entity to deal the damage from.
     * @return this instance
     */
    public EEntity damage(double amount, Entity source) {
        if (entity instanceof Damageable) {
            ((Damageable)entity).damage(amount, source);
        }
        return this;
    }

    /**
     * Deals the given amount of damage to the entity, from the specified entity source.
     * <p/>
     * <b>Entities: </b> {@link Damageable}
     *
     * @param amount the amount of damage to deal
     * @param source entity to deal the damage from.
     * @return this instance
     */
    public EEntity damage(double amount, EEntity source) {
        if (entity instanceof Damageable) {
            ((Damageable)entity).damage(amount, source.bukkit());
        }
        return this;
    }

    /**
     * Get current health from the entity.
     * <p/>
     * <b>Entities: </b> {@link Damageable}
     *
     * @return health represented from 0 to {@link #getMaxHealth()} (-1 when not damageable)
     */
    public double getHealth() {
        if (entity instanceof Damageable) {
            return ((Damageable)entity).getHealth();
        }
        return -1;
    }

    /**
     * Set the entity's health.
     * Amount will be clamped between 0 and {@link #getMaxHealth()}
     * <p/>
     * <b>Entities: </b> {@link Damageable}
     *
     * @param amount new health to set
     * @return this instance
     */
    public EEntity setHealth(double amount) {
        if (entity instanceof Damageable) {
            ((Damageable)entity).setHealth(Math.min(Math.max(0d, amount), getMaxHealth()));
        }
        return this;
    }

    /**
     * Gets the maximum health the entity can have.
     * <p/>
     * <b>Entities: </b> {@link Damageable}
     *
     * @return maximum health. (-1 when not damageable)
     */
    public double getMaxHealth() {
        if (entity instanceof Damageable) {
            return ((Damageable)entity).getMaxHealth();
        }
        return -1;
    }

    /**
     * Set the maximum health the entity can have.
     * <p/>
     * If the health of the entity is above the amount the entity's health will be set to the max amount.
     * <p/>
     * <b>Entities: </b> {@link Damageable}
     *
     * @param amount amount of health to set the maximum to.
     * @return this instance
     */
    public EEntity setMaxHealth(double amount) {
        if (entity instanceof Damageable) {
            ((Damageable)entity).setMaxHealth(amount);
        }
        return this;
    }

    /**
     * Resets the maximum health the entity can have back to the original amount.
     * <p/>
     * <b>Entities: </b> {@link Damageable}
     *
     * @return this instance
     */
    public EEntity resetMaxHealth() {
        if (entity instanceof Damageable) {
            ((Damageable)entity).resetMaxHealth();
        }
        return this;
    }
    //endregion



    // ##################################################
    // ###################### LIVNG #####################
    // ##################################################
    //region Living

    //region Location stuff

    /**
     * Gets the height of the living entity's eyes above its Location.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @return height of the living entity's eyes above its location (0 when not living)
     */
    public double getEyeHeight() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getEyeHeight();
        }
        return 0;
    }

    /**
     * Gets the height of the living entity's eyes above its Location.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param ignoreSneaking if set to true, the effects of sneaking will be ignored
     * @return height of the living entity's eyes above its location (0 when not living)
     */
    public double getEyeHeight(Boolean ignoreSneaking) {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getEyeHeight(ignoreSneaking);
        }
        return 0;
    }

    /**
     * Get a Location detailing the current eye position of the living entity.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @return a location at the eyes of the living entity ({@link #getLocation()} when not living)
     */
    public Location getEyeLocation() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getEyeLocation();
        }
        return getLocation();
    }

    /**
     * Gets all blocks along the living entity's line of sight.
     * <p/>
     * This list contains all blocks from the living entity's eye position to target inclusive.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param transparent HashSet containing all transparent block Materials (set to null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited by server by at least 100 blocks, no less)
     * @return list containing all blocks along the living entity's line of sight (empty list when not living)
     */
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getLineOfSight(transparent, maxDistance);
        }
        return new ArrayList<>();
    }

    /**
     * Checks whether the living entity has block line of sight to another.
     * <p/>
     * This uses the same algorithm that hostile mobs use to find the closest player.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param other the entity to determine line of sight to
     * @return true if there is a line of sight, false if not
     */
    public Boolean hasLineOfSight(Entity other) {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).hasLineOfSight(other);
        }
        return false;
    }

    /**
     * Checks whether the living entity has block line of sight to another.
     * <p/>
     * This uses the same algorithm that hostile mobs use to find the closest player.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param other the entity to determine line of sight to
     * @return true if there is a line of sight, false if not
     */
    public Boolean hasLineOfSight(EEntity other) {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).hasLineOfSight(other.bukkit());
        }
        return false;
    }

    /**
     * Gets the block that the living entity has targeted.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param transparent HashSet containing all transparent block Materials (set to null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited by server by at least 100 blocks, no less)
     * @return block that the living entity has targeted ({@code null} when not living)
     */
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getTargetBlock(transparent, maxDistance);
        }
        return null;
    }

    /**
     * Gets the last two blocks along the living entity's line of sight.
     * <p/>
     * The target block will be the last block in the list.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param transparent HashSet containing all transparent block Materials (set to null for only air)
     * @param maxDistance this is the maximum distance to scan. This may be further limited by the server, but never to less than 100 blocks
     * @return list containing the last 2 blocks along the living entity's line of sight (empty list when not living)
     */
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getLastTwoTargetBlocks(transparent, maxDistance);
        }
        return new ArrayList<>();
    }
    //endregion


    //region Air

    /**
     * Returns the amount of air that the living entity has remaining, in ticks.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @return amount of air remaining (-1 when not living)
     */
    public int getRemainingAir() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getRemainingAir();
        }
        return -1;
    }

    /**
     * Sets the amount of air that the living entity has remaining, in ticks.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param ticks amount of air remaining
     * @return this instance
     */
    public EEntity setRemainingAir(int ticks) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).setRemainingAir(ticks);
        }
        return this;
    }

    /**
     * Returns the maximum amount of air the living entity can have, in ticks.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @return maximum amount of air (-1 when not living)
     */
    public int getMaximumAir() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getMaximumAir();
        }
        return -1;
    }

    /**
     * Sets the maximum amount of air the living entity can have, in ticks.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param ticks maximum amount of air
     * @return this instance
     */
    public EEntity setMaximumAir(int ticks) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).setMaximumAir(ticks);
        }
        return this;
    }
    //endregion


    //region Damage

    /**
     * Returns the living entity's current maximum no damage ticks.
     * <p/>
     * This is the maximum duration in which the living entity will not take damage.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @return maximum no damage ticks (-1 when not living)
     */
    public int getMaximumNoDamageTicks() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getMaximumNoDamageTicks();
        }
        return -1;
    }

    /**
     * Sets the living entity's current maximum no damage ticks.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param ticks maximum amount of no damage ticks
     * @return this instance
     */
    public EEntity setMaximumNoDamageTicks(int ticks) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).setMaximumNoDamageTicks(ticks);
        }
        return this;
    }

    /**
     * Returns the living entity's last damage taken in the current no damage ticks time.
     * <p/>
     * Only damage higher than this amount will further damage the living entity.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @return damage taken since the last no damage ticks time period (0 when not living)
     */
    public double getLastDamage() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getLastDamage();
        }
        return 0;
    }

    /**
     * Sets the damage dealt within the current no damage ticks time period.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param damage amount of damage
     * @return this instance
     */
    public EEntity setLastDamage(double damage) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).setLastDamage(damage);
        }
        return this;
    }

    /**
     * Returns the living entity's current no damage ticks.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @return amount of no damage ticks (0 when not living)
     */
    public int getNoDamageTicks() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getNoDamageTicks();
        }
        return 0;
    }

    /**
     * Sets the living entity's current no damage ticks.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param ticks amount of no damage ticks
     * @return this instance
     */
    public EEntity setNoDamageTicks(int ticks) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).setNoDamageTicks(ticks);
        }
        return this;
    }

    /**
     * Gets the player identified as the killer of the living entity.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @return killer player, or {@code null} if none found or if not living
     */
    public Player getKiller() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getKiller();
        }
        return null;
    }
    //endregion


    //region Equipment
    public double getItemInHandDropChance() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getEquipment().getItemInHandDropChance();
        }
        return 0f;
    }

    public EEntity setItemInHandDropChance(double chance) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getEquipment().setItemInHandDropChance((float)chance);
        }
        return this;
    }

    public double getHelmetDropChance() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getEquipment().getItemInHandDropChance();
        }
        return 0f;
    }

    public EEntity setHelmetDropChance(double chance) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getEquipment().setHelmetDropChance((float)chance);
        }
        return this;
    }

    public double getChestplateDropChance() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getEquipment().getChestplateDropChance();
        }
        return 0f;
    }

    public EEntity setChestplateDropChance(double chance) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getEquipment().setChestplateDropChance((float)chance);
        }
        return this;
    }

    public double getLeggingsDropChance() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getEquipment().getLeggingsDropChance();
        }
        return 0f;
    }

    public EEntity setLeggingsDropChance(double chance) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getEquipment().setLeggingsDropChance((float)chance);
        }
        return this;
    }

    public double getBootsDropChance() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getEquipment().getBootsDropChance();
        }
        return 0f;
    }

    public EEntity setBootsDropChance(double chance) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getEquipment().setBootsDropChance((float)chance);
        }
        return this;
    }

    /**
     * Sets whether or not the living entity can pick up items.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param pickup whether or not the living entity can pick up items
     * @return this instance
     */
    public EEntity setCanPickupItems(Boolean pickup) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).setCanPickupItems(pickup);
        }
        return this;
    }

    /**
     * Gets if the living entity can pick up items.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @return whether or not the living entity can pick up items (false when not living)
     */
    public Boolean getCanPickupItems() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getCanPickupItems();
        }
        return false;
    }

    //endregion


    //region Misc

    /**
     * Returns if the living entity despawns when away from players or not.
     * <p/>
     * By default, animals are not removed while other mobs are.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @return true if the living entity is removed when away from players (false when not living)
     */
    public Boolean getRemoveWhenFarAway() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getRemoveWhenFarAway();
        }
        return false;
    }

    /**
     * Sets whether or not the living entity despawns when away from players or not.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param remove the removal status
     * @return this instance
     */
    public EEntity setRemoveWhenFarAway(Boolean remove) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).setRemoveWhenFarAway(remove);
        }
        return this;
    }

    /**
     * Returns whether the entity is currently leashed.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @return whether the entity is leashed (false when not living)
     */
    public Boolean isLeashed() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).isLeashed();
        }
        return false;
    }

    /**
     * Gets the entity that is currently leading this entity.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @return the entity holding the leash or null when not leashed (null when not living)
     */
    public EEntity getLeashHolder() {
        if (isLeashed()) {
            return new EEntity(((LivingEntity)entity).getLeashHolder());
        }
        return null;
    }

    /**
     * Sets the leash on this entity to be held by the supplied entity.
     * <p/>
     * This method has no effect on EnderDragons, Withers, Players, or Bats.
     * Non-living entities excluding leashes will not persist as leash holders.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param holder the entity to leash this entity to
     * @return this instance
     */
    public EEntity setLeashHolder(Entity holder) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).setLeashHolder(holder);
        }
        return this;
    }

    /**
     * Sets the leash on this entity to be held by the supplied entity.
     * <p/>
     * This method has no effect on EnderDragons, Withers, Players, or Bats.
     * Non-living entities excluding leashes will not persist as leash holders.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param holder the entity to leash this entity to
     * @return this instance
     */
    public EEntity setLeashHolder(EEntity holder) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).setLeashHolder(holder.bukkit());
        }
        return this;
    }
    //endregion
    //endregion



    // ##################################################
    // #################### AGEABLE #####################
    // ##################################################
    //region Ageable

    /**
     * Gets the age of this animal.
     * <p/>
     * <b>Entities: </b> {@link Ageable}
     *
     * @return age (-1 when not Ageable)
     */
    public int getAge() {
        if (entity instanceof Ageable) {
            return ((Ageable)entity).getAge();
        }
        return -1;
    }

    /**
     * Sets the age of this animal.
     * <p/>
     * <b>Entities: </b> {@link Ageable}
     *
     * @param age new age
     * @return this instance
     */
    public EEntity setAge(int age) {
        if (entity instanceof Ageable) {
            ((Ageable)entity).setAge(age);
        }
        return this;
    }

    /**
     * Gets the current agelock.
     * <p/>
     * <b>Entities: </b> {@link Ageable}
     *
     * @return the current agelock state (true when not Ageable)
     */
    public Boolean getAgeLock() {
        if (entity instanceof Ageable) {
            return ((Ageable)entity).getAgeLock();
        }
        return true;
    }

    /**
     * Lock the age of the animal, setting this will prevent the animal from maturing or getting ready for mating.
     * <p/>
     * <b>Entities: </b> {@link Ageable}, {@link Zombie}
     *
     * @param lock gets the current agelock.
     * @return this instance
     */
    public EEntity setAgeLock(Boolean lock) {
        if (entity instanceof Ageable) {
            ((Ageable)entity).setAgeLock(lock);
        }
        return this;
    }

    /**
     * Returns true if the animal is a baby.
     * <p/>
     * <b>Entities: </b> {@link Ageable}, {@link Zombie}
     *
     * @return true if the animal is a baby. (false when not Ageable)
     */
    public Boolean isBaby() {
        if (entity instanceof Ageable) {
            return !((Ageable)entity).isAdult();
        } else if (entity instanceof Zombie) {
            return ((Zombie)entity).isBaby();
        }
        return false;
    }

    /**
     * Sets the age of the animal to a baby
     * <p/>
     * <b>Entities: </b> {@link Ageable}, {@link Zombie}
     *
     * @return this instance
     */
    public EEntity setBaby() {
        if (entity instanceof Ageable) {
            ((Ageable)entity).setBaby();
        } else if (entity instanceof Zombie) {
            ((Zombie)entity).setBaby(true);
        }
        return this;
    }

    /**
     * Sets the age of the animal to baby or adult.
     * <p/>
     * <b>Entities: </b> {@link Ageable}, {@link Zombie}
     *
     * @param baby whether to set the animal to baby or adult.
     * @return this instance
     */
    public EEntity setBaby(Boolean baby) {
        if (entity instanceof Ageable) {
            if (baby) {
                ((Ageable)entity).setBaby();
            } else {
                ((Ageable)entity).setAdult();
            }
        } else if (entity instanceof Zombie) {
            ((Zombie)entity).setBaby(baby);
        }
        return this;
    }


    /**
     * Returns true if the animal is an adult.
     * <p/>
     * <b>Entities: </b> {@link Ageable}, {@link Zombie}
     *
     * @return true if the animal is an adult. (true when not Ageable)
     */
    public Boolean isAdult() {
        if (entity instanceof Ageable) {
            return ((Ageable)entity).isAdult();
        } else if (entity instanceof Zombie) {
            return !((Zombie)entity).isBaby();
        }
        return true;
    }

    /**
     * Sets the age of the animal to an adult
     * <p/>
     * <b>Entities: </b> {@link Ageable}
     *
     * @return this instance
     */
    public EEntity setAdult() {
        if (entity instanceof Ageable) {
            ((Ageable)entity).setAdult();
        } else if (entity instanceof Zombie) {
            ((Zombie)entity).setBaby(false);
        }
        return this;
    }

    /**
     * Return the ability to breed of the animal
     * <p/>
     * <b>Entities: </b> {@link Ageable}
     *
     * @return the ability to breed of the animal (false when not Ageable)
     */
    public Boolean canBreed() {
        if (entity instanceof Ageable) {
            return ((Ageable)entity).canBreed();
        }
        return false;
    }

    /**
     * Set breedability of the animal,
     * if the animal is a baby and set to breed it will instantly grow up.
     * <p/>
     * <b>Entities: </b> {@link Ageable}
     *
     * @param breed breedability of the animal
     * @return this instance
     */
    public EEntity setBreed(Boolean breed) {
        if (entity instanceof Ageable) {
            ((Ageable)entity).setBreed(breed);
        }
        return this;
    }
    //endregion



    // ##################################################
    // #################### TAMEABLE ####################
    // ##################################################
    //region Tameable
    /**
     * Check if this is tamed
     * <p/>
     * If something is tamed then a player can not tame it through normal methods, even if it does not belong to anyone in particular.
     * <p/>
     * <b>Entities: </b> {@link Tameable}
     *
     * @return true if this has been tamed (false when not Tameable)
     */
    public Boolean isTamed() {
        if (entity instanceof Tameable) {
            return ((Tameable)entity).isTamed();
        }
        return false;
    }

    /**
     * Sets if this has been tamed. Not necessary if the method setOwner has been used, as it tames automatically.
     * <p/>
     * If something is tamed then a player can not tame it through normal methods, even if it does not belong to anyone in particular.
     * <p/>
     * <b>Entities: </b> {@link Tameable}
     *
     * @param tamed true if tame
     * @return this instance
     */
    public EEntity setTamed(Boolean tamed) {
        if (entity instanceof Tameable) {
            ((Tameable)entity).setTamed(tamed);
        }
        return this;
    }

    /**
     * Gets the current owning AnimalTamer
     * <p/>
     * <b>Entities: </b> {@link Tameable}
     *
     * @return the owning {@link AnimalTamer}, or null if not owned ({@code null} when not Tameable)
     */
    public AnimalTamer getOwner() {
        if (entity instanceof Tameable) {
            return ((Tameable)entity).getOwner();
        }
        return null;
    }

    /**
     * Set this to be owned by given AnimalTamer.
     * <p/>
     * If the owner is not null, this will be tamed and will have any current path it is following removed. If the owner is set to null, this will be untamed, and the current owner removed.
     * <p/>
     * <b>Entities: </b> {@link Tameable}
     *
     * @param owner the {@link AnimalTamer} who should own this
     * @return this instance
     */
    public EEntity setOwner(AnimalTamer owner) {
        if (entity instanceof Tameable) {
            ((Tameable)entity).setOwner(owner);
        }
        return this;
    }
    //endregion



    // ##################################################
    // ################### CREATURES ####################
    // ##################################################
    //region Creature

    /**
     * Gets the current target of this Creature
     * <p/>
     * <b>Entities: </b> {@link Creature}, {@link ShulkerBullet}
     *
     * @return Current target of this creature, or null if none exists ({@code null} when not Creature)
     */
    public EEntity getTarget() {
        if (entity instanceof ShulkerBullet) {
            return new EEntity(((ShulkerBullet)entity).getTarget());
        } else if (entity instanceof Creature) {
            return new EEntity(((Creature)entity).getTarget());
        }
        return null;
    }

    /**
     * Instructs this Creature to set the specified LivingEntity as its target.
     * <p/>
     * Hostile creatures may attack their target, and friendly creatures may follow their target.
     * <p/>
     * <b>Entities: </b> {@link Creature}, {@link ShulkerBullet}
     *
     * @param target New {@link LivingEntity} to target, or {@code null} to clear the target
     * @return this instance
     */
    public EEntity setTarget(EEntity target) {
        if (entity instanceof ShulkerBullet) {
            ((ShulkerBullet)entity).setTarget(target.bukkit());
        } else if (entity instanceof Creature) {
            if (target.bukkit() instanceof LivingEntity) {
                ((Creature)entity).setTarget((LivingEntity)target.bukkit());
            }
        }
        return this;
    }
    //endregion



    // ##################################################
    // ################## PROJECTILES ###################
    // ##################################################
    //region Projectile

    /**
     * Retrieve the shooter of this projectile.
     * <p/>
     * <b>Entities: </b> {@link Projectile}, {@link ShulkerBullet}
     *
     * @return the {@link ProjectileSource} that shot this projectile ({@code null} when not Projectile)
     */
    public ProjectileSource getShooter() {
        if (entity instanceof Projectile) {
            return ((Projectile)entity).getShooter();
        } else if (entity instanceof ShulkerBullet) {
            return ((ShulkerBullet)entity).getShooter();
        }
        return null;
    }

    /**
     * Set the shooter of this projectile.
     * <p/>
     * <b>Entities: </b> {@link Projectile}, {@link ShulkerBullet}
     *
     * @param shooter the {@link ProjectileSource} that shot this projectile
     * @return this instance
     */
    public EEntity setShooter(ProjectileSource shooter) {
        if (entity instanceof Projectile) {
            ((Projectile)entity).setShooter(shooter);
        } else if (entity instanceof ShulkerBullet) {
            ((ShulkerBullet)entity).setShooter(shooter);
        }
        return this;
    }

    public EEntity setShooter(EEntity shooter) {
        if (shooter.bukkit() instanceof ProjectileSource) {
            if (entity instanceof Projectile) {
                ((Projectile)entity).setShooter((ProjectileSource)shooter.bukkit());
            } else if (entity instanceof ShulkerBullet) {
                ((ShulkerBullet)entity).setShooter((ProjectileSource)shooter.bukkit());
            }
        }
        return this;
    }

    /**
     * Determine if this projectile should bounce or not when it hits.
     * <p/>
     * If a small fireball does not bounce it will set the target on fire.
     * <p/>
     * <b>Entities: </b> {@link Projectile}
     *
     * @return true if it should bounce.
     */
    public Boolean doesBounce() {
        if (entity instanceof Projectile) {
            return ((Projectile)entity).doesBounce();
        }
        return false;
    }

    /**
     * Set whether or not this projectile should bounce or not when it hits something.
     * <p/>
     * If a small fireball does not bounce it will set the target on fire.
     * <p/>
     * <b>Entities: </b> {@link Projectile}
     *
     * @param bounce whether or not it should bounce.
     * @return this instance
     */
    public EEntity setBounce(Boolean bounce) {
        if (entity instanceof Projectile) {
            ((Projectile)entity).setBounce(bounce);
        }
        return this;
    }
    //endregion

    //region ProjectileSource

    public <T extends Projectile> EEntity launchProjectile(Class<? extends T> projectile) {
        if (entity instanceof ProjectileSource) {
            ((ProjectileSource)entity).launchProjectile(projectile);
        }
        return this;
    }

    public <T extends Projectile> EEntity launchProjectile(Class<? extends T> projectile, Vector velocity) {
        if (entity instanceof ProjectileSource) {
            ((ProjectileSource)entity).launchProjectile(projectile, velocity);
        }
        return this;
    }
    //endregion


    // ##################################################
    // #################### HANGING #####################
    // ##################################################
    //region Hanging

    /**
     * Sets the direction of the hanging entity, potentially overriding rules of placement.
     * <p/>
     * Note that if the result is not valid the object would normally drop as an item.
     * <b>Entities: </b> {@link Hanging}
     *
     * @param face The new direction.
     * @param force Whether to force it.
     * @return False if force was false and there was no block for it to attach to in order to face the given direction.
     */
    public Boolean setFacingDirection(BlockFace face, Boolean force) {
        if (entity instanceof Hanging) {
            return ((Hanging)entity).setFacingDirection(face, force);
        }
        return false;
    }
    //endregion



    // ##################################################
    // #################### ENTITIES ####################
    // ##################################################
    //region Entities

    //region Mixed

    //region Equipment

    /**
     * Gets the inventory with the equipment worn by the living entity.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}, {@link ArmorStand}
     *
     * @return the living entity's inventory
     */
    public EntityEquipment getEquipment() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity) entity).getEquipment();
        } else if (entity instanceof ArmorStand) {
            return ((ArmorStand) entity).getEquipment();
        }
        return null;
    }

    public EItem getItemInMainHand() {
        if (entity instanceof LivingEntity) {
            return new EItem(((LivingEntity)entity).getEquipment().getItemInMainHand());
        } else if (entity instanceof ArmorStand) {
            return new EItem(((ArmorStand)entity).getEquipment().getItemInMainHand());
        }
        return null;
    }

    public EEntity setItemInMainHand(EItem item) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getEquipment().setItemInMainHand(item);
        } else if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).getEquipment().setItemInMainHand(item);
        }
        return this;
    }

    public EItem getItemInOffHand() {
        if (entity instanceof LivingEntity) {
            return new EItem(((LivingEntity)entity).getEquipment().getItemInOffHand());
        } else if (entity instanceof ArmorStand) {
            return new EItem(((ArmorStand)entity).getEquipment().getItemInOffHand());
        }
        return null;
    }

    public EEntity setItemInOffHand(EItem item) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getEquipment().setItemInOffHand(item);
        } else if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).getEquipment().setItemInMainHand(item);
        }
        return this;
    }

    public EItem[] getArmorContents() {
        EItem[] items = null;
        if (entity instanceof LivingEntity) {
            items = new EItem[((LivingEntity)entity).getEquipment().getArmorContents().length];
        } else if (entity instanceof ArmorStand) {
            items = new EItem[((ArmorStand)entity).getEquipment().getArmorContents().length];
        }
        for (int i = 0; i < items.length; i++) {
            items[i] = new EItem(((LivingEntity)entity).getEquipment().getArmorContents()[i]);
        }
        return items;
    }

    public EEntity setArmorContents(EItem[] items) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getEquipment().setArmorContents(items);
        } else if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).getEquipment().setArmorContents(items);
        }
        return this;
    }

    public EEntity clearEquipment() {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getEquipment().clear();
        } else if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).getEquipment().clear();
        }
        return this;
    }

    public EItem getBoots() {
        if (entity instanceof ArmorStand) {
            return new EItem(((ArmorStand)entity).getBoots());
        } else if (entity instanceof LivingEntity) {
            return new EItem(((LivingEntity)entity).getEquipment().getBoots());
        }
        return null;
    }

    public EEntity setBoots(EItem item) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setBoots(item);
        } else if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getEquipment().setBoots(item);
        }
        return this;
    }

    public EItem getLeggings() {
        if (entity instanceof ArmorStand) {
            return new EItem(((ArmorStand)entity).getLeggings());
        } else if (entity instanceof LivingEntity) {
            return new EItem(((LivingEntity)entity).getEquipment().getLeggings());
        }
        return null;
    }

    public EEntity setLeggings(EItem item) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setLeggings(item);
        } else if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getEquipment().setLeggings(item);
        }
        return this;
    }

    public EItem getChestplate() {
        if (entity instanceof ArmorStand) {
            return new EItem(((ArmorStand)entity).getChestplate());
        } else if (entity instanceof LivingEntity) {
            return new EItem(((LivingEntity)entity).getEquipment().getChestplate());
        }
        return null;
    }

    public EEntity setChestplate(EItem item) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setChestplate(item);
        } else if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getEquipment().setChestplate(item);
        }
        return this;
    }

    public EItem getHelmet() {
        if (entity instanceof ArmorStand) {
            return new EItem(((ArmorStand)entity).getHelmet());
        } else if (entity instanceof LivingEntity) {
            return new EItem(((LivingEntity)entity).getEquipment().getHelmet());
        }
        return null;
    }

    public EEntity setHelmet(EItem item) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setHelmet(item);
        } else if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getEquipment().setHelmet(item);
        }
        return this;
    }
    //endregion Equipment


    //region Items
    public EItem getItem() {
        if (entity instanceof Item) {
            return new EItem(((Item)entity).getItemStack());
        } else if (entity instanceof ItemFrame) {
            return new EItem(((ItemFrame)entity).getItem());
        } else if (entity instanceof ThrownPotion) {
            return new EItem(((ThrownPotion)entity).getItem());
        }
        return null;
    }

    public EEntity setItem(EItem stack) {
        if (entity instanceof Item) {
            ((Item)entity).setItemStack(stack);
        } else if (entity instanceof ItemFrame) {
            ((ItemFrame)entity).setItem(stack);
        } else if (entity instanceof ThrownPotion) {
            ((ThrownPotion)entity).setItem(stack);
        }
        return this;
    }
    //endregion


    //region PotionEffects
    //TODO: Methods to apply potion effects safely. (increase duration for same effect, override when effect is better and ignore when effect is worse)

    /**
     * Adds the given {@link PotionEffect} to the living entity.
     * <p/>
     * Only one potion effect can be present for a given {@link PotionEffectType}.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}, {@link AreaEffectCloud}
     *
     * @param effect PotionEffect to be added
     * @return this instance
     */
    public EEntity addEffect(PotionEffect effect) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).addPotionEffect(effect);
        } else if (entity instanceof AreaEffectCloud) {
            ((AreaEffectCloud)entity).addEffect(effect);
        }
        return this;
    }

    /**
     * Adds the given {@link PotionEffect} to the living entity.
     * <p/>
     * Only one potion effect can be present for a given {@link PotionEffectType}.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}, {@link AreaEffectCloud}
     *
     * @param effect PotionEffect to be added
     * @param force whether conflicting effects should be removed/overwritten
     * @return this instance
     */
    public EEntity addEffect(PotionEffect effect, boolean force) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).addPotionEffect(effect, force);
        } else if (entity instanceof AreaEffectCloud) {
            ((AreaEffectCloud)entity).addEffect(effect);
        }
        return this;
    }

    /**
     * Attempts to add all of the given {@link PotionEffect} to the living entity.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}, {@link AreaEffectCloud}
     *
     * @param effects the effects to add
     * @return this instance
     */
    public EEntity addEffects(Collection<PotionEffect> effects) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).addPotionEffects(effects);
        } else if (entity instanceof AreaEffectCloud) {
            for (PotionEffect effect : effects) {
                ((AreaEffectCloud)entity).addEffect(effect);
            }
        }
        return this;
    }

    /**
     * Attempts to add all of the given {@link PotionEffect} to the living entity.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}, {@link AreaEffectCloud}
     *
     * @param effects the effects to add
     * @return this instance
     */
    public EEntity setEffects(Collection<PotionEffect> effects) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getActivePotionEffects().clear();
            ((LivingEntity)entity).addPotionEffects(effects);
        } else if (entity instanceof AreaEffectCloud) {
            ((AreaEffectCloud)entity).setEffects((List<PotionEffect>)effects);
        }
        return this;
    }

    /**
     * Returns whether the living entity already has an existing effect of the given {@link PotionEffectType} applied to it.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}, {@link AreaEffectCloud}
     *
     * @param type the potion type to check
     * @return whether the living entity has this potion effect active on them
     */
    public Boolean hasEffect(PotionEffectType type) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).hasPotionEffect(type);
        } else if (entity instanceof AreaEffectCloud) {
            for (PotionEffect effect : ((AreaEffectCloud)entity).getEffects()) {
                if (effect.getType() == type) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes any effects present of the given {@link PotionEffectType}.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}, {@link AreaEffectCloud}
     *
     * @param type the potion type to remove
     * @return this instance
     */
    public EEntity removeEffect(PotionEffectType type) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).removePotionEffect(type);
        } else if (entity instanceof AreaEffectCloud) {
            List<PotionEffect> acitveEffects = new ArrayList<>(((AreaEffectCloud)entity).getEffects());
            for (PotionEffect effect : acitveEffects) {
                if (effect.getType() == type) {
                    ((AreaEffectCloud)entity).getEffects().remove(effect);
                }
            }
        }
        return this;
    }

    /**
     * Returns all currently active {@link PotionEffect}s on the living entity.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}, {@link AreaEffectCloud}, {@link ThrownPotion}
     *
     * @return a collection of {@link PotionEffect}s
     */
    public Collection<PotionEffect> getEffects() {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getActivePotionEffects();
        } else if (entity instanceof AreaEffectCloud) {
            ((AreaEffectCloud)entity).getEffects();
        } else if (entity instanceof ThrownPotion) {
            return ((ThrownPotion)entity).getEffects();
        }
        return new ArrayList<>();
    }
    //endregion


    //region Sitting
    public Boolean isSitting() {
        if (entity instanceof Ocelot) {
            return ((Ocelot)entity).isSitting();
        } else if (entity instanceof Wolf) {
            return ((Wolf)entity).isSitting();
        }
        return null;
    }

    public EEntity setSitting(Boolean sitting) {
        if (entity instanceof Ocelot) {
            ((Ocelot)entity).setSitting(sitting);
        } else if (entity instanceof Wolf) {
            ((Wolf)entity).setSitting(sitting);
        }
        return this;
    }
    //endregion


    //region Angry
    public Boolean isAngry() {
        if (entity instanceof Wolf) {
            return ((Wolf)entity).isAngry();
        } else if (entity instanceof PigZombie) {
            return ((PigZombie)entity).isAngry();
        }
        return null;
    }

    public EEntity setAngry(Boolean angry) {
        if (entity instanceof Wolf) {
            ((Wolf)entity).setAngry(angry);
        } else if (entity instanceof PigZombie) {
            ((PigZombie)entity).setAngry(angry);
        }
        return this;
    }
    //endregion


    //region Saddle
    public Boolean hasSaddle() {
        if (entity instanceof Pig) {
            return ((Pig)entity).hasSaddle();
        } else if (entity instanceof Horse) {
            return ((Horse)entity).getInventory().getSaddle() != null;
        }
        return false;
    }

    public EEntity setSaddle(Boolean saddled) {
        if (entity instanceof Pig) {
            ((Pig)entity).setSaddle(saddled);
        } else if (entity instanceof Horse) {
            ((Horse)entity).getInventory().setSaddle(new EItem(Material.SADDLE));
        }
        return this;
    }

    public EItem getSaddle() {
        if (entity instanceof Horse) {
            return new EItem(((Horse)entity).getInventory().getSaddle());
        } else if (entity instanceof Pig) {
            return ((Pig)entity).hasSaddle() ? new EItem(Material.SADDLE) : null;
        }
        return null;
    }

    public EEntity setSaddle(EItem stack) {
        if (entity instanceof Horse) {
            ((Horse)entity).getInventory().setSaddle(stack);
        } else if (entity instanceof Pig) {
            ((Pig)entity).setSaddle(stack != null && stack.getType() == Material.SADDLE);
        }
        return this;
    }
    //endregion


    //region DyeColor
    public DyeColor getColor() {
        if (entity instanceof Colorable) {
            return ((Colorable)entity).getColor();
        } else if (entity instanceof Wolf) {
            return ((Wolf)entity).getCollarColor();
        }
        return DyeColor.WHITE;
    }

    public EEntity setColor(DyeColor color) {
        if (entity instanceof Colorable) {
            ((Colorable)entity).setColor(color);
        } else if (entity instanceof Wolf) {
            ((Wolf)entity).setCollarColor(color);
        }
        return this;
    }
    //endregion


    //region Profession
    public Villager.Profession getProfession() {
        if (entity instanceof Villager) {
            return ((Villager)entity).getProfession();
        } else if (entity instanceof Zombie) {
            return ((Zombie)entity).getVillagerProfession();
        }
        return Villager.Profession.FARMER;
    }

    public EEntity setProfession(Villager.Profession profession) {
        if (entity instanceof Villager) {
            ((Villager)entity).setProfession(profession);
        } else if (entity instanceof Zombie) {
            ((Zombie)entity).setVillagerProfession(profession);
        }
        return this;
    }
    //endregion
    //endregion



    //region ArmorStand

    public EulerAngle getBodyPose() {
        if (entity instanceof ArmorStand) {
            return ((ArmorStand)entity).getBodyPose();
        }
        return new EulerAngle(0,0,0);
    }

    public EEntity setBodyPose(EulerAngle pose) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setBodyPose(pose);
        }
        return this;
    }

    public EEntity setBodyPose(Vector pose) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setBodyPose(new EulerAngle(Math.toRadians(pose.getX()), Math.toRadians(pose.getY()), Math.toRadians(pose.getZ())));
        }
        return this;
    }

    public EulerAngle getHeadPose() {
        if (entity instanceof ArmorStand) {
            return ((ArmorStand)entity).getHeadPose();
        }
        return new EulerAngle(0,0,0);
    }

    public EEntity setHeadPose(EulerAngle pose) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setHeadPose(pose);
        }
        return this;
    }

    public EEntity setHeadPose(Vector pose) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setHeadPose(new EulerAngle(Math.toRadians(pose.getX()), Math.toRadians(pose.getY()), Math.toRadians(pose.getZ())));
        }
        return this;
    }

    public EulerAngle getLeftArmPose() {
        if (entity instanceof ArmorStand) {
            return ((ArmorStand)entity).getLeftArmPose();
        }
        return new EulerAngle(0,0,0);
    }

    public EEntity setLeftArmPose(EulerAngle pose) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setLeftArmPose(pose);
        }
        return this;
    }

    public EEntity setLeftArmPose(Vector pose) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setLeftArmPose(new EulerAngle(Math.toRadians(pose.getX()), Math.toRadians(pose.getY()), Math.toRadians(pose.getZ())));
        }
        return this;
    }

    public EulerAngle getRightArmPose() {
        if (entity instanceof ArmorStand) {
            return ((ArmorStand)entity).getRightArmPose();
        }
        return new EulerAngle(0,0,0);
    }

    public EEntity setRightArmPose(EulerAngle pose) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setRightArmPose(pose);
        }
        return this;
    }

    public EEntity setRightArmPose(Vector pose) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setRightArmPose(new EulerAngle(Math.toRadians(pose.getX()), Math.toRadians(pose.getY()), Math.toRadians(pose.getZ())));
        }
        return this;
    }

    public EulerAngle getLeftLegPose() {
        if (entity instanceof ArmorStand) {
            return ((ArmorStand)entity).getLeftLegPose();
        }
        return new EulerAngle(0,0,0);
    }

    public EEntity setLeftLegPose(EulerAngle pose) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setLeftLegPose(pose);
        }
        return this;
    }

    public EEntity setLeftLegPose(Vector pose) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setLeftLegPose(new EulerAngle(Math.toRadians(pose.getX()), Math.toRadians(pose.getY()), Math.toRadians(pose.getZ())));
        }
        return this;
    }

    public EulerAngle getRightLegPose() {
        if (entity instanceof ArmorStand) {
            return ((ArmorStand)entity).getRightLegPose();
        }
        return new EulerAngle(0,0,0);
    }

    public EEntity setRightLegPose(EulerAngle pose) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setRightLegPose(pose);
        }
        return this;
    }

    public EEntity setRightLegPose(Vector pose) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setRightLegPose(new EulerAngle(Math.toRadians(pose.getX()), Math.toRadians(pose.getY()), Math.toRadians(pose.getZ())));
        }
        return this;
    }

    public Boolean hasBasePlate() {
        if (entity instanceof ArmorStand) {
            return ((ArmorStand)entity).hasBasePlate();
        }
        return false;
    }

    public EEntity setBasePlate(Boolean baseplate) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setBasePlate(baseplate);
        }
        return this;
    }

    public Boolean hasGravity() {
        if (entity instanceof ArmorStand) {
            return ((ArmorStand)entity).hasGravity();
        }
        return true;
    }

    public EEntity setGravity(Boolean gravity) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setGravity(gravity);
        }
        return this;
    }

    public Boolean isSmall() {
        if (entity instanceof ArmorStand) {
            return ((ArmorStand)entity).isSmall();
        }
        return true;
    }

    public EEntity setSmall(Boolean small) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setSmall(small);
        }
        return this;
    }

    public Boolean isVisible() {
        if (entity instanceof ArmorStand) {
            return ((ArmorStand)entity).isVisible();
        }
        return true;
    }

    public EEntity setVisible(Boolean visible) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setVisible(visible);
        }
        return this;
    }

    public Boolean hasArms() {
        if (entity instanceof ArmorStand) {
            return ((ArmorStand)entity).hasArms();
        }
        return false;
    }

    public EEntity setArms(Boolean arms) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setArms(arms);
        }
        return this;
    }

    public Boolean isMarker() {
        if (entity instanceof ArmorStand) {
            return ((ArmorStand)entity).isMarker();
        }
        return false;
    }

    public EEntity setMarker(Boolean marker) {
        if (entity instanceof ArmorStand) {
            ((ArmorStand)entity).setMarker(marker);
        }
        return this;
    }
    //endregion



    //region Arrow

    public int getKnockbackStrength() {
        if (entity instanceof Arrow) {
            return ((Arrow)entity).getKnockbackStrength();
        }
        return 0;
    }

    public EEntity setKnockbackStrength(int knockbackStrength) {
        if (entity instanceof Arrow) {
            ((Arrow)entity).setKnockbackStrength(knockbackStrength);
        }
        return this;
    }

    public Boolean isCritical() {
        if (entity instanceof Arrow) {
            return ((Arrow)entity).isCritical();
        }
        return false;
    }

    public EEntity setCritical(Boolean critical) {
        if (entity instanceof Arrow) {
            ((Arrow)entity).setCritical(critical);
        }
        return this;
    }
    //endregion



    //region AreaEffectCloud

    public int getDuration() {
        if (entity instanceof AreaEffectCloud) {
            return ((AreaEffectCloud)entity).getDuration();
        }
        return 0;
    }

    public EEntity setDuration(int ticks) {
        if (entity instanceof AreaEffectCloud) {
            ((AreaEffectCloud)entity).setDuration(ticks);
        }
        return this;
    }

    public int getWaitTime() {
        if (entity instanceof AreaEffectCloud) {
            return ((AreaEffectCloud)entity).getWaitTime();
        }
        return 0;
    }

    public EEntity setWaitTime(int ticks) {
        if (entity instanceof AreaEffectCloud) {
            ((AreaEffectCloud)entity).setWaitTime(ticks);
        }
        return this;
    }

    public int getReapplicationDelay() {
        if (entity instanceof AreaEffectCloud) {
            return ((AreaEffectCloud)entity).getReapplicationDelay();
        }
        return 0;
    }

    public EEntity setReapplicationDelay(int ticks) {
        if (entity instanceof AreaEffectCloud) {
            ((AreaEffectCloud)entity).setReapplicationDelay(ticks);
        }
        return this;
    }

    public int getDurationOnUse() {
        if (entity instanceof AreaEffectCloud) {
            return ((AreaEffectCloud)entity).getDurationOnUse();
        }
        return 0;
    }

    public EEntity setDurationOnUse(int ticks) {
        if (entity instanceof AreaEffectCloud) {
            ((AreaEffectCloud)entity).setDurationOnUse(ticks);
        }
        return this;
    }

    public double getRadius() {
        if (entity instanceof AreaEffectCloud) {
            return ((AreaEffectCloud)entity).getRadius();
        }
        return 0f;
    }

    public EEntity setRadius(double radius) {
        if (entity instanceof AreaEffectCloud) {
            ((AreaEffectCloud)entity).setRadius((float)radius);
        }
        return this;
    }

    public double getRadiusOnUse() {
        if (entity instanceof AreaEffectCloud) {
            return ((AreaEffectCloud)entity).getRadiusOnUse();
        }
        return 0f;
    }

    public EEntity setRadiusOnUse(double radius) {
        if (entity instanceof AreaEffectCloud) {
            ((AreaEffectCloud)entity).setRadiusOnUse((float)radius);
        }
        return this;
    }

    public double getRadiusPerTick() {
        if (entity instanceof AreaEffectCloud) {
            return ((AreaEffectCloud)entity).getRadiusPerTick();
        }
        return 0f;
    }

    public EEntity setRadiusPerTick(double radius) {
        if (entity instanceof AreaEffectCloud) {
            ((AreaEffectCloud)entity).setRadiusPerTick((float)radius);
        }
        return this;
    }

    public Particle getParticle() {
        if (entity instanceof AreaEffectCloud) {
            return ((AreaEffectCloud)entity).getParticle();
        }
        return null;
    }

    public EEntity setParticle(Particle particle) {
        if (entity instanceof AreaEffectCloud) {
            ((AreaEffectCloud)entity).setParticle(particle);
        }
        return this;
    }

    public Color getEffectColor() {
        if (entity instanceof AreaEffectCloud) {
            return ((AreaEffectCloud)entity).getColor();
        }
        return Color.fromRGB(0,0,0);
    }

    public EEntity setEffectColor(Color color) {
        if (entity instanceof AreaEffectCloud) {
            ((AreaEffectCloud)entity).setColor(color);
        }
        return this;
    }
    //endregion



    //region Minecarts

    public double getDamage() {
        if (entity instanceof Minecart) {
            return ((Minecart)entity).getDamage();
        }
        return 0d;
    }

    public EEntity setDamage(double damage) {
        if (entity instanceof Minecart) {
            ((Minecart)entity).setDamage(damage);
        }
        return this;
    }

    public double getMaxSpeed() {
        if (entity instanceof Minecart) {
            return ((Minecart)entity).getMaxSpeed();
        }
        return 0d;
    }

    public EEntity setMaxSpeed(double speed) {
        if (entity instanceof Minecart) {
            ((Minecart)entity).setMaxSpeed(speed);
        }
        return this;
    }

    public Boolean isSlowWhenEmpty() {
        if (entity instanceof Minecart) {
            return ((Minecart)entity).isSlowWhenEmpty();
        }
        return false;
    }

    public EEntity setSlowWhenEmpty(Boolean slow) {
        if (entity instanceof Minecart) {
            ((Minecart)entity).setSlowWhenEmpty(slow);
        }
        return this;
    }

    public Vector getFlyingVelocityMod() {
        if (entity instanceof Minecart) {
            return ((Minecart)entity).getFlyingVelocityMod();
        }
        return new Vector(0,0,0);
    }

    public EEntity setFlyingVelocityMod(Vector flying) {
        if (entity instanceof Minecart) {
            ((Minecart)entity).setFlyingVelocityMod(flying);
        }
        return this;
    }

    public Vector getDerailedVelocityMod() {
        if (entity instanceof Minecart) {
            return ((Minecart)entity).getDerailedVelocityMod();
        }
        return new Vector(0,0,0);
    }

    public EEntity setDerailedVelocityMod(Vector derailed) {
        if (entity instanceof Minecart) {
            ((Minecart)entity).setDerailedVelocityMod(derailed);
        }
        return this;
    }

    public MaterialData getDisplayBlock() {
        if (entity instanceof Minecart) {
            return ((Minecart)entity).getDisplayBlock();
        }
        return null;
    }

    public EEntity setDisplayBlock(MaterialData material) {
        if (entity instanceof Minecart) {
            ((Minecart)entity).setDisplayBlock(material);
        }
        return this;
    }

    public int getDisplayBlockOffset() {
        if (entity instanceof Minecart) {
            return ((Minecart)entity).getDisplayBlockOffset();
        }
        return 0;
    }

    public EEntity setDisplayBlockOffset(int offset) {
        if (entity instanceof Minecart) {
            ((Minecart)entity).setDisplayBlockOffset(offset);
        }
        return this;
    }

    public String getCommand() {
        if (entity instanceof CommandMinecart) {
            return ((CommandMinecart)entity).getCommand();
        }
        return "";
    }

    public EEntity setCommand(String command) {
        if (entity instanceof CommandMinecart) {
            ((CommandMinecart)entity).setCommand(command);
        }
        return this;
    }

    public EEntity setName(String name) {
        if (entity instanceof CommandMinecart) {
            ((CommandMinecart)entity).setCommand(name);
        }
        return this;
    }
    //endregion



    //region Enderdragon

    public ComplexLivingEntity getParent() {
        if (entity instanceof ComplexEntityPart) {
            return ((ComplexEntityPart)entity).getParent();
        }
        return null;
    }

    public Set<ComplexEntityPart> getParts() {
        if (entity instanceof ComplexLivingEntity) {
            return ((ComplexLivingEntity)entity).getParts();
        } else if (entity instanceof EnderDragon) {
            return ((EnderDragon)entity).getParts();
        }
        return new HashSet<>();
    }
    //endregion



    //region ExperienceOrb

    public int getExperience() {
        if (entity instanceof ExperienceOrb) {
            return ((ExperienceOrb)entity).getExperience();
        }
        return 0;
    }

    public EEntity setExperience(int experience) {
        if (entity instanceof ExperienceOrb) {
            ((ExperienceOrb)entity).setExperience(experience);
        }
        return this;
    }
    //endregion



    //region FallingBlock

    public Material getMaterial() {
        if (entity instanceof FallingBlock) {
            return ((FallingBlock)entity).getMaterial();
        }
        return Material.AIR;
    }

    public Byte getData() {
        if (entity instanceof FallingBlock) {
            return ((FallingBlock)entity).getBlockData();
        }
        return 0;
    }

    public Boolean getDropItem() {
        if (entity instanceof FallingBlock) {
            return ((FallingBlock)entity).getDropItem();
        }
        return false;
    }

    public EEntity setDropItem(Boolean drop) {
        if (entity instanceof FallingBlock) {
            ((FallingBlock)entity).setDropItem(drop);
        }
        return this;
    }

    public Boolean canHurtEntities() {
        if (entity instanceof FallingBlock) {
            return ((FallingBlock)entity).canHurtEntities();
        }
        return false;
    }

    public EEntity setHurtEntities(Boolean hurtEntities) {
        if (entity instanceof FallingBlock) {
            ((FallingBlock)entity).setHurtEntities(hurtEntities);
        }
        return this;
    }
    //endregion



    //region Firework

    public FireworkMeta getFireworkMeta() {
        if (entity instanceof Firework) {
            return ((Firework)entity).getFireworkMeta();
        }
        return null;
    }

    public EEntity setFireworkMeta(FireworkMeta fireworkMeta) {
        if (entity instanceof Firework) {
            ((Firework)entity).setFireworkMeta(fireworkMeta);
        }
        return this;
    }

    public EEntity detonate() {
        if (entity instanceof Firework) {
            ((Firework)entity).detonate();
        }
        return this;
    }

    public EEntity detonate(boolean detonate) {
        if (entity instanceof Firework && detonate) {
            ((Firework)entity).detonate();
        }
        return this;
    }
    //endregion



    //region Item

    public int getPickupDelay() {
        if (entity instanceof Item) {
            return ((Item)entity).getPickupDelay();
        }
        return 0;
    }

    public EEntity setPickupDelay(int delay) {
        if (entity instanceof Item) {
            ((Item)entity).setPickupDelay(delay);
        }
        return this;
    }
    //endregion



    //region ItemFrame

    public Rotation getRotation() {
        if (entity instanceof ItemFrame) {
            return ((ItemFrame)entity).getRotation();
        }
        return Rotation.NONE;
    }

    public EEntity setRotation(Rotation rotation) {
        if (entity instanceof ItemFrame) {
            ((ItemFrame)entity).setRotation(rotation);
        }
        return this;
    }
    //endregion



    //region Painting

    public Art getArt() {
        if (entity instanceof Painting) {
            return ((Painting)entity).getArt();
        }
        return Art.ALBAN;
    }

    public EEntity setArt(Art art) {
        if (entity instanceof Painting) {
            ((Painting)entity).setArt(art);
        }
        return this;
    }

    public EEntity setArt(Art art, Boolean force) {
        if (entity instanceof Painting) {
            ((Painting)entity).setArt(art, force);
        }
        return this;
    }
    //endregion



    //region FishHook

    public double getBiteChance() {
        if (entity instanceof FishHook) {
            return ((FishHook)entity).getBiteChance();
        }
        return 0d;
    }

    public EEntity setBiteChance(double chance) {
        if (entity instanceof FishHook) {
            ((FishHook)entity).setBiteChance(chance);
        }
        return this;
    }
    //endregion



    //region TNTPrimed

    public int getFuseTicks() {
        if (entity instanceof TNTPrimed) {
            return ((TNTPrimed)entity).getFuseTicks();
        }
        return 0;
    }

    public EEntity setFuseTicks(int fuseTicks) {
        if (entity instanceof TNTPrimed) {
            ((TNTPrimed)entity).setFuseTicks(fuseTicks);
        }
        return this;
    }

    public EEntity getSource() {
        if (entity instanceof TNTPrimed) {
            return new EEntity(((TNTPrimed)entity).getSource());
        }
        return null;
    }
    //endregion



    //region Explosive

    public double getYield() {
        if (entity instanceof Explosive) {
            return ((Explosive)entity).getYield();
        }
        return 0f;
    }

    public EEntity setYield(double yield) {
        if (entity instanceof Explosive) {
            ((Explosive)entity).setYield((float)yield);
        }
        return this;
    }

    public Boolean isIncendiary() {
        if (entity instanceof Explosive) {
            return ((Explosive)entity).isIncendiary();
        }
        return false;
    }

    public EEntity setIsIncendiary(Boolean isIncendiary) {
        if (entity instanceof Explosive) {
            ((Explosive)entity).setIsIncendiary(isIncendiary);
        }
        return this;
    }
    //endregion



    //region WitherSkull

    public Boolean isCharged() {
        if (entity instanceof WitherSkull) {
            return ((WitherSkull)entity).isCharged();
        }
        return false;
    }

    public EEntity setCharged(Boolean charged) {
        if (entity instanceof WitherSkull) {
            ((WitherSkull)entity).setCharged(charged);
        }
        return this;
    }
    //endregion



    //region LightningStrike

    public Boolean isEffect() {
        if (entity instanceof LightningStrike) {
            return ((LightningStrike)entity).isEffect();
        }
        return false;
    }
    //endregion



    //region Bat

    public Boolean isAwake() {
        if (entity instanceof Bat) {
            return ((Bat)entity).isAwake();
        }
        return false;
    }

    public EEntity setAwake(Boolean awake) {
        if (entity instanceof Bat) {
            ((Bat)entity).setAwake(awake);
        }
        return this;
    }
    //endregion



    //region Creeper

    public Boolean isPowered() {
        if (entity instanceof Creeper) {
            return ((Creeper)entity).isPowered();
        }
        return false;
    }

    public EEntity setPowered(Boolean powered) {
        if (entity instanceof Creeper) {
            ((Creeper)entity).setPowered(powered);
        }
        return this;
    }
    //endregion



    //region Enderman

    public MaterialData getCarriedMaterial() {
        if (entity instanceof Enderman) {
            return ((Enderman)entity).getCarriedMaterial();
        }
        return null;
    }

    public EEntity setCarriedMaterial(MaterialData material) {
        if (entity instanceof Enderman) {
            ((Enderman)entity).setCarriedMaterial(material);
        }
        return this;
    }
    //endregion



    //region Guardian

    public Boolean isElder() {
        if (entity instanceof Guardian) {
            return ((Guardian)entity).isElder();
        }
        return false;
    }

    public EEntity setElder(Boolean elder) {
        if (entity instanceof Guardian) {
            ((Guardian)entity).setElder(elder);
        }
        return this;
    }
    //endregion



    //region Horse

    public Horse.Variant getHorseVariant() {
        if (entity instanceof Horse) {
            return ((Horse)entity).getVariant();
        }
        return Horse.Variant.HORSE;
    }

    public EEntity setHorseVariant(Horse.Variant variant) {
        if (entity instanceof Horse) {
            ((Horse)entity).setVariant(variant);
        }
        return this;
    }

    public Horse.Color getHorseColor() {
        if (entity instanceof Horse) {
            return ((Horse)entity).getColor();
        }
        return Horse.Color.WHITE;
    }

    public EEntity setHorseColor(Horse.Color color) {
        if (entity instanceof Horse) {
            ((Horse)entity).setColor(color);
        }
        return this;
    }

    public Horse.Style getHorseStyle() {
        if (entity instanceof Horse) {
            return ((Horse)entity).getStyle();
        }
        return Horse.Style.NONE;
    }

    public EEntity setHorseStyle(Horse.Style style) {
        if (entity instanceof Horse) {
            ((Horse)entity).setStyle(style);
        }
        return this;
    }

    public Boolean isCarryingChest() {
        if (entity instanceof Horse) {
            return ((Horse)entity).isCarryingChest();
        }
        return false;
    }

    public EEntity setCarryingChest(Boolean chest) {
        if (entity instanceof Horse) {
            ((Horse)entity).setCarryingChest(chest);
        }
        return this;
    }

    public int getDomestication() {
        if (entity instanceof Horse) {
            return ((Horse)entity).getDomestication();
        }
        return 0;
    }

    public EEntity setDomestication(int level) {
        if (entity instanceof Horse) {
            ((Horse)entity).setDomestication(level);
        }
        return this;
    }

    public int getMaxDomestication() {
        if (entity instanceof Horse) {
            return ((Horse)entity).getMaxDomestication();
        }
        return 0;
    }

    public EEntity setMaxDomestication(int level) {
        if (entity instanceof Horse) {
            ((Horse)entity).setMaxDomestication(level);
        }
        return this;
    }

    public double getJumpStrength() {
        if (entity instanceof Horse) {
            return ((Horse)entity).getJumpStrength();
        }
        return 0d;
    }

    public EEntity setJumpStrength(double strength) {
        if (entity instanceof Horse) {
            ((Horse)entity).setJumpStrength(strength);
        }
        return this;
    }

    public EItem getHorseArmor() {
        if (entity instanceof Horse) {
            return new EItem(((Horse)entity).getInventory().getArmor());
        }
        return null;
    }

    public EEntity setHorseArmor(EItem stack) {
        if (entity instanceof Horse) {
            ((Horse)entity).getInventory().setArmor(stack);
        }
        return this;
    }
    //endregion



    //region Ocelot

    public Ocelot.Type getCatType() {
        if (entity instanceof Ocelot) {
            return ((Ocelot)entity).getCatType();
        }
        return Ocelot.Type.WILD_OCELOT;
    }

    public EEntity setCatType(Ocelot.Type type) {
        if (entity instanceof Ocelot) {
            ((Ocelot)entity).setCatType(type);
        }
        return this;
    }
    //endregion



    //region Rabbit

    public Rabbit.Type getRabbitType() {
        if (entity instanceof Rabbit) {
            return ((Rabbit)entity).getRabbitType();
        }
        return Rabbit.Type.GOLD;
    }

    public EEntity setRabbitType(Rabbit.Type type) {
        if (entity instanceof Rabbit) {
            ((Rabbit)entity).setRabbitType(type);
        }
        return this;
    }
    //endregion



    //region PigZombie

    public int getAnger() {
        if (entity instanceof PigZombie) {
            return ((PigZombie)entity).getAnger();
        }
        return 0;
    }

    public EEntity setAnger(int level) {
        if (entity instanceof PigZombie) {
            ((PigZombie)entity).setAnger(level);
        }
        return this;
    }
    //endregion



    //region Sheep

    public Boolean isSheared() {
        if (entity instanceof Sheep) {
            return ((Sheep)entity).isSheared();
        }
        return false;
    }

    public EEntity setSheared(Boolean flag) {
        if (entity instanceof Sheep) {
            ((Sheep)entity).setSheared(flag);
        }
        return this;
    }
    //endregion



    //region Skeleton

    public Skeleton.SkeletonType getSkeletonType() {
        if (entity instanceof Skeleton) {
            return ((Skeleton)entity).getSkeletonType();
        }
        return Skeleton.SkeletonType.NORMAL;
    }

    public boolean isWitherSkeleton() {
        if (entity instanceof Skeleton) {
            return ((Skeleton)entity).getSkeletonType() == Skeleton.SkeletonType.WITHER;
        }
        return false;
    }

    public EEntity setWitherSkeleton(Boolean wither) {
        if (entity instanceof Skeleton) {
            if (wither) {
                ((Skeleton)entity).setSkeletonType(Skeleton.SkeletonType.WITHER);
            } else {
                ((Skeleton)entity).setSkeletonType(Skeleton.SkeletonType.NORMAL);
            }
        }
        return this;
    }

    public EEntity setSkeletonType(Skeleton.SkeletonType type) {
        if (entity instanceof Skeleton) {
            ((Skeleton)entity).setSkeletonType(type);
        }
        return this;
    }
    //endregion



    //region Slime

    public int getSize() {
        if (entity instanceof Slime) {
            return ((Slime)entity).getSize();
        }
        return 0;
    }

    public EEntity setSize(int size) {
        if (entity instanceof Slime) {
            ((Slime)entity).setSize(size);
        }
        return this;
    }
    //endregion



    //region villager

    public List<MerchantRecipe> getRecipes() {
        if (entity instanceof Villager) {
            return ((Villager)entity).getRecipes();
        }
        return new ArrayList<>();
    }

    public EEntity setRecipes(List<MerchantRecipe> recipes) {
        if (entity instanceof Villager) {
            ((Villager)entity).setRecipes(recipes);
        }
        return this;
    }

    public MerchantRecipe getRecipe(int i) {
        if (entity instanceof Villager) {
            return ((Villager)entity).getRecipe(i);
        }
        return null;
    }

    public EEntity setRecipe(int i, MerchantRecipe recipe) {
        if (entity instanceof Villager) {
            ((Villager)entity).setRecipe(i, recipe);
        }
        return this;
    }

    public int getRecipeCount() {
        if (entity instanceof Villager) {
            return ((Villager)entity).getRecipeCount();
        }
        return 0;
    }

    public Boolean isTrading() {
        if (entity instanceof Villager) {
            return ((Villager)entity).isTrading();
        }
        return false;
    }

    public HumanEntity getTrader() {
        if (entity instanceof Villager) {
            return ((Villager)entity).getTrader();
        }
        return null;
    }

    public int getRiches() {
        if (entity instanceof Villager) {
            return ((Villager)entity).getRiches();
        }
        return 0;
    }

    public EEntity setRiches(int riches) {
        if (entity instanceof Villager) {
            ((Villager)entity).setRiches(riches);
        }
        return this;
    }
    //endregion



    //region IronGolem

    public Boolean isPlayerCreated() {
        if (entity instanceof IronGolem) {
            return ((IronGolem)entity).isPlayerCreated();
        }
        return false;
    }

    public EEntity setPlayerCreated(Boolean playerCreated) {
        if (entity instanceof IronGolem) {
            ((IronGolem)entity).setPlayerCreated(playerCreated);
        }
        return this;
    }
    //endregion


    //region Zombie

    public Boolean isVillager() {
        if (entity instanceof Zombie) {
            return ((Zombie)entity).isVillager();
        }
        return false;
    }
    //endregion

    //endregion

}
