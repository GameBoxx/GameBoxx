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
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WorldO extends SingleOption<World, WorldO> {

    @Override
    public boolean parse(Player player, String input) {
        Server server = Bukkit.getServer();

        if (input.startsWith("@")) {
            PlayerO playerOption = new PlayerO();
            playerOption.def(player);
            playerOption.parse(player, input.substring(1));
            if (!playerOption.hasValue()) {
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
    public WorldO clone() {
        return super.cloneData(new WorldO());
    }
}