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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Common {@link String} utilities.
 */
public class Str {

    private static final String CLR_CHARS = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    private static final Pattern COLOR = Pattern.compile("&([" + CLR_CHARS + "])");
    private static final Pattern COLOR_REPLACE = Pattern.compile("§([" + CLR_CHARS + "])");
    private static final Pattern COLOR_STRIP = Pattern.compile("§[" + CLR_CHARS + "]|&[" + CLR_CHARS + "]");

    /**
     * Integrate ChatColor in a string based on color codes.
     * This replaces codes like &amp;a&amp;l with §a§l
     * @param str The string to apply color to.
     * @return formatted string
     */
    public static String color(String str) {
        return COLOR.matcher(str).replaceAll("§$1");
    }

    /**
     * Remove all color and put regular colors as the formatting codes like &amp;1.
     * @param str The string to remove color from.
     * @return formatted string
     */
    public static String replaceColor(String str) {
        return COLOR_REPLACE.matcher(str).replaceAll("&$1");
    }

    /**
     * Strips all coloring from the specified string.
     * For example a string like: '&amp;a&amp;ltest' becomes 'test' and '§a&ltest' becomes 'test'.
     * @param str The string to remove color from.
     * @return String without any colors and without any color codes.
     */
    public static String stripColor(String str) {
        return COLOR_STRIP.matcher(str).replaceAll("");
    }


    /**
     * Capitalize the first character of a string.
     * @param str The string that needs to be capitalized.
     * @return Capitalized string
     */
    public static String capitalize(String str) {
        if (str.length() == 0) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Formats a string with underscores to CamelCase.
     * This can be used for displaying enum keys and such.
     * @param str The string to format to camel case.
     * @return CamelCased string
     */
    public static String camelCase(String str) {
        return WordUtils.capitalizeFully(str, new char[]{'_'}).replaceAll("_", "");
    }

    /**
     * Get the best matching value for the specified input out of the array of values.
     * This uses the levenshtein distance from {@link StringUtils}
     * If an exact match is found that match will be returned.
     *
     * @param input The input string to find a match for.
     * @param values Array of values to match with input string.
     * @return The best match from the specified values. (May be empty when there are no values or no match)
     */
    public static String bestMatch(String input, String... values) {
        String bestMatch = "";
        int lowestDiff = input.length()-1;
        for (String value : values) {
            int diff = StringUtils.getLevenshteinDistance(input, value);
            if (diff == 0) {
                return value;
            }
            if (diff < lowestDiff) {
                bestMatch = value;
                lowestDiff = diff;
            }
        }
        return bestMatch;
    }

    /**
     * @see Str#bestMatch(String, String...)
     */
    public static String bestMatch(String input, Collection<? extends String> values) {
        return bestMatch(input, values.toArray(new String[values.size()]));
    }



    /**
     * Wrap the specified string to multiple lines by adding a newline symbol '\n'
     *
     * This does not break up words.
     * Which means, if there is a word that is longer than the wrap limit it will exceed the limit.
     *
     * @param string The string that needs to be wrapped.
     * @param length The maximum length for each line.
     * @return String with linebreaks.
     */
    public static String wrapString(String string, int length) {
        StringBuilder sb = new StringBuilder(string);
        int i = 0;
        while ((i = sb.indexOf(" ", i + length)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        return sb.toString();
    }

    /**
     * Wrap the specified string to multiple lines by adding a newline symbol '\n'
     *
     * The lines will always be exactly the length specified.
     * Words will be cut in half and a new line will be forced.
     * Use {@link #wrapString(String, int)} to not have this behaviour.
     *
     * @param string The string that needs to be wrapped.
     * @param length The maxmimum length for each line.
     * @return String with linebreaks.
     */
    public static String wrapStringExact(String string, int length) {
        return string.replaceAll("(.{" + length + "})", "$1\n");
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

}
