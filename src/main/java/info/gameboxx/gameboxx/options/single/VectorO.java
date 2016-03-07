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

import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.util.Numbers;
import info.gameboxx.gameboxx.util.Parse;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class VectorO extends SingleOption<Vector, VectorO> {

    @Override
    public boolean parse(Player player, String input) {
        //Get vector from player location. @[Player name/uuid]
        if (input.startsWith("@") || input.startsWith("#") || input.startsWith("^")) {
            PlayerO playerOption = new PlayerO();
            playerOption.def(player);
            playerOption.parse(player, input.substring(1));
            if (!playerOption.hasValue()) {
                error = playerOption.getError();
                return false;
            }
            if (input.startsWith("#")) {
                Block target = playerOption.getValue().getTargetBlock(Utils.TRANSPARENT_MATERIALS, 128);
                if (target == null) {
                    error = Msg.getString("block.no-target");
                }
                value = target.getLocation().toVector();
            } else if (input.startsWith("^")) {
                List<Block> blocks = playerOption.getValue().getLastTwoTargetBlocks(Utils.TRANSPARENT_MATERIALS, 128);
                Block block = blocks.get(1);
                if (block.getType() == Material.AIR) {
                    error = Msg.getString("block.no-target");
                    return false;
                }
                value = block.getRelative(blocks.get(1).getFace(blocks.get(0))).getLocation().toVector();
            } else {
                value = playerOption.getValue().getLocation().toVector();
            }
            return true;
        }

        Vector v = new Vector(0, 0, 0);

        //Get the components x,y,z
        String[] components = input.split(",");
        if (components.length < 3) {
            error = Msg.getString("vector.invalid", Param.P("input", input));
            return false;
        }


        String[] axisKeys = new String[] {"x", "y", "z"};
        for (int i = 0; i < axisKeys.length; i++) {
            if (Parse.Double(components[0]) == null) {
                error = Msg.getString("axis-invalid-double", Param.P("input", value), Param.P("axis", axisKeys[i]));
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
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(Vector vector) {
        return vector == null ? null : Msg.getString("vector.display", Param.P("x", vector.getX()), Param.P("y", vector.getY()), Param.P("z", vector.getZ()));
    }

    public String getDisplayValue(int roundDecimals) {
        return display(getValue(), roundDecimals);
    }

    public static String display(Vector vector, int roundDecimals) {
        return vector == null ? null : Msg.getString("vector.display", Param.P("x", Numbers.round(vector.getX(), roundDecimals)), Param.P("y", Numbers.round(vector.getY(), roundDecimals)),
                Param.P("z", Numbers.round(vector.getZ(), roundDecimals)));
    }

    @Override
    public VectorO clone() {
        return super.cloneData(new VectorO());
    }
}
