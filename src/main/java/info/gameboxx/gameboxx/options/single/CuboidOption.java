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

import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.util.Parse;
import info.gameboxx.gameboxx.util.Utils;
import info.gameboxx.gameboxx.util.cuboid.Cuboid;
import info.gameboxx.gameboxx.util.cuboid.SelectionManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

public class CuboidOption extends SingleOption {

    public CuboidOption() {
        super();
    }

    public CuboidOption(String name) {
        super(name);
    }

    public CuboidOption(String name, Cuboid defaultValue) {
        super(name, defaultValue);
    }


    @Override
    public boolean parse(Object input) {
        if (!parseObject(input)) {
            return false;
        }
        if (value != null) {
            return true;
        }
        return parse((String)input);
    }

    @Override
    public boolean parse(String input) {
        return parse(null, input);
    }

    @Override
    public boolean parse(Player player, String input) {
        //Get cuboid from player. @[Player name/uuid]
        if (input.startsWith("@")) {
            PlayerOption playerOption = new PlayerOption();
            playerOption.setDefault(player);
            playerOption.parse(player, input.substring(1));
            if (!playerOption.hasValue()) {
                error = playerOption.getError().isEmpty() ? "Invalid player to get the location from." : playerOption.getError();
                return false;
            }
            value = SelectionManager.inst().getSelection(playerOption.getValue());
            if (!hasValue()) {
                error = "No cuboid selection found for " + playerOption.getValue().getName() + ".";
                return false;
            }
            return true;
        }

        Location pos1 = new Location(null, 0,0,0, 0f,0f);
        Location pos2 = new Location(null, 0,0,0, 0f,0f);


        //Split string by semicolon like x,y,z:x,y,z:world or x,y,z:x,y,z:@player
        String[] split = input.split(":");
        if (split.length > 1) {
            String data = split[split.length - 1];
            if (data.startsWith("@") || data.startsWith("#")) {
                //Get world/location from player
                PlayerOption playerOption = new PlayerOption();
                playerOption.setDefault(player);
                playerOption.parse(player, data.substring(1));
                if (!playerOption.hasValue()) {
                    error = playerOption.getError().isEmpty() ? "Invalid player to get the world/location from." : playerOption.getError();
                    return false;
                }
                if (input.startsWith("#")) {
                    pos1 = playerOption.getValue().getTargetBlock(Utils.TRANSPARENT_MATERIALS, 64).getLocation();
                    pos2 = playerOption.getValue().getTargetBlock(Utils.TRANSPARENT_MATERIALS, 64).getLocation();
                } else {
                    pos1 = playerOption.getValue().getLocation();
                    pos2 = playerOption.getValue().getLocation();
                }
            } else {
                //Get world.
                WorldOption worldOption = new WorldOption();
                worldOption.setDefault(player == null ? null : player.getWorld());
                worldOption.parse(player, data);
                if (!worldOption.hasValue()) {
                    error = worldOption.getError().isEmpty() ? "Invalid world specified." : worldOption.getError();
                    return false;
                }
                pos1.setWorld(worldOption.getValue());
                pos2.setWorld(worldOption.getValue());
            }
        }

        if (pos1.getWorld() == null || pos2.getWorld() == null) {
            error = "Invalid cuboid string must specify a world like x,y,z:x,y,z:world.";
            return false;
        }

        //Get the coords x,y,z:x,y,z
        for (int i = 0; i < split.length-1; i++) {
            String[] coords = split[i].split(",");

            if (coords.length < 3) {
                error = "Invalid cuboid location string must have at least x,y,z values.";
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
                    error = "The " + mapKeys[c] + " value is not a whole number.";
                    return false;
                }

                //Add relative coords to the value.
                if (relative) {
                    if (val == null) {
                        val = 0;
                    }
                    val += (Integer)locMap.get(mapKeys[c]);
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
            error = "Invalid cuboid string it needs to have a format like x,y,z:x,y,z:world.";
            return false;
        }

        value = new Cuboid(pos1, pos2);
        return true;
    }

    @Override
    public Cuboid getValue() {
        return (Cuboid)getValueOrDefault();
    }

    @Override
    public String getTypeName() {
        return "cuboid";
    }

    @Override
    public Class getRawClass() {
        return Cuboid.class;
    }

    @Override
    public CuboidOption clone() {
        return new CuboidOption(name, (Cuboid) defaultValue);
    }
}
