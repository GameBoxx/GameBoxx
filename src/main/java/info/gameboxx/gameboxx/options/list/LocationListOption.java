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

package info.gameboxx.gameboxx.options.list;

import info.gameboxx.gameboxx.options.ListOption;
import info.gameboxx.gameboxx.options.single.LocationOption;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class LocationListOption extends ListOption {

    public LocationListOption() {
        super();
    }

    public LocationListOption(String name) {
        super(name);
    }

    public LocationListOption(String name, Location defaultValue) {
        super(name, defaultValue);
    }


    @Override
    public LocationOption getSingleOption() {
        return (LocationOption) new LocationOption(name, (Location)defaultValue).setDescription(description).setFlag(flag);
    }

    @Override
    public List<Location> getValues() {
        return getValues(null);
    }

    public List<Location> getValues(World world) {
        List<Location> values = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            values.add(getValue(i, world));
        }
        return values;
    }

    @Override
    public Location getValue(int index) {
        return (Location)getValueOrDefault(index);
    }

    public Location getValue(int index, World world) {
        Location l = getValue(index);
        if (l == null || world == null) {
            return l;
        }
        l.setWorld(world);
        return l;
    }

    @Override
    public LocationListOption clone() {
        return (LocationListOption) new LocationListOption(name, (Location) defaultValue).setDescription(description).setFlag(flag);
    }
}
