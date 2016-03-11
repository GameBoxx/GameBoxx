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
import org.bukkit.block.Biome;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Biomes extends AliasMap<Biome> {

    private Biomes() {
        super("Biomes", new File(ALIASES_FOLDER, "Biomes.yml"), "biome");
    }

    @Override
    public void onLoad() {
        add(Biome.OCEAN, "Ocean");
        add(Biome.PLAINS, "Plains");
        add(Biome.DESERT, "Desert");
        add(Biome.EXTREME_HILLS, "Extreme Hills", "EHills");
        add(Biome.FOREST, "Forest");
        add(Biome.TAIGA, "Taiga");
        add(Biome.SWAMPLAND, "Swampland", "Swamp");
        add(Biome.RIVER, "River");
        add(Biome.HELL, "Hell", "Nether");
        add(Biome.SKY, "Sky", "The End", "End");
        add(Biome.FROZEN_OCEAN, "Frozen Ocean");
        add(Biome.FROZEN_RIVER, "Frozen River");
        add(Biome.ICE_FLATS, "Ice Flats", "Ice Flat");
        add(Biome.ICE_MOUNTAINS, "Ice Mountains", "Ice Mountain", "IceM");
        add(Biome.MUSHROOM_ISLAND, "Mushroom Island", "Shroom Island", "MushroomI", "ShroomI");
        add(Biome.MUSHROOM_ISLAND_SHORE, "Mushroom Island Shore", "Shroom Island Shore", "MushroomI Shore", "ShroomI Shore", "MushroomIS", "ShroomIS");
        add(Biome.BEACHES, "Beach");
        add(Biome.DESERT_HILLS, "Desert Hills", "DesertH");
        add(Biome.FOREST_HILLS, "Forest Hills", "ForestH");
        add(Biome.TAIGA_HILLS, "Taiga Hills", "TaigaH");
        add(Biome.SMALLER_EXTREME_HILLS, "Smaller Extreme Hills", "SExtreme Hills", "SExtremeH", "Small Extreme Hills", "Small ExtremeH", "Smaller ExtremeH",
                "Smaller EHills", "SEHills", "SmallEHills");
        add(Biome.JUNGLE, "Jungle");
        add(Biome.JUNGLE_HILLS, "Jungle Hills", "JungleH");
        add(Biome.JUNGLE_EDGE, "Jungle Edge", "JungleE");
        add(Biome.DEEP_OCEAN, "Deep Ocean", "DOcean");
        add(Biome.STONE_BEACH, "Stone Beach", "SBeach");
        add(Biome.COLD_BEACH, "Cold Beach", "CBeach", "Frozen Beach", "FBeach");
        add(Biome.BIRCH_FOREST, "Birch Forst", "BForest");
        add(Biome.BIRCH_FOREST_HILLS, "Birch Forest Hills", "BForest Hills", "Birch ForestH", "BForestH");
        add(Biome.ROOFED_FOREST, "Roofed Forest", "RForest");
        add(Biome.TAIGA_COLD, "Taiga Cold", "TaigaC", "Taiga Frozen", "TaigaF");
        add(Biome.TAIGA_COLD_HILLS, "Taiga Cold Hills", "TaigaC Hills", "Taiga ColdH", "TaigaCH");
        add(Biome.REDWOOD_TAIGA, "Redwood Taiga", "RTaiga", "Red Taiga");
        add(Biome.REDWOOD_TAIGA_HILLS, "Redwood Taiga Hills", "RTaiga Hills", "Redwood TaigaH", "Red Taiga Hills", "Red TaigaH", "RTaigaH");
        add(Biome.EXTREME_HILLS_WITH_TREES, "Extreme Hills With Trees", "EHills With Trees", "Extreme Hills Trees", "EHills Trees");
        add(Biome.SAVANNA, "Savanna");
        add(Biome.SAVANNA_ROCK, "Savanna Rock", "SavannaR");
        add(Biome.MESA, "Mesa");
        add(Biome.MESA_ROCK, "Mesa Rock", "MesaR");
        add(Biome.MESA_CLEAR_ROCK, "Mesa Clear Rock", "Mesa ClearR", "MesaCR", "Mesa CRock");
        add(Biome.VOID, "Void", "None");
        add(Biome.MUTATED_PLAINS, "Mutated Plains", "MPlains");
        add(Biome.MUTATED_DESERT, "Mutated Desert", "MDesert");
        add(Biome.MUTATED_EXTREME_HILLS, "Mutated Extreme Hills", "MExtreme Hills", "Mutated EHills", "MEHills");
        add(Biome.MUTATED_FOREST, "Mutated Forest", "MForest");
        add(Biome.MUTATED_TAIGA, "Mutated Taiga", "MTaiga");
        add(Biome.MUTATED_SWAMPLAND, "Mutated Swampland", "MSwampland", "Mutated Swamp", "MSwamp");
        add(Biome.MUTATED_ICE_FLATS, "Mutated Ice Flats", "MIce Flats", "Mutated IceF", "MIceF");
        add(Biome.MUTATED_JUNGLE, "Mutated Jungle", "MJungle");
        add(Biome.MUTATED_JUNGLE_EDGE, "Mutated Jungle Edge", "MJungle Edge", "Mutated JungleE", "MJungleE");
        add(Biome.MUTATED_BIRCH_FOREST, "Mutated Birch Forest", "MBirch Forest", "Mutated BirchF", "MBirchF");
        add(Biome.MUTATED_BIRCH_FOREST_HILLS, "Mutated Birch Forest Hills", "MBirch Forest Hills", "Mutated Birch ForestH", "MBirch ForestH", "Mutated BirchFH", "MBirchFH");
        add(Biome.MUTATED_ROOFED_FOREST, "Mutated Roofed Forest", "MRoofed Forest", "Mutated RForest", "MRForest");
        add(Biome.MUTATED_TAIGA_COLD, "Mutated Taiga Cold", "MTaiga Cold", "Mutated Taiga Frozen", "MTaiga Frozen", "Mutated TaigaC", "MTaigaC", "Mutated TaigaF", "MTaigaF");
        add(Biome.MUTATED_REDWOOD_TAIGA, "Mutated Redwood Taiga", "MRedwood Taiga", "Mutated Red Taiga", "MRed Taiga", "Mutated RTaiga", "MRTaiga");
        add(Biome.MUTATED_REDWOOD_TAIGA_HILLS, "Mutated Redwood Taiga Hills", "MRedwood Taiga Hills", "Mutated Red Taiga Hills", "MRed Taiga Hills", "Mutated Redwood TaigaH",
                "MRedwood TaigaH", "Mutated Red TaigaH", "MRed TaigaH", "Mutated RTaigaH", "MRTaigaH");
        add(Biome.MUTATED_EXTREME_HILLS_WITH_TREES, "Mutated Extreme Hills With Trees", "MExtreme Hills With Trees", "Mutated Extreme Hills Trees", "MExtreme Hills Trees",
                "Mutated EHills Trees", "MEHills Trees", "Mutated EHillsT", "MEHillsT");
        add(Biome.MUTATED_SAVANNA, "Mutated Savanna", "MSavanna");
        add(Biome.MUTATED_SAVANNA_ROCK, "Mutated Savanna Rock", "MSavanna Rock", "Mutated SavannaR", "MSavannaR");
        add(Biome.MUTATED_MESA, "Mutated Mesa", "MMesa");
        add(Biome.MUTATED_MESA_ROCK, "Mutated Mesa Rock", "MMesa Rock", "Mutated MesaR", "MMesaR");
        add(Biome.MUTATED_MESA_CLEAR_ROCK, "Mutated Mesa Clear Rock", "MMesa Clear Rock", "Mutated Mesa CRock", "MMesa CRock", "Mutated MesaCR", "MMesaCR");
    }

    public static Biome get(String string) {
        return instance()._get(string);
    }

    public static String getName(Biome key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(Biome key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(Biome key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static Biomes instance() {
        if (getMap(Biomes.class) == null) {
            aliasMaps.put(Biomes.class, new Biomes());
        }
        return (Biomes)getMap(Biomes.class);
    }
}
