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
import info.gameboxx.gameboxx.util.Str;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerOption extends SingleOption<Player, PlayerOption> {

    @Override
    public boolean parse(Object input) {
        if (input instanceof UUID) {
            input = input.toString();
        }
        return super.parse(input);
    }

    @Override
    public boolean parse(Player player, String input) {
        if (input.startsWith("@")) {
            value = player;
            if (value == null) {
                error = "No player assigned.";
                return false;
            }
            return true;
        }

        String[] components = input.split("-");
        if (input.length() == 36 && components.length == 5 && !components[0].isEmpty()) {
            value = Bukkit.getPlayer(UUID.fromString(input));
            if (value == null) {
                error = "No player with the specified UUID.";
                return false;
            }
        } else {
            value = Bukkit.getPlayer(input);
        }

        if (value == null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (Str.stripColor(p.getDisplayName()).equalsIgnoreCase(input) || Str.stripColor(p.getPlayerListName()).equalsIgnoreCase(input)) {
                    value = p;
                    break;
                }
            }
        }

        if (value == null) {
            error = "No player with the specified name.";
            return false;
        }

        return true;
    }

    @Override
    public String serialize() {
        return getValue() == null ? null : getValue().getUniqueId().toString();
    }

    @Override
    public String getDisplayValue() {
        Player value = getValue();
        if (value == null) {
            return null;
        }
        return value.getName();
    }

    @Override
    public PlayerOption clone() {
        return super.cloneData(new PlayerOption());
    }
}
