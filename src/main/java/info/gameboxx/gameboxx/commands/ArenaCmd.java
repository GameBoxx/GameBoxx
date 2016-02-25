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
import info.gameboxx.gameboxx.exceptions.ArenaAlreadyExistsException;
import info.gameboxx.gameboxx.game.Arena;
import info.gameboxx.gameboxx.game.ArenaType;
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.nms.NMS;
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
            Msg.get("arena.help", Param.P("cmd", label)).send(sender);
            return true;
        }
        args = Utils.fixCommandArgs(args);

        //arena create {game} {name} {type} [world gen data]
        if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("generate") || args[0].equalsIgnoreCase("new")) {
            if (args.length < 4) {
                Msg.get("invalid-usage", Param.P("usage", "/" + label + " " + args[0] + " {game} {name} {type} [world data]")).send(sender);
                return true;
            }

            Game game = gb.getGM().getGame(args[1]);
            if (game == null) {
                Msg.get("game.invalid", Param.P("input", args[1]), Param.P("games", Str.implode(gb.getGM().getGameNames(), ", ", " & "))).send(sender);
                return true;
            }

            ArenaType type = ArenaType.fromName(args[3]);
            if (type == null) {
                Msg.get("arena.type.invalid", Param.P("input", args[3]), Param.P("types", Utils.getAliasesString("arena.type.entry", ArenaType.getAliasesMap()))).send(sender);
                return true;
            }

            final String name = args[2];
            try {
                Arena arena = game.createArena(type, name);
            } catch (ArenaAlreadyExistsException e) {
                Msg.get("arena.create.exists", Param.P("name", name), Param.P("arenas", Str.implode(game.getArenaNames(), ", ", " & "))).send(sender);
                return true;
            } catch (IOException e) {
                Msg.get("arena.create.error", Param.P("name", name), Param.P("error", e.getMessage())).send(sender);
                return true;
            }

            if (type == ArenaType.DEFAULT) {
                Msg.get("arena.create.default", Param.P("name", name)).send(sender);
                return true;
            }

            //Get the world creator based on remaining arguments.
            //TODO: Add support for copying other worlds like copy:{game}:{arena} for WORLD type only.
            final WorldCreator worldCreator = Utils.getWorldCreator(name + "_TEMPLATE", args, 4);
            //TODO: Save world creator data to arena.

            if (type == ArenaType.GENERATE_WORLD) {
                Msg.get("arena.create.generate", Param.P("name", name)).send(sender);
                return true;
            }

            Msg.get("arena.create.creating", Param.P("name", name)).send(sender);
            //Create the template world and set spawn location at the center
            new BukkitRunnable() {
                @Override
                public void run() {
                    final World world = NMS.get().getWorldLoader().createAsyncWorld(worldCreator);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Block block = Utils.getHighestBlockAt(world, 0, 0);
                            world.setSpawnLocation(0, block.getY(), 0);
                            world.save();

                            //Teleport the player to the new world.
                            if (sender instanceof Player) {
                                ((Player)sender).teleport(block.getLocation());
                            }

                            Msg.get("arena.create.world", Param.P("name", name)).send(sender);
                        }
                    }.runTaskLater(gb, 1);
                }
            }.runTaskAsynchronously(gb);
            return true;
        }

        //Get/validate the arena selection.
        Arena arena = ArenaSelection.getSel(sender, args);
        if (arena == null) {
            Msg.get("no-selection").send(sender);
            return true;
        }

        //arena save
        if (args[0].equalsIgnoreCase("save")) {
            World world = gb.getServer().getWorld(arena.getName() + "_TEMPLATE");
            if (world == null) {
                sender.sendMessage("Nothing to save...");
                return true;
            }

            //Remove all players from world.
            World defaultWorld = gb.getServer().getWorld(gb.getCfg().defaultWorld);
            if (defaultWorld == null) {
                defaultWorld = gb.getServer().getWorlds().get(0);
            }
            for (Player player : world.getPlayers()) {
                player.teleport(defaultWorld.getSpawnLocation());
                player.sendMessage("Send back to the default world because the arena is being saved...");
            }

            //Unload the world and save it.
            gb.getServer().unloadWorld(world, true);

            //Copy the world back to template world directory.
            File worldFile = new File(gb.getServer().getWorldContainer(), arena.getName() + "_TEMPLATE");
            File mapDir = new File(gb.getDataFolder().getAbsolutePath() + File.separator + "maps" + File.separator + arena.getGame().getName() + File.separator + arena.getName());
            mapDir.mkdirs();
            try {
                FileUtils.copyDirectory(worldFile, mapDir);
                sender.sendMessage("World saved...");
                try {
                    FileUtils.deleteDirectory(worldFile);
                    sender.sendMessage("World deleted...");
                } catch (IOException e) {
                    sender.sendMessage("Failed to delete world...");
                }
            } catch (IOException e) {
                sender.sendMessage("Failed to save...");
            }
            return true;
        }

        //GameMsg.ARENA_HELP.send(sender, false, true);
        return true;
    }
}
