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

package info.gameboxx.gameboxx.commands.test;

import info.gameboxx.gameboxx.commands.api.BaseCmd;
import info.gameboxx.gameboxx.commands.api.CmdData;
import info.gameboxx.gameboxx.commands.api.data.ArgRequirement;
import info.gameboxx.gameboxx.options.single.PlayerO;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HealCmd extends BaseCmd {

    public HealCmd() {
        super("heal");
        addArgument("player", ArgRequirement.REQUIRED_NON_PLAYER, new PlayerO());

        addFlag("a");

        addRemoveLink("a", "player");
    }

    @Override
    public void onCommand(CmdData data) {
        if (data.hasFlag("a")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setHealth(player.getMaxHealth());
                player.sendMessage("Healed!");
            }
            data.getSender().sendMessage("All players healed...");
            return;
        }

        data.getPlayer("player").setHealth(data.getPlayer("player").getMaxHealth());
        data.getPlayer("player").sendMessage("Healed!");
        if (!data.getSender().equals(data.getPlayer("player"))) {
            data.getSender().sendMessage("Healed " + data.getPlayer("player").getName() + "!");
        }
    }
}
