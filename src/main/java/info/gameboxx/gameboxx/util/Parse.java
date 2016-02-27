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
import org.bukkit.block.Block;

import java.util.*;

/**
 * Utility class to parse data in a user friendly way.
 */
public class Parse {

    /**
     * Parse a comma separated string by splitting it in to a list.
     * @param str The string to parse.
     * @return List with strings.
     */
    public static List<String> List(String str) {
        return List(str, ",");
    }

    /**
     * Parse a string by splitting it in to a list.
     * @param str The string to parse.
     * @param separator The regex to split with.
     * @return List with strings.
     */
    public static List<String> List(String str, String separator) {
        if (str == null || str.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(str.split(separator));
    }

    /**
     * Parse a comma separated string by splitting it in to a UUID list.
     * @param str The string to parse.
     * @return List with UUID's.
     */
    public static List<UUID> UUIDList(String str) {
        if (str == null || str.isEmpty()) {
            return Collections.emptyList();
        }
        String[] split = str.split(",");
        List<UUID> uuids = new ArrayList<UUID>();
        for (String uuidStr : split) {
            UUID uuid = UUID(uuidStr);
            if (uuid != null) {
                uuids.add(uuid);
            }
        }
        return uuids;
    }

    /**
     * Parse a list in to a comma separated string.
     * @param list The list to parse.
     * @return String with list values comma separated.
     */
    public static String List(List<? extends Object> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        List<String> values = new ArrayList<String>();
        for (Object obj : list) {
            values.add(obj.toString());
        }
        return Str.implode(values, ",");
    }

    /**
     * Parse an array in to a comma separated string.
     * It used {@link Object#toString()}
     * @param objects The objects to parse.
     * @return String with list values comma separated.
     */
    public static String Array(Object... objects) {
        if (objects == null || objects.length == 0) {
            return "";
        }
        List<String> values = new ArrayList<String>();
        for (Object obj : objects) {
            values.add(obj.toString());
        }
        return Str.implode(values, ",");
    }

    /**
     * Convert a string like 'true' to a Boolean. Returns null if it's invalid.
     * @param str
     * @return Boolean
     */
    public static Boolean Bool(String str) {
        if (str == null) {
            return null;
        }
        if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("y") || str.equalsIgnoreCase("v")) {
            return true;
        }
        if (str.equalsIgnoreCase("false") || str.equalsIgnoreCase("no") || str.equalsIgnoreCase("n") || str.equalsIgnoreCase("x")) {
            return false;
        }
        return null;
    }


