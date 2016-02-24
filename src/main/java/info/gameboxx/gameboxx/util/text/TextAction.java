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
     * Appends the text to the chat input.
     * <b>Trigger:</b> Shift click
     * <b>Syntax:</b> ((Insert||Text))
     */
    INSERT("insertion", "((", "))"),

    /**
     * Opens the specified URL
     * <b>Trigger:</b> Click
     * <b>Syntax:</b> {{URL||text}}
     */
    URL("open_url", "{{", "}}"),

    /**
     * Switches to the specified page if it exists.
     * <b>Trigger:</b> Click
     * <b>Syntax:</b> <<#page||text>>
     */
    PAGE("change_page", "<<#", ">>"),

    /**
     * Puts the specified command in player chat. (replaces any existing content)
     * <b>Trigger:</b> Click
     * <b>Syntax:</b> <<!command||text>>
     */
    CMD_SUGGEST("suggest_command", "<<!", ">>"),

    /**
     * Runs the specified command.
     * <b>Trigger:</b> Click
     * <b>Syntax:</b> <<command||text>>
     */
    CMD("run_command", "<<", ">>"),

    /**
     * Shows a message.
     * This syntax can be used within any of the above syntaxes.
     * For example <<heal||[[Click to heal yourself!]heal]>> would display heal, on click run /heal and on hover display 'Click to heal yourself!'
     * <b>Trigger:</b> Hover
     * <b>Syntax:</b> [[message||text]]
     */
    HOVER("show_text", "[[", "]]"),
    ;


    private String name;
    private String prefix;
    private String suffix;

    TextAction(String name, String prefix, String suffix) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * The name for the text action used in the JSON string.
     * @return The internal name of the action.
     */
    public String getName() {
        return name;
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
}
