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

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.GameMsg;
import info.gameboxx.gameboxx.config.messages.Param;
import info.gameboxx.gameboxx.game.Arena;
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.util.Str;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SelectCmd implements CommandExecutor {

    private final GameBoxx gb;

    public SelectCmd(GameBoxx gb) {
        this.gb = gb;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        args = Utils.fixCommandArgs(args);

        if (args.length < 2) {
            GameMsg.INVALID_USAGE.send(sender, Param.P("{usage}", "/" + label + " {game} {arena}"));
            return true;
        }

        Game game = gb.getGM().getGame(args[0]);
        if (game == null) {
            GameMsg.INVALID_GAME.send(sender, Param.P("{name}", args[0]), Param.P("{games}", Str.implode(gb.getGM().getGameNames(), ", ", " & ")));
            return true;
        }

        Arena arena = game.getArena(args[1]);
        if (arena == null) {
            GameMsg.INVALID_ARENA.send(sender, Param.P("{name}", args[0]), Param.P("{arenas}", Str.implode(game.getArenaNames(), ", ", " & ")));
            return true;
        }

        ArenaSelection.setSel(sender, arena);
        GameMsg.ARENA_SELECTED.send(sender, Param.P("{game}", game.getName()), Param.P("{arena}", arena.getName()));
        return true;
    }
}
