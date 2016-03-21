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
import info.gameboxx.gameboxx.commands.api.parse.CmdParser;
import info.gameboxx.gameboxx.options.SingleOption;

/**
 * An option modifier which can be used in commands.
 * </p>
 * Use {@link Cmd#addModifier(String, SingleOption)} to add a modifier to a command.
 */
public class Modifier {

    private final String name;
    private final SingleOption option;

    private String description = "";
    private String permission = "";

    /** Construct a new Modifier. */
    public Modifier(String name, SingleOption option) {
        this.name = name;
        this.option = option;
    }


    /**
     * Get the name of the modifier.
     *
     * @return The name of the modifier.
     */
    public String name() {
        return name;
    }

    /**
     * Get the description of the modifier.
     *
     * @return The description of the modifier. (Empty string when no description)
     */
    public String desc() {
        return description;
    }

    /**
     * Set the description of the modifier.
     *
     * @param description The description to set. (Empty string for no description)
     * @return modifier instance
     */
    public Modifier desc(String description) {
        this.description = description == null ? "" : description;
        return this;
    }

    /**
     * Get the permission node that is required to specify this modifier.
     *
     * @return The permission node required to specify the modifier. (Empty string when no node)
     */
    public String perm() {
        return permission;
    }

    /**
     * Set the permission node required to specify this modifier.
     *
     * @param permission The permission node to set. (Empty string for no permission)
     * @return modifier instance
     */
    public Modifier perm(String permission) {
        this.permission = permission == null ? "" : permission;
        return this;
    }

    /**
     * Get the {@link SingleOption} for this modifier.
     * <p/>
     * This option does not contain a value.
     * In the {@link CmdParser} a clone of this option will be created and then it will be parsed and added to the {@link CmdData}
     *
     * @return The {@link SingleOption} for this modifier.
     */
    public SingleOption option() {
        return option;
    }
}
