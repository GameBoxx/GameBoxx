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

package info.gameboxx.gameboxx.util.alias;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Item aliases and display names for items.
 * This allows more user friendly user input and displaying for items.
 * For example you could use cyanwool to get WOOL with data 9.
 * Or you could use a shorter alias like dsword to get a DIAMOND_SWORD.
 */
public class Items {

    private static final List<ItemData> items = new ArrayList<>();
    private static final HashMap<String, ItemData> itemLookup = new HashMap<>();
    private static final HashMap<String, String> matchLookup = new HashMap<>();

    /**
     * Get the {@link ItemData} for the specified MaterialData.
     * If there is no item registered with the provided materialdata it will return the item with data value 0.
     * If the material is not registered at all it will return a new blank item data without aliases and just the material as name.
     * @see #getItem(Material, short)
     * @param materialData The material and data to look up.
     * @return The {@link ItemData} for the given material and data containing display name, aliases etc.
     */
    public static ItemData getItem(MaterialData materialData) {
        return getItem(materialData.getItemType(), materialData.getData());
    }

    /**
     * Get the {@link ItemData} for the specified material and data.
     * If there is no item registered with the provided material and data it will return the item with data value 0.
     * If the material is not registered at all it will return a new blank item data without aliases and just the material as name.
     * @param material The material to look up.
     * @param data The material data to look up. (damage/durability/data value)
     * @return The {@link ItemData} for the given material and data containing display name, aliases etc.
     */
    public static ItemData getItem(Material material, short data) {
        String key = material.toString().toLowerCase() + "-" + ((short)Math.max(data, 0));
        if (itemLookup.containsKey(key)) {
            return itemLookup.get(key);
        }
        key = material.toString().toLowerCase() + "-0";
        if (itemLookup.containsKey(key)) {
            return itemLookup.get(key);
        }
        return new ItemData(material.toString().toLowerCase().replace("_", " "), material, data);
    }

    /**
     * Get the {@link ItemData} for the specified string.
     * The string can be any of the following. (Casing doesn't matter)
     * <ul>
     *     <li>Material name with underscores (DIAMOND_SWORD)</li>
     *     <li>Material name without underscores (DIAMONDSWORD)</li>
     *     <li>Display name with underscores (Diamond_Sword)</li>
     *     <li>Display name without underscores (DiamondSword)</li>
     *     <li>Any of the aliases (diasword, dsword, etc)</li>
     *     <li>The item ID</li>
     * </ul>
     * You can follow up the string with a semicolon(:), hashtag(#), dot(.) or a dash(-) and the data.
     * For example wool:9, cloth#9, whitewool.9 or 35-9 would return Cyan Wool.
     * When you specify a specific item with data like cyanwool and specify data the data will be used.
     * So if you do cyanwool:10 you would get Purple Wool and not Cyan Wool.
     * @param string The string to find matching {@link ItemData} for. (See description for values)
     * @return The {@link ItemData} that matches the string. (Will be {@code null} when there is no match found!)
     */
    public static ItemData getItem(String string) {
        string = string.toLowerCase().trim();

        String[] split;
        if (string.contains("-")) {
            split = string.split("\\-");
        } else if (string.contains("#")) {
            split = string.split("#");
        } else if (string.contains(".")) {
            split = string.split("\\.");
        } else {
            split = string.split(":");
        }

        String search = split[0];
        short data = 0;
        if (split.length > 1) {
            try {
                data = Short.parseShort(split[1]);
            } catch (Exception e) {
            }
        }

        //Get item from ID
        try {
            int id = Integer.parseInt(search);
            Material mat = Material.getMaterial(id);
            if (mat != null) {
                return getItem(mat, data);
            }
        } catch (Exception e) {}

        //Get item from string
        if (matchLookup.containsKey(search)) {
            ItemData match = itemLookup.get(matchLookup.get(search));
            return getItem(match.getType(), data);
        }

        //No match
        return null;
    }

    /**
     * Find matching {@link MaterialData} for the specified string.
     * See {@link #getItem(String)} to know what strings will match.
     * @param match The string to find a match with. {@link #getItem(String)}
     * @return The {@link MaterialData} that matches the string. (Will be {@code null} when there is no match found!)
     */
    public static MaterialData getMaterialData(String match) {
        ItemData alias = getItem(match);
        if (alias == null) {
            return null;
        }
        return new MaterialData(alias.getType(), (byte)alias.getData());
    }

    /**
     * Get the display name for the specified item match string.
     * See {@link #getItem(String)} to know what strings will match.
     * @see ItemData#getName()
     * @param match The string to find a match with. {@link #getItem(String)}
     * @return The display name for the matched item. (Will return match string when there is no match found!)
     */
    public static String getName(String match) {
        ItemData alias = getItem(match);
        if (alias == null) {
            return match;
        }
        return alias.getName();
    }

    /**
     * Get the display name for the specified materialdata item.
     * For example if you put WOOL with data 9 it would return 'Cyan Wool'.
     * If no name is found with the data the data value will be appended like 'Wool:9'
     * @see #getItem(MaterialData)
     * @see ItemData#getName()
     * @param materialData The materialdata to get the name from.
     * @return The display name for the specified materialdata item.
     */
    public static String getName(MaterialData materialData) {
        return getName(materialData.getItemType(), materialData.getData());
    }

    /**
     * Get the display name for the specified material and data item.
     * For example if you put WOOL with data 9 it would return 'Cyan Wool'.
     * If no name is found with the data the data value will be appended like 'Wool:9'
     * @see #getItem(Material, short)
     * @see ItemData#getName()
     * @param material The material to get the name from.
     * @param data The data to get the name from. (0 for the default name for most items)
     * @return The display name for the specified material and data item.
     */
    public static String getName(Material material, short data) {
        ItemData item = getItem(material, data);
        if (item.getData() == data) {
            return item.getName();
        }
        return item.getName() + ":" + data;
    }

    /**
     * Register a item or update an existing item name/aliases.
     * If there is already an item registered with the material and data it will update it.
     * When updating the name will be set to the specified displayName and it will try to add the specified aliases.
     * If the aliases are already registered for the same item it will just ignore it.
     * @see ItemData
     * @param displayName The display name for the item.
     *                    Should have spaces between words and camel cased.
     *                    For example: Diamond Sword
     * @param material The material type of the item to register.
     * @param data The material data of the item to register.
     * @param aliases Array with aliases for the item.
     *                Aliases should be all lowercased and not contain any spaces or underscores etc.
     *                May be null or empty.
     */
    public static void register(String displayName, Material material, short data, String... aliases) {
        String key = material.toString().toLowerCase() + "-" + data;

        ItemData item;
        if (itemLookup.containsKey(key)) {
            //Update the item name and add aliases if they are unique.
            item = itemLookup.get(key);
            item.addAliases(aliases);
            item.setName(displayName);
        } else {
            //Register new item
            item = new ItemData(displayName, material, data, aliases);
            items.add(item);
            itemLookup.put(key, item);
        }

        //Register match strings
        for (String match : item.getMatchStrings()) {
            matchLookup.put(match,key);
        }
    }

