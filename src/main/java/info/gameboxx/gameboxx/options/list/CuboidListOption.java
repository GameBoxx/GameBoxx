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
import info.gameboxx.gameboxx.options.single.CuboidOption;
import info.gameboxx.gameboxx.util.cuboid.Cuboid;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class CuboidListOption extends ListOption {

    public CuboidListOption() {
        super();
    }

    public CuboidListOption(String name) {
        super(name);
    }

    public CuboidListOption(String name, Cuboid defaultValue) {
        super(name, defaultValue);
    }


    @Override
    public CuboidOption getSingleOption(int index) {
        return (CuboidOption) new CuboidOption(name, (Cuboid)getDefault(index)).setDescription(description).setFlag(flag);
    }

    @Override
    public List<Cuboid> getValues() {
        return getValues(null);
    }

    public List<Cuboid> getValues(World world) {
        List<Cuboid> values = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            values.add(getValue(i, world));
        }
        return values;
    }

    @Override
    public Cuboid getValue(int index) {
        return (Cuboid)getValueOrDefault(index);
    }

    public Cuboid getValue(int index, World world) {
        Cuboid c = getValue(index);
        if (c == null || world == null) {
            return c;
        }
        c.setWorld(world);
        return c;
    }

    @Override
    public CuboidListOption clone() {
        return (CuboidListOption) new CuboidListOption(name, (Cuboid) defaultValue).setDescription(description).setFlag(flag);
    }
}
