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

package info.gameboxx.gameboxx.components.internal;

import info.gameboxx.gameboxx.GameBoxx;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * A specific type of Listener designed for components,
 * will only register if it hasn't been registed before.
 */
public abstract class ComponentListener implements Listener {
	
	private boolean isRegistered = false;
	
	/**
	 * Registers an event, if it is not already registered.
	 * @param api The main plugin to register the event to.
	 */
	public void register(GameBoxx api) {
	    if (!(isRegistered)) {
	        Bukkit.getPluginManager().registerEvents(this, api);
			isRegistered = true;
		}
	}

}
