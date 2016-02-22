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
import info.gameboxx.gameboxx.options.single.IntOption;

import java.util.ArrayList;
import java.util.List;

public class IntListOption extends ListOption {

    private Integer min = null;
    private Integer max = null;

    public IntListOption() {
        super();
    }

    public IntListOption(String name) {
        super(name);
    }

    public IntListOption(String name, Integer defaultValue) {
        super(name, defaultValue);
    }


    public IntListOption min(Integer min) {
        this.min = min;
        return this;
    }

    public IntListOption max(Integer max) {
        this.max = max;
        return this;
    }


    @Override
    public IntOption getSingleOption() {
        return (IntOption) new IntOption(name, (Integer)defaultValue).min(min).max(max).setDescription(description).setFlag(flag);
    }

    @Override
    public List<Integer> getValues() {
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < value.size(); i++) {
            values.add(getValue(i));
        }
        return values;
    }

    @Override
    public Integer getValue(int index) {
        return (Integer)getValueOrDefault(index);
    }

    @Override
    public IntListOption clone() {
        return (IntListOption) new IntListOption(name, (Integer)defaultValue).min(min).max(max).setDescription(description).setFlag(flag);
    }
}
