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
import info.gameboxx.gameboxx.options.single.IntO;
import info.gameboxx.gameboxx.options.single.ItemO;
import info.gameboxx.gameboxx.options.single.PlayerO;
import info.gameboxx.gameboxx.util.ItemUtil;
import info.gameboxx.gameboxx.util.item.EItem;

import java.io.File;

public class GiveCmd extends BaseCmd {

    public GiveCmd(File file) {
        super("give", "item", "i");
        file(file);
        desc("Play a game!");
        perm("gameboxx.cmd.give");

        addArgument("player", ArgRequirement.REQUIRED_NON_PLAYER, new PlayerO()).desc("The player to give the item to").perm("gameboxx.cmd.give.others").skippable(true);
        addArgument("item", ArgRequirement.REQUIRED, new ItemO()).desc("The full item string which may contain meta and such.").span(-1);

        addModifier("slot", new IntO().min(0).max(39)).desc("The slot to put the item in.");

        addFlag("d").desc("Drop items on the ground if they don't fit?");
        addFlag("u").desc("Unstack items if they exceed the max stack size?");
    }

    @Override
    public void onCommand(CmdData data) {
        ItemUtil.add(data.getPlayer("player").getInventory(), (EItem)data.getArg("item"), data.hasMod("slot") ? (int)data.getMod("slot") : -1, data.hasFlag("dropFull"), data.hasFlag("unstack"));
        data.getSender().sendMessage("Item given!");
    }
}
