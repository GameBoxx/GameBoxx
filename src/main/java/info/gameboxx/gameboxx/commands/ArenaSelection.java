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
import info.gameboxx.gameboxx.game.Arena;
import info.gameboxx.gameboxx.game.Game;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Used to get/set {@link Arena} selections.
 * Commands will use this selection so that you don't have to provide the game/arena name for each command.
 * Selections for players are stored in {@link info.gameboxx.gameboxx.user.User} and the console selection is stored in this class.
 */
public class ArenaSelection {

    private static Arena consoleSelection = null;

    /**
     * Get the {@link Arena} selection for the specified {@link CommandSender}.
     * @param sender The sender of the command.
     * @return The {@link Arena} selection for the specified sender.
     */
    public static Arena getSel(CommandSender sender) {
        if (sender instanceof Player) {
            return GameBoxx.get().getUM().getUser((Player)sender).getSelectedArena();
        } else {
            return consoleSelection;
        }
    }

    /**
     * Get the {@link Arena} selection for the specified {@link CommandSender}.
     * It will then check the specified command arguments for a arena:{game}:{arena} argument.
     * If it has that argument it will try to parse it and override the selection.
     * @param sender The sender of the command.
     * @param commandArgs The command arguments used to scan for a arena:{game}:{arena} arg.
     * @return The {@link Arena} selection for the specified sender or the specified arena in the command args.
     */
    public static Arena getSel(CommandSender sender, String[] commandArgs) {
        Arena arena = getSel(sender);
        for (String arg : commandArgs) {
            if (arg.startsWith("arena:") || arg.startsWith("ar:")) {
                String[] split = arg.split(":");
                if (split.length > 2) {
                    Game game = GameBoxx.get().getGM().getGame(split[1]);
                    if (game != null) {
                        Arena override = game.getArena(split[2]);
                        if (override != null) {
                            arena = override;
                        }
                    }
                }
            }
        }
        return arena;
    }

    /**
     * Set the {@link Arena} selection for the specified {@link CommandSender}.
     * @param sender The sender of the command.
     * @param arena The {@link Arena} to set.
     */
    public static void setSel(CommandSender sender, Arena arena) {
        if (sender instanceof Player) {
            GameBoxx.get().getUM().getUser((Player)sender).setSelectedArena(arena);
        } else {
            consoleSelection = arena;
        }
    }


}
