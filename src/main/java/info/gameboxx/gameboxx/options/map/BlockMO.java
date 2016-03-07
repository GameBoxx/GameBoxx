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
import info.gameboxx.gameboxx.options.single.BlockO;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.*;

public class BlockMO extends MapOption<Block, BlockMO, BlockO> {

    public Map<String, Block> getValues(World world) {
        Map<String, Block> values = new HashMap<>();
        for (String key : this.values.keySet()) {
            values.put(key, getValue(key, world));
        }
        return values;
    }

    public Block getValue(String key, World world) {
        Block b = getValue(key);
        if (b == null || world == null) {
            return b;
        }
        return world.getBlockAt(b.getLocation());
    }

    @Override
    public BlockO getSingleOption() {
        return new BlockO();
    }

    @Override
    public BlockMO clone() {
        return cloneData(new BlockMO());
    }
}
