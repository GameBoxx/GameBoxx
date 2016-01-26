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

package info.gameboxx.gameboxx.setup;

import info.gameboxx.gameboxx.exceptions.InvalidSetupDataException;
import info.gameboxx.gameboxx.setup.options.*;
import org.bukkit.configuration.ConfigurationSection;

public enum SetupType {
    /** A single location with yaw and pitch etc. */
    LOCATION,

    /** Multiple indexed locations. */
    MULTI_LOCATION,

    /** A block location with just x,y,z and world */
    BLOCK,

    /** Multiple indexed block locations */
    MULTI_BLOCK,

    /** A single cuboid which has two block locations and a world */
    CUBOID,

    /** Multiple indexed cuboids */
    MULTI_CUBOID,

    /** An {@link Integer} value (whole number) */
    INT,

    /** A {@link Double} value (decimal number) */
    DOUBLE,

    /** A {@link Float} value (decimal number) */
    FLOAT,

    /** A {@link Boolean} value (true/false yes/no v/x f/t y/n) */
    BOOLEAN,

    /** A {@link String} value (text) */
    STRING,
    ;

    public SetupOption newOption(String name, String description, ConfigurationSection cfgSection) throws InvalidSetupDataException {
        switch (this) {
            case LOCATION:
                return new LocationOption(name, description, cfgSection);
            case MULTI_LOCATION:
                return new MultiLocationOption(name, description, cfgSection);
            case BLOCK:
                return new BlockOption(name, description, cfgSection);
            case MULTI_BLOCK:
                return new MultiBlockOption(name, description, cfgSection);
            case CUBOID:
                return new CuboidOption(name, description, cfgSection);
            case MULTI_CUBOID:
                return new MultiCuboidOption(name, description, cfgSection);
            case INT:
                return new IntOption(name, description, cfgSection);
            case DOUBLE:
                return new DoubleOption(name, description, cfgSection);
            case FLOAT:
                return new FloatOption(name, description, cfgSection);
            case BOOLEAN:
                return new BooleanOption(name, description, cfgSection);
            case STRING:
                return new StringOption(name, description, cfgSection);
        }
        return new StringOption(name, description, cfgSection);
    }

    public SetupOption newOption(String name, String description, Object value) throws InvalidSetupDataException {
        switch (this) {
            case LOCATION:
                return new LocationOption(name, description, value);
            case MULTI_LOCATION:
                return new MultiLocationOption(name, description, value);
            case BLOCK:
                return new BlockOption(name, description, value);
            case MULTI_BLOCK:
                return new MultiBlockOption(name, description, value);
            case CUBOID:
                return new CuboidOption(name, description, value);
            case MULTI_CUBOID:
                return new MultiCuboidOption(name, description, value);
            case INT:
                return new IntOption(name, description, value);
            case DOUBLE:
                return new DoubleOption(name, description, value);
            case FLOAT:
                return new FloatOption(name, description, value);
            case BOOLEAN:
                return new BooleanOption(name, description, value);
            case STRING:
                return new StringOption(name, description, value);
        }
        return new StringOption(name, description, value);
    }
}
