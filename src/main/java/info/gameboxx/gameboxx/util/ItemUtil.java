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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/** Utility methods for items and inventories. */
public class ItemUtil {

    /**
     * Set of materials that are transparent.
     * This means an entity can pass through it. It doesn't necessarily mean the texture is transparent.
     */
    public static final Set<Material> TRANSPARENT_MATERIALS = new HashSet<Material>();

    /**
     * Air item stack.
     */
    public static final ItemStack AIR = new ItemStack(Material.AIR);

    static {
        //Get list of transparent materials.
        for (Material material : Material.values()) {
            if (material.isTransparent()) {
                TRANSPARENT_MATERIALS.add(material);
            }
        }
    }


    /**
     * Compare the given item with the match item.
     *
     * @param item The item to compare.
     * @param match The match item to compare with.
     * @param exact Does the amount, durability, meta and name need to match?
     * @return true if everything specified matched and false if any matches failed.
     */
    public static boolean compare(ItemStack item, ItemStack match, boolean exact) {
        return compare(item, match, exact, exact, exact, exact);
    }

    /**
     * Compare the given item with the match item.
     *
     * @param item The item to compare.
     * @param match The match item to compare with.
     * @param amount Does the amount need to match?
     * @param exact Does the durability, meta and name need to match?
     * @return true if everything specified matched and false if any matches failed.
     */
    public static boolean compare(ItemStack item, ItemStack match, boolean amount, boolean exact) {
        return compare(item, match, amount, exact, exact, exact);
    }

    /**
     * Compare the given item with the match item.
     *
     * @param item The item to compare.
     * @param match The match item to compare with.
     * @param amount Does the amount need to match?
     * @param durability Does the durability need to match?
     * @param meta Does the meta need to match?
     * @param name Does the name need to match?
     * @return true if everything specified matched and false if any matches failed.
     */
    public static boolean compare(ItemStack item, ItemStack match, boolean amount, boolean durability, boolean meta, boolean name) {
        if (item == null && match == null) {
            return true;
        }
        if (item == null || match == null) {
            return false;
        }
        if (item.getType() == Material.AIR && match.getType() == Material.AIR) {
            return true;
        }
        if (item.getType() != match.getType()) {
            return false;
        }
        if (durability && item.getDurability() != match.getDurability()) {
            return false;
        }
        if (!durability && item.getDurability() != match.getDurability() && item.getType().getMaxDurability() == 0 && match.getType().getMaxDurability() == 0) {
            return false;
        }
        if (amount && item.getAmount() != match.getAmount()) {
            return false;
        }
        if (!item.hasItemMeta() && !match.hasItemMeta()) {
            return true;
        }
        if (!item.hasItemMeta()) {
            return false;
        }
        ItemMeta itemMeta = item.getItemMeta();
        ItemMeta matchMeta = match.getItemMeta();
        if (meta && !Bukkit.getItemFactory().equals(itemMeta, matchMeta)) {
            return false;
        }
        if (!meta && name && !itemMeta.getDisplayName().equalsIgnoreCase(matchMeta.getDisplayName())) {
            return false;
        }
        return true;
    }


    /**
     * Get the amount of items in the specified inventory.
     *
     * @param inv The inventory to look in for items.
     * @param match The item to look for.
     * @param exact Does the durability, meta and name need to match?
     * @return The total amount of items found.
     */
    public static int count(Inventory inv, ItemStack match, boolean exact) {
        return count(inv, match, exact, exact, exact);
    }

    /**
     * Get the amount of items in the specified inventory.
     *
     * @param inv The inventory to look in for items.
     * @param match The item to look for.
     * @param durability Does the durability need to match?
     * @param meta Does the meta need to match?
     * @param name Does the name need to match?
     * @return The total amount of items found.
     */
    public static int count(Inventory inv, ItemStack match, boolean durability, boolean meta, boolean name) {
        int amt = 0;
        for (ItemStack item : inv.getContents()) {
            if (compare(item, match, false, durability, meta, name)) {
                amt += item.getAmount();
            }
        }
        return amt;
    }


