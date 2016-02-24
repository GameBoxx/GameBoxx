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

import info.gameboxx.gameboxx.util.text.TextParser;
import junit.framework.TestCase;
import org.junit.Assert;

public class TextParserTest extends TestCase {

    private static final boolean DEBUG = false;

    private void parse(String text) {
        TextParser parser = new TextParser(text);
        if (DEBUG) {
            System.out.println("=== PARSING: " + text);
            System.out.println(parser.getJSON());
            System.out.println();
        }
        Assert.assertTrue(parser.isValid());
    }

    public void testHoverMultiple() {
        parse("test [[hover1||text1]]. &a[[hover2||text2]]");
    }

    public void testHover() {
        parse("[[hover||text]]");
    }

    public void testCmd() {
        parse("<<cmd arg||text>>");
    }

    public void testCmdSuggest() {
        parse("<<!cmd arg||text>>");
    }

    public void testUrl() {
        parse("{{http://gameboxx.info||text}}");
    }

    public void testInsert() {
        parse("((insert||text))");
    }

    public void testPage() {
        parse("<<#1||text>>");
    }

    public void testHoverCmd() {
        parse("<<cmd arg||[[hover||text]]>>");
    }

    public void testHoverUrl() {
        parse("{{http://gameboxx.info||[[&7Go to the wiki!||&9&lGameBoxx]]}}");
    }

    public void testHoverInsert() {
        parse("((&ainsert text||[[&7Shift click to insert text!||&9&linsert]]))");
    }

    public void testHoverClickColored() {
        parse("<<heal||[[&eClick &7to &cheal &7yourself!||&a&lheal]]>>");
    }

    public void testColored() {
        parse("&aThis &bis &ca test &d&lwith colors&e.");
    }

    public void testColoredHover() {
        parse("[[&ahover&5&ltext||&adisplay &5&ltext]]");
    }

    public void testNewLinesHover() {
        parse("[[&aline1\nline2\n&bline3||text]]");
    }

    public void testNewLinesText() {
        parse("[[hover||&aline1\nline2\n&bline3]]");
    }

    public void testCharacters() {
        parse("[[h.!@#$%^&*(){}:|<>?/.,;'][-=_+over||text]]");
    }

    public void testLongMessage() {
        parse("[[&a&lhover!||&aThis is a very long hover message that should span multiple lines and keep the coloring.]]");
    }
}
