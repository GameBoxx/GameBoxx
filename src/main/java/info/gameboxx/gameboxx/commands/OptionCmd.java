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
import info.gameboxx.gameboxx.options.ListOption;
import info.gameboxx.gameboxx.options.Option;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.util.Parse;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class OptionCmd implements CommandExecutor {

    private final GameBoxx gb;

    public OptionCmd(GameBoxx gb) {
        this.gb = gb;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //Get/validate the arena selection.
        Arena arena = ArenaSelection.getSel(sender, args);
        if (arena == null) {
            GameMsg.NO_ARENA_SELECTION.send(sender);
            return true;
        }

        if (args.length < 1) {
            GameMsg.INVALID_USAGE.send(sender, Param.P("{usage}", "/" + label + " {name}"));
            return true;
        }

        Option option = arena.getOption(args[0]);
        if (option == null) {
            sender.sendMessage("Invalid option...");
            return true;
        }

        if (option instanceof SingleOption) {
            // /option {name} [value]
            SingleOption singleOption = (SingleOption)option;

            if (args.length < 2) {
                //TODO: Display option value and info.
                sender.sendMessage("Single option");
                sender.sendMessage("Value: " + (singleOption.getValue() == null ? "null" : singleOption.getValue().toString()));
                return true;
            }

            if (!singleOption.parse(args[1])) {
                sender.sendMessage(singleOption.getError());
                return true;
            }
            arena.save();
            sender.sendMessage("Option value set!");
            return true;
        } else if (option instanceof ListOption) {
            // /option {name} [index] [value]
            ListOption listOption = (ListOption)option;

            if (args.length < 2) {
                //TODO: Display option values and info.
                sender.sendMessage("List option");
                sender.sendMessage("Values:");
                for (Object val : listOption.getValues()) {
                    sender.sendMessage(val.toString());
                }
                return true;
            }

            Integer index = Parse.Int(args[1]);
            if (index == null) {
                GameMsg.NOT_A_NUMBER.send(sender, Param.P("{input}", args[1]));
                return true;
            }

            if (args.length < 3) {
                //TODO: Display option value for index.
                Object value = listOption.getValue(index);
                sender.sendMessage("value: " + (value == null ? "null" : value.toString()));
                return true;
            }

            if (!listOption.parse(index, args[2])) {
                sender.sendMessage(listOption.getError());
                return true;
            }
            arena.save();
            sender.sendMessage("Option value set!");
            return true;
        }
        //TODO: Map option

        sender.sendMessage("Invalid option...");
        return true;
    }
}