    /**
     * Convert a string like '1' to a int. Returns null if it's invalid.
     * @param str
     * @return int
     */
    public static Integer Int(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /**
     * Convert a string like '1' to a short. Returns null if it's invalid.
     * @param str
     * @return short
     */
    public static Short Short(String str) {
        try {
            return Short.parseShort(str);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /**
     * Convert a string like '1' to a byte. Returns null if it's invalid.
     * @param str
     * @return byte
     */
    public static Byte Byte(String str) {
        try {
            return Byte.parseByte(str);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /**
     * Convert a string like '1' to a long. Returns null if it's invalid.
     * @param str
     * @return int
     */
    public static Long Long(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /**
     * Convert a string like '1.5' to a double. Returns null if it's invalid.
     * @param str
     * @return double
     */
    public static Double Double(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /**
     * Convert a string like '1.12' to a float. Returns null if it's invalid.
     * @param str
     * @return float
     */
    public static Float Float(String str) {
        if (str != null && str != "") {
            try {
                return Float.parseFloat(str);
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    /**
     * Convert a uuid string to a UUID. Returns null if it's invalid.
     * @param str
     * @return uuid
     */
    public static UUID UUID(String str) {
        if (str != null && str != "") {
            try {
                return UUID.fromString(str);
            } catch (IllegalArgumentException e) {
            }
        }
        return null;
    }

    /**
     * Parse a block in to a string.
     * The string will be formated like x,y,z:world
     * @param input Block that needs to be parsed.
     * @return Location string with proper formatting. Will return null if the input block is null.
     */
    public static String Block(Block input) {
        if (input == null) {
            return null;
        }
        return Location(input.getLocation(), true);
    }

    /**
     * Parse a string to a block.
     * The string needs to have a format like: x,y,z:world
     * @param input String with location data.
     * @return The parsed Block or null if the parsing failed.
     */
    public static Block Block(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        Location loc = Location(input);
        if (loc == null) {
            return null;
        }
        return loc.getBlock();
    }


    /**
     * Parse a location in to a string.
     * The string will be formated like x,y,z,yaw,pitch:world (yaw and pitch can be left out of the location is a block location)
     * @param input Location that needs to be parsed.
     * @return Location string with proper formatting. Will return null if the input location is null.
     */
    public static String Location(Location input) {
        return Location(input, false);
    }

    /**
     * Parse a location in to a string.
     * The string will be formated like x,y,z,yaw,pitch:world (yaw and pitch can be left out of the location is a block location)
     * @param input Location that needs to be parsed.
     * @param blockLocation If set to true it wont put the exact coordinates but instead it will put the block coordinates.
     *                      By default it will check if it's a block location or a normal location and put the proper values in the string.
     *                      So it's recommended to keep this false unless it's for display purposes.
     * @return Location string with proper formatting. Will return null if the input location is null.
     */
    public static String Location(Location input, boolean blockLocation) {
        if (input == null) {
            return null;
        }
        String x = (blockLocation || input.getX()%1 == 0) ? Integer.toString(input.getBlockX()) : Double.toString(input.getX());
        String y = (blockLocation || input.getY()%1 == 0) ? Integer.toString(input.getBlockY()) : Double.toString(input.getY());
        String z = (blockLocation || input.getZ()%1 == 0) ? Integer.toString(input.getBlockZ()) : Double.toString(input.getZ());

        if (input.getYaw() == 0 && input.getPitch() == 0) {
            return  x + "," + y + "," + z + ":" + input.getWorld().getName();
        }
        return  x + "," + y + "," + z + "," + input.getYaw() + "," + input.getPitch() + ":" + input.getWorld().getName();
    }

    /**
     * Parse a string to a location.
     * The string needs to have a format like: x,y,z:world or x,y,z,yaw,pitch:world
     * @param input String with location data.
     * @return The parsed Location or null if the parsing failed.
     */
    public static Location Location(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        //Split string by semicolon like x,y,z:world or x,y,z:player
        World world = null;
        String[] split = input.split(":");
        if (split.length > 1) {
            //Get world.
            world = Bukkit.getWorld(split[1]);
        }
        if (world == null) {
            return null;
        }

        //Get the coords x,y,z[,yaw,pitch]
        String[] coords = split[0].split(",");
        if (coords.length < 3) {
            return null;
        }

        Double x = Double(coords[0]);
        Double y = Double(coords[1]);
        Double z = Double(coords[2]);
        if (x == null || y == null || z == null) {
            return null;
        }

        Location loc = new Location(world, x, y, z);
        if (coords.length >= 4 && Float(coords[3]) != null) {
            loc.setYaw(Float(coords[3]));
        }
        if (coords.length >= 5 && Float(coords[4]) != null) {
            loc.setPitch(Float(coords[4]));
        }

        return loc;
    }

    /**
     * Get a string (r,g,b) from a color.
     * @param color The color to parse
     * @return String with r,g,b color format.
     */
    public static String Color(Color color) {
        if (color == null) {
            return "";
        }
        return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
    }

    /**
     * Get a Color from a string.
     * The string can be either rrr,ggg,bbb or #hexhex or without the hashtag.
     * @param string The string to parse.
     * @return The Color that has been parsed.
     */
    public static Color Color(String string) {
        if (string.isEmpty()) {
            return Color.WHITE;
        }
        if (string.contains("#")) {
            string = string.replace("#", "");
        }

        if (string.split(",").length > 2) {
            return getColorFromRGB(string);
        } else if (string.matches("[0-9A-Fa-f]+")) {
            return getColorFromHex(string);
        } else {
            return null;
        }
    }

    private static Color getColorFromHex(String string) {
        int c = 0;
        if (string.contains("#")) {
            string = string.replace("#", "");
        }
        if (string.matches("[0-9A-Fa-f]+")) {
            return Color.fromRGB(Integer.parseInt(string, 16));
        }
        return null;
    }

    private static Color getColorFromRGB(String string) {
        String[] split = string.split(",");
        if (split.length < 3) {
            return null;
        }
        Integer red = Int(split[0]);
        Integer green = Int(split[1]);
        Integer blue = Int(split[2]);
        if (red == null || green == null || blue == null) {
            return null;
        }
        return Color.fromRGB(Math.min(Math.max(red, 0), 255), Math.min(Math.max(green, 0), 255), Math.min(Math.max(blue, 0), 255));
    }

}
