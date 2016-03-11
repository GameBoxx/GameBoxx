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

package info.gameboxx.gameboxx.nms.util;

import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.Packet;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * NMS Utilities for version 1.9 R1
 */
public class NMSUtil_V1_9_R1 {

    /**
     * Send a packet to the specified player.
     *
     * @param player The player to send the packet to.
     * @param packet The {@link Packet} to send.
     */
    public static void sendPacket(Player player, Packet packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    /**
     * Send multiple packets to the specified player.
     *
     * @param player  The player to send the packets to.
     * @param packets One or more {@link Packet}s to send.
     */
    public static void sendPackets(Player player, Packet... packets) {
        for (Packet packet : packets) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    /**
     * Sends a packet to the specified players.
     *
     * @param players {@link Collection<Player>} that the packet will be sent to.
     * @param packet The {@link Packet} to send.
     */
    public static void sendPacket(Collection<Player> players, Packet packet) {
        for (Player player : players) {
            sendPacket(player, packet);
        }
    }

    /**
     * Sends multiple packets to the specified players.
     *
     * @param players {@link Collection<Player>} that the packet will be sent to.
     * @param packets One or more {@link Packet}s to send.
     */
    public static void sendPackets(Collection<Player> players, Packet... packets) {
        for (Player player : players) {
            sendPackets(player, packets);
        }
    }

    /**
     * Serialize a message.
     *
     * @param message The message that needs to be serialized.
     * @return Serialized chat as {@link IChatBaseComponent}
     */
    public static IChatBaseComponent serializeChat(String message) {
        return IChatBaseComponent.ChatSerializer.a(message);
    }

}
