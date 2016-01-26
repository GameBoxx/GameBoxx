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

package info.gameboxx.gameboxx.setup.options;

import info.gameboxx.gameboxx.exceptions.InvalidSetupDataException;
import info.gameboxx.gameboxx.setup.SetupOption;
import info.gameboxx.gameboxx.setup.SetupType;
import org.bukkit.configuration.ConfigurationSection;

public class IntOption extends SetupOption {

    private Integer value;

    public IntOption(String name, ConfigurationSection section) throws InvalidSetupDataException {
        super(name);
        if (!section.isInt(getName())) {
            throw new InvalidSetupDataException(getType(), section.getString(getName()), getName());
        }
        this.value = section.getInt(getName());
    }

    public IntOption(String name, Object value) throws InvalidSetupDataException {
        super(name);
        if (!(value instanceof Integer)) {
            throw new InvalidSetupDataException(getType(), value.toString(), getName());
        }
        this.value = (Integer)value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public SetupType getType() {
        return SetupType.INT;
    }
}
