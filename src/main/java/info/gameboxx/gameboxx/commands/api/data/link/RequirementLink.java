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

package info.gameboxx.gameboxx.commands.api.data.link;

import info.gameboxx.gameboxx.commands.api.data.ArgRequirement;

/**
 * A modification {@link Link} that will modify the {@link ArgRequirement} of an argument.
 * <p/>
 * If all the set names of the {@link Link} have been specified the argument with the specified name will get a new requirement.
 */
public class RequirementLink extends Link {

    private final String argName;
    private final ArgRequirement requirement;

    /**
     * Construct a new requirement link.
     *
     * @param argName The name of the argument to modify.
     * @param requirement The new requirement to set for the argument.
     * @param names One or more names of arguments, modifiers or flags that have to be specified.
     *              If ALL the specified names are specified in the command the argument requirement will be modified.
     */
    public RequirementLink(String argName, ArgRequirement requirement, String... names) {
        super(names);
        this.argName =argName;
        this.requirement = requirement;
    }

    /**
     * Get the name of the argument that may need to be modified.
     *
     * @return name of the argument that may need to be modified
     */
    public String argName() {
        return argName;
    }

    /**
     * Get the {@link ArgRequirement} that may need to be set.
     *
     * @return {@link ArgRequirement} that may need to be set.
     */
    public ArgRequirement requirement() {
        return requirement;
    }
}
