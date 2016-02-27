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

package info.gameboxx.gameboxx.menu;

import info.gameboxx.gameboxx.util.Str;
import info.gameboxx.gameboxx.util.item.EItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public abstract class Menu {

    private static List<Menu> menus = new ArrayList<Menu>();

    private Plugin plugin;
    private String name;
    private int id; //Random generated ID to identify the menu easier. (set as max item stack in menus)
    private int rows = 1;
    private String title;

    private Map<String, Object> data = new HashMap<String, Object>();

    private EItem[] items;
    private List<Inventory> openInventories = new ArrayList<Inventory>();


    /**
     * Creates a new menu instance with the default "&amp;8&amp;lMenu" title.
     *
     * @param plugin The plugin that owns this menu which is used for event listeners.
     * @param name The menu name which is the identifier key.
     * @param rows The amount of rows for the menu. (Not recommended to set above 6)
     */
    public Menu(Plugin plugin, String name, int rows) {
        this(plugin, name, rows, "&8&lMenu");
    }

    /**
     * Creates a new menu instance.
     *
     * @param plugin The plugin that owns this menu which is used for event listeners.
     * @param name The menu name which is the identifier key.
     * @param rows The amount of rows for the menu. (Not recommended to set above 6)
     * @param title The title for the menu.
     */
    public Menu(Plugin plugin, String name, int rows, String title) {
        this.plugin = plugin;
        this.rows = rows;
        this.name = name;
        this.title = title;
        id = new Random().nextInt(Integer.MAX_VALUE - 64);
        items = new EItem[getSlots()];

        menus.add(this);
    }

    /**
     * Get the name for the menu used as an identifier.
     *
     * @return The menu name
     */
    protected String getName() {
        return name;
    }

    /**
     * Get the random generated ID for this menu as an identifier.
     *
     * @return random generated ID unique to this menu.
     */
    protected int getID() {
        return id;
    }


    /**
     * Get the display title for the inventory.
     *
     * @return The title for the inventory.
     */
    protected String getTitle() {
        return title;
    }

    /**
     * Set the title of the inventory.
     * This will also update the title for all open inventories.
     *
     * @param title The title string to set.
     */
    protected void setTitle(String title, boolean recreateMenus) {
        this.title = title;

        //Recreate all open menus.
        if (recreateMenus) {
            updateInventories();
        }
    }


    /**
     * Get the row count for the inventory menu.
     *
     * @return The amount of rows for the inventory.
     */
    protected int getRows() {
        return rows;
    }

    /**
     * Get the amount of slots for the inventory menu. (rows*9)
     *
     * @return The amount of slots in the inventory.
     */
    protected int getSlots() {
        return rows * 9;
    }

    /**
     * Resizes the menu to the given row count.
     * Please note that when you reduce the menu size that the items on the last rows will be deleted.
     *
     * @param rows The new row count for the menu. (Not recommended to set above 6)
     * @param recreateMenus When set to true it will recreate all open menus to update with the new size. When set to false open menus will remain their previous size.
     */
    protected void resize(int rows, boolean recreateMenus) {
        if (rows < 1) {
            return;
        }
        this.rows = rows;

        //Resize the item array.
        EItem[] newArray = new EItem[getSlots()];
        for (int i = 0; i < getSlots(); i++) {
            if (items.length > i) {
                newArray[i] = items[i];
            }
        }
        items = newArray;

        //Recreate all open menus.
        if (recreateMenus) {
            updateInventories();
        }
    }


    /**
     * Updates all inventories by recreating them.
     * This is used when setting titles and resizing menus.
     */
    protected void updateInventories() {
        List<Inventory> openInvs = new ArrayList<Inventory>(openInventories);
        for (int i = 0; i < openInvs.size(); i++) {
            Inventory prev = openInventories.get(i);

            List<HumanEntity> viewers = new ArrayList<HumanEntity>(prev.getViewers());
            for (HumanEntity he : viewers) {
                Player player = (Player)he;
                player.closeInventory();

                //Recreate menu with previous contents to restore player set items properly.
                Inventory inv = Bukkit.createInventory(player, getSlots(), Str.color(title));
                inv.setMaxStackSize(id);
                for (int s = 0; s < getSlots(); s++) {
                    if (prev.getContents().length > s) {
                        inv.setItem(s, prev.getContents()[s]);
                    }
                }

                if (openInventories.size() > i) {
                    openInventories.set(i, inv);
                } else {
                    openInventories.add(i, inv);
                }
                player.openInventory(inv);
            }
        }
    }


    /**
     * Set extra meta data for the the menu.
     * Please note that this will overwrite any existing data if there is data for the given key already.
     *
     * @param key The identifying key for the data object.
     * @param data The data object to store at the given key identifier.
     */
    protected void setData(String key, Object data) {
        this.data.put(key, data);
    }

    /**
     * Remove extra data from the menu at the given key.
     *
     * @param key The key for the data that needs to be removed.
     * @return Will return true if it removed data and false if the menu has no data of the given key.
     */
    protected boolean removeData(String key) {
        if (data.containsKey(key)) {
            data.remove(key);
            return true;
        }
        return false;
    }

    /**
     * Checks if the menu has extra data for the given key.
     *
     * @param key The key for the data that needs to be checked.
     * @return Wether the menu has data for the given key or not.
     */
    protected boolean hasData(String key) {
        return data.containsKey(key);
    }

    /**
     * Get data from the menu for the given key.
     * If the menu doesn't have any data for the key it will return null.
     * The value returned is an Object so you'll have to cast it to whatever data you pass in.
     *
     * @param key The key for the data that needs to be returned.
     * @return The data object at the given key or null if the menu doesn't have data with the given key.
     */
    protected Object getData(String key) {
        if (hasData(key)) {
            return data.get(key);
        }
        return null;
    }



    /**
     * Get the array with all items registered for this menu.
     * Please note that this does not have any items that are set for specific players.
     * These are the global items that will be added to the menu when opening it.
     * Empty slots are filled with EItem.AIR items.
     * Use {@link #setSlot(int, EItem, Player)} to set items for the inventory.
     *
     * @return Array with all EItems for this menu.
     */
    protected EItem[] getItems() {
        return items;
    }

    /**
     * Fill up a slot with a specified item.
     *
     * @param slot The slot to fill.
     * @param item The item to fill the slot with. (Will override any existing items)
     */
    protected void setSlot(int slot, EItem item) {
        setSlot(slot, item, null);
    }

    /**
     * Fill up a slot with a specified item.
     * If a player is specified it will only display for that player.
     * It will update all open menus that are shown to players.
     * <p/>
     * When setting an item for a player the item isn't registered in this menu.
     * This also means it's only possible to set items for players when the inventory is open and shown to the player.
     *
     * @param slot The slot to fill.
     * @param item The item to fill the slot with. (Will override any existing items)
     * @param player When not null it will only update the inventory for this player.
     */
    protected void setSlot(int slot, EItem item, Player player) {
        if (player == null) {
            this.items[slot] = item;
        }

        for (Inventory inv : openInventories) {
            if (player == null) {
                inv.setItem(slot, item);
            } else {
                if (inv.getViewers().contains(player)) {
                    inv.setItem(slot, item);
                }
            }
        }
    }


    /**
     * Clear all items out of the menu.
     * It will update all open menus that are shown to players.
     */
    protected void clearMenu() {
        clearMenu(null);
    }

    /**
     * Clear all items out of the menu.
     * It will update all open menus that are shown to players.
     * <p/>
     * If a player is specified it will only clear items for that player.
     * This means the items are not cleared inside this menu class instance only inside the inventory displayed to the player.
     * This also means it's only possible to clear a menu for a player that has the menu opened.
     *
     * @param player When not null it will only clear the inventory of this player.
     */
    protected void clearMenu(Player player) {
        for (int i = 0; i < getSlots(); i++) {
            setSlot(i, EItem.AIR, player);
        }
    }



    /**
     * Get a list of menu inventories that are shown to players.
     *
     * @return List with inventories.
     */
    protected List<Inventory> getOpenInventories() {
        return openInventories;
    }

    /**
     * Shows this menu to the specified player.
     * To close the inventory just call player.closeInventory() and the menu will detect that.
     *
     * @param player The player to show this menu to.
     */
    public void show(Player player) {
        player.closeInventory();

        Inventory inv = Bukkit.createInventory(player, getSlots(), Str.color(title));
        inv.setMaxStackSize(id);

        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                inv.setItem(i, items[i]);
            }
        }

        player.openInventory(inv);
        openInventories.add(inv);
    }


    /**
     * Checks if the given inventory is this menu.
     * The type, id, slots and title has to match for a menu to be equal if exact is true.
     * When resizing or setting the title of menus without recreating menus you would have to set exact to false.
     * Because when you don't recreate the menus on resize etc the inventory title and slots wont match.
     * This might give inaccurate results though.
     *
     * @param inv The inventory to compare this menu with.
     * @param exact When true it will be more precise for checking for a match. (See description)
     * @return True if the inventory belongs to this menu and false if not.
     */
    public boolean isMenu(Inventory inv, boolean exact) {
        if (inv.getType() != InventoryType.CHEST) {
            return false;
        }
        if (inv.getMaxStackSize() != getID()) {
            return false;
        }
        if (!exact) {
            return true;
        }
        if (inv.getSize() != getSlots()) {
            return false;
        }
        if (!Str.replaceColor(inv.getTitle()).equalsIgnoreCase(getTitle())) {
            return false;
        }
        return true;
    }


    /**
     * Will destroy the menu closing all open inventories and unregister it.
     */
    public void destroyMenu() {
        List<Inventory> openInvs = new ArrayList<Inventory>(openInventories);
        for (int i = 0; i < openInvs.size(); i++) {
            Inventory prev = openInvs.get(i);
            List<HumanEntity> viewers = new ArrayList<HumanEntity>(prev.getViewers());
            for (HumanEntity he : viewers) {
                Player player = (Player)he;
                player.closeInventory();
            }
        }
        menus.remove(this);
        onDestroy();
    }

    /**
     * Called when a menu is completely destroyed.
     */
    abstract protected void onDestroy();

    /**
     * Called when a menu is shown.
     *
     * @param event The InventoryOpenEvent that triggered this menu open.
     */
    abstract protected void onShow(InventoryOpenEvent event);

    /**
     * Called when a menu is closed. Can be through code or when the player closes the inventory etc.
     *
     * @param event The InventoryCloseEvent that triggered this menu close.
     */
    abstract protected void onClose(InventoryCloseEvent event);

    /**
     * Called when a player clicks on a menu slot.
     *
     * @param event The InventoryClickEvent that triggered this menu click. Use this to get the slot, player, item etc.
     */
    abstract protected void onClick(InventoryClickEvent event);


    public static class Events implements Listener {
        @EventHandler
        private void menuOpen(final InventoryOpenEvent event) {
            Inventory inv = event.getInventory();
            for (final Menu menu : menus) {
                if (!menu.isMenu(inv, true)) {
                    continue;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        menu.onShow(event);
                    }
                }.runTask(menu.plugin);
                return;
            }
        }

        @EventHandler
        private void menuClose(InventoryCloseEvent event) {
            Inventory inv = event.getInventory();
            for (Menu menu : menus) {
                if (!menu.isMenu(inv, true)) {
                    continue;
                }
                menu.openInventories.remove(event.getInventory());
                menu.onClose(event);
            }
        }

        @EventHandler
        private void menuClick(InventoryClickEvent event) {
            Inventory inv = event.getInventory();
            for (Menu menu : menus) {
                if (!menu.isMenu(inv, true)) {
                    continue;
                }
                menu.onClick(event);
            }
        }

        @EventHandler
        private void pluginDisable(PluginDisableEvent event) {
            if (menus == null || menus.isEmpty()) {
                return;
            }
            List<Menu> menusClone = new ArrayList<Menu>(menus);
            for (Menu menu : menusClone) {
                if (menu.plugin == event.getPlugin()) {
                    menu.destroyMenu();
                }
            }
        }
    }
}
