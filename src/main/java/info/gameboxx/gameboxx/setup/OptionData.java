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

package info.gameboxx.gameboxx.setup;

/**
 * Data for setup options.
 * Components use this when registering setup options.
 * The {@link info.gameboxx.gameboxx.game.Game} has a list of all the setup options registered by components.
 */
public class OptionData {

    private SetupType type;
    private String name;
    private String description;
    private Object defaultValue;

    /**
     * Create new data for a setup option.
     * @param type The {@link SetupType} option. This is used for validation and determines the user input for setting up.
     * @param name The name for the option used in the setup command and such. It should be short but descriptive like boundary, spawn, spectatorspawn, lobbyboundary, lobbyspawn etc.
     * @param description The description for the option for admins to know what each option means.
     */
    public OptionData(SetupType type, String name, String description) {
        this(type, name, description, null);
    }

    /**
     * Create new data for a setup option.
     * @param type The {@link SetupType} option. This is used for validation and determines the user input for setting up.
     * @param name The name for the option used in the setup command and such. It should be short but descriptive like boundary, spawn, spectatorspawn, lobbyboundary, lobbyspawn etc.
     * @param description The description for the option for admins to know what each option means.
     * @param defaultValue A default value for the option which will be set when first creating the arena.
     *                     Set to {@code null} to not have a default value.
     *                     If you set a default value it means users don't HAVE to set it up but they CAN to override the default.
     */
    public OptionData(SetupType type, String name, String description, Object defaultValue) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    /**
     * Get the type for the option.
     * @return The {@link SetupType} of the option.
     */
    public SetupType getType() {
        return type;
    }

    /**
     * Get the name for the option.
     * @return The name of the option for example 'boundary'.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description for the option.
     * For displaying to the user when setting up arenas.
     * @return The description of the option.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the default value for the option.
     * @return A default value or {@code null} when there is no default value.
     */
    public Object getDefaultValue() {
        return defaultValue;
    }
}
