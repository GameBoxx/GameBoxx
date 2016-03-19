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
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.util.Parse;
import info.gameboxx.gameboxx.util.Utils;
import info.gameboxx.gameboxx.util.cuboid.Cuboid;
import info.gameboxx.gameboxx.util.cuboid.SelectionManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class CuboidO extends SingleOption<Cuboid, CuboidO> {

    @Override
    public boolean parse(CommandSender sender, String input) {
        //Get cuboid from player. @[Player name/uuid]
        if (input.startsWith("@")) {
            PlayerO playerOption = new PlayerO();
            playerOption.parse(sender, input.substring(1));
            if (!playerOption.hasValue()) {
                error = playerOption.getError();
                return false;
            }
            value = SelectionManager.inst().getSelection(playerOption.getValue());
            if (!hasValue()) {
                if (playerOption.getValue().equals(sender)) {
                    error = Msg.getString("cuboid.no-selection");
                } else {
                    error = Msg.getString("cuboid.no-selection-other", Param.P("player", playerOption.getValue().getName()));
                }
                return false;
            }
            return true;
        }

        Location pos1 = new Location(null, 0, 0, 0, 0f, 0f);
        Location pos2 = new Location(null, 0, 0, 0, 0f, 0f);


        //Split string by semicolon like x,y,z:x,y,z:world or x,y,z:x,y,z:@player
        String[] split = input.split(":");
        if (split.length > 1) {
            String data = split[split.length - 1];

            if (data.equals("@")) {
                pos1 = Utils.getLocation(sender);
                if (pos1 == null) {
                    error = Msg.getString("selector-console-player", Param.P("type", data));
                    return false;
                }
                pos2 = pos1.clone();
            } else if (data.startsWith("@")) {
                //Get world/location from player
                PlayerO playerOption = new PlayerO();
                playerOption.parse(sender, data.substring(1));
                if (!playerOption.hasValue()) {
                    error = playerOption.getError();
                    return false;
                }
                pos1 = playerOption.getValue().getLocation();
                pos2 = playerOption.getValue().getLocation();
            } else {
                //Get world.
                WorldO worldOption = new WorldO();
                worldOption.parse(sender, data);
                if (!worldOption.hasValue()) {
                    error = worldOption.getError();
                    return false;
                }
                pos1.setWorld(worldOption.getValue());
                pos2.setWorld(worldOption.getValue());
            }
        }

        if (pos1.getWorld() == null || pos2.getWorld() == null) {
            error = Msg.getString("cuboid.missing-world", Param.P("input", input));
            return false;
        }

        //Get the coords x,y,z:x,y,z
        for (int i = 0; i < split.length - 1; i++) {
            String[] coords = split[i].split(",");

            if (coords.length < 3) {
                error = Msg.getString("cuboid.missing-coords", Param.P("input", split[i]));
                return false;
            }

            Location pos = null;
            if (i == 0) {
                pos = pos1;
            } else {
                pos = pos2;
            }


            Map<String, Object> locMap = pos.serialize();
            String[] mapKeys = new String[] {"x", "y", "z"};

            for (int c = 0; c < coords.length && c < mapKeys.length; c++) {
                String value = coords[c];
                //Check if it's a relative value or not.
                boolean relative = false;
                if (value.startsWith("~") || value.isEmpty()) {
                    value = value.isEmpty() ? value : value.substring(1);
                    relative = true;
                }
                //Parse the value.
                Integer val = Parse.Int(value);
                if (val == null && !value.isEmpty()) {
                    error = Msg.getString("axis-invalid-int", Param.P("axis", mapKeys[i]), Param.P("input", value));
                    return false;
                }

                //Add relative coords to the value.
                if (relative) {
                    if (val == null) {
                        val = 0;
                    }
                    val += (int)Math.round((Double)locMap.get(mapKeys[c]));
                }

                locMap.put(mapKeys[c], val);
            }

            if (i == 0) {
                pos1 = Location.deserialize(locMap);
            } else {
                pos2 = Location.deserialize(locMap);
            }
        }

        if (pos1 == null || pos2 == null) {
            error = Msg.getString("cuboid.invalid", Param.P("input", input));
            return false;
        }

        value = new Cuboid(pos1, pos2);
        return true;
    }

    public Cuboid getValue(World world) {
        getValue().setWorld(world);
        return getValue();
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(Cuboid cuboid) {
        return cuboid == null ? null : Msg.getString("cuboid.display", Param.P("x1", cuboid.getMinX()), Param.P("y1", cuboid.getMinY()), Param.P("z1", cuboid.getMinZ()),
                Param.P("x2", cuboid.getMaxX()), Param.P("y2", cuboid.getMaxY()), Param.P("z2", cuboid.getMaxZ()), Param.P("world", cuboid.getWorld()));
    }

    @Override
    public String getTypeName() {
        return "Cuboid";
    }

    @Override
    public CuboidO clone() {
        return super.cloneData(new CuboidO());
    }
}
