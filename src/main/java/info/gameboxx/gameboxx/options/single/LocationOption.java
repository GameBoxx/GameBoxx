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
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Map;

public class LocationOption extends SingleOption {

    public LocationOption() {
        super();
    }

    public LocationOption(String name) {
        super(name);
    }

    public LocationOption(String name, Location defaultValue) {
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
        //Get location from player. @[Player name/uuid]
        if (input.startsWith("@") || input.startsWith("#")) {
            PlayerOption playerOption = new PlayerOption();
            playerOption.setDefault(player);
            playerOption.parse(player, input.substring(1));
            if (!playerOption.hasValue()) {
                error = playerOption.getError().isEmpty() ? "Invalid player to get the location from." : playerOption.getError();
                return false;
            }
            if (input.startsWith("#")) {
                value = playerOption.getValue().getTargetBlock(Utils.TRANSPARENT_MATERIALS, 64).getLocation();
            } else  {
                value = playerOption.getValue().getLocation();
            }
            return true;
        }

        Location location = new Location(null, 0,0,0, 0f,0f);


        //Split string by semicolon like x,y,z:world or x,y,z:player
        String[] split = input.split(":");
        if (split.length > 1) {
            String data = split[1];
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
                    location = playerOption.getValue().getTargetBlock(Utils.TRANSPARENT_MATERIALS, 64).getLocation();
                } else {
                    location = playerOption.getValue().getLocation();
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
                location.setWorld(worldOption.getValue());
            }
        }


        //Get the coords x,y,z[,yaw,pitch]
        String[] coords = split[0].split(",");
        if (coords.length < 3) {
            error = "Invalid location string must have at least x,y,z values.";
            return false;
        }

        if (location.getWorld() == null) {
            error = "Invalid location string must specify a world like x,y,z:world.";
            return false;
        }

        //Convert the location in a map so we can easily modify the values.
        Map<String, Object> locMap = location.serialize();
        String[] mapKeys = new String[] {"x", "y", "z", "yaw", "pitch"};

        //Go through all the values.
        for (int i = 0; i < coords.length && i < mapKeys.length; i++) {
            String value = coords[i];
            //Check if it's a relative value or not.
            boolean relative = false;
            if (value.startsWith("~") || value.isEmpty()) {
                value = value.isEmpty() ? value : value.substring(1);
                relative = true;
            }
            //Parse the value.
            Double val = Parse.Double(value);
            if (val == null && !value.isEmpty()) {
                error = "The " + mapKeys[i] + " value is not a decimal number.";
                return false;
            }

            //Add relative coords to the value.
            if (relative) {
                if (val == null) {
                    val = 0d;
                }
                val += (Double)locMap.get(mapKeys[i]);
            }

            locMap.put(mapKeys[i], val);
        }

        //Convert the location map back to a location.
        value = Location.deserialize(locMap);
        if (value == null) {
            error = "Invalid location string it needs to have a format like x,y,z:world.";
            return false;
        }
        return true;
    }

    @Override
    public Location getValue() {
        return (Location)getValueOrDefault();
    }

    public Location getValue(World world) {
        Location l = getValue();
        if (l == null || world == null) {
            return l;
        }
        l.setWorld(world);
        return l;
    }

    @Override
    public String serialize() {
        Location value = getValue();
        if (value == null) {
            return null;
        }
        return value.getX() + "," + value.getY() + "," + value.getZ() + "," + value.getYaw() + "," + value.getPitch() + ":" + value.getWorld().getName();
    }

    @Override
    public String getTypeName() {
        return "location";
    }

    @Override
    public Class getRawClass() {
        return Location.class;
    }

    @Override
    public LocationOption clone() {
        return (LocationOption) new LocationOption(name, (Location) defaultValue).setDescription(description).setFlag(flag);
    }
}
