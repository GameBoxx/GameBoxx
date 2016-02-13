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

package info.gameboxx.gameboxx.options;

/**
 * Base class for an option.
 * Options can be used to parse input and validate it.
 */
public abstract class Option {

    protected String name = "";
    protected String description = "";

    protected String error = "";

    public Option() {}

    public Option(String name) {
        this.name = name;
    }

    /**
     * Check whether or not the option produced an error when parsing.
     * @return True when there is an error message and false if not.
     */
    public boolean hasError() {
        return !error.isEmpty();
    }

    /**
     * Get the error message that was produced when parsing the option.
     * Use {@link #hasError()} to check if there is an error message.
     * @return The error message.
     */
    public String getError() {
        return error;
    }

    /**
     * Get the name of the option which was specified when creating the option.
     * This will be an empty string if the option doesn't have a name.
     * @return The name of the option.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the setDescription for this option.
     * The setDescription is what's set with {@link #setDescription(String)}
     * May be an empty string if the option doesn't have a setDescription set.
     * @return The setDescription string or an empty string.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the setDescription for this option.
     * The setDescription can be retreived for display purposes with {@link #getDescription()}
     * By default nothing is done with descriptions.
     * @param description The setDescription message. (May contain colors and new lines with \n)
     * @return The option instance.
     */
    public Option setDescription(String description) {
        this.description = description;
        return this;
    }

}