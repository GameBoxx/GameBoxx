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

import info.gameboxx.gameboxx.aliases.Environments;
import info.gameboxx.gameboxx.aliases.WorldTypes;
import info.gameboxx.gameboxx.aliases.items.Items;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.nms.item.ItemUtils;
import info.gameboxx.gameboxx.util.item.EItem;
import info.gameboxx.gameboxx.util.item.ItemParser;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

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
        } catch (ClassCastException e) {
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
     *
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
     *
     * @param args The list of arguments to fix.
     * @return The modified array with arguments.
     */
    public static String[] fixCommandArgs(String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i]);
            if (i + 1 < args.length) {
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
     * <li>environment:{environment} (default, nether, end)</li>
     * <li>type:{type} (normal, amplified, flat, largebiomes)</li>
     * <li>generator:{plugin:generator} or just {plugin}</li>
     * <li>settings:{settings...} extra settings for the generator</li>
     * </ul>
     *
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
                    worldCreator.environment(Environments.get(val));
                } else if (key.equals("worldtype") || key.equals("type") || key.equals("t") || key.equals("wt")) {
                    worldCreator.type(WorldTypes.get(val));
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
     *
     * @param world The world to look in.
     * @param x The x coordinate.
     * @param z The z coordinate.
     * @return The highest non air block respecting the nether ceiling.
     */
    public static Block getHighestBlockAt(World world, int x, int z) {
        Block block = world.getBlockAt(x, world.getEnvironment() == World.Environment.NETHER ? 127 : 255, z);
        while (block.getType() != Material.AIR && block.getY() > 0) {
            block = block.getRelative(BlockFace.DOWN);
        }
        while (block.getType() == Material.AIR && block.getY() > 0) {
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
     *
     * @param time The time to convert to min:sec
     * @param syntax The string with the above options which will be replaced with the time.
     * @return Formatted time string.
     */
    public static String formatTime(long time, String syntax, boolean extraZeros) {
        //time = time / 1000;

        int days = (int)time / MSEC_IN_DAY;
        time = time - days * MSEC_IN_DAY;

        int hours = (int)time / MSEC_IN_HOUR;
        time = time - hours * MSEC_IN_HOUR;

        int mins = (int)time / MSEC_IN_MIN;
        time = time - mins * MSEC_IN_MIN;

        int secs = (int)time / MSEC_IN_SEC;
        time = time - secs * MSEC_IN_SEC;

        int ms = (int)time;
        int ds = (int)time / 100;
        int fs = (int)time / 10;

        syntax = syntax.replace("%D", "" + days);
        syntax = syntax.replace("%H", "" + (hours < 10 && extraZeros ? "0" + hours : hours));
        syntax = syntax.replace("%M", "" + (mins < 10 && extraZeros ? "0" + mins : mins));
        syntax = syntax.replace("%S", "" + (secs < 10 && extraZeros ? "0" + secs : secs));
        syntax = syntax.replace("%MS", "" + ms);
        syntax = syntax.replace("%%%", "" + fs);
        syntax = syntax.replace("%%", "" + ds);
        return syntax;
    }

    /**
     * Convert an object to string.
     * It will try to format the object nicely in a user friendly way for common objects.
     * If no custom formatting it will fall back to {@link Object#toString()}
     * If the object is null it will return 'null'.
     *
     * @param obj The object to convert to string.
     * @return String from object.
     */
    public static String getString(Object obj) {
        if (obj == null) {
            return "null";
        }

        switch (obj.getClass().getName().toLowerCase()) {
            case "location":
                return Parse.Location(((Location)obj));
            case "block":
                return Parse.Block(((Block)obj));
            case "player":
                return ((Player)obj).getName();
            case "world":
                return ((World)obj).getName();
            case "color":
                return Parse.Color(((Color)obj));
            case "itemstack":
                return new ItemParser(((ItemStack)obj)).getString();
            case "eitem":
                return new ItemParser(((EItem)obj)).getString();
            case "material":
                return Items.getName(((Material)obj), (short)0);
            case "materialdata":
                return Items.getName(((MaterialData)obj));
        }

        return obj.toString();
    }

    public static String getAliasesString(String key, Map<String, List<String>> values) {
        Msg format = Msg.get(key);
        if (format.isUndefined()) {
            format = new Msg("[[<aliases>||<name>]]");
        }
        List<String> formats = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : values.entrySet()) {
            String name = entry.getKey();
            String aliases = Str.wrapString("&7" + Str.implode(entry.getValue(), "&8, &7"), 50);

            formats.add(format.clone().params(Param.P("name", name), Param.P("key", name), Param.P("display", name), Param.P("value", name),
                    Param.P("aliases", aliases), Param.P("alias", aliases), Param.P("values", aliases)).getRaw());
        }
        return Str.implode(formats, "&8, &r");
    }

    public static Location getLocation(CommandSender sender) {
        if (sender instanceof Entity) {
            return ((Entity)sender).getLocation();
        } else if (sender instanceof BlockCommandSender) {
            return ((BlockCommandSender)sender).getBlock().getLocation().add(0.5f, 0.5f, 0.5f);
        }
        return null;
    }

    /**
     * Get a Byte[] array with the texture skull url
     * The input string can be a textures.minecraft.net link, the code from the link only or a Base64 encoded string.
     * http://heads.freshcoal.com/maincollection.php
     * </p>
     * Used by {@link ItemUtils#setSkullTexture(SkullMeta, String)}
     */
    public static byte[] getSkullTexture(String input) {
        if (input.endsWith("=")) {
            //Encoded texture.
            //eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTU1ZDYxMWE4NzhlODIxMjMxNzQ5YjI5NjU3MDhjYWQ5NDI2NTA2NzJkYjA5ZTI2ODQ3YTg4ZTJmYWMyOTQ2In19fQ==
            return input.getBytes();
        } else {
            if (input.contains("/")) {
                //Whole texture url from textures.minecraft.net
                //http://textures.minecraft.net/texture/955d611a878e821231749b2965708cad942650672db09e26847a88e2fac2946
                String[] split = input.split("/");
                input = split[split.length-1];
            }
            //Texture code split from the url.
            //955d611a878e821231749b2965708cad942650672db09e26847a88e2fac2946
            input = "http://textures.minecraft.net/texture/" + input;
            return Base64.encodeBase64(String.format("{\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}", input).getBytes());
        }
    }

}
