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

import info.gameboxx.gameboxx.nms.annotation.NMSDependant;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Send action bar messages and chat messages.
 */
@NMSDependant(implementationPath = "info.gameboxx.gameboxx.nms.chat")
public interface Chat {

    /**
     * Send raw chat message to the player
     *
     * @param message The message to be sent to the player.
     * If this message doesn't have JSON formatting it will be sent as a regular text message.
     * @param player The player the message has to be sent to.
     * @return Chat instance
     */
    Chat send(String message, Player player);

    /**
     * Send raw chat message to the players
     *
     * @param message The message to be sent to the player.
     * If this message doesn't have JSON formatting it will be sent as a regular text message.
     * @param players The players the message has to be sent to.
     * @return Chat instance
     */
    Chat send(String message, Player... players);

    /**
     * Send raw chat message to the players
     *
     * @param message The message to be sent to the player.
     * If this message doesn't have JSON formatting it will be sent as a regular text message.
     * @param players The players the message has to be sent to.
     * @return Chat instance
     */
    Chat send(String message, Collection<? extends Player> players);

    /**
     * Send actionbar to the player.
     *
     * @param message The message to be sent to the player.
     * @param player The player the message has to be sent to.
     * @return Chat instance
     */
    Chat sendBar(String message, Player player);

    /**
     * Send actionbar to the players
     *
     * @param message The message to be sent to the player.
     * @param players The players the message has to be sent to.
     * @return Chat instance
     */
    Chat sendBar(String message, Player... players);

    /**
     * Send actionbar to the players
     *
     * @param message The message to be sent to the player.
     * @return Chat instance
     */
    Chat sendBar(String message, Collection<? extends Player> players);
}
