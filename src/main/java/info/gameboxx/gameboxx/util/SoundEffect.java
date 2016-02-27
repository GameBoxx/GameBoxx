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

package info.gameboxx.gameboxx.util;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * A {@link Sound} with a volume and pitch.
 * Has a bunch of methods to play the sounds.
 */
public class SoundEffect {

    private Sound sound;
    private String customSound;
    private float volume = 1;
    private float pitch = 1;

    /**
     * Creates a new SoundEffect instance with the specified volume and pitch.
     * @param sound The {@link Sound} effect.
     * @param volume The volume (This should be a float between 0,2)
     * @param pitch The pitch (This should be a float between 0,2)
     */
    public SoundEffect(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    /**
     * Creates a new SoundEffect instance with default volume and pitch.
     * @param sound The {@link Sound} effect.
     */
    public SoundEffect(Sound sound) {
        this.sound = sound;
    }

    /**
     * Creates a new SoundEffect instance with the specified volume and pitch.
     * @param customSound The name of the custom sound effect.
     *                    This name must be the name of a sound defined in a server resource pack.
     *                    If no sound is found with this name and you play the sound nothing will happen.
     * @param volume The volume (This should be a float between 0,2)
     * @param pitch The pitch (This should be a float between 0,2)
     */
    public SoundEffect(String customSound, float volume, float pitch) {
        this.customSound = customSound;
        this.volume = volume;
        this.pitch = pitch;
    }

    /**
     * Creates a new SoundEffect instance with default volume and pitch.
     * @param customSound The name of the custom sound effect.
     *                    This name must be the name of a sound defined in a server resource pack.
     *                    If no sound is found with this name and you play the sound nothing will happen.
     */
    public SoundEffect(String customSound) {
        this.customSound = customSound;
    }

    /**
     * Get the {@link Sound} effect.
     * @return The {@link Sound} effect. (May be {@code null} when constructed with a custom sound!)
     */
    public Sound getSound() {
        return sound;
    }

    /**
     * Get the name of the custom sound effect.
     * @return The name of the custom sound effect. (May be {@code null} when constructed with a regular sound!)
     */
    public String getCustomSound() {
        return customSound;
    }

    /**
     * Set the {@link Sound} effect.
     * @param sound The {@link Sound} effect.
     */
    public void setSound(Sound sound) {
        this.sound = sound;
    }

    /**
     * Set the custom sound effect.
     * This name must be the name of a sound defined in a server resource pack.
     * If no sound is found with this name and you play the sound nothing will happen.
     * @param customSound Name of the custom sound effect.
     */
    public void setCustomSound(String customSound) {
        this.customSound = customSound;
    }

    /**
     * Get the volume for the sound.
     * @return The volume.
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Set the volume for the sound.
     * @param volume The volume. (This should be a float between 0,2)
     */
    public void setVolume(float volume) {
        this.volume = volume;
    }

    /**
     * Get the pitch for the sound.
     * @return The pitch.
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * SEt the pitch for the sound.
     * @param pitch The pitch. (This should be a float between 0,2)
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * Play the sound for the player only at the player location.
     * @see Player#playSound(Location, Sound, float, float)
     */
    public void play(Player player) {
        play(player, player.getLocation());
    }

    /**
     * Play the sound for the player only at the specified location.
     * @see Player#playSound(Location, Sound, float, float)
     */
    public void play(Player player, Location location) {
        if (customSound != null) {
            player.playSound(location, customSound, volume, pitch);
        } else {
            player.playSound(location, sound, volume, pitch);
        }
    }

    /**
     * Play the sound for the player only at the player location with a certain offset.
     * @see Player#playSound(Location, Sound, float, float)
     */
    public void play(Player player, double offsetX, double offsetY, double offsetZ) {
        play(player, player.getLocation().add(offsetX, offsetY, offsetZ));
    }

    /**
     * Play the sound for all players in the specified world.
     * @see Player#playSound(Location, Sound, float, float)
     */
    public void play(World world) {
        play(Bukkit.getServer().getOnlinePlayers());
    }

    /**
     * Play the sound for all players in the specified world.
     * The sound will be played at each players location with the specified offset.
     * @see Player#playSound(Location, Sound, float, float)
     */
    public void play(World world, double offsetX, double offsetY, double offsetZ) {
        play(Bukkit.getServer().getOnlinePlayers(), offsetX, offsetY, offsetZ);
    }

    /**
     * Play the sound for all the listed players specified.
     * The sound will be played at each players location.
     * @see Player#playSound(Location, Sound, float, float)
     */
    public void play(Collection<? extends Player> players) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            play(player);
        }
    }

    /**
     * Play the sound for all the listed players specified.
     * The sound will be played at each players location with the specified offset.
     * @see Player#playSound(Location, Sound, float, float)
     */
    public void play(Collection<? extends Player> players, double offsetX, double offsetY, double offsetZ) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            play(player, offsetX, offsetY, offsetZ);
        }
    }

    /**
     * Play the sound for all players nearby the specified location.
     * <b>This does not support custom sounds!</b>
     * @see World#playSound(Location, Sound, float, float)
     */
    public void play(World world, Location location) {
        if (sound != null) {
            world.playSound(location, sound, volume, pitch);
        }
    }
}
