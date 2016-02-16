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

package info.gameboxx.gameboxx.options.single;

import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.util.Parse;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VectorOption extends SingleOption {

    public VectorOption() {
        super();
    }

    public VectorOption(String name) {
        super(name);
    }

    public VectorOption(String name, Vector defaultValue) {
        super(name, defaultValue);
    }


    @Override
    public boolean parse(Object input) {
        if (!parseObject(input)) {
            return false;
        }
        if (value != null) {
            return true;
        }
        return parse((String)input);
    }

    @Override
    public boolean parse(String input) {
        return parse(null, input);
    }

    @Override
    public boolean parse(Player player, String input) {
        //Get vector from player location. @[Player name/uuid]
        if (input.startsWith("@") || input.startsWith("#")) {
            PlayerOption playerOption = new PlayerOption();
            playerOption.setDefault(player);
            playerOption.parse(player, input.substring(1));
            if (!playerOption.hasValue()) {
                error = playerOption.getError().isEmpty() ? "Invalid player to get the location vector from." : playerOption.getError();
                return false;
            }
            if (input.startsWith("#")) {
                value = playerOption.getValue().getTargetBlock(Utils.TRANSPARENT_MATERIALS, 64).getLocation().toVector();
            } else  {
                value = playerOption.getValue().getLocation().toVector();
            }
            return true;
        }

        Vector v = new Vector(0,0,0);

        //Get the components x,y,z
        String[] components = input.split(",");
        if (components.length < 3) {
            error = "Invalid vector string must have at least 1,2,3 values.";
            return false;
        }


        for (int i = 0; i < 3; i++) {
            if (Parse.Double(components[0]) == null) {
                error = "Value " + (i+1) + " for the vector isn't a decimal number.";
                return false;
            }
        }

        v.setX(Parse.Double(components[0]));
        v.setY(Parse.Double(components[1]));
        v.setZ(Parse.Double(components[2]));

        value = v;
        return true;
    }

    @Override
    public Vector getValue() {
        return (Vector)getValueOrDefault();
    }

    @Override
    public String getTypeName() {
        return "vector";
    }

    @Override
    public Class getRawClass() {
        return Vector.class;
    }

    @Override
    public VectorOption clone() {
        return new VectorOption(name, (Vector) defaultValue);
    }
}
