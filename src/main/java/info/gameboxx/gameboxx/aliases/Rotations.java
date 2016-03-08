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
import org.bukkit.Rotation;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Rotations extends AliasMap<Rotation> {

    private Rotations() {
        super("Rotations", new File(ALIASES_FOLDER, "Rotations.yml"), "rotation");
    }

    @Override
    public void onLoad() {
        add(Rotation.NONE, "None", "0", "360", "North", "Up");
        add(Rotation.CLOCKWISE_45, "Clockwise 45", "45", "-315", "North East", "Up Right");
        add(Rotation.CLOCKWISE, "Clockwise", "90", "-270", "East", "Right");
        add(Rotation.CLOCKWISE_135, "Clockwise 135", "135", "-225", "South East", "Down Right");
        add(Rotation.FLIPPED, "Flipped", "180", "-180", "South", "Down");
        add(Rotation.FLIPPED_45, "Flipped 45", "225", "-135", "South West", "Down Left");
        add(Rotation.COUNTER_CLOCKWISE, "Counter Clockwise", "270", "-90", "West", "Left");
        add(Rotation.COUNTER_CLOCKWISE_45, "Counter Clockwise 45", "315", "-45", "North West", "Up Left");
    }

    public static Rotation get(String string) {
        return instance()._get(string);
    }

    public static String getName(Rotation key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(Rotation key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(Rotation key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static Rotations instance() {
        if (getMap(Rotations.class) == null) {
            aliasMaps.put(Rotations.class, new Rotations());
        }
        return (Rotations)getMap(Rotations.class);
    }
}
