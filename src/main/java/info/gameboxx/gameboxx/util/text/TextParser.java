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

import info.gameboxx.gameboxx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser to format {@link TextAction} syntax to JSON text.
 * Supports hover text, insertion, commands, command suggestions, opening urls and changing book pages.
 * The output JSON can be used for sending messages, titles, action bars, signs, book and more.
 * <p/>
 * It even supports inheritance for hover messages.
 * For example you could do <<heal||[[Click to heal yourself!|heal]]>>
 * <p/>
 * New lines with \n in messages are preserved and work as expected.
 * All coloring is supported including formatting. {@link Format}
 * <p/>
 * See {@link TextAction} for the format for each action.
 */
public class TextParser {

    private String text;
    private String json;

    /**
     * Parse a text message to JSON.
     * After parsing you can use {@link #getJSON()} to get the formatted JSON message.
     * <p/>
     * This will also parse regular text to JSON like {\"text\": \"{text}\"}
     * If you don't want that you should use {@link TextParser#hasSyntax(String)} before constructing this parser.
     *
     * @param text The text that needs to be parsed.
     */
    public TextParser(String text) {
        this.text = text;

        //StringBuilder JSON = new StringBuilder();
        Map<Integer, Pair<Integer, String>> parsedSections = new HashMap<>();

        //Parse all the actions by replacing the syntax regex match.
        for (TextAction action : TextAction.values()) {

            //Get the syntax from action out of the text string.
            Pattern p = Pattern.compile(action.getRegex(), Pattern.DOTALL);
            Matcher m = p.matcher(text);

            //All the matches found.
            int lastMatchIndex = 0;
            while (m.find(lastMatchIndex)) {
                //Get the proper start/end position of the syntax.
                int start = m.start(2) - action.getPrefix().length();
                int end = m.end(3) + 2;
                lastMatchIndex = end;

                //This shouldn't happen but just to be safe.
                if (m.group(1).length() < 2 || m.group(4).length() < 2) {
                    continue;
                }

                //Skip matches that are already parsed. (for example inherited hover messages and such)
                boolean match = false;
                for (Map.Entry<Integer, Pair<Integer, String>> entry : parsedSections.entrySet()) {
                    if (start >= entry.getKey() && end <= entry.getValue().first) {
                        match = true;
                        break;
                    }
                }
                if (match) {
                    continue;
                }

                parsedSections.put(start, Pair.P(end, "{" + getJsonComponent(action, m.group(2), m.group(3)) + "}"));
            }
        }

        //Add regular text in between parsed sections.
        Map<Integer, Pair<Integer, String>> sections = new TreeMap<>(parsedSections);
        int lastIndex = 0;
        for (Map.Entry<Integer, Pair<Integer, String>> entry : sections.entrySet()) {
            if ((lastIndex == 0 && entry.getKey() > 0) || lastIndex < entry.getKey()) {
                String str = text.substring(lastIndex, entry.getKey());
                parsedSections.put(lastIndex, Pair.P(entry.getKey(), "{" + getJsonText(str) + "}"));
            }
            lastIndex = entry.getValue().first;
        }

        //Get remaining text on the end
        if (lastIndex < text.length()) {
            String endText = text.substring(lastIndex, text.length());
            parsedSections.put(lastIndex, Pair.P(text.length(), "{" + getJsonText(endText) + "}"));
        }

        json = "[";
        sections = new TreeMap<>(parsedSections);
        for (Pair<Integer, String> pair : sections.values()) {
            json += pair.second + ",";
        }
        json = json.substring(0,json.length()-1) + "]";
    }

