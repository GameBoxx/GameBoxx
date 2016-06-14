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

package info.gameboxx.gameboxx.options.single;

import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.util.Parse;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;

import java.util.*;

public class WorldO extends SingleOption<World, WorldO> {

    @Override
    public boolean parse(CommandSender sender, String input) {
        Server server = Bukkit.getServer();

        if (input.isEmpty() || input.equals("@")) {
            Location location = Utils.getLocation(sender);
            if (location == null) {
                error = Msg.getString("selector-console-player", Param.P("type", input));
                return false;
            }
            value = location.getWorld();
            return true;
        }

        if (input.startsWith("@")) {
            PlayerO playerOption = new PlayerO();
            if (!playerOption.parse(sender, input.substring(1))) {
                error = playerOption.getError();
                return false;
            }
            value = playerOption.getValue().getWorld();
            return true;
        }

        if (Parse.Int(input) != null) {
            value = server.getWorlds().size() < Parse.Int(input) ? null : server.getWorlds().get(Parse.Int(input));
        } else if (input.length() == 36 && input.split("-").length == 5) {
            value = server.getWorld(UUID.fromString(input));
        } else {
            value = server.getWorld(input);
        }

        if (value == null) {
            error = Msg.getString("world.invalid", Param.P("input", input));
            return false;
        }
        return true;
    }

    @Override
    public String serialize() {
        return serialize(getValue());
    }

    public static String serialize(World world) {
        return world == null ? null : world.getName();
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(World world) {
        return world == null ? null : Msg.getString("world.display", Param.P("name", world.getName()), Param.P("uuid", world.getUID()));
    }

    @Override
    public String getTypeName() {
        return "World";
    }

    @Override
    public List<String> onComplete(CommandSender sender, String prefix, String input) {
        List<String> suggestions = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            if (input.trim().isEmpty() || world.getName().toLowerCase().startsWith(input)) {
                suggestions.add(prefix + world.getName());
            }
        }

        Collections.sort(suggestions);
        return suggestions;
    }

    @Override
    public WorldO clone() {
        return super.cloneData(new WorldO());
    }
}
