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
import org.bukkit.event.inventory.InventoryType;

import java.io.File;
import java.util.List;
import java.util.Map;

public class InventoryTypes extends AliasMap<InventoryType> {

    private InventoryTypes() {
        super("InventoryTypes", new File(ALIASES_FOLDER, "InventoryTypes.yml"), "inventorytype", "invtypes", "invtype");
    }

    @Override
    public void onLoad() {
        add(InventoryType.CHEST, "Chest", "CH", "Default", "Def", "D");
        add(InventoryType.DISPENSER, "Dispenser", "DI", "Dispense");
        add(InventoryType.DROPPER, "Dropper", "DR", "Drop");
        add(InventoryType.FURNACE, "Furnace", "FU", "Stove");
        add(InventoryType.WORKBENCH, "Workbench", "WB", "WBench", "WorkB", "Crafting Table", "Craft Table", "CTable", "CraftingT", "CraftT");
        add(InventoryType.CRAFTING, "Crafting", "CR", "Craft", "Player Crafting", "Player Craft", "PCrafting", "PCraft");
        add(InventoryType.ENCHANTING, "Enchanting", "EN", "Enchant", "Ench");
        add(InventoryType.BREWING, "Brewing", "BR", "Brew");
        add(InventoryType.PLAYER, "Player", "PL");
        add(InventoryType.CREATIVE, "Creative", "Crea");
        add(InventoryType.MERCHANT, "Merchant", "ME", "Merch", "Trade", "Trading", "Villagers", "Villager");
        add(InventoryType.ENDER_CHEST, "Ender Chest", "EC", "End Chest", "EChest", "ECH", "EnderC", "EndC", "ENC");
        add(InventoryType.ANVIL, "Anvil", "AN", "Anv");
        add(InventoryType.BEACON, "Beacon", "BE");
        add(InventoryType.HOPPER, "Hopper", "HO", "Hop");
    }

    public static InventoryType get(String string) {
        return instance()._get(string);
    }

    public static String getName(InventoryType key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(InventoryType key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(InventoryType key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static InventoryTypes instance() {
        if (getMap(InventoryTypes.class) == null) {
            aliasMaps.put(InventoryTypes.class, new InventoryTypes());
        }
        return (InventoryTypes)getMap(InventoryTypes.class);
    }
}
