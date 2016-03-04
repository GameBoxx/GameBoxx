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

import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;

public class EntityStack {

    private final List<EEntity> entities = new ArrayList<>();

    public EntityStack() {}

    public EntityStack(EEntity[] entities) {
        this.entities.addAll(Arrays.asList(entities));
    }

    public EntityStack(Collection<EEntity> entities) {
        this.entities.addAll(entities);
    }

    public EntityStack(EEntity entity) {
        EEntity current = entity;
        while (current.hasPassenger()) {
            current = entity.getPassenger();
            entities.add(current);
        }
        current = entity;
        while (current.hasVehicle()) {
            current = entity.getVehicle();
            entities.set(0, current);
        }
    }

    public void add(EEntity entity) {
        if (entity != null) {
            entities.add(entity);
        }
    }

    public void remove(EEntity entity) {
        if (entity != null) {
            entities.remove(entity);
        }
    }

    public void remove(int index) {
        if (index < entities.size()) {
            entities.remove(index);
        }
    }

    public EEntity get(int index) {
        return entities.get(index);
    }

    public EEntity getBottom() {
        return entities.get(0);
    }

    public EEntity getTop() {
        return entities.get(entities.size()-1);
    }

    public List<EEntity> getEntities() {
        return entities;
    }

    public int getAmount() {
        return entities.size();
    }

    public void clear() {
        entities.clear();
    }

    public void unstack() {
        for (int i = 0; i < entities.size()-1; i++) {
            entities.get(i).eject();
        }
    }

    public void stack() {
        for (int i = 0; i < entities.size()-1; i++) {
            entities.get(i).setPassenger(entities.get(i+1));
        }
    }

    public void killAll() {
        for (EEntity entity : entities) {
            entity.remove();
        }
    }

    public void teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        unstack();
        for (EEntity entity : entities) {
            entity.bukkit().teleport(location, cause);
        }
        stack();
    }

}
