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

package info.gameboxx.gameboxx.util.cuboid;

import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.util.ItemUtil;
import info.gameboxx.gameboxx.util.item.EItem;
import info.gameboxx.gameboxx.util.particles.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelectionManager {

    private static SelectionManager instance;
    private SelectionListener listener;
    private EItem basicWand = new EItem(Material.STONE_AXE, 1, (short)0).makeGlowing(true).addAllFlags(true).setName("&6&lSelection Wand");

    Map<UUID, SelectionData> selections = new HashMap<UUID, SelectionData>();
    SelectionData globalSelection = new SelectionData();

    public SelectionManager() {
        instance = this;
        listener = new SelectionListener();
    }

    public static SelectionManager inst() {
        return instance;
    }

    public SelectionListener getListener() {
        return listener;
    }

    /**
     * Get a Cuboid from the selection.
     * If only one of the locations is set a 1*1*1 cuboid will be returned at that location.
     * @param player Get selection from the specified player or get global selection if it's null.
     * @return Cuboid or null if both locations aren't set.
     */
    public Cuboid getSelection(Player player) {
        SelectionData sd;
        if (player == null) {
            sd = globalSelection;
        } else {
            UUID uuid = player.getUniqueId();
            if (!selections.containsKey(uuid)) {
                selections.put(uuid, new SelectionData());
            }
            sd = selections.get(uuid);
        }
        if (sd.pos1 != null && sd.pos2 != null) {
            return new Cuboid(sd.pos1, sd.pos2);
        } else if (sd.pos1 != null) {
            return new Cuboid(sd.pos1, sd.pos1);
        } else if (sd.pos2 != null) {
            return new Cuboid(sd.pos2, sd.pos2);
        } else {
            return null;
        }
    }

    public Location getPos(Player player, SelectionType type) {
        if (type == SelectionType.PRIMARY) {
            return getPos1(player);
        } else {
            return getPos2(player);
        }
    }

    public void setPos(Player player, SelectionType type, Location location) {
        if (type == SelectionType.PRIMARY) {
            setPos1(player, location);
        } else {
            setPos2(player, location);
        }
    }

    /**
     * Get the first selected position.
     * If the position isn't set it'll return null.
     * @param player Get pos1 from the specified player or get global pos1 if it's null.
     * @return Location of pos1 or null
     */
    public Location getPos1(Player player) {
        if (player == null) {
            return globalSelection.pos1;
        } else {
            UUID uuid = player.getUniqueId();
            if (!selections.containsKey(uuid)) {
                selections.put(uuid, new SelectionData());
            }
            return selections.get(uuid).pos1;
        }
    }

    /**
     * Set the first position to the specified location.
     * @param player Set pos1 from the specified player or set global pos1 if it's null.
     * @param location The location used to be set as pos1.
     */
    public void setPos1(Player player, Location location) {
        if (player == null) {
            globalSelection.pos1 = location;
        } else {
            UUID uuid = player.getUniqueId();
            if (!selections.containsKey(uuid)) {
                selections.put(uuid, new SelectionData());
            }
            selections.get(uuid).pos1 = location;
        }
    }


    /**
     * Get the second selected position.
     * If the position isn't set it'll return null.
     * @param player Get pos2 from the specified player or get global pos2 if it's null.
     * @return Location of pos2 or null
     */
    public Location getPos2(Player player) {
        if (player == null) {
            return globalSelection.pos2;
        } else {
            UUID uuid = player.getUniqueId();
            if (!selections.containsKey(uuid)) {
                selections.put(uuid, new SelectionData());
            }
            return selections.get(uuid).pos2;
        }
    }

    /**
     * Set the second position to the specified location.
     * @param player Set pos2 from the specified player or set global pos2 if it's null.
     * @param location The location used to be set as pos2.
     */
    public void setPos2(Player player, Location location) {
        if (player == null) {
            globalSelection.pos2 = location;
        } else {
            UUID uuid = player.getUniqueId();
            if (!selections.containsKey(uuid)) {
                selections.put(uuid, new SelectionData());
            }
            selections.get(uuid).pos2 = location;
        }
    }


    /**
     * Get a enum value that shows which points have been set.
     * It'll return BOTH, NONE, POS1 or POS2.
     * @param player Get points from the specified player or from global if it's null.
     * @return SelectionStatus enum value based on points set.
     */
    public SelectionStatus getStatus(Player player) {
        SelectionData sd;
        if (player == null) {
            sd = globalSelection;
        } else {
            UUID uuid = player.getUniqueId();
            if (!selections.containsKey(uuid)) {
                selections.put(uuid, new SelectionData());
            }
            sd = selections.get(uuid);
        }
        if (sd.pos1 != null && sd.pos2 != null) {
            return SelectionStatus.BOTH;
        } else if (sd.pos1 != null) {
            return SelectionStatus.POS1;
        } else if (sd.pos2 != null) {
            return SelectionStatus.POS2;
        } else {
            return SelectionStatus.NONE;
        }
    }

    /**
     * Get the wand item
     * @return EItem
     */
    public EItem getWand() {
        return basicWand;
    }



    public enum SelectionStatus {
        BOTH, NONE, POS1, POS2;
    }

    public enum SelectionType {
        PRIMARY, SECONDARY;
    }



    public class SelectionData {
        public Location pos1;
        public Location pos2;
    }


    public class SelectionListener implements Listener {

        @EventHandler
        private void onInteract(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            if (!player.hasPermission("selection.select")) {
                return;
            }
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) {
                return;
            }

            //Compare item with wands.
            EItem item = new EItem(event.getItem());
            if (!ItemUtil.compare(item, getWand(), true)) {
                return;
            }

            event.setCancelled(true);
            Location loc = event.getClickedBlock().getLocation();

            SelectionType type = SelectionType.PRIMARY;
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                type = SelectionType.SECONDARY;
            }

            //Select!

            Msg.get("wand.selected", Param.P("type", type == SelectionType.PRIMARY ? Msg.getString("wand.pos-1") : Msg.getString("wand.pos-2")),
                    Param.P("x", loc.getBlockX()), Param.P("y", loc.getBlockY()), Param.P("z", loc.getBlockZ()),
                    Param.P("material", event.getClickedBlock().getType().toString()), Param.P("data", event.getClickedBlock().getData())).send(player);
            setPos(player, type, loc);

            //Display particles for selection.
            if (getStatus(player) == SelectionStatus.BOTH) {
                Cuboid cuboid = getSelection(player);
                for (org.bukkit.util.Vector vector : cuboid.getEdgeVectors()) {
                    ParticleEffect.VILLAGER_HAPPY.display(0.1f, 0.1f, 0.1f, 0, 2, vector.toLocation(player.getWorld()).add(0.5f, 0.5f, 0.5f), 300);
                }
            }
        }
    }

}