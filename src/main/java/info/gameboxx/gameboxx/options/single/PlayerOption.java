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

public class PlayerOption extends SingleOption {

    public PlayerOption() {
        super();
    }

    public PlayerOption(String name) {
        super(name);
    }

    public PlayerOption(String name, String playerName) {
        super(name, Bukkit.getServer().getPlayer(playerName));
    }

    public PlayerOption(String name, Player player) {
        super(name, player);
    }

    public PlayerOption(String name, UUID playerUUID) {
        super(name, Bukkit.getServer().getPlayer(playerUUID));
    }


    @Override
    public boolean parse(Object input) {
        if (!parseObject(input)) {
            if (input instanceof UUID) {
                value = Bukkit.getServer().getPlayer((UUID)input);
                if (value == null) {
                    error = "No player with the specified UUID.";
                }
                return success();
            }
            return false;
        }
        if (value != null) {
            return true;
        }
        return parse((String)input);
    }

    @Override
    public boolean parse(String input) {
        return parse(null, input);
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
    public Player getValue() {
        return (Player)getValueOrDefault();
    }

    @Override
    public String serialize() {
        Player value = getValue();
        if (value == null) {
            return null;
        }
        return value.getUniqueId().toString();
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
    public String getTypeName() {
        return "player";
    }

    @Override
    public Class getRawClass() {
        return Player.class;
    }

    @Override
    public PlayerOption clone() {
        return (PlayerOption)new PlayerOption(name, (Player)defaultValue).setDescription(description).setFlag(flag);
    }
}
