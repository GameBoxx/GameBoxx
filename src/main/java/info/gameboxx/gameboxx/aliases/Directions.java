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

package info.gameboxx.gameboxx.aliases;

import info.gameboxx.gameboxx.aliases.internal.AliasMap;
import org.bukkit.block.BlockFace;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Directions extends AliasMap<BlockFace> {

    private Directions() {
        super("Directions", new File(ALIASES_FOLDER, "Directions.yml"), "direction", "dirs", "dir");
    }

    @Override
    public void onLoad() {
        add(BlockFace.NORTH, "North", "N", "0", "360");
        add(BlockFace.EAST, "East", "E", "90");
        add(BlockFace.SOUTH, "South", "S", "180");
        add(BlockFace.WEST, "West", "W", "270");
        add(BlockFace.UP, "Up", "U", "Top", "T", "Above", "A");
        add(BlockFace.DOWN, "Down", "D", "Bottom", "B", "Below");
        add(BlockFace.NORTH_EAST, "North East", "NE", "45");
        add(BlockFace.NORTH_WEST, "North West", "NW", "315");
        add(BlockFace.SOUTH_EAST, "South East", "SE", "135");
        add(BlockFace.SOUTH_WEST, "South West", "SW", "225");
        add(BlockFace.WEST_NORTH_WEST, "West North West", "WNW");
        add(BlockFace.NORTH_NORTH_WEST, "North North West", "NNW");
        add(BlockFace.NORTH_NORTH_EAST, "North North East", "NNE");
        add(BlockFace.EAST_NORTH_EAST, "East North East", "ENE");
        add(BlockFace.EAST_SOUTH_EAST, "East South East", "ESE");
        add(BlockFace.SOUTH_SOUTH_EAST, "South South East", "SSE");
        add(BlockFace.SOUTH_SOUTH_WEST, "South South West", "SSW");
        add(BlockFace.WEST_SOUTH_WEST, "West South West", "WSW");
        add(BlockFace.SELF, "Self", "None", "Base");
    }

    public static BlockFace get(String string) {
        return instance()._get(string);
    }

    public static String getName(BlockFace key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(BlockFace key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(BlockFace key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static Map<String, List<String>> getAliasMap(Type type) {
        Map<String, List<String>> aliases = new HashMap<>();
        for (BlockFace dir : BlockFace.values()) {
            if (dir == BlockFace.NORTH || dir == BlockFace.EAST || dir == BlockFace.WEST || dir == BlockFace.SOUTH) {
                if (type != Type.TWO && type != Type.TWO_SELF) {
                    aliases.put(getName(dir), getAliases(dir));
                }
            } else if (dir == BlockFace.UP || dir == BlockFace.DOWN) {
                if (type != Type.FOUR && type != Type.EIGHT && type != Type.SIXTEEN) {
                    aliases.put(getName(dir), getAliases(dir));
                }
            } else if (dir == BlockFace.NORTH_EAST || dir == BlockFace.NORTH_WEST || dir == BlockFace.SOUTH_EAST || dir == BlockFace.SOUTH_SOUTH_WEST) {
                if (type != Type.TWO && type != Type.TWO_SELF && type != Type.FOUR && type != Type.FOUR_SELF && type != Type.SIX && type != Type.SIX_SELF) {
                    aliases.put(getName(dir), getAliases(dir));
                }
            } else if (dir == BlockFace.SELF) {
                if (type != Type.TWO && type != Type.FOUR && type != Type.EIGHT && type != Type.SIXTEEN && type != Type.EIGHTEEN) {
                    aliases.put(getName(dir), getAliases(dir));
                }
            } else if (type == Type.SIXTEEN || type == Type.SIX_SELF || type == Type.EIGHTEEN || type == Type.ALL) {
                aliases.put(getName(dir), getAliases(dir));
            }
        }
        return aliases;
    }

    public static Directions instance() {
        if (getMap(Directions.class) == null) {
            aliasMaps.put(Directions.class, new Directions());
        }
        return (Directions)getMap(Directions.class);
    }

    public enum Type {
        /** All 19 directions including self. */
        ALL,
        /** Up & Down */
        TWO,
        /** Up & Down including self. */
        TWO_SELF,
        /** North, East, South & West */
        FOUR,
        /** North, East, South & West including self. */
        FOUR_SELF,
        /** North, East, South, West, Up & Down */
        SIX,
        /** North, East, South, West, Up & Down including self. */
        SIX_SELF,
        /** North, East, South, West, NE, NW, SE & SW. */
        EIGHT,
        /** North, East, South, West, NE, NW, SE & SW including self. */
        EIGHT_SELF,
        /** North, East, South, West, NE, NW, SE, SW, Up & Down. */
        TEN,
        /** North, East, South, West, NE, NW, SE, SW, Up & Down including self. */
        TEN_SELF,
        /** All directions except Up, Down and self. */
        SIXTEEN,
        /** All directions except Up & Down */
        SIXTEEN_SELF,
        /** All directions except self */
        EIGHTEEN;
    }
}
