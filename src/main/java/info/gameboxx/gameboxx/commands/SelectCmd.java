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

package info.gameboxx.gameboxx.commands;

import info.gameboxx.gameboxx.commands.api.BaseCmd;
import info.gameboxx.gameboxx.commands.api.CmdData;
import info.gameboxx.gameboxx.commands.api.data.ArgRequirement;
import info.gameboxx.gameboxx.game.Arena;
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.options.single.StringO;
import info.gameboxx.gameboxx.user.ArenaSelection;
import info.gameboxx.gameboxx.util.Str;

import java.io.File;

public class SelectCmd extends BaseCmd {

    public SelectCmd(File configFile) {
        super("select", "sel");
        file(configFile);
        desc("Select an game/arena for editing and such.");
        perm("gameboxx.cmd.select");

        addArgument("game", ArgRequirement.REQUIRED, new StringO()).desc("The game name to select.");
        addArgument("arena", ArgRequirement.REQUIRED, new StringO()).desc("The arena name to select from the game.");
    }

    @Override
    public void onCommand(CmdData data) {
        Game game = getGB().getGM().getGame((String)data.getArg("game"));
        if (game == null) {
            Msg.get("game.invalid", Param.P("input", data.getArg("game")), Param.P("games", Str.implode(getGB().getGM().getGameNames()))).send(data.getSender());
            return;
        }

        Arena arena = game.getArena((String)data.getArg("arena"));
        if (arena == null) {
            Msg.get("arena.invalid", Param.P("input", data.getArg("arena")), Param.P("game", game.getName()), Param.P("arenas", Str.implode(game.getArenaNames()))).send(data.getSender());
            return;
        }

        ArenaSelection.setSel(data.getSender(), arena);
        Msg.get("select.selected", Param.P("arena", arena.getName()), Param.P("game", game.getName())).send(data.getSender());
    }
}
