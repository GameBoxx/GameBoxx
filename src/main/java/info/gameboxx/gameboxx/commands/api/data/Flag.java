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

/**
 * A boolean flag which can be used in commands.
 * <p/>
 * Use {@link Cmd#addFlag(String)}   to add a flag to a command.
 */
public class Flag {

    private final String name;
    private String description = "";
    private String permission = "";

    /** Construct a new Flag. */
    public Flag(String name) {
        if (name.startsWith("-")) {
            name = name.substring(1);
        }
        this.name = name;
    }

    /**
     * Get the name of the flag.
     * <p/>
     * This name does not have the '-' prefix.
     *
     * @return The name of the flag.
     */
    public String name() {
        return name;
    }

    /**
     * Get the description of the flag.
     *
     * @return The description of the flag. (Empty string when no description)
     */
    public String desc() {
        return description;
    }

    /**
     * Set the description of the flag.
     *
     * @param description The description to set. (Empty string for no description)
     * @return flag instance
     */
    public Flag desc(String description) {
        this.description = description == null ? "" : description;
        return this;
    }

    /**
     * Get the permission node that is required to specify this flag.
     *
     * @return The permission node required to specify the flag. (Empty string when no node)
     */
    public String perm() {
        return permission;
    }

    /**
     * Set the permission node required to specify this flag.
     *
     * @param permission The permission node to set. (Empty string for no permission)
     * @return flag instance
     */
    public Flag perm(String permission) {
        this.permission = permission == null ? "" : permission;
        return this;
    }
}
