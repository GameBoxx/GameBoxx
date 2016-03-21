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

/**
 * A link between arguments, modifiers or flags.
 *
 * @see RemoveLink
 * @see RequirementLink
 */
public abstract class Link {

    private final String[] names;

    /**
     * Construct a new link
     *
     * @param names One or more names of arguments, modifiers or flags that have to be specified.
     *              If ALL the specified names are specified in the command the link will execute.
     */
    public Link(String... names) {
        this.names = names;
    }

    /**
     * Get the array of names that have to be specified.
     * <p/>
     * These may be the names of arguments, modifiers or flags.
     *
     * @return array of names that must be specified in the command for the link to execute
     */
    public String[] names() {
        return names;
    }
}
