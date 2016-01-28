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

package info.gameboxx.gameboxx.events;

import info.gameboxx.gameboxx.game.GameSession;
import info.gameboxx.gameboxx.game.LeaveReason;
import org.bukkit.entity.Player;

/**
 * The event that gets called whenever a player leaves a session.
 */
public final class PlayerLeaveSessionEvent extends SessionEvent {
    
    private Player player;
    private LeaveReason reason;
    
    /**
     * Called whenever a Player leaves a session.
     * @param who The player who left a session.
     * @param session The session that a player left.
     * @param reason The reason why a player was disconnected from their session.
     */
    public PlayerLeaveSessionEvent(Player player, GameSession session, LeaveReason reason) {
        super(session);
        this.player = player;
        this.reason = reason;
    }

    /**
     * Get the reason ({@link LeaveReason}) that the player left a session.
     * @return The reason for leaving.
     */
    public final LeaveReason getLeaveReason() {
        return this.reason;
    }
    
    /**
     * Get the player who left a session.
     * @return The Player who left.
     */
    public Player getPlayer() {
        return this.player;
    }
    
}
