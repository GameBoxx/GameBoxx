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

package info.gameboxx.gameboxx.commands.api.data;

import info.gameboxx.gameboxx.commands.api.Cmd;
import info.gameboxx.gameboxx.commands.api.CmdData;

/**
 * A boolean flag which can be used in commands.
 * <p/>
 * Use {@link Cmd#addFlag(String, String, String)} to add a flag to a command.
 */
public class Flag {

    private final String name;
    private String description;
    private String permission;

    /**
     * Construct a new Flag.
     * <p/>
     * Use {@link Cmd#addFlag(String, String, String)} to add a flag to a command.
     * Which means it shouldn't be needed to use this.
     *
     * @param name The flag name/key used to identify the flag.
     *             This name must be used with the {@link CmdData} result to check if the flag was specified.
     *             This name is also what the user needs to use to specify the flag. (-{name})
     * @param description Description that describes the flag used in the command help.
     * @param permission The permission node required to specify this flag.
     */
    public Flag(String name, String description, String permission) {
        this.name = name;
        this.description = description == null ? "" : description;
        this.permission = permission == null ? "" : permission;
    }

    /**
     * Get the name of the flag.
     * <p/>
     * This name does not have the '-' prefix.
     *
     * @return The name of the flag.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description of the flag.
     *
     * @return The description of the flag. (Empty string when no description)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the flag.
     * <p/>
     * This is mainly used when the command config file is loaded to override the default value.
     *
     * @param description The description to set. (Empty string for no description)
     */
    public void setDescription(String description) {
        this.description = description == null ? "" : description;
    }

    /**
     * Get the permission node that is required to specify this flag.
     *
     * @return The permission node required to specify the flag. (Empty string when no node)
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Set the permission node required to specify this flag.
     * <p/>
     * This is mainly used when the command config file is loaded to override the default value.
     *
     * @param permission The permission node to set. (Empty string for no permission)
     */
    public void setPermission(String permission) {
        this.permission = permission == null ? "" : permission;
    }
}
