/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2016 GameBoxx <http://gameboxx.info>
 *  Copyright (c) 2016 contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package info.gameboxx.gameboxx.util;


import com.google.common.collect.Lists;

import java.util.List;

public class Numbers {

    /**
     * Returns the smallest entry in the array. If there are multiple entries that are equal, it will choose a random one if 'randomIfMultiple' is true. Otherwise, the first element in the array will be chosen.
     *
     * @param ints {@link Integer} array
     * @return smallest entry in array
     */
    public static int smallest(Integer[] ints, boolean randomIfMultiple) {
        int res = ints[0];
        List<Integer> list = Lists.newArrayList();
        for (int anInt : ints) {
            if (anInt < res) {
                res = anInt;
            }
        }
        for (int anInt : ints) {
            if (anInt == res) {
                list.add(anInt);
            }
        }
        if (list.size() == 0) {
            return res;
        }
        if (randomIfMultiple) {
            return list.get(Random.Int(list.size()));
        }
        return list.get(0);
    }

    /**
     * Returns the largest entry in the array. If there are multiple entries that are equal, it will choose a random one if 'randomIfMultiple' is true. Otherwise, the first element in the array will be chosen.
     *
     * @param ints {@link Integer} array
     * @return largest entry in array
     */
    public static int largest(Integer[] ints, boolean randomIfMultiple) {
        int res = ints[0];
        List<Integer> list = Lists.newArrayList();
        for (int anInt : ints) {
            if (anInt > res) {
                res = anInt;
            }
        }
        for (int anInt : ints) {
            if (anInt == res) {
                list.add(anInt);
            }
        }
        if (list.size() == 0) {
            return res;
        }
        if (randomIfMultiple) {
            return list.get(Random.Int(list.size()));
        }
        return list.get(0);
    }
}
