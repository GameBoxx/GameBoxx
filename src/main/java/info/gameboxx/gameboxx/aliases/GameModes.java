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

package info.gameboxx.gameboxx.aliases;

import info.gameboxx.gameboxx.aliases.internal.AliasMap;
import org.bukkit.GameMode;

import java.io.File;
import java.util.List;
import java.util.Map;

public class GameModes extends AliasMap<GameMode> {

    private GameModes() {
        super("GameModes", new File(ALIASES_FOLDER, "PlayerData.yml"), "gamemode", "modes", "mode");
    }

    @Override
    public void onLoad() {
        add(GameMode.CREATIVE, "Creative", "1", "Crea", "Cre", "C");
        add(GameMode.SURVIVAL, "Survival", "0", "Survive", "Sur", "S");
        add(GameMode.ADVENTURE, "Adventure", "2", "Advent", "Adv", "A");
        add(GameMode.SPECTATOR, "Spectator", "3", "Spectate", "Spec", "SP");
    }

    public static GameMode get(String string) {
        return instance()._get(string);
    }

    public static String getName(GameMode key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(GameMode key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(GameMode key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static GameModes instance() {
        if (getMap(GameModes.class) == null) {
            aliasMaps.put(GameModes.class, new GameModes());
        }
        return (GameModes)getMap(GameModes.class);
    }
}
