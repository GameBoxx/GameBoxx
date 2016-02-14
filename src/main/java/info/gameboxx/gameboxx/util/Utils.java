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

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public class Utils {

    public static final int MSEC_IN_DAY = 86400000;
    public static final int MSEC_IN_HOUR = 3600000;
    public static final int MSEC_IN_MIN = 60000;
    public static final int MSEC_IN_SEC = 1000;
    public static final Set<Material> TRANSPARENT_MATERIALS = new HashSet<Material>();

    static {
        for (Material material : Material.values()) {
            if (material.isTransparent()) {
                TRANSPARENT_MATERIALS.add(material);
            }
        }
    }

    public static <T> T convertInstance(Object o, Class<T> clazz) {
        try {
            return clazz.cast(o);
        } catch(ClassCastException e) {
            return null;
        }
    }

    public static Map<String, File> getFiles(File dir, final String extension) {
        Map<String, File> names = new HashMap<String, File>();
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return names;
        }

        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith("." + extension);
            }
        });

        for (File file : files) {
            names.put(file.getName().substring(0, file.getName().length() - extension.length() - 1), file);
        }

        return names;
    }

    /**
     * Split a camel cased string and put something else in between each word.
     * For example a space and then a string like 'GameSession' would change to 'Game Session'
     * Some other examples: 'SimpleXMLParser' -> 'Simple XML Parser', 'May12' -> 'May 12'
     * http://stackoverflow.com/a/2560017
     * @param string The string to split and replace.
     * @param splitReplace The string to put between each split.
     * @return The formatted string.
     */
    public static String splitCamelCase(String string, String splitReplace) {
        return string.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ), splitReplace
        );
    }

    /**
     * Fixes the specified list of command arguments.
     * It will combine arguments that are quoted with spaces.
     * For example the following string: 'Hello there "you''re awesome"!'
     * Would result in 0:Hello 1:there 2:You''re awesome 3:!
     * @param args The list of arguments to fix.
     * @return The modified array with arguments.
     */
    public static String[] fixCommandArgs(String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0 ; i < args.length; i++) {
            builder.append(args[i]);
            if (i+1 < args.length) {
                builder.append(" ");
            }
        }

        List<String> newArgList = Str.splitQuotes(builder.toString());
        return newArgList.toArray(new String[newArgList.size()]);
    }


    /**
     * Gets a {@link WorldCreator} based on the specified string arguments.
     * This does not actually create a world it just builds the generator and return it.
     * You can then use {@link WorldCreator#createWorld()} to create the actual world.
     * If any of the options are missing or if they are invalid it will set it to default. (default world, default type and no generator)
     * <b>The options are:</b><ul>
     *     <li>environment:{environment} (default, nether, end)</li>
     *     <li>type:{type} (normal, amplified, flat, largebiomes)</li>
     *     <li>generator:{plugin:generator} or just {plugin}</li>
     *     <li>settings:{settings...} extra settings for the generator</li>
     * </ul>
     * @param name The name for the world to create.
     * @param args The list of arguments to parse.
     * @param startIndex The start index for the arguments to start parsing at.
     * @return A new WorldCreator.
     */
    public static WorldCreator getWorldCreator(String name, String[] args, int startIndex) {
        WorldCreator worldCreator = new WorldCreator(name);

        if (args.length > startIndex) {
            for (int i = startIndex; i < args.length; i++) {
                String[] arg = args[i].split(":", 2);
                if (arg.length < 2) {
                    continue;
                }
                String key = arg[0].toLowerCase();
                String val = arg[1].toLowerCase();

                if (key.equals("environment") || key.equals("env") || key.equals("e")) {
                    if (val.equals("default") || val.equals("normal")) {
                        worldCreator.environment(World.Environment.NORMAL);
                    } else if (val.equals("nether") || val.equals("hell")) {
                        worldCreator.environment(World.Environment.NETHER);
                    } else if (val.equals("end") || val.equals("theend")) {
                        worldCreator.environment(World.Environment.THE_END);
                    }
                } else if (key.equals("worldtype") || key.equals("type") || key.equals("t") ||key.equals("wt")) {
                    if (val.equals("default") || val.equals("normal")) {
                        worldCreator.type(WorldType.NORMAL);
                    } else if (val.equals("amplified") || val.equals("hills") || val.equals("mountains")) {
                        worldCreator.type(WorldType.AMPLIFIED);
                    } else if (val.equals("flat") || val.equals("creative")) {
                        worldCreator.type(WorldType.FLAT);
                    } else if (val.equals("largebiomes") || val.equals("bigbiomes") || val.equals("extremebiomes") || val.equals("bb") || val.equals("lb") || val.equals("eb")) {
                        worldCreator.type(WorldType.LARGE_BIOMES);
                    }
                } else if (key.equals("generator") || key.equals("gen") || key.equals("g")) {
                    worldCreator.generator(arg[1]);
                } else if (key.equals("settings") || key.equals("set") || key.equals("s")) {
                    worldCreator.generatorSettings(args[1]);
                }
            }
        }

        return worldCreator;
    }

    /**
     * Get the highest block at the given x and z coordinate.
     * It will work properly for the nether and such.
     * Basically instead of starting from the top it will first scan down for the first empty block.
     * From there on it will scan down for air and then return the first non air block.
     * @param world The world to look in.
     * @param x The x coordinate.
     * @param z The z coordinate.
     * @return The highest non air block respecting the nether ceiling.
     */
    public static Block getHighestBlockAt(World world, int x, int z) {
        Block block = world.getBlockAt(x, world.getEnvironment() == World.Environment.NETHER ? 127 : 255, z);
        while(block.getType() != Material.AIR && block.getY() > 0) {
            block = block.getRelative(BlockFace.DOWN);
        }
        while(block.getType() == Material.AIR && block.getY() > 0) {
            block = block.getRelative(BlockFace.DOWN);
        }
        return block;
    }

    /**
     * Format a timestamp to a string with days/hours/mins/secs/ms.
     * For example: '%Dd %H:%M:%S' will be replaced with something like '1d 23:12:52'
     * The possible options in the syntax are:
     * %D = Days
     * %H = Hours
     * %M = Minutes
     * %S = Seconds
     * %MS = MilliSeconds
     * %% Remainder percentage of seconds with 1 decimal like '%S.%%s' could be '34.1s'
     * %%% Remainder percentage of seconds with 2 decimals like '%S.%%%s' could be '34.13s'
     * @param time The time to convert to min:sec
     * @param syntax The string with the above options which will be replaced with the time.
     * @return Formatted time string.
     */
    public static String formatTime(long time, String syntax, boolean extraZeros) {
        //time = time / 1000;

        int days = (int) time / MSEC_IN_DAY;
        time = time - days * MSEC_IN_DAY;

        int hours = (int) time / MSEC_IN_HOUR;
        time = time - hours * MSEC_IN_HOUR;

        int mins = (int) time / MSEC_IN_MIN;
        time = time - mins * MSEC_IN_MIN;

        int secs = (int) time / MSEC_IN_SEC;
        time = time - secs * MSEC_IN_SEC;

        int ms = (int) time;
        int ds = (int) time / 100;
        int fs = (int) time / 10;

        syntax = syntax.replace("%D", "" + days);
        syntax = syntax.replace("%H", "" + (hours < 10 && extraZeros ? "0"+hours : hours));
        syntax = syntax.replace("%M", "" + (mins < 10 && extraZeros ? "0"+mins : mins));
        syntax = syntax.replace("%S", "" + (secs < 10 && extraZeros ? "0"+secs : secs));
        syntax = syntax.replace("%MS", "" + ms);
        syntax = syntax.replace("%%%", "" + fs);
        syntax = syntax.replace("%%", "" + ds);
        return syntax;
    }
}