    /**
     * Used to get JSON formatted text.
     * <b>This does not parse syntax of {@link TextAction}!</b>
     * <p/>
     * All this does is convert chat colors like §a and §l etc to JSON syntax.
     * For example a string like '§atest §b§lexample' would output in the following JSON:
     * "text":"","extra":[{"text":"test", "color":"green"},{"text":"example","color":"aqua","bold":true}]
     * <p/>
     * <b>Important: </b> As you can see in the example it does not add '{' and '}' before and after the JSON.
     * Manually add this to get valid JSON!
     *
     * @param text The text that needs to be converted to JSON.
     * @return JSON text with tellraw format. (No { in front and no } on the end!)
     */
    public static String getJsonText(String text) {
        //When the text doesn't have color codes we just return the text as JSON but without any formatting.
        if (!text.contains("§")) {
            return "\"text\":\"" + JSONValue.escape(text) + "\"";
        }

        //Find all color codes within the string.
        //For example '§a' or '§a§l' '§c§l§n' will mitch.
        Pattern p = Pattern.compile("(§[\\da-fA-Fk-oK-OrR])+");
        Matcher m = p.matcher(text);

        StringBuilder JSON = new StringBuilder("\"text\":\"\",\"extra\":[");
        int lastIndex = 0;
        String lastMatch = "";
        while (m.find()) {
            //Create a text section for each set of color codes.
            //Using the #getJsonFormat method to get the formatting for the colors in the current match.
            //We ignore the first match because we want to color the matches with the previous color.
            if (m.start() > lastIndex) {
                JSON.append("{\"text\":\"" + JSONValue.escape(text.substring(lastIndex, m.start())) + "\"" + getJsonFormat(lastMatch) + "}");
                JSON.append(",");
            }
            lastIndex = m.end();
            lastMatch = m.group();
        }

        //Color the last match result if we haven't done it. (Will only be the case when there is one match)
        if (lastIndex < text.length()) {
            JSON.append("{\"text\": \"" + JSONValue.escape(text.substring(lastIndex, text.length())) + "\"" + getJsonFormat(lastMatch) + "}");
        }

        //Remove trailing comma if it's there
        if (JSON.lastIndexOf(",") == JSON.length() - 1) {
            JSON.setLength(JSON.length() - 1);
        }

        //Finalize
        JSON.append("]");
        return JSON.toString();
    }

    /**
     * Used for {@link #getJsonText(String)} to get JSON values for color codes.
     * It supports all {@link Format} colors and other formatting codes.
     * <p/>
     * For example: if you provide '§a§l' this would return '"color":"green","bold":true'
     *
     * @param color String with color codes only. Like '§c§l§n' (red bold strikethrough)
     * @return String with format for the specified color codes or an empty string if there are no color codes. (see example)
     */
    private static String getJsonFormat(String color) {
        if (color.trim().isEmpty()) {
            return "";
        }
        //Get string with color characters only like '§a§l' becomes 'al'
        String colorChars = color.replace("§", "").toLowerCase();

        //Go through all the color codes in the color string and get the format/color string.
        StringBuilder JSON = new StringBuilder(",");
        String clrString = "";
        for (char c : colorChars.toCharArray()) {
            Format format = Format.getByChar(c);
            if (format != null) {
                String name = format.toString().toLowerCase();
                if (format.isFormat()) {
                    JSON.append("\"" + name + "\":true,");
                } else {
                    clrString = name;
                }
            }
        }
        //Set the color if there was a color code.
        //We manually set this as last because there can only be one color and the last color in the color string should be used.
        //Like '§a§l§c' would be red and not green.
        if (!clrString.isEmpty()) {
            JSON.append("\"color\":\"" + clrString + "\"");
        }

        //Remove trailing comma if it's there
        if (JSON.lastIndexOf(",") == JSON.length() - 1) {
            JSON.setLength(JSON.length() - 1);
        }

        //Finalize
        return JSON.toString();
    }

