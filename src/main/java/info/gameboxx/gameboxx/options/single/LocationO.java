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
import info.gameboxx.gameboxx.util.Numbers;
import info.gameboxx.gameboxx.util.Parse;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class LocationO extends SingleOption<Location, LocationO> {

    @Override
    public boolean parse(CommandSender sender, String input) {
        //Get location from sender.
        if (input.isEmpty() || input.equals("@")) {
            value = Utils.getLocation(sender);
            if (value == null) {
                error = Msg.getString("selector-console-player", Param.P("type", input));
                return false;
            }
            return true;
        }
        if (input.equals("#") && !(sender instanceof Player)) {
            error = Msg.getString("selector-console-player", Param.P("type", input));
            return false;
        }

        //Get location from specific player  @{Player name/uuid}
        if (input.startsWith("@") || input.startsWith("#")) {
            PlayerO playerOption = new PlayerO();
            playerOption.parse(sender, input.substring(1));
            if (!playerOption.hasValue()) {
                error = playerOption.getError();
                return false;
            }
            if (input.startsWith("#")) {
                List<Block> blocks = playerOption.getValue().getLastTwoTargetBlocks(Utils.TRANSPARENT_MATERIALS, 128);
                Block block = blocks.get(1);
                if (block.getType() == Material.AIR) {
                    error = Msg.getString("location.no-target");
                    return false;
                }
                value = block.getRelative(blocks.get(1).getFace(blocks.get(0))).getLocation().add(0.5f, 0.5f, 0.5f);
            } else {
                value = playerOption.getValue().getLocation();
            }
            return true;
        }

        Location location = new Location(null, 0, 0, 0, 0f, 0f);


        //Split string by semicolon like x,y,z:world or x,y,z:player
        String[] split = input.split(":");
        if (split.length > 1) {
            String data = split[1];
            if (data.equals("@")) {
                location = Utils.getLocation(sender);
                if (location == null) {
                    error = Msg.getString("selector-console-player", Param.P("type", data));
                    return false;
                }
            } else if (data.equals("#") && !(sender instanceof Player)) {
                error = Msg.getString("selector-console-player", Param.P("type", data));
                return false;
            } else if (data.startsWith("@") || data.startsWith("#")) {
                //Get world/location from player
                PlayerO playerOption = new PlayerO();
                playerOption.parse(sender, data.substring(1));
                if (!playerOption.hasValue()) {
                    error = playerOption.getError();
                    return false;
                }
                if (input.startsWith("#")) {
                    List<Block> blocks = playerOption.getValue().getLastTwoTargetBlocks(Utils.TRANSPARENT_MATERIALS, 128);
                    Block block = blocks.get(1);
                    if (block.getType() == Material.AIR) {
                        error = Msg.getString("location.no-target");
                        return false;
                    }
                    location = block.getRelative(blocks.get(1).getFace(blocks.get(0))).getLocation().add(0.5f, 0.5f, 0.5f);
                } else {
                    location = playerOption.getValue().getLocation();
                }
            } else {
                //Get world.
                WorldO worldOption = new WorldO();
                worldOption.parse(sender, data);
                if (!worldOption.hasValue()) {
                    error = worldOption.getError();
                    return false;
                }
                location.setWorld(worldOption.getValue());
            }
        }


        //Get the coords x,y,z[,yaw,pitch]
        String[] coords = split[0].split(",");
        if (coords.length < 3) {
            error = Msg.getString("location.missing-xyz", Param.P("input", split[0]));
            return false;
        }

        if (location.getWorld() == null) {
            error = Msg.getString("location.missing-world", Param.P("input", input));
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
                error = Msg.getString("axis-invalid-double", Param.P("input", value), Param.P("axis", mapKeys[i]));
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
            error = Msg.getString("location.invalid", Param.P("input", input));
            return false;
        }
        return true;
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
        return serialize(getValue());
    }

    public static String serialize(Location loc) {
        return loc == null ? null : loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch() + ":" +loc.getWorld().getName();
    }

    public String serialize(int roundDecimals) {
        return serialize(getValue(), roundDecimals);
    }

    public static String serialize(Location loc, int roundDecimals) {
        return loc == null ? null : Numbers.round(loc.getX(), roundDecimals) + "," + Numbers.round(loc.getY(), roundDecimals) + "," + Numbers.round(loc.getZ(), roundDecimals) + ","
                + Numbers.round(loc.getYaw(), roundDecimals) + "," + Numbers.round(loc.getPitch(), roundDecimals) + ":" + loc.getWorld().getName();
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(Location loc) {
        return loc == null ? null : Msg.getString("location.display", Param.P("x", loc.getX()), Param.P("y", loc.getY()), Param.P("z", loc.getZ()),
                Param.P("yaw", loc.getYaw()), Param.P("pitch", loc.getPitch()), Param.P("world", loc.getWorld().getName()));
    }

    public String getDisplayValue(int roundDecimals) {
        return display(getValue(), roundDecimals);
    }

    public static String display(Location loc, int roundDecimals) {
        return loc == null ? null : Msg.getString("location.display", Param.P("x", Numbers.round(loc.getX(), roundDecimals)), Param.P("y", Numbers.round(loc.getY(), roundDecimals)),
                Param.P("z", Numbers.round(loc.getZ(), roundDecimals)), Param.P("yaw", Numbers.round(loc.getYaw(), roundDecimals)), Param.P("pitch", Numbers.round(loc.getPitch(), roundDecimals)),
                Param.P("world", loc.getWorld().getName()));
    }

    @Override
    public String getTypeName() {
        return "Location";
    }

    @Override
    public LocationO clone() {
        return super.cloneData(new LocationO());
    }
}
