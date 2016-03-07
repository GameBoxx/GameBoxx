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

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleEffect {

    private final Particle particle;
    private final int amount;
    private final double offsetX;
    private final double offsetY;
    private final double offsetZ;
    private final double speed;
    private final Object data;

    public ParticleEffect(Particle particle) {
        this.particle = particle;
        this.amount = 1;
        this.offsetX = 0;
        this.offsetY = 0;
        this.offsetZ = 0;
        this.speed = 1;
        this.data = null;
    }

    public ParticleEffect(Particle particle, int amount) {
        this.particle = particle;
        this.amount = amount;
        this.offsetX = 0;
        this.offsetY = 0;
        this.offsetZ = 0;
        this.speed = 1;
        this.data = null;
    }

    public ParticleEffect(Particle particle, int amount, double offsetX, double offsetY, double offsetZ) {
        this.particle = particle;
        this.amount = amount;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = 1;
        this.data = null;
    }

    public ParticleEffect(Particle particle, int amount, double offsetX, double offsetY, double offsetZ, double speed) {
        this.particle = particle;
        this.amount = amount;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
        this.data = null;
    }

    public ParticleEffect(Particle particle, int amount, double offsetX, double offsetY, double offsetZ, double speed, Object data) {
        this.particle = particle;
        this.amount = amount;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
        this.data = data;
    }

    public Particle getParticle() {
        return particle;
    }

    public int getAmount() {
        return amount;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getOffsetZ() {
        return offsetZ;
    }

    public double getSpeed() {
        return speed;
    }

    public Object getData() {
        return data;
    }

    public boolean hasData() {
        return data != null && data.getClass().equals(particle.getDataType());
    }

    public void displayWorld(Location location) {
        if (hasData()) {
            location.getWorld().spawnParticle(particle, location, amount, offsetX, offsetY, offsetZ, speed, data);
        } else {
            location.getWorld().spawnParticle(particle, location, amount, offsetX, offsetY, offsetZ, speed);
        }
    }

    public void displayPlayer(Player player) {
        displayPlayer(player, player.getLocation());
    }

    public void displayPlayer(Player player, Location location) {
        if (hasData()) {
            player.spawnParticle(particle, location, amount, offsetX, offsetY, offsetZ, speed, data);
        } else {
            player.spawnParticle(particle, location, amount, offsetX, offsetY, offsetZ, speed);
        }
    }

}