    private String getJsonComponent(TextAction action, String value, String display) {
        if (action == TextAction.HOVER) {
            //Just a hover text message.
            return getJsonText(display) + ",\"hoverEvent\": {\"action\": \"show_text\",\"value\": {" + getJsonText(value) + "}}";
        } else {
            //Check for inherited hover message like <<cmd||[[hover||text]]>> it will then display text, on hover display hover and on click run the command cmd.
            Pattern p = Pattern.compile(TextAction.HOVER.getRegex(), Pattern.DOTALL);
            Matcher m = p.matcher(display);

            //When there is hover text change the display text to use the value from hover and set the hover text component.
            String hover = "";
            if (m.find()) {
                hover = ",\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"\",\"extra\": [{" + getJsonText(m.group(2)) + "}]}}";
                display = m.group(3);
            }

            //Insertion doesn't use clickEvent
            if (action == TextAction.INSERT) {
                //We set obfuscated to false to fix the bug where it doesn't work. Bug: [MC-82425]
                return getJsonText(display) + ",\"obfuscated\":false,\"insertion\": \"" + JSONValue.escape(value) + "\"" + hover;
            }

            //Append slash for commands if it's not there.
            if (action == TextAction.CMD || action == TextAction.CMD_SUGGEST && !value.trim().startsWith("/")) {
                value = "/" + value.trim();
            }

            //Return the JSON for general click events.
            return getJsonText(display) + ",\"clickEvent\": {\"action\": \"" + JSONValue.escape(action.getName()) + "\",\"value\": \"" + value + "\"}" + hover;
        }
    }

    /**
     * Check wether or not the provided text message contains custom syntax.
     * Use this if you don't want to parse to JSON if there is no custom syntax.
     * By default it will still parse regular text to JSON.
     * It will create JSON like {\"text\": \"Your input text\"}
     *
     * @param text The text to check for syntax.
     * @return Whether or not the specified text contains custom JSON syntax.
     */
    public static boolean hasSyntax(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        for (TextAction ta : TextAction.values()) {
            if (text.matches(ta.getRegex())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the regular text message that was given to the parser.
     *
     * @return Regular text message which is the same as the input of {@link #TextParser(String)}
     */
    public String getText() {
        return text;
    }

    /**
     * Get the JSON string that has been generated from the parsed input text.
     *
     * @return JSON string with /tellraw format.
     */
    public String getJSON() {
        return json;
    }

    /**
     * Get the JSON that has been generated from the parsed input text as a {@link JSONArray}.
     *
     * @return {@link JSONArray} with /tellraw format. (May return {@code null} when the JSON is invalid!)
     */
    public JSONArray getJSONArray() {
        JSONParser jp = new JSONParser();
        try {
            return (JSONArray)jp.parse(getJSON());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Check if the JSON is valid.
     * It uses the {@link JSONParser} and checks if it's a {@link JSONArray}
     *
     * @return True when the JSON is valid and false if not.
     */
    public boolean isValid() {
        return getJSONArray() != null;
    }

    /**
     * Formatting codes that are converted to JSON.
     */
    public enum Format {
        BLACK('0'),
        DARK_BLUE('1'),
        DARK_GREEN('2'),
        DARK_AQUA('3'),
        DARK_RED('4'),
        DARK_PURPLE('5'),
        GOLD('6'),
        GRAY('7'),
        DARK_GRAY('8'),
        BLUE('9'),
        GREEN('a'),
        AQUA('b'),
        RED('c'),
        LIGHT_PURPLE('d'),
        YELLOW('e'),
        WHITE('f'),
        OBFUSCATED('k', true),
        BOLD('l', true),
        STRIKETHROUGH('m', true),
        UNDERLINED('n', true),
        ITALIC('o', true);


        private static final Map<Character, Format> BY_CHAR = new HashMap<>();

        static {
            for (Format format : values()) {
                BY_CHAR.put(Character.valueOf(format.getChar()), format);
            }
        }

        private char c;
        private boolean format;

        Format(char c) {
            this.c = c;
            this.format = false;
        }

        Format(char c, boolean format) {
            this.c = c;
            this.format = format;
        }

        /**
         * Get the character for the format which is used together with '§' like 'a'.
         *
         * @return The character for the format.
         */
        public char getChar() {
            return c;
        }

        /**
         * Checks whether or not the format is a formatting format or not.
         * If not it's a coloring format. 0-9 and a-f are color formats and k-o are formatting formats.
         *
         * @return True when it's a formatting format and false if it's a coloring format.
         */
        public boolean isFormat() {
            return format;
        }

        /**
         * Get a {@link Format} by character.
         * Whe no format is found for the character this will be null.
         *
         * @param c The character to get the format from.
         * @return The {@link Format} for the specified character or {@code null} when there is no format for the specified character.
         */
        public static Format getByChar(char c) {
            return BY_CHAR.get(Character.valueOf(c));
        }
    }
}
