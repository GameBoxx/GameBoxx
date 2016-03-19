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

package info.gameboxx.gameboxx.options.single;

import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.util.Utils;
import info.gameboxx.gameboxx.util.entity.EEntity;
import info.gameboxx.gameboxx.util.entity.EntityParser;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.util.List;

public class EntityO extends SingleOption<EEntity, EntityO> {

    @Override
    public boolean parse(CommandSender sender, String input) {

        if (input.startsWith("#") || input.startsWith("@")) {
            PlayerO playerOption = new PlayerO();
            playerOption.parse(sender, input.substring(1));

            if (!playerOption.hasValue()) {
                error = playerOption.getError();
                return false;
            }

            if (input.startsWith("#")) {
                Block[] blocks = playerOption.getValue().getLineOfSight(Utils.TRANSPARENT_MATERIALS, 32).toArray(new Block[0]);
                List<Entity> near = playerOption.getValue().getNearbyEntities(32, 32, 32);
                for (Block b : blocks) {
                    for (Entity e : near) {
                        if (e.equals(playerOption.getValue())) {
                            continue;
                        }
                        if (e.getLocation().distance(b.getLocation()) < 2) {
                            value = new EEntity(e);
                            return true;
                        }
                    }
                }
                error = Msg.getString("entity.not-looking");
                return false;
            }

            if (input.startsWith("@")) {
                List<Entity> near = playerOption.getValue().getNearbyEntities(32, 32, 32);
                Entity match = null;
                double shortestDistance = Double.MAX_VALUE;
                for (Entity e : near) {
                    if (e.equals(playerOption.getValue())) {
                        continue;
                    }
                    double distance = e.getLocation().distanceSquared(playerOption.getValue().getLocation());
                    if (match == null || distance < shortestDistance) {
                        shortestDistance = distance;
                        match = e;
                    }
                }
                if (match == null) {
                    error = Msg.getString("entity.none-nearby");
                    return false;
                }
                value = new EEntity(match);
                return true;
            }
        }

        //TODO: Entity selector stuff
        error = "Not yet implemented! Use @ to get the nearest or # to get the entity you're looking at!";
        return false;
    }

    @Override
    public String serialize() {
        return serialize(getValue());
    }

    public static String serialize(EEntity entity) {
        return entity == null ? null : new EntityParser(entity).getString();
    }

    @Override
    public String getTypeName() {
        return "Entity";
    }

    @Override
    public EntityO clone() {
        return super.cloneData(new EntityO());
    }
}
