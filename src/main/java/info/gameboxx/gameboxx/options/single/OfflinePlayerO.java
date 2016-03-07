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
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class OfflinePlayerO extends SingleOption<OfflinePlayer, OfflinePlayerO> {

    @Override
    public boolean parse(Object input) {
        if (input instanceof UUID) {
            input = input.toString();
        }
        return super.parse(input);
    }

    @Override
    public boolean parse(Player player, String input) {
        if (input.isEmpty() || input.equals("@")) {
            value = player;
            if (value == null) {
                error = Msg.getString("offline-player.non-player");
                return false;
            }
            return true;
        }

        if (input.startsWith("@")) {
            input = input.substring(1);
        }

        String[] components = input.split("-");
        if (input.length() == 36 && components.length == 5 && !components[0].isEmpty()) {
            value = Bukkit.getOfflinePlayer(UUID.fromString(input));
            if (value == null) {
                error = Msg.getString("offline-player.invalid", Param.P("input", input));
                return false;
            }
        } else {
            value = Bukkit.getOfflinePlayer(input);
        }

        if (value == null) {
            error = Msg.getString("offline-player.invalid", Param.P("input", input));
            return false;
        }

        return true;
    }

    @Override
    public String serialize() {
        return serialize(getValue());
    }

    public static String serialize(OfflinePlayer player) {
        return player == null ? null : player.getUniqueId().toString();
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(OfflinePlayer player) {
        return player == null ? null : Msg.getString("offline-player.display", Param.P("name", player.getName()), Param.P("uuid", player.getUniqueId()));
    }

    @Override
    public OfflinePlayerO clone() {
        return super.cloneData(new OfflinePlayerO());
    }
}
