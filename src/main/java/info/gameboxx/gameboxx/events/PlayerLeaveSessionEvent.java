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
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The event that gets called whenever a player leaves a session.
 */
public final class PlayerLeaveSessionEvent extends Event {
	
	private Player who;
	private GameSession session;
	private LeaveReason reason;
	
	/**
	 * Called whenever a Player leaves a session.
	 * @param who The player who left a session.
	 * @param session The session that a player left.
	 * @param reason The reason why a player was disconnected from their session.
	 */
	public PlayerLeaveSessionEvent(Player who, GameSession session, LeaveReason reason) {
		this.who = who;
		this.session = session;
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
	public Player getWho() {
		return this.who;
	}
	
	/**
	 * Get the session that a player left from.
	 * @return The session that was left.
	 */
	public GameSession getLeftSession() {
		return this.session;
	}

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
