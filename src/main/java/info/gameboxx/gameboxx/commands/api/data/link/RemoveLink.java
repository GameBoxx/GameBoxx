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
 * A removal {@link Link} that will remove a argument, modifier or flag.
 * <p/>
 * If all the set names of the {@link Link} have been specified the specified XX with the removeName will be removed.
 * where XX can be an argument, modifier or flag.
 */
public class RemoveLink extends Link {

    private final String removeName;

    /**
     * Construct a new remove link.
     *
     * @param removeName The name of the argument, modifier or flag to remove.
     * @param names One or more names of arguments, modifiers or flags that have to be specified.
     *              If ALL the specified names are specified in the command the argument, modifier or link will be removed/ignored.
     */
    public RemoveLink(String removeName, String... names) {
        super(names);
        this.removeName = removeName;
    }

    /**
     * Get the name of the argument, modifier or flag that may need to be removed.
     *
     * @return name that may need to be removed.
     */
    public String removeName() {
        return removeName;
    }
}
