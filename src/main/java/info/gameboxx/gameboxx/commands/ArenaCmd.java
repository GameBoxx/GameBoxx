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
import info.gameboxx.gameboxx.exceptions.ArenaAlreadyExistsException;
import info.gameboxx.gameboxx.game.Arena;
import info.gameboxx.gameboxx.game.ArenaType;
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.util.Str;
import info.gameboxx.gameboxx.util.Utils;
import org.apache.commons.io.FileUtils;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class ArenaCmd implements CommandExecutor {

    private final GameBoxx gb;

    public ArenaCmd(GameBoxx gb) {
        this.gb = gb;
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
            //GameMsg.ARENA_HELP.send(sender, false, true);
            return true;
        }
        args = Utils.fixCommandArgs(args);

        //arena create {game} {name} {type} [world gen data]
        if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("generate") || args[0].equalsIgnoreCase("new")) {
            if (args.length < 4) {
                GameMsg.INVALID_USAGE.send(sender, Param.P("{usage}", "/" + label + " " + args[0] + " {game} {name} {type} [world data]"));
                return true;
            }

            Game game = gb.getGM().getGame(args[1]);
            if (game == null) {
                GameMsg.INVALID_GAME.send(sender, Param.P("{name}", args[1]), Param.P("{games}", Str.implode(gb.getGM().getGameNames(), ", ", " & ")));
                return true;
            }

            ArenaType type = ArenaType.fromName(args[3]);
            if (type == null) {
                GameMsg.INVALID_ARENA_TYPE.send(sender, Param.P("{type}", args[3]));
                return true;
            }

            try {
                Arena arena = game.createArena(type, args[2]);
            } catch (ArenaAlreadyExistsException e) {
                GameMsg.ARENA_NAME_ALREADY_EXISTS.send(sender, Param.P("{name}", args[2]), Param.P("{arenas}", Str.implode(game.getArenaNames(), ", ", " & ")));
                return true;
            } catch (IOException e) {
                //TODO: Send message that it failed to create arena config file.
                return true;
            }

            if (type == ArenaType.DEFAULT) {
                //TODO: Arena created..
                sender.sendMessage("Default arena created...");
                return true;
            }

            //Get the world creator based on remaining arguments.
            //TODO: Add support for copying other worlds like copy:{game}:{arena} for WORLD type only.
            final WorldCreator worldCreator = Utils.getWorldCreator(args[2] + "_TEMPLATE", args, 4);
            //TODO: Save world creator data to arena.

            if (type == ArenaType.GENERATE_WORLD) {
                //TODO: Arena created..
                sender.sendMessage("Generate world arena created...");
                return true;
            }

            sender.sendMessage("Creating the world for world arena.");
            //Create the template world and set spawn location at the center
            new BukkitRunnable() {
                @Override
                public void run() {
                    final World world = gb.getWorldLoader().createAsyncWorld(worldCreator);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Block block = Utils.getHighestBlockAt(world, 0, 0);
                            world.setSpawnLocation(0, block.getY(), 0);
                            world.save();

                            //Teleport the player to the new world.
                            if (sender instanceof Player) {
                                sender.sendMessage("Teleporting the player to the new spawn!");
                                ((Player)sender).teleport(block.getLocation());
                            }

                            sender.sendMessage("World arena created...");
                        }
                    }.runTaskLater(gb, 1);
                }
            }.runTaskAsynchronously(gb);

            //TODO: Arena created..
            return true;
        }

        //Get/validate the arena selection.
        Arena arena = ArenaSelection.getSel(sender, args);
        if (arena == null) {
            GameMsg.NO_ARENA_SELECTION.send(sender);
            return true;
        }


        //GameMsg.ARENA_HELP.send(sender, false, true);
        return true;
    }
}
