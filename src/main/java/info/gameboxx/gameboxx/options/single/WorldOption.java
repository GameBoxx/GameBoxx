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

import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.util.Parse;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WorldOption extends SingleOption<World, WorldOption> {

    @Override
    public boolean parse(Player player, String input) {
        Server server = Bukkit.getServer();

        if (input.startsWith("@")) {
            PlayerOption playerOption = new PlayerOption();
            playerOption.def(player);
            playerOption.parse(player, input.substring(1));
            if (!playerOption.hasValue()) {
                error = playerOption.getError().isEmpty() ? "Invalid player to get the world from." : playerOption.getError();
                return false;
            }
            value = playerOption.getValue().getWorld();
            return true;
        }

        if (Parse.Int(input) != null) {
            value = server.getWorlds().size() < Parse.Int(input) ? null : server.getWorlds().get(Parse.Int(input));
            if (value == null) {
                error = "No world with the specified ID.";
                return false;
            }
        } else if (input.length() == 36 && input.split("-").length == 5) {
            value = server.getWorld(UUID.fromString(input));
            if (value == null) {
                error = "No world with the specified UUID.";
                return false;
            }
        } else {
            value = server.getWorld(input);
            if (value == null) {
                error = "No world with the specified name.";
                return false;
            }
        }

        return true;
    }

    @Override
    public String serialize() {
        return getValue() == null ? null : getValue().getName();
    }

    @Override
    public WorldOption clone() {
        return super.cloneData(new WorldOption());
    }
}
