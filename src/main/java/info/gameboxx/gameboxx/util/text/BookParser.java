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

package info.gameboxx.gameboxx.util.text;

import info.gameboxx.gameboxx.nms.NMS;
import info.gameboxx.gameboxx.nms.item.ItemUtils;
import info.gameboxx.gameboxx.util.item.EItem;
import org.bukkit.inventory.meta.BookMeta;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Used to parse book content/pages to JSON.
 * Mostly used in {@link EItem#setContent(String...)}
 *
 * To apply the book pages as JSON use {@link NMS#getItemUtils()} {@link ItemUtils#setBookPages(BookMeta, List)}
 */
public class BookParser {

    private List<String> pages;
    private List<String> JSONPages;

    /** @see #BookParser(List) */
    public BookParser(String... strings) {
        this(Arrays.asList(strings));
    }

    /**
     * Parse the specified strings to JSON.
     * <p/>
     * If the strings are already JSON formatted it will just use the already formatted strings.
     * <p/>
     * The strings may contain custom JSON syntax including {@link TextAction}s.
     * Chat colors with &amp; symbols won't be formatted but with the § symbol will. (that's how the {@link TextParser} works)
     * Literal new line text will be replaced with the actual new line character. (\\\\n to \n)
     * <p/>
     * Each string in the list will be it's own page in the book.
     * Each individual string can have multiple pages too as each string will be split by the \p (\\\\p) text.
     * This means you could just pass a single string from user input and still have multiple pages.
     * <p/>
     * If the string has too many characters and/or too many lines it will be cut off.
     * It will still be parsed and returned but when setting it on the book it won't display it.
     * It's up to the user input to make sure that the pages fit correctly.
     *
     * @param strings The string(s) that need to be parsed.
     */
    public BookParser(List<String> strings) {
        pages = new ArrayList<>();
        for (String string : strings) {
            String[] split = string.split("\\\\p");
            for (String str : split) {
                pages.add(str.replaceAll("\\\\n", "\n"));
            }
        }

        JSONPages = new ArrayList<>();
        for (String page : pages) {
            JSONParser jp = new JSONParser();
            try {
                JSONObject result = (JSONObject)jp.parse(page);
                JSONPages.add(page);
            } catch (Exception e) {
                TextParser parser = new TextParser(page);
                JSONPages.add(parser.getJSON());
            }
        }
    }

    //TODO: Inverted parsing

    /**
     * Get the book pages as regular text without JSON formatting.
     * <p/>
     * If the input strings were already JSON formatted they will actually have JSON format.
     * <p/>
     * This list might be slightly different from the input strings.
     * These strings already have the pages split with \p.
     * So, there might be more pages compared to the input.
     *
     * @return The book pages as regular tet.
     */
    public List<String> getPages() {
        return pages;
    }

    /**
     * Get the JSON formatted book pages.
     *
     * @return The book pages with JSON formatting.
     */
    public List<String> getJSONPages() {
        return JSONPages;
    }

}
