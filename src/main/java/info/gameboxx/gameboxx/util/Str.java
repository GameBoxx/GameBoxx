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

package info.gameboxx.gameboxx.util;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Str {

    /**
     * Integrate ChatColor in a string based on color codes.
     * @param str The string to apply color to.
     * @return formatted string
     */
    public static String color(String str) {
        for (ChatColor c : ChatColor.values()) {
            str = str.replaceAll("&" + c.getChar() + "|&" + Character.toUpperCase(c.getChar()), "§" + c.getChar());
        }
        return str;
    }

    /**
     * Integrate ChatColor in a string based on color codes.
     * @param str The string to apply color to.
     * @return formatted string
     */
    public static String colorChatColors(String str) {
        for (ChatColor c : ChatColor.values()) {
            str = str.replaceAll("&" + c.getChar() + "|&" + Character.toUpperCase(c.getChar()), c.toString());
        }
        return str;
    }

    /**
     * Remove all color and put colors as the formatting codes like &amp;1.
     * @param str The string to remove color from.
     * @return formatted string
     */
    public static String replaceColor(String str) {
        for (ChatColor c : ChatColor.values()) {
            str = str.replace(c.toString(), "&" + c.getChar());
        }
        return str;
    }

    /**
     * Strips all coloring from the specified string.
     * @param str The string to remove color from.
     * @return String without any colors and without any color symbols.
     */
    public static String stripColor(String str) {
        return ChatColor.stripColor(colorChatColors(str));
    }


    public static String implode(Object[] arr, String glue, String lastGlue, int start, int end) {
        String ret = "";

        if (arr == null || arr.length <= 0)
            return ret;

        for (int i = start; i <= end && i < arr.length; i++) {
            if (i >= end-1 || i >= arr.length-2) {
                ret += arr[i].toString() + lastGlue;
            } else {
                ret += arr[i].toString() + glue;
            }
        }

        if (ret.trim().isEmpty()) {
            return ret;
        }
        return ret.substring(0, ret.length() - lastGlue.length());
    }

    public static String implode(Object[] arr, String glue, int start) {
        return implode(arr, glue, glue, start, arr.length - 1);
    }

    public static String implode(Object[] arr, String glue, String lastGlue) {
        return implode(arr, glue, lastGlue, 0, arr.length - 1);
    }

    public static String implode(Object[] arr, String glue) {
        return implode(arr, glue, 0);
    }

    public static String implode(Collection<?> args, String glue) {
        if (args.isEmpty())
            return "";
        return implode(args.toArray(new Object[args.size()]), glue);
    }

    public static String implode(Collection<?> args, String glue, String lastGlue) {
        if (args.isEmpty())
            return "";
        return implode(args.toArray(new Object[args.size()]), glue, lastGlue);
    }


    public static List<String> splitNewLines(List<String> list) {
        if (list != null && list.size() > 0) {
            List<String> loreList = new ArrayList<String>();
            List<String> listClone = list;
            for (String string : listClone) {
                loreList.addAll(Arrays.asList(string.split("\n")));
            }
            return loreList;
        }
        return list;
    }

    /**
     * Splits the specified string in sections.
     * Strings inside quotes will be placed together in sections.
     * For example 'This plugin is "super awesome"' will return {"this", "plugin", "is", "super awesome"}
     * @param string The string that needs to be split.
     * @return List of strings split from the input string.
     */
    public static List<String> splitQuotes(String string) {
        List<String> sections = new ArrayList<String>();

        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher regexMatcher = regex.matcher(string);

        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                sections.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                sections.add(regexMatcher.group(2));
            } else {
                sections.add(regexMatcher.group());
            }
        }
        return sections;
    }

    /**
     * Capitalize the first character of a string.
     * @param str
     * @return Capitalized string
     */
    public static String capitalize(String str) {
        if (str.length() == 0) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String wrapStringExact(String string, int length) {
        return string.replaceAll("(.{" + length + "})", "$1\n");
    }

    public static String wrapString(String string, int length) {
        StringBuilder sb = new StringBuilder(string);
        int i = 0;
        while ((i = sb.indexOf(" ", i + length)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        return sb.toString();
    }
}
