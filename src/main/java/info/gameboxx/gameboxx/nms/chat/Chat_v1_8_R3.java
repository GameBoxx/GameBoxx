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

package info.gameboxx.gameboxx.nms.chat;

import info.gameboxx.gameboxx.nms.util.NMSUtil_v1_8_R3;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.entity.Player;
import org.json.simple.JSONValue;

import java.util.Collection;

public class Chat_v1_8_R3 implements Chat {

    @Override
    public Chat send(String message, Player player) {
        if (!message.startsWith("{") && !message.startsWith("[")) {
            player.sendMessage(message);
            return this;
        }
        PacketPlayOutChat chatPacket = new PacketPlayOutChat(NMSUtil_v1_8_R3.serializeChat(message), (byte)0);
        NMSUtil_v1_8_R3.sendPacket(player, chatPacket);
        return this;
    }

    @Override
    public Chat send(String message, Player... players) {
        PacketPlayOutChat packet = null;
        if (message.startsWith("{") || message.startsWith("[")) {
            packet = new PacketPlayOutChat(NMSUtil_v1_8_R3.serializeChat(message), (byte)0);
        }

        for (Player player : players) {
            if (packet == null) {
                player.sendMessage(message);
            } else {
                NMSUtil_v1_8_R3.sendPacket(player, packet);
            }
        }
        return this;
    }

    @Override
    public Chat send(String message, Collection<? extends Player> players) {
        PacketPlayOutChat packet = null;
        if (message.startsWith("{") || message.startsWith("[")) {
            packet = new PacketPlayOutChat(NMSUtil_v1_8_R3.serializeChat(message), (byte)0);
        }

        for (Player player : players) {
            if (packet == null) {
                player.sendMessage(message);
            } else {
                NMSUtil_v1_8_R3.sendPacket(player, packet);
            }
        }
        return this;
    }

    @Override
    public Chat sendBar(String message, Player player) {
        if (!message.startsWith("{") && !message.startsWith("[")) {
            message = "{\"text\":\"" + JSONValue.escape(message) + "\"}";
        }
        PacketPlayOutChat packet = new PacketPlayOutChat(NMSUtil_v1_8_R3.serializeChat(message), (byte)2);
        NMSUtil_v1_8_R3.sendPacket(player, packet);
        return this;
    }

    @Override
    public Chat sendBar(String message, Player... players) {
        if (!message.startsWith("{") && !message.startsWith("[")) {
            message = "{\"text\":\"" + JSONValue.escape(message) + "\"}";
        }
        PacketPlayOutChat packet = new PacketPlayOutChat(NMSUtil_v1_8_R3.serializeChat(message), (byte)2);

        for (Player player : players) {
            NMSUtil_v1_8_R3.sendPacket(player, packet);
        }
        return this;
    }

    @Override
    public Chat sendBar(String message, Collection<? extends Player> players) {
        if (!message.startsWith("{") && !message.startsWith("[")) {
            message = "{\"text\":\"" + JSONValue.escape(message) + "\"}";
        }
        PacketPlayOutChat packet = new PacketPlayOutChat(NMSUtil_v1_8_R3.serializeChat(message), (byte)2);

        for (Player player : players) {
            NMSUtil_v1_8_R3.sendPacket(player, packet);
        }
        return this;
    }
}
