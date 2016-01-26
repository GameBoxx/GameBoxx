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

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The event that is called whenever a player joins a {@link GameSession}.
 */
public final class PlayerJoinSessionEvent extends Event {
	
	private Player who;
	private GameSession session;
	
	/**
	 * The constructor to call the PlayerJoinSessionEvent.
	 * @param who The {@link Player} that joins a session.
	 * @param session The {@link GameSession} that the player has joined.
	 */
	public PlayerJoinSessionEvent(Player who, GameSession session) {
		this.who = who;
		this.session = session;
	}
	
	/**
	 * Returns all the handlers associated with this event.
	 * @return A new HandlerList.
	 */
	@Override
	public HandlerList getHandlers() {
		return new HandlerList();
	}
	
	/**
	 * Get the {@link Player} who joined a session.
	 * @return The Player.
	 */
	public Player getWhoJoined() {
		return this.who;
	}
	
	/**
	 * Get the {@link GameSession} that a player joined.
	 * @return The GameSession.
	 */
	public GameSession getJoinedSession() {
		return this.session;
	}

}
