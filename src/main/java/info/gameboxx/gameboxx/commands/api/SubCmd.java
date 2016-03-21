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

package info.gameboxx.gameboxx.commands.api;

import info.gameboxx.gameboxx.commands.api.data.ArgRequirement;
import info.gameboxx.gameboxx.commands.api.parse.SubCmdO;
import info.gameboxx.gameboxx.options.SingleOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Sub custom command.
 * <p/>
 * Extend this class to create sub commands just like regular {@link BaseCmd}s.
 * <p/>
 * Sub commands don't have to be registered with the {@link CmdRegistration}
 * <p/>
 * To use sub commands you add a regular argument with {@link #addArgument(String, ArgRequirement, SingleOption)}
 * The option of the argument must be a {@link SubCmdO}
 * In the constructor of the {@link SubCmdO} you create instances of all the sub commands.
 * <p/>
 * Check out the wiki for full details about creating custom commands and to see some example commands.
 * <p/>
 * After calling super in your constructor you can add arguments and such just like {@link BaseCmd}s.
 * {@link #addArgument(String, ArgRequirement, SingleOption)}
 * {@link #addModifier(String, SingleOption)}
 * {@link #addFlag(String)}
 *
 * @param <PARENT> The parent {@link Cmd} where this sub command was created.
 */
public abstract class SubCmd<PARENT extends BaseCmd> extends Cmd {

    private PARENT parent;
    private String subName;
    private List<String> aliases = new ArrayList<>();

    /**
     * Construct a new command with the given name and aliases.
     *
     * @param parent The parent {@link BaseCmd} where this sub command was created.
     * @param name The name/label of the command.
     * @param aliases Aliases for the command.
     */
    public SubCmd(PARENT parent, String name, String... aliases) {
        super(parent.getName() + " " + name, new String[0]);
        this.parent = parent;
        this.subName = name;
        this.aliases.addAll(Arrays.asList(aliases));
    }


    /**
     * Get the parent {@link BaseCmd} that registered this sub command as argument.
     *
     * @return The parent {@link BaseCmd}
     */
    public PARENT getParent() {
        return parent;
    }


    /**
     * Get the name of the sub command.
     * <p/>
     * The {@link #getName()} will be the full name including the parent name.
     * This sub name is the name specified in the constructor.
     *
     * @return The sub command name.
     */
    public String getSubName() {
        return subName;
    }

    /**
     * Set the name of the sub command.
     * <p/>
     * This will override the name specified in the constructor and the one from the config.
     *
     * @param subName The new sub command name.
     */
    public void setSubName(String subName) {
        this.subName = subName;
        setName(parent.getName() + " " + subName);
    }

    /**
     * Get the aliases of the sub command.
     * <p/>
     * The {@link #getAliases()} will return an empty list for sub commands.
     * This actually returns the list of aliases that have been set in the constructor/configured.
     *
     * @return The list of sub command aliases.
     */
    public List<String> getSubAliases() {
        return aliases;
    }

    /**
     * Set the aliases of the sub command.
     * <p/>
     * This will override the aliases specified in the constructor and the ones from the config.
     *
     * @param aliases New list of aliases to set
     */
    public void setSubAliases(List<String> aliases) {
        this.aliases = aliases;
    }

}
