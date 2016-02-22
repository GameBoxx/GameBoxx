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
import info.gameboxx.gameboxx.util.alias.ItemData;
import info.gameboxx.gameboxx.util.alias.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

public class MaterialOption extends SingleOption {

    public MaterialOption() {
        super();
    }

    public MaterialOption(String name) {
        super(name);
    }

    public MaterialOption(String name, MaterialData defaultValue) {
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
        if (input.startsWith("@")) {
            PlayerOption playerOption = new PlayerOption();
            playerOption.setDefault(player);
            playerOption.parse(player, input.substring(1));
            if (!playerOption.hasValue()) {
                error = playerOption.getError().isEmpty() ? "Invalid player to get the materialdata from." : playerOption.getError();
                return false;
            }
            if (playerOption.getValue().getItemInHand() == null) {
                value = new MaterialData(Material.AIR);
            } else {
                value = playerOption.getValue().getItemInHand().getData();
            }
            return true;
        }

        ItemData item = Items.getItem(input);
        if (item == null) {
            error = "No item found with the specified input.";
            return false;
        }
        value = new MaterialData(item.getType(), (byte)item.getData());
        return true;
    }

    @Override
    public MaterialData getValue() {
        return (MaterialData)getValueOrDefault();
    }

    @Override
    public String serialize() {
        MaterialData value = getValue();
        if (value == null) {
            return null;
        }
        return value.getItemType().toString() + ":" + value.getData();
    }

    @Override
    public String getDisplayValue() {
        MaterialData value = getValue();
        if (value == null) {
            return null;
        }
        return Items.getName(value);
    }

    @Override
    public String getTypeName() {
        return "materialdata";
    }

    @Override
    public Class getRawClass() {
        return MaterialData.class;
    }

    @Override
    public MaterialOption clone() {
        return (MaterialOption) new MaterialOption(name, (MaterialData)defaultValue).setDescription(description).setFlag(flag);
    }
}
