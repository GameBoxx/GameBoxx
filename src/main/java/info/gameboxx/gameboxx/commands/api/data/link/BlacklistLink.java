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

import info.gameboxx.gameboxx.commands.api.Cmd;

/**
 * A blacklist {@link Link} that will change the blacklist of the command.
 * <p/>
 * If all the set names of the {@link Link} have been specified the command blacklist will be modified.
 */
public class BlacklistLink extends Link {

    private final Cmd.SenderType[] blacklist;

    /**
     * Construct a new blacklist link.
     *
     * @param blacklist Array with {@link Cmd.SenderType}s that will be blacklisted.
     * @param names One or more names of arguments, modifiers or flags that have to be specified.
     *              If ALL the specified names are specified in the command the blacklist will be modified.
     */
    public BlacklistLink(Cmd.SenderType[] blacklist, String... names) {
        super(names);
        this.blacklist = blacklist;
    }

    /**
     * Get the blacklist array with {@link Cmd.SenderType}s that may need to be set.
     *
     * @return blacklist array with {@link Cmd.SenderType}s that may need to be set.
     */
    public Cmd.SenderType[] blacklist() {
        return blacklist;
    }
}
