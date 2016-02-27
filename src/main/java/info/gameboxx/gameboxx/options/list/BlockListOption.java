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
import info.gameboxx.gameboxx.options.single.BlockOption;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockListOption extends ListOption {

    public BlockListOption() {
        super();
    }

    public BlockListOption(String name) {
        super(name);
    }

    public BlockListOption(String name, Block defaultValue) {
        super(name, defaultValue);
    }


    @Override
    public BlockOption getSingleOption(int index) {
        return (BlockOption)new BlockOption(name, (Block)getDefault(index)).setDescription(description).setFlag(flag);
    }

    @Override
    public List<Block> getValues() {
        return getValues(null);
    }

    public List<Block> getValues(World world) {
        List<Block> values = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            values.add(getValue(i, world));
        }
        return values;
    }

    @Override
    public Block getValue(int index) {
        return (Block)getValueOrDefault(index);
    }

    public Block getValue(int index, World world) {
        Block b = getValue(index);
        if (b == null || world == null) {
            return b;
        }
        return world.getBlockAt(b.getLocation());
    }

    @Override
    public BlockListOption clone() {
        return (BlockListOption)new BlockListOption(name, (Block)defaultValue).setDescription(description).setFlag(flag);
    }
}
