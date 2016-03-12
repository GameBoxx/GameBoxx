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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class BlockO extends SingleOption<Block, BlockO> {

    @Override
    public boolean parse(CommandSender sender, String input) {
        //Get block from sender.
        if (input.isEmpty() || input.equals("@")) {
            Location loc = Utils.getLocation(sender);
            if (loc == null) {
                error = Msg.getString("selector-console-player", Param.P("type", input));
                return false;
            }
            value = loc.getBlock();
            return true;
        }
        if ((input.equals("#") || input.equals("^")) && !(sender instanceof Player)) {
            error = Msg.getString("selector-console-player", Param.P("type", input));
            return false;
        }

        //Get block from player location. @[Player name/uuid]
        if (input.startsWith("@") || input.startsWith("#") || input.startsWith("^")) {
            PlayerO playerOption = new PlayerO();
            playerOption.parse(sender, input.substring(1));
            if (!playerOption.hasValue()) {
                error = playerOption.getError();
                return false;
            }
            if (input.startsWith("#")) {
                value = playerOption.getValue().getTargetBlock(Utils.TRANSPARENT_MATERIALS, 128);
                if (value == null) {
                    error = Msg.getString("block.no-target");
                }
            } else if (input.startsWith("^")) {
                List<Block> blocks = playerOption.getValue().getLastTwoTargetBlocks(Utils.TRANSPARENT_MATERIALS, 128);
                Block block = blocks.get(1);
                if (block.getType() == Material.AIR) {
                    error = Msg.getString("block.no-target");
                    return false;
                }
                value = block.getRelative(blocks.get(1).getFace(blocks.get(0)));
            } else {
                value = playerOption.getValue().getLocation().getBlock();
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
            } else if ((data.equals("#") || data.equals("^")) && !(sender instanceof Player)) {
                error = Msg.getString("selector-console-player", Param.P("type", data));
                return false;
            } else if (data.startsWith("@") || data.startsWith("#") || input.startsWith("^")) {
                //Get block/location from player
                PlayerO playerOption = new PlayerO();
                playerOption.parse(sender, data.substring(1));
                if (!playerOption.hasValue()) {
                    error = playerOption.getError();
                    return false;
                }
                if (input.startsWith("#")) {
                    Block b = playerOption.getValue().getTargetBlock(Utils.TRANSPARENT_MATERIALS, 128);
                    if (b == null) {
                        error = Msg.getString("block.no-target");
                    }
                    location = b.getLocation();
                } else if (input.startsWith("^")) {
                    List<Block> blocks = playerOption.getValue().getLastTwoTargetBlocks(Utils.TRANSPARENT_MATERIALS, 128);
                    Block block = blocks.get(1);
                    if (block.getType() == Material.AIR) {
                        error = Msg.getString("block.no-target");
                        return false;
                    }
                    location = block.getRelative(blocks.get(1).getFace(blocks.get(0))).getLocation();
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
            error = Msg.getString("block.missing-xyz", Param.P("input", split[0]));
            return false;
        }

        if (location.getWorld() == null) {
            error = Msg.getString("block.missing-world", Param.P("input", input));
            return false;
        }

        //Convert the location in a map so we can easily modify the values.
        Map<String, Object> locMap = location.serialize();
        String[] mapKeys = new String[] {"x", "y", "z"};

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
            Integer val = Parse.Int(value);
            if (val == null && !value.isEmpty()) {
                error = Msg.getString("axis-invalid-int", Param.P("input", value), Param.P("axis", mapKeys[i]));
                return false;
            }

            //Add relative coords to the value.
            if (relative) {
                if (val == null) {
                    val = 0;
                }
                val += (int)Math.round((Double)locMap.get(mapKeys[i]));
            }

            locMap.put(mapKeys[i], val);
        }

        //Convert the location map back to a location.
        value = Location.deserialize(locMap).getBlock();
        if (value == null) {
            error = Msg.getString("block.invalid", Param.P("input", input));
            return false;
        }
        return true;
    }

    public Block getValue(World world) {
        Block b = getValue();
        if (b == null || world == null) {
            return b;
        }
        return world.getBlockAt(b.getLocation());
    }

    @Override
    public String serialize() {
        return serialize(getValue());
    }

    public static String serialize(Block block) {
        return block == null ? null : block.getX() + "," + block.getY() + "," + block.getZ() + ":" + block.getWorld().getName();
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(Block block) {
        return block == null ? null : Msg.getString("block.display", Param.P("x", block.getX()), Param.P("y", block.getY()), Param.P("z", block.getZ()), Param.P("world", block.getWorld()));
    }

    @Override
    public BlockO clone() {
        return super.cloneData(new BlockO());
    }
}