    //Register all the items.
    //TODO: Add a bunch more aliases
    static {
        register("Air", Material.AIR, (short)0);

        //Sand, Dirt etc
        register("Grass", Material.GRASS, (short)0);
        register("Dirt", Material.DIRT, (short)0, "earth");
        register("Coarse Dirt", Material.DIRT, (short)1, "coarse", "coarsed", "cdirt");
        register("Podzol", Material.DIRT, (short)2, "pzol", "podz");
        register("Farmland", Material.SOIL, (short)0, "tilleddirt", "tilledearth", "farm", "farml", "fland");
        register("Sand", Material.SAND, (short)0, "whitesand", "wsand", "yellowsand", "ysand");
        register("Red Sand", Material.SAND, (short)1, "rsand", "reds", "darksand", "dsand");
        register("Gravel", Material.GRAVEL, (short)0, "grav", "grit");
        register("Flint", Material.FLINT, (short)0, "pebble", "peb", "pinnacle", "pinn");
        register("Mycelium", Material.MYCEL, (short)0, "myc", "sgrass", "mgrass", "shroomgrass", "mushroomgrass");

        //Nether
        register("Netherrack", Material.NETHERRACK, (short)0, "netherstone", "nrack", "nstone", "netherr", "nethers");
        register("Nether Brick", Material.NETHER_BRICK, (short)0, "netherbrickblock", "nbrick", "netherb", "nbrickblock", "netherbrickb", "netherbb");
        register("Nether Brick Item", Material.NETHER_BRICK_ITEM, (short)0, "netherbi", "netherbricki", "nbrickitem", "nbricki");
        register("Soul Sand", Material.SOUL_SAND, (short)0, "nethersand", "ssand", "souls", "brownsand", "bsand", "slowsand");
        register("Glowstone", Material.GLOWSTONE, (short)0, "glowingstone", "glowstoneblock", "glowblock", "glowstoneb", "gstone");
        register("Glowstone Dust", Material.GLOWSTONE_DUST, (short)0, "glowingstonedust", "gstonedust", "glowdust", "gdust", "gsdust", "glowingdust", "glowstoned", "glowstoneitem", "glowi", "glowstonei");
        register("Nether Quartz", Material.QUARTZ, (short)0, "quartzitem", "quartzi", "nquartz", "netherq", "netherqi", "nquartzi", "qua");
        register("Quartz Block", Material.QUARTZ_BLOCK, (short)0, "quartzb", "nquartzb", "quablock");
        register("Chiseled Quartz", Material.QUARTZ_BLOCK, (short)1, "chisquartz", "chizeledquartz", "chizquartz", "chiseledq", "quartzc", "quartzchis", "quartzchiz");
        register("Quartz Pillar", Material.QUARTZ_BLOCK, (short)2, "pillarquartz", "quartzp", "pillarq", "quartzpil", "pilquartz");

        //End
        register("End Stone", Material.ENDER_STONE, (short)0, "estone", "enders", "ends");

        //Snow & Ice
        register("Snow", Material.SNOW, (short)0, "snowlayer", "layeredsnow", "snowpile", "scarpet", "snowcarpet", "slayer", "snowl", "lsnow", "stackablesnow", "ssnow", "stacksnow");
        register("Ice", Material.ICE, (short)0, "iceblock", "icy", "frozenwater", "fwater");
        register("Packed Ice", Material.PACKED_ICE, (short)0, "pice", "packedi", "solidice", "packice", "packi");
        register("Snow Block", Material.SNOW_BLOCK, (short)0, "snowb", "bsnow", "blocksnow");
        register("Snowball", Material.SNOW_BALL, (short)0, "sball", "snowb");

        //Stones
        register("Stone", Material.STONE, (short)0, "smoothstone", "rock");
        register("Granite", Material.STONE, (short)1, "gran", "grani");
        register("Polished Granite", Material.STONE, (short)2, "pgranite", "polgranite", "pgran", "polgran", "granitep");
        register("Diorite", Material.STONE, (short)3, "dior");
        register("Polished Diorite", Material.STONE, (short)4, "pdiorite", "poldiorite", "pdior", "poldior", "dioritep");
        register("Andesite", Material.STONE, (short)5, "ande", "andes");
        register("Polished Andesite", Material.STONE, (short)6, "pandesite", "polandesite", "pandes", "polandes", "andesitep");
        register("Cobblestone", Material.COBBLESTONE, (short)0, "cobble", "cstone", "cs", "cobstone");
        register("Moss Stone", Material.MOSSY_COBBLESTONE, (short)0, "moss", "mosss", "mossc", "mossystone", "mossycobble", "mosscobble", "mcobble", "mcobblestone");
        register("Stone Brick", Material.SMOOTH_BRICK, (short)0, "smoothstone", "smoothbrick", "smoothstonebrick", "sbrick", "sstone");
        register("Mossy Stone Brick", Material.SMOOTH_BRICK, (short)1, "mossbrick", "mossybrick", "msbrick", "mosss", "mossb", "mossys", "mossyb");
        register("Cracked Stone Brick", Material.SMOOTH_BRICK, (short)2, "crackbrick", "crackedbrick", "crackstone", "crackedstone", "csbrick", "cracks", "crackb");
        register("Chiseled Stone Brick", Material.SMOOTH_BRICK, (short)3, "chisbrick", "chizbrick", "chiseledbrick", "chiseledstone", "chizeledbrick", "chizeledstone", "chbrick", "chstone", "chiss", "chizs", "chisb", "chizb");
        register("Stone Monster Egg", Material.MONSTER_EGGS, (short)0, "stoneegg", "monsteregg");
        register("Cobblestone Monster Egg", Material.MONSTER_EGGS, (short)1, "cobblestoneegg", "cobbleegg", "cobegg");
        register("Stone Brick Monster Egg", Material.MONSTER_EGGS, (short)2, "stonebrickegg", "brickegg", "smoothstoneegg", "ssegg");
        register("Mossy Stone Brick Monster Egg", Material.MONSTER_EGGS, (short)3, "mossystonebrickegg", "mossybrickegg", "mossstoneegg", "mossegg", "mossyegg", "msegg");
        register("Cracked Stone Brick Monster Egg", Material.MONSTER_EGGS, (short)4, "crackedstonebrickegg", "crackedstoneegg", "crackedbrickegg", "crackegg", "crackedegg", "csegg");
        register("Chiseled Stone Brick Monster Egg", Material.MONSTER_EGGS, (short)5, "chiseledstonebrickegg", "chiseledstoneegg", "chizeledstoneegg", "chiseledbrickegg", "chizeledbrickegg", "chisegg", "chizegg", "chsegg");
        register("Obsidian", Material.OBSIDIAN, (short)0, "obsid", "oby", "obby");
        register("Bedrock", Material.BEDROCK, (short)0, "adminblock", "oprock", "adminrock", "adminium", "brock");

        //Sandstone
        register("Sandstone", Material.SANDSTONE, (short)0, "sastone", "sands");
        register("Chiseled Sandstone", Material.SANDSTONE, (short)1, "chiselsandstone", "chiseledss", "chiselss", "csstone", "creepersandstone", "creepsandstone", "creepersstone");
        register("Smooth Sandstone", Material.SANDSTONE, (short)2, "smoothsand", "ssstone", "smoothss");
        register("Red Sandstone", Material.RED_SANDSTONE, (short)0, "redsands", "rsandstone", "darksandstone", "dsandstone", "dsands", "darksands");
        register("Chiseled Red Sandstone", Material.RED_SANDSTONE, (short)1, "withersandstone", "witherss", "withersands", "withersstone", "crsandstone", "crsand", "chredsand", "chredsandstone");
        register("Smooth Red Sandstone", Material.RED_SANDSTONE, (short)2, "smoothredsand", "smoothrsandstone", "srsandstone", "srsand");

        //Clay & Brick
        register("Clay", Material.CLAY_BALL, (short)0, "clayballs", "cball", "clayitem", "clayi");
        register("Clay Block", Material.CLAY, (short)0, "clayb");
        register("Clay Brick", Material.CLAY_BRICK, (short)0, "cbrick", "brickitem", "bricki", "claybrickitem", "claybricki");
        register("Brick", Material.BRICK, (short)0, "brickblock", "cbrickb", "claybrickb", "brickb", "bricks");
        register("Hardened Clay", Material.HARD_CLAY, (short)0, "hclay", "clayhard", "clayh");
        register("White Stained Clay", Material.STAINED_CLAY, (short)0, "whiteclay", "stainedc", "coloredclay", "colorclay", "clayc", "clay", "clrclay");
        register("Orange Stained Clay", Material.STAINED_CLAY, (short)1, "orangeclay", "lightredclay", "lredclay");
        register("Magenta Stained Clay", Material.STAINED_CLAY, (short)2, "magentaclay", "lightpurpleclay", "lpurpleclay", "pinkclay");
        register("Light Blue Stained Clay", Material.STAINED_CLAY, (short)3, "lightblueclay", "lblueclay", "aquaclay");
        register("Yellow Stained Clay", Material.STAINED_CLAY, (short)4, "yellowclay", "yelclay");
        register("Lime Stained Clay", Material.STAINED_CLAY, (short)5, "limeclay", "lightgreenclay", "lgreenclay");
        register("Pink Stained Clay", Material.STAINED_CLAY, (short)6, "pinkclay", "lightpurpleclay", "lpurpleclay");
        register("Gray Stained Clay", Material.STAINED_CLAY, (short)7, "grayclay", "darkgrayclay", "dgrayclay");
        register("Light Gray Stained Clay", Material.STAINED_CLAY, (short)8, "lightgrayclay", "lgrayclay");
        register("Cyan Stained Clay", Material.STAINED_CLAY, (short)9, "cyanclay", "darkaquaclay", "daquaclay");
        register("Purple Stained Clay", Material.STAINED_CLAY, (short)10, "purpleclay", "darkpurpleclay", "dpurpleclay");
        register("Blue Stained Clay", Material.STAINED_CLAY, (short)11, "blueclay", "darkblueclay", "dblueclay");
        register("Brown Stained Clay", Material.STAINED_CLAY, (short)12, "brownclay", "darkbrownclay", "dbrownclay");
        register("Green Stained Clay", Material.STAINED_CLAY, (short)13, "greenclay", "darkgreenclay", "dgreenclay");
        register("Red Stained Clay", Material.STAINED_CLAY, (short)14, "redclay", "darkredclay", "dredclay");
        register("Black Stained Clay", Material.STAINED_CLAY, (short)15, "blackclay", "darkclay");

        //Prismarine
        register("Prismarine", Material.PRISMARINE, (short)0, "pris", "prismarineb", "prisb", "prismarineblock", "prisblock");
        register("Prismarine Bricks", Material.PRISMARINE, (short)1, "smoothprismarine", "prismarinesmooth", "prismarinebrick", "prisbricks", "prisbrick", "prissmooth", "smoothpris");
        register("Dark Prismarine", Material.PRISMARINE, (short)2, "darkpris", "darkprismarineb", "darkprisb", "darkprisblock");
        register("Sea Lantern", Material.SEA_LANTERN, (short)0, "slantern", "sealamp", "sealight", "prismarinelamp", "prismarinelantern", "prismarinelight", "prislamp", "prislantern", "prislight");

        //Trees
        register("Oak Log", Material.LOG, (short)0, "log", "trunk", "oak", "olog", "owood");
        register("Spruce Log", Material.LOG, (short)1, "slog", "sprucel");
        register("Birch Log", Material.LOG, (short)2, "blog", "birchl");
        register("Jungle Log", Material.LOG, (short)3, "jlog", "junglel");
        register("Acacia Log", Material.LOG_2, (short)0, "alog", "acacial");
        register("Dark Oak Log", Material.LOG_2, (short)1, "dolog", "darkl", "darkoakl", "doakl");
        register("Oak Leaves", Material.LEAVES, (short)0, "oakleaf", "oleaves", "oleaf", "leaves", "leaf");
        register("Spruce Leaves", Material.LEAVES, (short)1, "spruceleaf", "sleaves", "sleaf");
        register("Birch Leaves", Material.LEAVES, (short)2, "birchleaf", "bleaves", "bleaf");
        register("Jungle Leaves", Material.LEAVES, (short)3, "jungleleaf", "jleaves", "jleaf");
        register("Acacia Leaves", Material.LEAVES_2, (short)0, "acacialeaf", "aleaves", "aleaf");
        register("Dark Oak Leaves", Material.LEAVES_2, (short)1, "darkoakleaf", "doleaves", "doleaf", "darkoleaves", "darkoleaf", "doakleaves", "doakleaf");
        register("Oak Sapling", Material.SAPLING, (short)0, "oaksap", "osapling", "osappling", "osap", "treesapling", "logsapling", "smalltree", "treesap", "logsap", "stree");
        register("Spruce Sapling", Material.SAPLING, (short)1, "sprucesap", "ssapling", "ssappling", "ssap");
        register("Birch Sapling", Material.SAPLING, (short)2, "birchsap", "bsapling", "bsappling", "bsap");
        register("Jungle Sapling", Material.SAPLING, (short)3, "junglesap", "jsapling", "jsappling", "jsap");
        register("Acacia Sapling", Material.SAPLING, (short)4, "acaciasap", "asapling", "asappling", "asap");
        register("Dark Oak Sapling", Material.SAPLING, (short)5, "darkoaksap", "dosapling", "dosappling", "darkosapling", "darkosappling", "darkosap", "doaksapling", "doaksappling", "doaksap", "dosap");

        //Wood
        register("Oak Plank", Material.WOOD, (short)0, "oplank", "oakwood", "oakp", "oakw", "owood", "woodenplank", "woodplank", "plankwooden", "plankwood", "plankw", "wplank");
        register("Spruce Plank", Material.WOOD, (short)1, "splank", "sprucewood", "sprucep", "sprucew", "swood");
        register("Birch Plank", Material.WOOD, (short)2, "bplank", "birchwood", "birchp", "birchw", "bwood");
        register("Jungle Plank", Material.WOOD, (short)3, "jplank", "junglewood", "junglep", "junglew", "jwood");
        register("Acacia Plank", Material.WOOD, (short)4, "aplank", "aciciawood", "aciciap", "aciciaw", "awood");
        register("Dark Oak Plank", Material.WOOD, (short)5, "doplank", "doakwood", "doakp", "doakw", "dowood", "darkoplank", "darkoakwood", "darkoakp", "darkoakw", "darkowood", "doakplank");
        register("Stick", Material.STICK, (short)0, "sticks", "branch", "branches");
        register("Ladder", Material.LADDER, (short)0, "lad");
        register("Bowl", Material.BOWL, (short)0, "soupbowl");

        //Double slabs
        register("Double Oak Slab", Material.WOOD_DOUBLE_STEP, (short)0);
        register("Double Spruce Slab", Material.WOOD_DOUBLE_STEP, (short)1);
        register("Double Birch Slab", Material.WOOD_DOUBLE_STEP, (short)2);
        register("Double Jungle Wood Slab", Material.WOOD_DOUBLE_STEP, (short)3);
        register("Double Acacia Wood Slab", Material.WOOD_DOUBLE_STEP, (short)4);
        register("Double Dark Oak Wood Slab", Material.WOOD_DOUBLE_STEP, (short)5);
        register("Double Stone Slab", Material.DOUBLE_STEP, (short)0);
        register("Double Sandstone Slab", Material.DOUBLE_STEP, (short)1);
        register("Double Wooden Slab", Material.DOUBLE_STEP, (short)2);
        register("Double Cobblestone Slab", Material.DOUBLE_STEP, (short)3);
        register("Double Brick Slab", Material.DOUBLE_STEP, (short)4);
        register("Double Stone Brick Slab", Material.DOUBLE_STEP, (short)5);
        register("Double Nether Brick Slab", Material.DOUBLE_STEP, (short)6);
        register("Double Quartz Slab", Material.DOUBLE_STEP, (short)7);
        register("Double Smooth Quartz Slab", Material.DOUBLE_STEP, (short)8);
        register("Double Smooth Sandstone Slab", Material.DOUBLE_STEP, (short)9);

        //Slabs
        register("Oak Slab", Material.WOOD_STEP, (short)0);
        register("Spruce Slab", Material.WOOD_STEP, (short)1);
        register("Birch Slab", Material.WOOD_STEP, (short)2);
        register("Jungle Wood Slab", Material.WOOD_STEP, (short)3);
        register("Acacia Slab", Material.WOOD_STEP, (short)4);
        register("Dark Oak Slab", Material.WOOD_STEP, (short)5);
        register("Stone Slab", Material.STEP, (short)0);
        register("Sandstone Slab", Material.STEP, (short)1);
        register("Wooden Slab", Material.STEP, (short)2);
        register("Cobblestone Slab", Material.STEP, (short)3);
        register("Brick Slab", Material.STEP, (short)4);
        register("Stone Brick Slab", Material.STEP, (short)5);
        register("Nether Brick Slab", Material.STEP, (short)6);
        register("Quartz Slab", Material.STEP, (short)7);
        register("Red Sandstone Slab", Material.STONE_SLAB2, (short)0);

        //Stairs
        register("Oak Wood Stairs", Material.WOOD_STAIRS, (short)0);
        register("Spruce Wood Stairs", Material.SPRUCE_WOOD_STAIRS, (short)0);
        register("Birch Wood Stairs", Material.BIRCH_WOOD_STAIRS, (short)0);
        register("Jungle Wood Stairs", Material.JUNGLE_WOOD_STAIRS, (short)0);
        register("Acacia Wood Stairs", Material.ACACIA_STAIRS, (short)0);
        register("Dark Oak Wood Stairs", Material.DARK_OAK_STAIRS, (short)0);
        register("Stone Brick Stairs", Material.SMOOTH_STAIRS, (short)0);
        register("Cobblestone Stairs", Material.COBBLESTONE_STAIRS, (short)0);
        register("Brick Stairs", Material.BRICK_STAIRS, (short)0);
        register("Nether Brick Stairs", Material.NETHER_BRICK_STAIRS, (short)0);
        register("Quartz Stairs", Material.QUARTZ_STAIRS, (short)0);
        register("Sandstone Stair", Material.SANDSTONE_STAIRS, (short)0);
        register("Red Sandstone Stairs", Material.RED_SANDSTONE_STAIRS, (short)0);

        //Fencing
        register("Iron Fence", Material.IRON_FENCE, (short)0);
        register("Fence Gate", Material.FENCE_GATE, (short)0);
        register("Spruce Fence Gate", Material.SPRUCE_FENCE_GATE, (short)0);
        register("Birch Fence Gate", Material.BIRCH_FENCE_GATE, (short)0);
        register("Jungle Fence Gate", Material.JUNGLE_FENCE_GATE, (short)0);
        register("Dark Oak Fence Gate", Material.DARK_OAK_FENCE_GATE, (short)0);
        register("Acacia Fence Gate", Material.ACACIA_FENCE_GATE, (short)0);
        register("Oak Fence", Material.FENCE, (short)0);
        register("Spruce Fence", Material.SPRUCE_FENCE, (short)0);
        register("Birch Fence", Material.BIRCH_FENCE, (short)0);
        register("Jungle Fence", Material.JUNGLE_FENCE, (short)0);
        register("Dark Oak Fence", Material.DARK_OAK_FENCE, (short)0);
        register("Acacia Fence", Material.ACACIA_FENCE, (short)0);
        register("Nether Brick Fence", Material.NETHER_FENCE, (short)0);
        register("Cobblestone Wall", Material.COBBLE_WALL, (short)0);
        register("Mossy Cobblestone Wall", Material.COBBLE_WALL, (short)1);

        //Gates
        register("Iron Trapdoor", Material.IRON_TRAPDOOR, (short)0);
        register("Wooden Trapdoor", Material.TRAP_DOOR, (short)0);
        register("Wooden Door", Material.WOOD_DOOR, (short)0);
        register("Wooden Door Block", Material.WOODEN_DOOR, (short)0);
        register("Iron Door", Material.IRON_DOOR, (short)0);
        register("Iron Door Block", Material.IRON_DOOR_BLOCK, (short)0);
        register("Spruce Door", Material.SPRUCE_DOOR_ITEM, (short)0);
        register("Spruce Door Block", Material.SPRUCE_DOOR, (short)0);
        register("Birch Door", Material.BIRCH_DOOR_ITEM, (short)0);
        register("Birch Door Block", Material.BIRCH_DOOR, (short)0);
        register("Jungle Door", Material.JUNGLE_DOOR_ITEM, (short)0);
        register("Jungle Door Block", Material.JUNGLE_DOOR, (short)0);
        register("Dark Oak Door", Material.DARK_OAK_DOOR_ITEM, (short)0);
        register("Dark Oak Door Block", Material.DARK_OAK_DOOR, (short)0);
        register("Acacia Door", Material.ACACIA_DOOR_ITEM, (short)0);
        register("Acacia Door Block", Material.ACACIA_DOOR, (short)0);

        //Wool
        register("White Wool", Material.WOOL, (short)0, "whitecloth", "whitew", "wool", "cloth");
        register("Orange Wool", Material.WOOL, (short)1, "orangecloth", "orangew");
        register("Magenta Wool", Material.WOOL, (short)2, "magentacloth", "magentaw");
        register("Light Blue Wool", Material.WOOL, (short)3, "lightbluecloth", "lightbluew");
        register("Yellow Wool", Material.WOOL, (short)4, "yellowcloth", "yelloww");
        register("Light Green Wool", Material.WOOL, (short)5, "greencloth", "greenw", "greenwool");
        register("Pink Wool", Material.WOOL, (short)6, "pinkcloth", "pinkw");
        register("Gray Wool", Material.WOOL, (short)7, "graycloth", "grayw", "darkgraywool");
        register("Light Gray Wool", Material.WOOL, (short)8, "lightgraycloth", "lightgrayw");
        register("Cyan Wool", Material.WOOL, (short)9, "cyancloth", "cyanw");
        register("Purple Wool", Material.WOOL, (short)10, "purplecloth", "purplew");
        register("Blue Wool", Material.WOOL, (short)11, "bluecloth", "bluew");
        register("Brown Wool", Material.WOOL, (short)12, "browncloth", "brownw");
        register("Dark Green Wool", Material.WOOL, (short)13, "darkgreencloth", "darkgreenw");
        register("Red Wool", Material.WOOL, (short)14, "redcloth", "redw");
        register("Black Wool", Material.WOOL, (short)15, "blackcloth", "blackw");
        register("White Carpet", Material.CARPET, (short)0);
        register("Orange Carpet", Material.CARPET, (short)1);
        register("Magenta Carpet", Material.CARPET, (short)2);
        register("Light Blue Carpet", Material.CARPET, (short)3);
        register("Yellow Carpet", Material.CARPET, (short)4);
        register("Light Green Carpet", Material.CARPET, (short)5);
        register("Pink Carpet", Material.CARPET, (short)6);
        register("Gray Carpet", Material.CARPET, (short)7);
        register("Light Gray Carpet", Material.CARPET, (short)8);
        register("Cyan Carpet", Material.CARPET, (short)9);
        register("Purple Carpet", Material.CARPET, (short)10);
        register("Blue Carpet", Material.CARPET, (short)11);
        register("Brown Carpet", Material.CARPET, (short)12);
        register("Dark Green Carpet", Material.CARPET, (short)13);
        register("Red Carpet", Material.CARPET, (short)14);
        register("Black Carpet", Material.CARPET, (short)15);

        //Ores
        register("Gold Ore", Material.GOLD_ORE, (short)0, "oregold", "gore", "oreg", "goldo");
        register("Iron Ore", Material.IRON_ORE, (short)0, "oreiron", "iore", "orei", "irono");
        register("Coal Ore", Material.COAL_ORE, (short)0, "orecoal", "core", "orec", "coalo");
        register("Lapis Lazuli Ore", Material.LAPIS_ORE, (short)0, "lapisore", "lapiso", "lore", "orelapis", "orel", "orelapislazuli");
        register("Diamond Ore", Material.DIAMOND_ORE, (short)0, "diaore");
        register("Redstone Ore", Material.REDSTONE_ORE, (short)0);
        register("Glowing Redstone Ore", Material.GLOWING_REDSTONE_ORE, (short)0);
        register("Nether Quartz Ore", Material.QUARTZ_ORE, (short)0);
        register("Emerald Ore", Material.EMERALD_ORE, (short)0);

        //Liquids
        register("Moving Water", Material.WATER, (short)0, "movingwater", "mwater", "flowingwater", "fwater");
        register("Water", Material.STATIONARY_WATER, (short)0);
        register("Moving Lava", Material.LAVA, (short)0, "movinglava", "mlava", "flowinglava", "flava");
        register("Lava", Material.STATIONARY_LAVA, (short)0);
        register("Bucket", Material.BUCKET, (short)0);
        register("Water Bucket", Material.WATER_BUCKET, (short)0, "wbucket");
        register("Lava Bucket", Material.LAVA_BUCKET, (short)0, "lbucket");
        register("Milk Bucket", Material.MILK_BUCKET, (short)0, "mbucket");

        //Glass
        register("Glass", Material.GLASS, (short)0, "glassblock", "blockglass");
        register("Glass Pane", Material.THIN_GLASS, (short)0, "pane");
        register("White Stained Glass", Material.STAINED_GLASS, (short)0);
        register("Orange Stained Glass", Material.STAINED_GLASS, (short)1);
        register("Magenta Stained Glass", Material.STAINED_GLASS, (short)2);
        register("Light Blue Stained Glass", Material.STAINED_GLASS, (short)3);
        register("Yellow Stained Glass", Material.STAINED_GLASS, (short)4);
        register("Light Green Stained Glass", Material.STAINED_GLASS, (short)5);
        register("Pink Stained Glass", Material.STAINED_GLASS, (short)6);
        register("Gray Stained Glass", Material.STAINED_GLASS, (short)7);
        register("Light Gray Stained Glass", Material.STAINED_GLASS, (short)8);
        register("Cyan Stained Glass", Material.STAINED_GLASS, (short)9);
        register("Purple Stained Glass", Material.STAINED_GLASS, (short)10);
        register("Blue Stained Glass", Material.STAINED_GLASS, (short)11);
        register("Brown Stained Glass", Material.STAINED_GLASS, (short)12);
        register("Dark Green Stained Glass", Material.STAINED_GLASS, (short)13);
        register("Red Stained Glass", Material.STAINED_GLASS, (short)14);
        register("Black Stained Glass", Material.STAINED_GLASS, (short)15);
        register("White Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)0);
        register("Orange Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)1);
        register("Magenta Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)2);
        register("Light Blue Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)3);
        register("Yellow Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)4);
        register("Light Green Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)5);
        register("Pink Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)6);
        register("Gray Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)7);
        register("Light Gray Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)8);
        register("Cyan Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)9);
        register("Purple Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)10);
        register("Blue Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)11);
        register("Brown Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)12);
        register("Dark Green Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)13);
        register("Red Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)14);
        register("Black Stained Glass Pane", Material.STAINED_GLASS_PANE, (short)15);

        //Containers
        register("Chest", Material.CHEST, (short)0);
        register("Trapped Chest", Material.TRAPPED_CHEST, (short)0);
        register("Ender Chest", Material.ENDER_CHEST, (short)0);
        register("Enchantment Table", Material.ENCHANTMENT_TABLE, (short)0, "enchanttable", "magictable");
        register("Anvil", Material.ANVIL, (short)0);
        register("Slightly Damaged Anvil", Material.ANVIL, (short)1);
        register("Very Damaged Anvil", Material.ANVIL, (short)2);
        register("Crafting Table", Material.WORKBENCH, (short)0, "worktable", "workbench");
        register("Furnace", Material.FURNACE, (short)0);
        register("Dispenser", Material.DISPENSER, (short)0, "dispense");
        register("Dropper", Material.DROPPER, (short)0);
        register("Hopper", Material.HOPPER, (short)0);
        register("Beacon Block", Material.BEACON, (short)0);

        //Utility blocks
        register("Bed Block", Material.BED_BLOCK, (short)0);
        register("Sticky Piston", Material.PISTON_STICKY_BASE, (short)0, "stickyp", "spiston", "psticky", "pistonsticky");
        register("Piston", Material.PISTON_BASE, (short)0);
        register("Piston Extension", Material.PISTON_EXTENSION, (short)0);
        register("Piston Head", Material.PISTON_MOVING_PIECE, (short)1);
        register("Sticky Piston Head", Material.PISTON_MOVING_PIECE, (short)9);

        //Music
        register("Note Block", Material.NOTE_BLOCK, (short)0, "nblock", "noteb", "note");
        register("Jukebox", Material.JUKEBOX, (short)0, "musicplayer");
        register("13 Disc", Material.GOLD_RECORD, (short)0);
        register("cat Disc", Material.GREEN_RECORD, (short)0);
        register("blocks Disc", Material.RECORD_3, (short)0);
        register("chirp Disc", Material.RECORD_4, (short)0);
        register("far Disc", Material.RECORD_5, (short)0);
        register("mall Disc", Material.RECORD_6, (short)0);
        register("mellohi Disc", Material.RECORD_7, (short)0);
        register("stahl Disc", Material.RECORD_8, (short)0);
        register("strad Disc", Material.RECORD_9, (short)0);
        register("ward Disc", Material.RECORD_10, (short)0);
        register("11 Disc", Material.RECORD_11, (short)0);
        register("wait Disc", Material.RECORD_12, (short)0);

        //Valuable blocks
        register("Gold Block", Material.GOLD_BLOCK, (short)0, "gblock", "blockgold", "goldb");
        register("Iron Block", Material.IRON_BLOCK, (short)0, "iblock", "blockiron", "ironb");
        register("Lapis Lazuli Block", Material.LAPIS_BLOCK, (short)0, "lapisblock", "lblock", "llblock", "blocklapis");
        register("Diamond Block", Material.DIAMOND_BLOCK, (short)0, "diablock");
        register("Emerald Block", Material.EMERALD_BLOCK, (short)0);
        register("Block of Coal", Material.COAL_BLOCK, (short)0);

        //Valuable items
        register("Coal", Material.COAL, (short)0);
        register("Charcoal", Material.COAL, (short)1, "treecoal", "logcoal");
        register("Diamond", Material.DIAMOND, (short)0, "dia");
        register("Iron Ingot", Material.IRON_INGOT, (short)0, "iron", "iingot");
        register("Gold Ingot", Material.GOLD_INGOT, (short)0, "gold", "gingot");
        register("Emerald", Material.EMERALD, (short)0);
        register("Gold Nugget", Material.GOLD_NUGGET, (short)0, "nugget", "gnugget");

        //Rails & Vehicles
        register("Rails", Material.RAILS, (short)0, "rail");
        register("Powered Rail", Material.POWERED_RAIL, (short)0, "prail", "prails", "ptrack", "poweredtrack", "boostrail", "boostrails", "boosttrack", "brail", "btrack", "brails");
        register("Detector Rail", Material.DETECTOR_RAIL, (short)0, "drail", "drails", "dtrack", "detectortrack");
        register("Activator Rail", Material.ACTIVATOR_RAIL, (short)0);
        register("Minecart", Material.MINECART, (short)0, "cart");
        register("Storage Minecart", Material.STORAGE_MINECART, (short)0, "storagemcart", "chestcart", "chestminecart");
        register("Powered Minecart", Material.POWERED_MINECART, (short)0, "powercart", "powerminecart", "furnaceminecart", "furnacecart", "enginecart", "engineminecart");
        register("Command Minecart", Material.COMMAND_MINECART, (short)0, "cmdminecart", "commandcart", "cmdcart");
        register("Explosive Minecart", Material.EXPLOSIVE_MINECART, (short)0);
        register("Hopper Minecart", Material.HOPPER_MINECART, (short)0);
        register("Boat", Material.BOAT, (short)0);

        //Redstone
        register("Redstone Dust", Material.REDSTONE, (short)0, "reddust", "rsdust", "rdust", "redstone");
        register("Redstone Wire", Material.REDSTONE_WIRE, (short)0);
        register("Block of Redstone", Material.REDSTONE_BLOCK, (short)0);
        register("Redstone Torch", Material.REDSTONE_TORCH_ON, (short)0);
        register("Redstone Torch Off", Material.REDSTONE_TORCH_OFF, (short)0);
        register("Redstone Lamp", Material.REDSTONE_LAMP_OFF, (short)0);
        register("Redstone Lamp On", Material.REDSTONE_LAMP_ON, (short)0);
        register("Wood Button", Material.WOOD_BUTTON, (short)0);
        register("Stone Button", Material.STONE_BUTTON, (short)0);
        register("Lever", Material.LEVER, (short)0, "switch");
        register("Stone Pressure Plate", Material.STONE_PLATE, (short)0, "stoneplate");
        register("Wooden Pressure Plate", Material.WOOD_PLATE, (short)0, "woodenplate", "woodplate");
        register("Weighted Gold Plate", Material.GOLD_PLATE, (short)0);
        register("Weighted Iron Plate", Material.IRON_PLATE, (short)0);
        register("Tripwire Hook", Material.TRIPWIRE_HOOK, (short)0);
        register("Tripwire", Material.TRIPWIRE, (short)0);
        register("Daylight Sensor", Material.DAYLIGHT_DETECTOR, (short)0);
        register("Inverted Daylight Sensor", Material.DAYLIGHT_DETECTOR_INVERTED, (short)0);
        register("Redstone Repeater", Material.DIODE, (short)0, "repeater", "repeator");
        register("Redstone Repeater Off", Material.DIODE_BLOCK_OFF, (short)0);
        register("Redstone Repeater On", Material.DIODE_BLOCK_ON, (short)0);
        register("Redstone Comparator", Material.REDSTONE_COMPARATOR, (short)0);
        register("Redstone Comparator On", Material.REDSTONE_COMPARATOR_ON, (short)0);
        register("Redstone Comparator Off", Material.REDSTONE_COMPARATOR_OFF, (short)0);
        register("Command Block", Material.COMMAND, (short)0);
        register("Slime Block", Material.SLIME_BLOCK, (short)0);

        //Plants
        register("Dead Bush", Material.DEAD_BUSH, (short)0, "deadbush", "dbush", "bush");
        register("Dead Shrub", Material.LONG_GRASS, (short)0, "dshrub", "shrub");
        register("Tall Grass", Material.LONG_GRASS, (short)1, "tgrass", "longgrass", "lgrass", "grasstall", "grasst", "grasslong", "grassl");
        register("Fern", Material.LONG_GRASS, (short)2);
        register("Dandelion", Material.YELLOW_FLOWER, (short)0, "yellowflower");
        register("Poppy", Material.RED_ROSE, (short)0);
        register("Blue Orchid", Material.RED_ROSE, (short)1);
        register("Allium", Material.RED_ROSE, (short)2);
        register("Azure Bluet", Material.RED_ROSE, (short)3);
        register("Red Tulip", Material.RED_ROSE, (short)4);
        register("Orange Tulip", Material.RED_ROSE, (short)5);
        register("White Tulip", Material.RED_ROSE, (short)6);
        register("Pink Tulip", Material.RED_ROSE, (short)7);
        register("Oxeye Daisy", Material.RED_ROSE, (short)8);
        register("Sunflower", Material.DOUBLE_PLANT, (short)0);
        register("Lilac", Material.DOUBLE_PLANT, (short)1);
        register("Double Tallgrass", Material.DOUBLE_PLANT, (short)2);
        register("Large Fern", Material.DOUBLE_PLANT, (short)3);
        register("Rose Bush", Material.DOUBLE_PLANT, (short)4);
        register("Peony", Material.DOUBLE_PLANT, (short)5);
        register("Brown Mushroom", Material.BROWN_MUSHROOM, (short)0, "brownshroom");
        register("Red Mushroom", Material.RED_MUSHROOM, (short)0, "redshroom");
        register("Huge Brown Mushroom", Material.HUGE_MUSHROOM_1, (short)0, "bigbrownmushroom", "hugebrownshroom", "bigbrownshroom");
        register("Huge Red Mushroom", Material.HUGE_MUSHROOM_2, (short)0, "bigredmushroom", "hugeredshroom", "bigredshroom");
        register("Cactus", Material.CACTUS, (short)0, "cacti", "cactuses");
        register("Pumpkin", Material.PUMPKIN, (short)0);
        register("Jack-O-Lantern", Material.JACK_O_LANTERN, (short)0, "jackolantern");
        register("Vines", Material.VINE, (short)0, "vine");
        register("Melon Block", Material.MELON_BLOCK, (short)0, "grownmelon");
        register("Sugar Cane", Material.SUGAR_CANE, (short)0, "reed", "reeds");
        register("Sugar Cane Block", Material.SUGAR_CANE_BLOCK, (short)0);
        register("Netherwarts", Material.NETHER_WARTS, (short)0);
        register("Netherstalk", Material.NETHER_STALK, (short)0);
        register("Lily Pad", Material.WATER_LILY, (short)0, "lillypad", "lilly", "lily", "lpad", "pad");
        register("Cocoa Pod", Material.COCOA, (short)0);

        //Crops
        register("Wheat", Material.WHEAT, (short)0);
        register("Crops", Material.CROPS, (short)0);
        register("Seeds", Material.SEEDS, (short)0, "seed");
        register("Pumpkin Seed", Material.PUMPKIN_SEEDS, (short)0, "pseed");
        register("Melon Seed", Material.MELON_SEEDS, (short)0, "mseed");
        register("Carrot Block", Material.CARROT, (short)0);
        register("Potato Block", Material.POTATO, (short)0);
        register("Pumpkin Stem", Material.PUMPKIN_STEM, (short)0);
        register("Melon Stem", Material.MELON_STEM, (short)0);

        //Food
        register("Cake", Material.CAKE, (short)0);
        register("Cake Block", Material.CAKE_BLOCK, (short)0);
        register("Pumpkin Pie", Material.PUMPKIN_PIE, (short)0);
        register("Bread", Material.BREAD, (short)0);
        register("Cookie", Material.COOKIE, (short)0);
        register("Mushroom Soup", Material.MUSHROOM_SOUP, (short)0, "soup", "shroomsoup");
        register("Rabbit Stew", Material.RABBIT_STEW, (short)0);
        register("Raw Chicken", Material.RAW_CHICKEN, (short)0, "uncookedchicken", "rawchick", "rchick", "rchicken");
        register("Cooked Chicken", Material.COOKED_CHICKEN, (short)0, "cchick", "cchicken", "cookedchick", "kfc");
        register("Raw Porkchop", Material.PORK, (short)0, "rawpork", "pork", "porkchop");
        register("Cooked Porkchop", Material.GRILLED_PORK, (short)0, "cpork", "cporkchop", "cookedpork");
        register("Raw Beef", Material.RAW_BEEF, (short)0, "uncookedbeef", "uncookedsteak", "rbeef", "rawsteak", "rsteak");
        register("Steak", Material.COOKED_BEEF, (short)0, "beef");
        register("Raw Mutton", Material.MUTTON, (short)0);
        register("Cooked Mutton", Material.COOKED_MUTTON, (short)0);
        register("Raw Rabbit", Material.RABBIT, (short)0);
        register("Cooked Rabbit", Material.COOKED_RABBIT, (short)0);
        register("Raw Fish", Material.RAW_FISH, (short)0, "rfish", "fish");
        register("Cooked Fish", Material.COOKED_FISH, (short)0, "cfish", "cookfish");
        register("Raw Salmon", Material.RAW_FISH, (short)1);
        register("Cooked Salmon", Material.COOKED_FISH, (short)1);
        register("Clownfish", Material.RAW_FISH, (short)2);
        register("Pufferfish", Material.RAW_FISH, (short)3);
        register("Apple", Material.APPLE, (short)0);
        register("Golden Apple", Material.GOLDEN_APPLE, (short)0, "gapple");
        register("Enchanted Golden Apple", Material.GOLDEN_APPLE, (short)1, "godapple", "enchantgoldapple", "enchantgapple");
        register("Melon Slice", Material.MELON, (short)0, "melon", "mslice");
        register("Glistering Melon", Material.SPECKLED_MELON, (short)0, "glistermelon", "gmelon");
        register("Carrot", Material.CARROT_ITEM, (short)0);
        register("Golden Carrot", Material.GOLDEN_CARROT, (short)0);
        register("Potato", Material.POTATO_ITEM, (short)0);
        register("Baked Potato", Material.BAKED_POTATO, (short)0);
        register("Poisonous Potato", Material.POISONOUS_POTATO, (short)0);
        register("Sugar", Material.SUGAR, (short)0);

        //Inksack
        register("Ink Sac", Material.INK_SACK, (short)0, "inksack", "dye", "sack", "sac");
        register("Red Dye", Material.INK_SACK, (short)1, "red");
        register("Cactus Green", Material.INK_SACK, (short)2, "greendye", "cactusdye", "cactusgreendye");
        register("Cocoa Beans", Material.INK_SACK, (short)3, "cocoa", "beans");
        register("Lapis Lazuli", Material.INK_SACK, (short)4, "lapis", "lazuli", "bluedye", "blue");
        register("Purple Dye", Material.INK_SACK, (short)5, "purple");
        register("Cyan Dye", Material.INK_SACK, (short)6, "cyan");
        register("Light Gray Dye", Material.INK_SACK, (short)7, "lightgreydye", "lightgray", "lightgrey", "lgraydye", "lgreydye");
        register("Gray Dye", Material.INK_SACK, (short)8, "greydye", "gray", "grey");
        register("Pink Dye", Material.INK_SACK, (short)9, "pink");
        register("Lime Dye", Material.INK_SACK, (short)10, "lime");
        register("Dandelion Yellow", Material.INK_SACK, (short)11, "yellow", "yellowdye", "yellowflowerdye");
        register("Light Blue Dye", Material.INK_SACK, (short)12, "lbdye", "lightblue", "lb");
        register("Magenta Dye", Material.INK_SACK, (short)13, "magenta", "mdye");
        register("Orange Dye", Material.INK_SACK, (short)14, "orange", "odye");
        register("Bone Meal", Material.INK_SACK, (short)15, "meal", "white", "whitedye", "wdye");

        //Writing
        register("Bookshelf", Material.BOOKSHELF, (short)0);
        register("Book", Material.BOOK, (short)0);
        register("Paper", Material.PAPER, (short)0);
        register("Book and Quill", Material.BOOK_AND_QUILL, (short)0);
        register("Written Book", Material.WRITTEN_BOOK, (short)0);
        register("Enchanted Book", Material.ENCHANTED_BOOK, (short)0);

        //Tools
        register("Wooden Shovel", Material.WOOD_SPADE, (short)0, "wshovel", "woodshovel");
        register("Wooden Pickaxe", Material.WOOD_PICKAXE, (short)0, "woodpick", "woodpickaxe", "wpick", "wpickaxe");
        register("Wooden Axe", Material.WOOD_AXE, (short)0, "woodaxe", "waxe");
        register("Wooden Hoe", Material.WOOD_HOE, (short)0, "whoe", "woodhoe");
        register("Stone Shovel", Material.STONE_SPADE, (short)0, "stonespade", "sshovel", "sspade");
        register("Stone Pickaxe", Material.STONE_PICKAXE, (short)0, "stonepick", "spick", "spickaxe");
        register("Stone Axe", Material.STONE_AXE, (short)0, "saxe");
        register("Stone Hoe", Material.STONE_HOE, (short)0, "shoe");
        register("Iron Shovel", Material.IRON_SPADE, (short)0, "ishovel", "ironspade", "ispade");
        register("Iron Pickaxe", Material.IRON_PICKAXE, (short)0, "ironpick", "ipick");
        register("Iron Axe", Material.IRON_AXE, (short)0, "iaxe");
        register("Iron Hoe", Material.IRON_HOE, (short)0, "ihoe");
        register("Gold Shovel", Material.GOLD_SPADE, (short)0, "gshovel", "gspade", "goldspade");
        register("Gold Pickaxe", Material.GOLD_PICKAXE, (short)0, "gpickaxe", "gpick", "goldpick");
        register("Gold Axe", Material.GOLD_AXE, (short)0, "gaxe");
        register("Gold Hoe", Material.GOLD_HOE, (short)0, "ghoe");
        register("Diamond Shovel", Material.DIAMOND_SPADE, (short)0, "diashovel", "dshovel");
        register("Diamond Pickaxe", Material.DIAMOND_PICKAXE, (short)0, "diapick", "diapickaxe", "diamondpick", "dpick", "dpickaxe");
        register("Diamond Axe", Material.DIAMOND_AXE, (short)0, "diaaxe", "daxe");
        register("Diamond Hoe", Material.DIAMOND_HOE, (short)0, "diahoe", "dhoe");
        register("Flint and Steel", Material.FLINT_AND_STEEL, (short)0, "lighter");
        register("Shears", Material.SHEARS, (short)0, "sheers", "shear", "sheer", "woolcutters", "woolcutter");
        register("Fishing Rod", Material.FISHING_ROD, (short)0, "fishrod", "frod", "rod");
        register("Carrot on a Stick", Material.CARROT_STICK, (short)0);
        register("Compass", Material.COMPASS, (short)0);
        register("Clock", Material.WATCH, (short)0);
        register("Map", Material.MAP, (short)0);
        register("Empty Map", Material.EMPTY_MAP, (short)0, "emap");
        register("Leash", Material.LEASH, (short)0);
        register("Name Tag", Material.NAME_TAG, (short)0);
        register("Eye of Ender", Material.EYE_OF_ENDER, (short)0, "endereye", "eeye");
        register("Torch", Material.TORCH, (short)0);
        register("Saddle", Material.SADDLE, (short)0);

        //Weapons
        register("Bow", Material.BOW, (short)0, "bowandarrow");
        register("Arrow", Material.ARROW, (short)0);
        register("Wooden Sword", Material.WOOD_SWORD, (short)0, "wsword", "woodsword");
        register("Stone Sword", Material.STONE_SWORD, (short)0, "ssword");
        register("Iron Sword", Material.IRON_SWORD, (short)0, "isword");
        register("Gold Sword", Material.GOLD_SWORD, (short)0, "gsword");
        register("Diamond Sword", Material.DIAMOND_SWORD, (short)0, "diasword", "dsword");

        //Armor
        register("Leather Cap", Material.LEATHER_HELMET, (short)0, "leatherhelmet", "leatherhelm", "lhelmet", "lhelm", "lcap");
        register("Leather Tunic", Material.LEATHER_CHESTPLATE, (short)0, "leatherchestplate", "leatherchest", "ltunic", "lchestplate", "lchest");
        register("Leather Pants", Material.LEATHER_LEGGINGS, (short)0, "lleggings", "leatherpants", "lpants");
        register("Leather Boots", Material.LEATHER_BOOTS, (short)0, "lboots", "lshoes", "leathershoes");
        register("Chainmail Helmet", Material.CHAINMAIL_HELMET, (short)0, "chelmet", "chelm", "chainhelmet", "chainhelm", "chelmet");
        register("Chainmail Chestplate", Material.CHAINMAIL_CHESTPLATE, (short)0, "cchest", "chainchest", "chainchestplate", "cchestplate");
        register("Chainmail Leggings", Material.CHAINMAIL_LEGGINGS, (short)0, "cleggings", "cpants", "chainleggings", "chainpants", "chainmailpants");
        register("Chainmail Boots", Material.CHAINMAIL_BOOTS, (short)0, "chainboots", "cboots", "cshoes", "chainshoes", "chainmailshoes");
        register("Iron Helmet", Material.IRON_HELMET, (short)0, "ironhelm", "ihelm", "ihelmet");
        register("Iron Chestplate", Material.IRON_CHESTPLATE, (short)0, "ichest", "ichestplate", "ironchest");
        register("Iron Leggings", Material.IRON_LEGGINGS, (short)0, "ileggings", "ipants", "ironpants");
        register("Iron Boots", Material.IRON_BOOTS, (short)0, "iboots", "ishoes");
        register("Gold Helmet", Material.GOLD_HELMET, (short)0, "ghelm", "goldhelm", "ghelmet");
        register("Gold Chestplate", Material.GOLD_CHESTPLATE, (short)0, "gchest", "goldchest", "gchestplate");
        register("Gold Leggings", Material.GOLD_LEGGINGS, (short)0, "gleggings", "glegs", "gpants", "goldlegs", "goldpants");
        register("Gold Boots", Material.GOLD_BOOTS, (short)0, "gshoes", "goldshoes", "gboots");
        register("Diamond Helmet", Material.DIAMOND_HELMET, (short)0, "diamondhelm", "diahelm", "diahelmet");
        register("Diamond Chestplate", Material.DIAMOND_CHESTPLATE, (short)0, "diachest", "diachestplate", "diamondchest", "dchest", "dchestplate");
        register("Diamond Leggings", Material.DIAMOND_LEGGINGS, (short)0, "diamondpants", "diapants", "dialeggings", "dleggings", "dpants");
        register("Diamond Boots", Material.DIAMOND_BOOTS, (short)0, "diaboots", "dshoes", "diashoes", "dboots");
        register("Iron Horse Armor", Material.IRON_BARDING, (short)0);
        register("Gold Horse Armor", Material.GOLD_BARDING, (short)0);
        register("Diamond Horse Armor", Material.DIAMOND_BARDING, (short)0);

        //Mob Drops
        register("Dragon Egg", Material.DRAGON_EGG, (short)0);
        register("Blaze Rod", Material.BLAZE_ROD, (short)0, "brod");
        register("Blaze Powder", Material.BLAZE_POWDER, (short)0);
        register("Magma Cream", Material.MAGMA_CREAM, (short)0, "cream");
        register("Ghast Tear", Material.GHAST_TEAR, (short)0, "tear");
        register("Leather", Material.LEATHER, (short)0);
        register("String", Material.STRING, (short)0);
        register("Feather", Material.FEATHER, (short)0);
        register("Gunpowder", Material.SULPHUR, (short)0, "powder", "sulfur", "sulphur");
        register("Bone", Material.BONE, (short)0, "bones");
        register("Ender Pearl", Material.ENDER_PEARL, (short)0, "pearl", "epearl");
        register("Slimeball", Material.SLIME_BALL, (short)0, "slime", "sball");
        register("Egg", Material.EGG, (short)0);
        register("Rotten Flesh", Material.ROTTEN_FLESH, (short)0, "rflesh", "flesh", "zombieflesh", "zflesh");
        register("Spider Eye", Material.SPIDER_EYE, (short)0, "eye");
        register("Fermented Spider Eye", Material.FERMENTED_SPIDER_EYE, (short)0, "fermentedeye");
        register("Rabbit Foot", Material.RABBIT_FOOT, (short)0);
        register("Rabbit Hide", Material.RABBIT_HIDE, (short)0);
        register("Prismarine Shard", Material.PRISMARINE_SHARD, (short)0);
        register("Prismarine Crystal", Material.PRISMARINE_CRYSTALS, (short)0);
        register("Skeleton Skull", Material.SKULL_ITEM, (short)0);
        register("Wither Skeleton Skull", Material.SKULL_ITEM, (short)1);
        register("Zombie Head", Material.SKULL_ITEM, (short)2);
        register("Human Head", Material.SKULL_ITEM, (short)3);
        register("Creeper Head", Material.SKULL_ITEM, (short)4);

        //Brewing
        register("Brewing Stand", Material.BREWING_STAND_ITEM, (short)0);
        register("Brewing Stand Block", Material.BREWING_STAND, (short)0);
        register("Cauldron", Material.CAULDRON_ITEM, (short)0, "cauldron");
        register("Cauldron Block", Material.CAULDRON, (short)0);

        //Potions
        register("Glass Bottle", Material.GLASS_BOTTLE, (short)0, "emptypot", "emptybottle", "bottle", "potionbottle", "emptypotion");
        register("Water Bottle", Material.POTION, (short)0, "wbottle", "potion", "pot", "filledbottle");
        register("Awkward Potion", Material.POTION, (short)16, "awkwardpot", "awkwardp", "apotion", "apot", "awkpotion", "awkpot", "awkp");
        register("Thick Potion", Material.POTION, (short)32, "thickpot", "thickp", "tpotion", "tpot");
        register("Mundane Potion (Extended);", Material.POTION, (short)64, "extendedmundanepotion", "emundanepot", "emundanep", "empotion", "empot", "emundpotion", "emundpot", "emundp", "mundanepote", "mundanepe", "mpotione", "mpote", "mundpotione", "mundpote", "mundpe");
        register("Mundane Potion", Material.POTION, (short)8192, "mundanepot", "mundanep", "mpotion", "mpot", "mundpotion", "mundpot", "mundp");
        register("Potion of Regeneration", Material.POTION, (short)8193);
        register("Potion of Regeneration (Extended);", Material.POTION, (short)8257);
        register("Potion of Regeneration II", Material.POTION, (short)8225);
        register("Potion of Swiftness", Material.POTION, (short)8194);
        register("Potion of Swiftness (Extended);", Material.POTION, (short)8258);
        register("Potion of Swiftness II", Material.POTION, (short)8226);
        register("Potion of Fire Resistance", Material.POTION, (short)8195);
        register("Potion of Fire Resistance (Extended);", Material.POTION, (short)8259);
        register("Potion of Fire Resistance (Reverted);", Material.POTION, (short)8227);
        register("Potion of Healing", Material.POTION, (short)8197);
        register("Potion of Healing (Reverted);", Material.POTION, (short)8261);
        register("Potion of Healing II", Material.POTION, (short)8229);
        register("Potion of Strength", Material.POTION, (short)8201);
        register("Potion of Strength (Extended);", Material.POTION, (short)8265);
        register("Potion of Strength II", Material.POTION, (short)8233);
        register("Potion of Poison", Material.POTION, (short)8196);
        register("Potion of Poison (Extended);", Material.POTION, (short)8260);
        register("Potion of Poison II", Material.POTION, (short)8228);
        register("Potion of Weakness", Material.POTION, (short)8200);
        register("Potion of Weakness (Extended);", Material.POTION, (short)8264);
        register("Potion of Weakness (Reverted);", Material.POTION, (short)8232);
        register("Potion of Slowness", Material.POTION, (short)8202);
        register("Potion of Slowness (Extended);", Material.POTION, (short)8266);
        register("Potion of Slowness (Reverted);", Material.POTION, (short)8234);
        register("Potion of Harming", Material.POTION, (short)8204);
        register("Potion of Harming (Reverted);", Material.POTION, (short)8268);
        register("Potion of Harming II", Material.POTION, (short)8236);
        register("Splash Mundane Potion", Material.POTION, (short)16384);
        register("Splash Potion of Regeneration", Material.POTION, (short)16385);
        register("Splash Potion of Regeneration (Extended);", Material.POTION, (short)16449);
        register("Splash Potion of Regeneration II", Material.POTION, (short)16417);
        register("Splash Potion of Swiftness", Material.POTION, (short)16386);
        register("Splash Potion of Swiftness (Extended);", Material.POTION, (short)16450);
        register("Splash Potion of Swiftness II", Material.POTION, (short)16418);
        register("Splash Potion of Fire Resistance", Material.POTION, (short)16387);
        register("Splash Potion of Fire Resistance (Extended);", Material.POTION, (short)16451);
        register("Splash Potion of Fire Resistance (Reverted);", Material.POTION, (short)16419);
        register("Splash Potion of Healing", Material.POTION, (short)16389);
        register("Splash Potion of Healing (Reverted);", Material.POTION, (short)16453);
        register("Splash Potion of Healing II", Material.POTION, (short)16421);
        register("Splash Potion of Strength", Material.POTION, (short)16393);
        register("Splash Potion of Strength (Extended);", Material.POTION, (short)16457);
        register("Splash Potion of Strength II", Material.POTION, (short)16425);
        register("Splash Potion of Poison", Material.POTION, (short)16388);
        register("Splash Potion of Poison (Extended);", Material.POTION, (short)16452);
        register("Splash Potion of Poison II", Material.POTION, (short)16420);
        register("Splash Potion of Weakness", Material.POTION, (short)16392);
        register("Splash Potion of Weakness (Extended);", Material.POTION, (short)16456);
        register("Splash Potion of Weakness (Reverted);", Material.POTION, (short)16424);
        register("Splash Potion of Slowness", Material.POTION, (short)16394);
        register("Splash Potion of Slowness (Extended);", Material.POTION, (short)16458);
        register("Splash Potion of Slowness (Reverted);", Material.POTION, (short)16426);
        register("Splash Potion of Harming", Material.POTION, (short)16396);
        register("Splash Potion of Harming (Reverted);", Material.POTION, (short)16460);
        register("Splash Potion of Harming II", Material.POTION, (short)16428);
        register("Potion of Water Breathing", Material.POTION, (short)8205);
        register("Potion of Water Breathing (Reverted);", Material.POTION, (short)8237);
        register("Potion of Water Breathing (Extended);", Material.POTION, (short)8269);
        register("Splash Potion of Water Breathing", Material.POTION, (short)16397);
        register("Splash Potion of Water Breathing (Reverted);", Material.POTION, (short)16429);
        register("Splash Potion of Water Breathing (Extended);", Material.POTION, (short)16461);
        register("Potion of Leaping", Material.POTION, (short)8203);
        register("Potion of Leaping (Extended);", Material.POTION, (short)8267);
        register("Potion of Leaping II", Material.POTION, (short)8235);
        register("Splash Potion of Leaping", Material.POTION, (short)16395);
        register("Splash Potion of Leaping (Extended);", Material.POTION, (short)16459);
        register("Splash Potion of Leaping II", Material.POTION, (short)16427);
        register("Potion of Invisibility", Material.POTION, (short)8206);
        register("Potion of Invisibility (Extended);", Material.POTION, (short)8270);
        register("Potion of Night Vision", Material.POTION, (short)8198);
        register("Potion of Night Vision (Extended);", Material.POTION, (short)8262);

        //Fireworks
        register("Nether Star", Material.NETHER_STAR, (short)0);
        register("Firework Star", Material.FIREWORK_CHARGE, (short)0);
        register("Firework Rocket", Material.FIREWORK, (short)0);
        register("White Firework Star", Material.FIREWORK_CHARGE, (short)1);
        register("Orange Firework Star", Material.FIREWORK_CHARGE, (short)2);
        register("Magenta Firework Star", Material.FIREWORK_CHARGE, (short)3);
        register("Light Blue Firework Star", Material.FIREWORK_CHARGE, (short)4);
        register("Yellow Firework Star", Material.FIREWORK_CHARGE, (short)5);
        register("Lime Firework Star", Material.FIREWORK_CHARGE, (short)6);
        register("Pink Firework Star", Material.FIREWORK_CHARGE, (short)7);
        register("Gray Firework Star", Material.FIREWORK_CHARGE, (short)8);
        register("Light Gray Firework Star", Material.FIREWORK_CHARGE, (short)9);
        register("Cyan Firework Star", Material.FIREWORK_CHARGE, (short)10);
        register("Purple Firework Star", Material.FIREWORK_CHARGE, (short)11);
        register("Blue Firework Star", Material.FIREWORK_CHARGE, (short)12);
        register("Brown Firework Star", Material.FIREWORK_CHARGE, (short)13);
        register("Green Firework Star", Material.FIREWORK_CHARGE, (short)14);
        register("Red Firework Star", Material.FIREWORK_CHARGE, (short)15);
        register("Black Firework Star", Material.FIREWORK_CHARGE, (short)16);

        //Banners
        register("Wall Banner", Material.WALL_BANNER, (short)0);
        register("Standing Banner", Material.STANDING_BANNER, (short)0);
        register("White Banner", Material.BANNER, (short)15);
        register("Orange Banner", Material.BANNER, (short)14);
        register("Magenta Banner", Material.BANNER, (short)13);
        register("Light Blue Banner", Material.BANNER, (short)12);
        register("Yellow Banner", Material.BANNER, (short)11);
        register("Lime Banner", Material.BANNER, (short)10);
        register("Pink Banner", Material.BANNER, (short)9);
        register("Gray Banner", Material.BANNER, (short)8);
        register("Light Gray Banner", Material.BANNER, (short)7);
        register("Cyan Banner", Material.BANNER, (short)6);
        register("Purple Banner", Material.BANNER, (short)5);
        register("Blue Banner", Material.BANNER, (short)4);
        register("Brown Banner", Material.BANNER, (short)3);
        register("Green Banner", Material.BANNER, (short)2);
        register("Red Banner", Material.BANNER, (short)1);
        register("Black Banner", Material.BANNER, (short)0);

        //Spawn eggs
        register("Spawn Egg", Material.MONSTER_EGG, (short)0, "spawn");
        register("Creeper Spawn Egg", Material.MONSTER_EGG, (short)50, "spawncreeper", "creeperegg", "creepegg");
        register("Skeleton Spawn Egg", Material.MONSTER_EGG, (short)51, "spawnskeleton", "spawnskelly", "skellyegg", "skelegg", "skeletonegg");
        register("Spider Spawn Egg", Material.MONSTER_EGG, (short)52, "spideregg", "spawnspider");
        register("Zombie Spawn Egg", Material.MONSTER_EGG, (short)54, "spawnzombie", "zombieegg");
        register("Slime Spawn Egg", Material.MONSTER_EGG, (short)55, "slimeegg", "spawnslime");
        register("Ghast Spawn Egg", Material.MONSTER_EGG, (short)56, "spawnghast", "ghastegg");
        register("Zombie Pigman Spawn Egg", Material.MONSTER_EGG, (short)57, "pigmanegg", "spawnpigman", "zombiepigmanegg", "spawnzombiepigman");
        register("Enderman Spawn Egg", Material.MONSTER_EGG, (short)58, "spawnenderman", "endermanegg");
        register("Cave Spider Spawn Egg", Material.MONSTER_EGG, (short)59, "cavespideregg", "spawncavespider");
        register("Silverfish Spawn Egg", Material.MONSTER_EGG, (short)60, "spawnsilverfish", "silverfishegg");
        register("Blaze Spawn Egg", Material.MONSTER_EGG, (short)61, "spawnblaze", "blazeegg");
        register("Magma Cube Spawn Egg", Material.MONSTER_EGG, (short)62, "spawnmagmacube", "magmacubeegg");
        register("Pig Spawn Egg", Material.MONSTER_EGG, (short)90, "spawnpig", "pigegg");
        register("Sheep Spawn Egg", Material.MONSTER_EGG, (short)91, "spawnsheep", "sheepegg");
        register("Cow Spawn Egg", Material.MONSTER_EGG, (short)92, "spawncow", "cowegg");
        register("Chicken Spawn Egg", Material.MONSTER_EGG, (short)93, "spawnchicken", "chickenegg", "spawnchick", "chickegg");
        register("Squid Spawn Egg", Material.MONSTER_EGG, (short)94, "spawnsquid", "squidegg");
        register("Wolf Spawn Egg", Material.MONSTER_EGG, (short)95, "spawnwolf", "wolfegg");
        register("Mooshroom Spawn Egg", Material.MONSTER_EGG, (short)96, "spawnmooshroom", "mooshroomegg", "mushroomcowspawnegg", "mushroomcowegg", "spawnmushroomcow", "shroomcowegg", "spawnshroomcow", "shroomcowspawnegg");
        register("Ocelot Spawn Egg", Material.MONSTER_EGG, (short)98, "spawnocelot", "spawncat", "ocelotegg", "categg");
        register("Villager Spawn Egg", Material.MONSTER_EGG, (short)120, "spawnvillager", "villageregg");
        register("Bat Spawn Egg", Material.MONSTER_EGG, (short)65);
        register("Witch Spawn Egg", Material.MONSTER_EGG, (short)66);
        register("Endermite Spawn Egg", Material.MONSTER_EGG, (short)67);
        register("Guardian Spawn Egg", Material.MONSTER_EGG, (short)68);
        register("Rabbit Spawn Egg", Material.MONSTER_EGG, (short)101);
        register("Horse Spawn Egg", Material.MONSTER_EGG, (short)100);

        //Portals
        register("Portal", Material.PORTAL, (short)0, "portalblock");
        register("End Portal", Material.ENDER_PORTAL, (short)0, "enderportal");
        register("End Portal Frame", Material.ENDER_PORTAL_FRAME, (short)0, "starframe");

        //Display
        register("Paintings", Material.PAINTING, (short)0, "painting", "paint");
        register("Sign", Material.SIGN, (short)0);
        register("Wall Sign", Material.WALL_SIGN, (short)0);
        register("Sign Post", Material.SIGN_POST, (short)0);
        register("Item Frame", Material.ITEM_FRAME, (short)0);
        register("Armor Stand", Material.ARMOR_STAND, (short)0);

        //misc
        register("Sponge", Material.SPONGE, (short)0);
        register("Wet Sponge", Material.SPONGE, (short)1);
        register("Barrier", Material.BARRIER, (short)0);
        register("Web", Material.WEB, (short)0, "cobweb", "spiderweb", "cweb", "sweb");
        register("TNT", Material.TNT, (short)0);
        register("Fire", Material.FIRE, (short)0);
        register("Fire Charge", Material.FIREBALL, (short)0, "firecharge");
        register("Monster Spawner", Material.MOB_SPAWNER, (short)0, "spawner");
        register("Bottle 'o Enchanting", Material.EXP_BOTTLE, (short)0, "xpbottle", "expbottle", "bottleoenchanting");
        register("Bed", Material.BED, (short)0);
        register("Bed Block", Material.BED_BLOCK, (short)0);
        register("Flower Pot Block", Material.FLOWER_POT, (short)0);
        register("Flower Pot", Material.FLOWER_POT_ITEM, (short)0);
        register("Hay Bale", Material.HAY_BLOCK, (short)0);
    }

}