    /**
     * Check if the specified inventory contains enough of the specified item.
     *
     * @param inv The inventory to look in for items.
     * @param match The item to look for.
     * @param amount The amount of items to check for.
     * @param exact Does the durability, meta and name need to match?
     * @return Whether or not the inventory contains enough items.
     */
    public static boolean contains(Inventory inv, ItemStack match, int amount, boolean exact) {
        return contains(inv, match, amount, exact, exact, exact);
    }

    /**
     * Check if the specified inventory contains enough of the specified item.
     *
     * @param inv The inventory to look in for items.
     * @param match The item to look for.
     * @param amount The amount of items to check for.
     * @param durability Does the durability need to match?
     * @param meta Does the meta need to match?
     * @param name Does the name need to match?
     * @return Whether or not the inventory contains enough items.
     */
    public static boolean contains(Inventory inv, ItemStack match, int amount, boolean durability, boolean meta, boolean name) {
        return count(inv, match, durability, meta, name) >= amount;
    }


    /**
     * Remove items from a Inventory.
     *
     * @param inv The inventory to remove the items from.
     * @param match The item to remove (used to check type, durability, name etc)
     * @param amount The amount of items to try and remove.
     * @param exact Does the durability, meta and name need to match?
     * @return Amount of items that didn't get removed. (remainder)
     */
    public static int remove(Inventory inv, ItemStack match, int amount, boolean exact) {
        return remove(inv, match, amount, exact, exact, exact);
    }

    /**
     * Remove items from a Inventory.
     *
     * @param inv The inventory to remove the items from.
     * @param match The item to remove (used to check type, durability, name etc)
     * @param amount The amount of items to try and remove.
     * @param durability Does the durability need to match?
     * @param meta Does the meta need to match?
     * @param name Does the name need to match?
     * @return Amount of items that didn't get removed. (remainder)
     */
    public static int remove(Inventory inv, ItemStack match, int amount, boolean durability, boolean meta, boolean name) {
        int stackAmt;
        for (int i = 0; i <= inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (compare(item, match, false, durability, meta, name)) {
                stackAmt = item.getAmount();
                if (stackAmt > amount) {
                    item.setAmount(stackAmt - amount);
                    return 0;
                }
                inv.setItem(i, AIR);
                if (stackAmt == amount) {
                    return 0;
                }
                amount -= stackAmt;
            }
        }
        return amount;
    }

    /**
     * Adds an item stack to the specified inventory.
     * Items will be dropped if the inventory is full.
     * Items that exceed the max stack size will be unstacked.
     *
     * @param inventory The inventory to add the item in.
     * @param item The item to add.
     * @return Map with excess items that didn't fit in the inventory. (This includes dropped items)
     */
    public static HashMap<Integer, ItemStack> add(Inventory inventory, ItemStack item) {
        return add(inventory, item, -1, true, true);
    }

    /**
     * Adds an item stack to the specified inventory.
     * Items will be dropped if the inventory is full.
     * Items that exceed the max stack size will be unstacked.
     *
     * @param inventory The inventory to add the item in.
     * @param item The item to add.
     * @param slot Try to add the item stack to this slot.
     * If the slot isn't empty and it can't be added to it the item will be added to the inventory normally.
     * @return Map with excess items that didn't fit in the inventory. (This includes dropped items)
     */
    public static HashMap<Integer, ItemStack> add(Inventory inventory, ItemStack item, int slot) {
        return add(inventory, item, slot, true, true);
    }

    /**
     * Adds an item stack to the specified inventory.
     * Items that exceed the max stack size will be unstacked.
     *
     * @param inventory The inventory to add the item in.
     * @param item The item to add.
     * @param dropifFull If the inventory is full should the items be dropped?
     * @return Map with excess items that didn't fit in the inventory. (This includes dropped items)
     */
    public static HashMap<Integer, ItemStack> add(Inventory inventory, ItemStack item, boolean dropifFull) {
        return add(inventory, item, -1, dropifFull, true);
    }

    /**
     * Adds an item stack to the specified inventory.
     *
     * @param inventory The inventory to add the item in.
     * @param item The item to add.
     * @param dropifFull If the inventory is full should the items be dropped?
     * @param unstack Ensure the max stack size and unstack items that exceed this?
     * @return Map with excess items that didn't fit in the inventory. (This includes dropped items)
     */
    public static HashMap<Integer, ItemStack> add(Inventory inventory, ItemStack item, boolean dropifFull, boolean unstack) {
        return add(inventory, item, -1, dropifFull, unstack);
    }

