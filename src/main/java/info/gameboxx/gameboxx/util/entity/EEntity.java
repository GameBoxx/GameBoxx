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

import info.gameboxx.gameboxx.util.Str;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
        this(location.getWorld().spawn(location, type.getEntityClass()));
    }
    //endregion



    // ##################################################
    // ################## UTILS/CUSTOM ##################
    // ##################################################
    //region Utils

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
    public Integer getID() {
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
    public List<EEntity> getNearbyEntities(Double x, Double y, Double z) {
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
    public List<EEntity> getNearbyEntities(Double radius) {
        List<Entity> nearby = entity.getNearbyEntities(radius, radius, radius);
        List<EEntity> converted = new ArrayList<>();
        for (Entity e : nearby) {
            converted.add(new EEntity(e));
        }
        return converted;
    }
    //endregion


    //region Teleportation
    //TODO: Methods for safe teleportation with mounts and such

    /**
     * Teleports this entity to the given location.
     * If this entity is riding a vehicle, it will be dismounted prior to teleportation.
     *
     * @param target Entity to teleport this entity to
     * @return true if the teleport was successful
     */
    public boolean teleport(Location target) {
        return entity.teleport(target);
    }

    /**
     * Teleports this entity to the target Entity.
     * If this entity is riding a vehicle, it will be dismounted prior to teleportation.
     *
     * @param target Entity to teleport this entity to
     * @param cause The cause of this teleportation
     * @return true if the teleport was successful
     */
    public boolean teleport(Location target, PlayerTeleportEvent.TeleportCause cause) {
        return entity.teleport(target, cause);
    }

    /**
     * Teleports this entity to the target Entity.
     * If this entity is riding a vehicle, it will be dismounted prior to teleportation.
     *
     * @param target Entity to teleport this entity to
     * @return true if the teleport was successful
     */
    public boolean teleport(Entity target) {
        return entity.teleport(target);
    }


    /**
     * Teleports this entity to the target Entity.
     * If this entity is riding a vehicle, it will be dismounted prior to teleportation.
     *
     * @param target Entity to teleport this entity to
     * @param cause The cause of this teleportation
     * @return true if the teleport was successful
     */
    public boolean teleport(Entity target, PlayerTeleportEvent.TeleportCause cause) {
        return entity.teleport(target, cause);
    }

    /**
     * Teleports this entity to the target Entity.
     * If this entity is riding a vehicle, it will be dismounted prior to teleportation.
     *
     * @param target EEntity to teleport this entity to
     * @return true if the teleport was successful
     */
    public boolean teleport(EEntity target) {
        return entity.teleport(target.bukkit());
    }

    /**
     * Teleports this entity to the target Entity.
     * If this entity is riding a vehicle, it will be dismounted prior to teleportation.
     *
     * @param target EEntity to teleport this entity to
     * @param cause The cause of this teleportation
     * @return true if the teleport was successful
     */
    public boolean teleport(EEntity target, PlayerTeleportEvent.TeleportCause cause) {
        return entity.teleport(target.bukkit(), cause);
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
    public EEntity setVelocity(Double x, Double y, Double z) {
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
        if (entity instanceof Fireball) {
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
        if (entity instanceof Fireball) {
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
        return entity.isEmpty();
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
    public Float getFallDistance() {
        return entity.getFallDistance();
    }

    /**
     * Sets the fall distance for this entity
     *
     * @param distance the new distance
     * @return this
     */
    public EEntity setFallDistance(Float distance) {
        entity.setFallDistance(distance);
        return this;
    }

    /**
     * Returns the entity's current fire ticks (ticks before the entity stops being on fire).
     *
     * @return fire ticks
     */
    public Integer getFireTicks() {
        return entity.getFireTicks();
    }

    /**
     * Sets the entity's current fire ticks (ticks before the entity stops being on fire).
     *
     * @param ticks ticks remaining
     * @return this instance
     */
    public EEntity setFireTicks(Integer ticks) {
        entity.setFireTicks(ticks);
        return this;
    }

    /**
     * Returns the entity's maximum fire ticks.
     *
     * @return max fire ticks
     */
    public Integer getMaxFireTicks() {
        return entity.getMaxFireTicks();
    }

    /**
     * Gets the amount of ticks this entity has lived for.
     * This is the equivalent to age in entities.
     *
     * @return amount of ticks the entity lived
     */
    public Integer getTicksLived() {
        return entity.getTicksLived();
    }

    /**
     * Sets the amount of ticks this entity has lived for.
     * This is the equivalent to age in entities.
     *
     * @param ticks amount of ticks the entity lived
     * @return this instance
     */
    public EEntity setTicksLived(Integer ticks) {
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


    //region Tags

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
    public EEntity damage(Double amount) {
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
    public EEntity damage(Double amount, Entity source) {
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
    public EEntity damage(Double amount, EEntity source) {
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
     * @return health represented from 0 to {@link #getMaxHealth()} ({@code null} when not damageable)
     */
    public Double getHealth() {
        if (entity instanceof Damageable) {
            return ((Damageable)entity).getHealth();
        }
        return null;
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
    public EEntity setHealth(Double amount) {
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
     * @return maximum health. ({@code null} when not damageable)
     */
    public Double getMaxHealth() {
        if (entity instanceof Damageable) {
            return ((Damageable)entity).getMaxHealth();
        }
        return null;
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
    public EEntity setMaxHealth(Double amount) {
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
     * @return height of the living entity's eyes above its location (0d when not living)
     */
    public Double getEyeHeight() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getEyeHeight();
        }
        return null;
    }

    /**
     * Gets the height of the living entity's eyes above its Location.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param ignoreSneaking if set to true, the effects of sneaking will be ignored
     * @return height of the living entity's eyes above its location (0d when not living)
     */
    public Double getEyeHeight(Boolean ignoreSneaking) {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getEyeHeight(ignoreSneaking);
        }
        return 0d;
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
     * @return amount of air remaining ({@code null} when not living)
     */
    public Integer getRemainingAir() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getRemainingAir();
        }
        return null;
    }

    /**
     * Sets the amount of air that the living entity has remaining, in ticks.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param ticks amount of air remaining
     * @return this instance
     */
    public EEntity setRemainingAir(Integer ticks) {
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
     * @return maximum amount of air ({@code null} when not living)
     */
    public Integer getMaximumAir() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getMaximumAir();
        }
        return null;
    }

    /**
     * Sets the maximum amount of air the living entity can have, in ticks.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param ticks maximum amount of air
     * @return this instance
     */
    public EEntity setMaximumAir(Integer ticks) {
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
     * @return maximum no damage ticks ({@code null} when not living)
     */
    public Integer getMaximumNoDamageTicks() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getMaximumNoDamageTicks();
        }
        return null;
    }

    /**
     * Sets the living entity's current maximum no damage ticks.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param ticks maximum amount of no damage ticks
     * @return this instance
     */
    public EEntity setMaximumNoDamageTicks(Integer ticks) {
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
     * @return damage taken since the last no damage ticks time period ({@code null} when not living)
     */
    public Double getLastDamage() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getLastDamage();
        }
        return null;
    }

    /**
     * Sets the damage dealt within the current no damage ticks time period.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param damage amount of damage
     * @return this instance
     */
    public EEntity setLastDamage(Double damage) {
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
     * @return amount of no damage ticks ({@code null} when not living)
     */
    public Integer getNoDamageTicks() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getNoDamageTicks();
        }
        return null;
    }

    /**
     * Sets the living entity's current no damage ticks.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param ticks amount of no damage ticks
     * @return this instance
     */
    public EEntity setNoDamageTicks(Integer ticks) {
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


    //region Potion Effects
    //TODO: Methods to apply potion effects safely. (increase duration for same effect, override when effect is better and ignore when effect is worse)

    /**
     * Adds the given {@link PotionEffect} to the living entity.
     * <p/>
     * Only one potion effect can be present for a given {@link PotionEffectType}.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param effect PotionEffect to be added
     * @return this instance
     */
    public EEntity addPotionEffect(PotionEffect effect) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).addPotionEffect(effect);
        }
        return this;
    }

    /**
     * Adds the given {@link PotionEffect} to the living entity.
     * <p/>
     * Only one potion effect can be present for a given {@link PotionEffectType}.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param effect PotionEffect to be added
     * @param force whether conflicting effects should be removed/overwritten
     * @return this instance
     */
    public EEntity addPotionEffect(PotionEffect effect, boolean force) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).addPotionEffect(effect, force);
        }
        return this;
    }

    /**
     * Attempts to add all of the given {@link PotionEffect} to the living entity.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param effects the effects to add
     * @return this instance
     */
    public EEntity addPotionEffects(Collection<PotionEffect> effects) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).addPotionEffects(effects);
        }
        return this;
    }

    /**
     * Returns whether the living entity already has an existing effect of the given {@link PotionEffectType} applied to it.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param type the potion type to check
     * @return whether the living entity has this potion effect active on them
     */
    public Boolean hasPotionEffect(PotionEffectType type) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).hasPotionEffect(type);
        }
        return false;
    }

    /**
     * Removes any effects present of the given {@link PotionEffectType}.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @param type the potion type to remove
     * @return this instance
     */
    public EEntity removePotionEffect(PotionEffectType type) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).removePotionEffect(type);
        }
        return this;
    }

    /**
     * Returns all currently active {@link PotionEffect}s on the living entity.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @return a collection of {@link PotionEffect}s
     */
    public Collection<PotionEffect> getActivePotionEffects() {
        if (entity instanceof LivingEntity) {
            ((LivingEntity)entity).getActivePotionEffects();
        }
        return new ArrayList<>();
    }

    //endregion


    //region Equipment
    //TODO: Wrapper methods for EntityEquipment like setHelmet and such.

    /**
     * Gets the inventory with the equipment worn by the living entity.
     * <p/>
     * <b>Entities: </b> {@link LivingEntity}
     *
     * @return the living entity's inventory
     */
    public EntityEquipment getEquipment() {
        if (entity instanceof LivingEntity) {
            return ((LivingEntity) entity).getEquipment();
        }
        return null;
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
    public Integer getAge() {
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
    public EEntity setAge(Integer age) {
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
     * <b>Entities: </b> {@link Ageable}
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
     * <b>Entities: </b> {@link Ageable}
     *
     * @return true if the animal is a baby. (false when not Ageable)
     */
    public Boolean isBaby() {
        if (entity instanceof Ageable) {
            return !((Ageable)entity).isAdult();
        }
        return false;
    }

    /**
     * Sets the age of the animal to a baby
     * <p/>
     * <b>Entities: </b> {@link Ageable}
     *
     * @return this instance
     */
    public EEntity setBaby() {
        if (entity instanceof Ageable) {
            ((Ageable)entity).setBaby();
        }
        return this;
    }

    /**
     * Sets the age of the animal to baby or adult.
     * <p/>
     * <b>Entities: </b> {@link Ageable}
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
        }
        return this;
    }


    /**
     * Returns true if the animal is an adult.
     * <p/>
     * <b>Entities: </b> {@link Ageable}
     *
     * @return true if the animal is an adult. (true when not Ageable)
     */
    public Boolean isAdult() {
        if (entity instanceof Ageable) {
            return ((Ageable)entity).isAdult();
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

    //endregion



    // ##################################################
    // ################## PROJECTILES ###################
    // ##################################################
    //region Projectile

    //endregion



    // ##################################################
    // #################### HANGING #####################
    // ##################################################
    //region Hanging

    //endregion



    // ##################################################
    // #################### ENTITIES ####################
    // ##################################################
    //region Entities

    //region ArmorStand

    //endregion



    //region Arrow

    //endregion



    //region Boat

    //endregion



    //region Minecarts

    //endregion



    //region ExperienceOrb

    //endregion



    //region FallingBlock

    //endregion



    //region Firework

    //endregion



    //region Item

    //endregion



    //region ItemFrame

    //endregion



    //region Painting

    //endregion



    //region ThrownPotion

    //endregion



    //region PrimedTNT

    //endregion



    //region WitherSkull

    //endregion



    //region Bat

    //endregion



    //region Creeper

    //endregion



    //region Enderman

    //endregion



    //region Guardian

    //endregion



    //region Horse

    //endregion



    //region Ocelot

    //endregion



    //region Rabbit

    //endregion



    //region Pig

    //endregion



    //region PigZombie

    //endregion



    //region Sheep

    //endregion



    //region Slime

    //endregion



    //region villager

    //endregion



    //region region Wolf

    //endregion



    //region Zombie

    //endregion
    //endregion
}
