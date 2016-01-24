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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * A {@link Sound} with a volume and pitch.
 * Has a bunch of methods to play the sounds.
 */
public class SoundEffect {

    private Sound sound;
    private float volume = 1;
    private float pitch = 1;

    /**
     * Creates a new SoundData instance with the specified volume and pitch.
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
     * Creates a new SoundData instance with default volume and pitch.
     * @param sound The {@link Sound} effect.
     */
    public SoundEffect(Sound sound) {
        this.sound = sound;
    }

    /**
     * Get the {@link Sound} effect.
     * @return The {@link Sound} effect.
     */
    public Sound getSound() {
        return sound;
    }

    /**
     * Set the {@link Sound} effect.
     * @param sound The {@link Sound} effect.
     */
    public void setSound(Sound sound) {
        this.sound = sound;
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
     * @see {@link Player#playSound(Location, Sound, float, float)}
     */
    public void play(Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Play the sound for the player only at the specified location.
     * @see {@link Player#playSound(Location, Sound, float, float)}
     */
    public void play(Player player, Location location) {
        player.playSound(location, sound, volume, pitch);
    }

    /**
     * Play the sound for the player only at the player location with a certain offset.
     * @see {@link Player#playSound(Location, Sound, float, float)}
     */
    public void play(Player player, double offsetX, double offsetY, double offsetZ) {
        player.playSound(player.getLocation().add(offsetX, offsetY, offsetZ), sound, volume, pitch);
    }

    /**
     * Play the sound for all players in the specified world.
     * @see {@link Player#playSound(Location, Sound, float, float)}
     */
    public void play(World world) {
        play(Bukkit.getServer().getOnlinePlayers());
    }

    /**
     * Play the sound for all players in the specified world.
     * The sound will be played at each players location with the specified offset.
     * @see {@link Player#playSound(Location, Sound, float, float)}
     */
    public void play(World world, double offsetX, double offsetY, double offsetZ) {
        play(Bukkit.getServer().getOnlinePlayers(), offsetX, offsetY, offsetZ);
    }

    /**
     * Play the sound for all the listed players specified.
     * The sound will be played at each players location.
     * @see {@link Player#playSound(Location, Sound, float, float)}
     */
    public void play(Collection<? extends Player> players) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            play(player);
        }
    }

    /**
     * Play the sound for all the listed players specified.
     * The sound will be played at each players location with the specified offset.
     * @see {@link Player#playSound(Location, Sound, float, float)}
     */
    public void play(Collection<? extends Player> players, double offsetX, double offsetY, double offsetZ) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            play(player, offsetX, offsetY, offsetZ);
        }
    }

    /**
     * Play the sound for all players nearby the specified location.
     * @see {@link World#playSound(Location, Sound, float, float)}
     */
    public void play(World world, Location location) {
        world.playSound(location, sound, volume, pitch);
    }
}
