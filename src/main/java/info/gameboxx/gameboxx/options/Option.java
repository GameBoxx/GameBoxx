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
 * Base class for all the options.
 * Options are used to parse user input for a bunch of different Objects.
 */
public abstract class Option<T extends Option> {

    protected String name = "";
    protected String description = "";

    protected OptionFlag flag = OptionFlag.NONE;

    protected String error = "";


    /**
     * Get the error message that was produced when parsing the option.
     *
     * @return The error message. (Empty string when there is no error)
     */
    public String getError() {
        return error;
    }

    /**
     * Check whether or not the option has a name set with {@link #name(String)}
     *
     * @return True when the option has a name.
     */
    public boolean hasName() {
        return !name.isEmpty();
    }

    /**
     * Set the name for the option.
     *
     * @param name The name for the option.
     * @return this instance
     */
    public T name(String name) {
        this.name = name;
        return (T)this;
    }

    /**
     * Get the name of the option which was set with {@link #name(String)}
     *
     * @return The name of the option. (Empty string when no name)
     */
    public String getName() {
        return name;
    }

    /**
     * Check whether or not the option has a description set with {@link #desc(String)}
     *
     * @return True when the option has a description.
     */
    public boolean hasDescription() {
        return !description.isEmpty();
    }

    /**
     * Get the setDescription for this option which was set with {@link #desc(String)}
     *
     * @return The desription of the option. (Empty string when no description)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the setDescription for this option.
     *
     * @param description The description for the option.
     * @return this instance
     */
    public T desc(String description) {
        this.description = description;
        return (T)this;
    }

    /**
     * Check whether or not this option has a {@link OptionFlag} set.
     *
     * @return True when the option has a flag.
     */
    public boolean hasFlag() {
        return flag != null;
    }

    /**
     * Check if the option has the specified {@link OptionFlag} set.
     *
     * @param flag The flag to check for.
     * @return True when the option has the specified flag.
     */
    public boolean hasFlag(OptionFlag flag) {
        return flag == this.flag;
    }

    /**
     * Get the {@link OptionFlag} for this option.
     *
     * @return The flag set for this option with {@link #flag(OptionFlag)}
     */
    public OptionFlag getFlag() {
        return flag;
    }

    /**
     * Set the {@link OptionFlag} for this option.
     *
     * @param flag The flag to set. (Set to {@link OptionFlag#NONE} for no flag.
     * @return The option instance.
     */
    public T flag(OptionFlag flag) {
        this.flag = flag;
        return (T)this;
    }

    /**
     * Create a new instance of the option.
     * <p/>
     * The name, description, flag and modifiers will be cloned in the new option.
     *
     * @return New option instance.
     */
    public abstract T clone();
}
