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

/**
 * Represents an action for JSON text messages.
 */
public enum TextAction {
    /**
     * No action but just coloring/formatting and such.
     */
    NONE("none", "", ""),

    /**
     * Shows a message.
     * <b>Trigger:</b> Hover
     * <b>Placement:</b> tellraw, book
     * <b>Syntax:</b> [[message||text]]
     */
    HOVER("show_text", "[[", "]]", TextPlacement.TEXT, TextPlacement.BOOK),

    /**
     * Appends the text to the chat input.
     * <b>Trigger:</b> Shift click
     * <b>Features:</b> tellraw
     * <b>Syntax:</b> ((Insert||Text))
     */
    INSERT("insertion", "((", "))", TextPlacement.TEXT),

    /**
     * Opens the specified URL
     * <b>Trigger:</b> Click
     * <b>Features:</b> tellraw, book
     * <b>Syntax:</b> {{URL||text}}
     */
    URL("open_url", "{{", "}}", TextPlacement.TEXT, TextPlacement.BOOK),

    /**
     * Runs the specified command.
     * <b>Trigger:</b> Click
     * <b>Features:</b> tellraw, book, sign
     * <b>Syntax:</b> <<command||text>>
     */
    CMD("run_command", "<<", ">>", TextPlacement.TEXT, TextPlacement.DISPLAY_TEXT, TextPlacement.SIGN),

    /**
     * Puts the specified command in player chat. (replaces any existing content)
     * <b>Trigger:</b> Click
     * <b>Features:</b> tellraw
     * <b>Syntax:</b> <<<command||text>>>
     */
    CMD_SUGGEST("suggest_cmd", "<<<", ">>>", TextPlacement.TEXT),

    /**
     * Switches to the specified page if it exists.
     * <b>Trigger:</b> Click
     * <b>Features:</b> book
     * <b>Syntax:</b> <<#page||text>>
     */
    PAGE("change_page", "<<#", ">>", TextPlacement.BOOK);


    private String name;
    private String prefix;
    private String suffix;
    private boolean mixedPrefix = false;
    private int prefixCount;
    private TextPlacement[] placement;

    TextAction(String name, String prefix, String suffix, TextPlacement... placement) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.placement = placement;

        char[] chars = prefix.toCharArray();
        prefixCount = chars.length;
        char c = chars[0];
        for (char ch : chars) {
            if (ch != c) {
                mixedPrefix = true;
                return;
            }
        }
    }

    /**
     * The name for the text action used in the JSON string.
     * @return The internal name of the action.
     */
    public String getName() {
        return getName();
    }

    /**
     * The prefix for the custom message syntax.
     * @return Prefix for syntax.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * The suffix for the custom message syntax.
     * @return Suffix for syntax.
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * The amount of characters the prefix has.
     * @return The amount of characters the prefix has.
     */
    public int getPrefixCount() {
        return prefixCount;
    }

    /**
     * Check if the prefix consists of mixed characters.
     * Actions with mixed characters have custom implementations.
     * @return Whether or not the prefix consists of mixed characters.
     */
    public boolean isMixed() {
        return mixedPrefix;
    }

    /**
     * Get an array of {@link TextPlacement} where this action can be used.
     * Not all actions can be used everywhere.
     * @return Array with {@link TextPlacement} values.
     */
    public TextPlacement[] getPlacement() {
        return placement;
    }
}
