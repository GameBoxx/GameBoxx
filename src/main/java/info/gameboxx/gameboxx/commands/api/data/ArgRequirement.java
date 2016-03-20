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

import org.bukkit.command.CommandSender;

/**
 * The requirement for an argument.
 * <p/>
 * If an argument is optional it can be left out in the user input.
 * If not, it must be specified in the command otherwise it will produce an error message.
 * <p/>
 * Use {@link Argument#required(CommandSender)} to check if the argument is required for the specified sender.
 */
public enum ArgRequirement {
    /** Any type of sender MUST specify the argument. */
    REQUIRED,
    /** Any type of sender MAY specify the argument but it's not required. */
    OPTIONAL,
    /** The console MUST specify the argument but any other sender MAY specify it. */
    REQUIRED_CONSOLE,
    /** The player MAY specify the argument but all the other senders MUST specify it. */
    REQUIRED_NON_PLAYER
}