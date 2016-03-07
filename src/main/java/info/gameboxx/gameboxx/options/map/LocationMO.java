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

package info.gameboxx.gameboxx.options.map;

import info.gameboxx.gameboxx.options.MapOption;
import info.gameboxx.gameboxx.options.single.LocationO;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

public class LocationMO extends MapOption<Location, LocationMO, LocationO> {

    public Map<String, Location> getValues(World world) {
        Map<String, Location> values = new HashMap<>();
        for (String key : this.values.keySet()) {
            values.put(key, getValue(key, world));
        }
        return values;
    }

    public Location getValue(String key, World world) {
        Location l = getValue(key);
        if (l == null || world == null) {
            return l;
        }
        l.setWorld(world);
        return l;
    }

    @Override
    public LocationO getSingleOption() {
        return new LocationO();
    }

    @Override
    public LocationMO clone() {
        return cloneData(new LocationMO());
    }
}
