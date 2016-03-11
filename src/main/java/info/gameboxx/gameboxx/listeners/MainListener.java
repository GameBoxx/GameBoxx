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

package info.gameboxx.gameboxx.listeners;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.system.points.concurrent.Loader;
import info.gameboxx.gameboxx.system.points.concurrent.Saver;
import info.gameboxx.gameboxx.system.points.model.Currency;
import info.gameboxx.gameboxx.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MainListener implements Listener {

    private GameBoxx gb;

    public MainListener(GameBoxx gb) {
        this.gb = gb;
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        if (gb.getCfg().sql) {
            User user = new User(event.getPlayer());
            gb.getUM().register(user);
            for (Currency currency : Currency.values()) {
                new Loader(user, currency).runTaskAsynchronously(gb);
            }
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        if (gb.getCfg().sql) {
            User user = gb.getUM().getUser(event.getPlayer().getUniqueId());
            for (Currency currency : Currency.values()) {
                new Saver(user, currency).runTaskAsynchronously(gb);
            }
        }
    }

}
