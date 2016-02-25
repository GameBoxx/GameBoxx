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

package info.gameboxx.gameboxx;

import info.gameboxx.gameboxx.util.Str;
import junit.framework.TestCase;
import org.junit.Assert;

public class UtilsTest extends TestCase {

    public void testClr() {
        Assert.assertTrue(Str.color("&aexample &b&lcolor &Kmsg").equals("§aexample §b§lcolor §Kmsg"));
        Assert.assertTrue(Str.color("&aThis & &eThat").equals("§aThis & §eThat"));
        Assert.assertTrue(Str.replaceColor("§aexample §b§lcolor §Kmsg").equals("&aexample &b&lcolor &Kmsg"));
        Assert.assertTrue(Str.stripColor("&aexample &b&lcolor &Kmsg").equals("example color msg"));
        Assert.assertTrue(Str.stripColor("§aexample §b§lcolor §Kmsg").equals("example color msg"));
    }

    public void testMatch() {
        String[] fruits = new String[] {"apple","pear","banana","melon","mandarin","lime","kiwi","strawberry"};
        System.out.println("Best matches:");
        System.out.println(Str.bestMatch("ban", fruits));
        System.out.println(Str.bestMatch("kiw", fruits));
    }

}