    /**
     * Adds all the items in the list of items to the specified inventory.
     *
     * @param inventory The inventory to add the item in.
     * @param items The array of items to add.
     * @param slot Try to add the item stack to this slot.
     * If the slot isn't empty and it can't be added to it the item will be added to the inventory normally.
     * @param dropIfFull If the inventory is full should the items be dropped?
     * @param unstack Ensure the max stack size and unstack items that exceed this?
     * @return Map with excess items that didn't fit in the inventory. (This includes dropped items)
     */
    public static HashMap<Integer, ItemStack> add(Inventory inventory, ItemStack[] items, int slot, boolean dropIfFull, boolean unstack) {
        HashMap<Integer, ItemStack> excess = null;
        for (ItemStack item : items) {
            excess.putAll(add(inventory, item, slot, dropIfFull, unstack));
        }
        return excess;
    }

    /**
     * Adds all the items in the list of items to the specified inventory.
     *
     * @param inventory The inventory to add the item in.
     * @param items The list of items to add.
     * @param slot Try to add the item stack to this slot.
     * If the slot isn't empty and it can't be added to it the item will be added to the inventory normally.
     * @param dropIfFull If the inventory is full should the items be dropped?
     * @param unstack Ensure the max stack size and unstack items that exceed this?
     * @return Map with excess items that didn't fit in the inventory. (This includes dropped items)
     */
    public static HashMap<Integer, ItemStack> add(Inventory inventory, List<ItemStack> items, int slot, boolean dropIfFull, boolean unstack) {
        HashMap<Integer, ItemStack> excess = null;
        for (ItemStack item : items) {
            excess.putAll(add(inventory, item, slot, dropIfFull, unstack));
        }
        return excess;
    }

    /**
     * Adds an item stack to the specified inventory.
     *
     * @param inventory The inventory to add the item in.
     * @param item The item to add.
     * @param slot Try to add the item stack to this slot.
     * If the slot isn't empty and it can't be added to it the item will be added to the inventory normally.
     * @param dropIfFull If the inventory is full should the items be dropped?
     * @param unstack Ensure the max stack size and unstack items that exceed this?
     * @return Map with excess items that didn't fit in the inventory. (This includes dropped items)
     */
    public static HashMap<Integer, ItemStack> add(Inventory inventory, ItemStack item, int slot, boolean dropIfFull, boolean unstack) {
        if (unstack && item.getAmount() > item.getMaxStackSize()) {
            List<ItemStack> splitStacks = new ArrayList<ItemStack>();
            int amount = item.getAmount();
            while (amount > 0) {
                amount -= item.getMaxStackSize();
                ItemStack splitStack = item.clone();
                splitStack.setAmount(amount >= 0 ? item.getMaxStackSize() : item.getMaxStackSize() + amount);
                splitStacks.add(splitStack);
            }
            add(inventory, splitStacks, slot, dropIfFull, false);
            return new HashMap<Integer, ItemStack>();
        }
        if (slot >= 0) {
            if (inventory.getItem(slot) == null || inventory.getItem(slot).getType() == Material.AIR) {
                inventory.setItem(slot, item);
                return new HashMap<Integer, ItemStack>();
            } else if (inventory.getItem(slot).equals(item)) {
                int newAmt = inventory.getItem(slot).getAmount() + item.getAmount();
                if (newAmt > item.getType().getMaxStackSize()) {
                    item.setAmount(newAmt - item.getType().getMaxStackSize());
                    newAmt = item.getType().getMaxStackSize();
                }
                inventory.getItem(slot).setAmount(newAmt);
            }
        }

        HashMap<Integer, ItemStack> excess = inventory.addItem(item);
        if (!dropIfFull) {
            return excess;
        }

        Location location = inventory.getLocation();
        if (location != null) {
            for (ItemStack itemStack : excess.values()) {
                location.getWorld().dropItem(location, itemStack);
            }
        }
        return excess;
    }


    /**
     * Clear an inventory completely.
     * This also clears armor slots for player inventories.
     *
     * @param inventory The inventory to clear
     */
    public static void clear(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, AIR);
        }
    }


}
