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

import info.gameboxx.gameboxx.aliases.items.ItemData;
import info.gameboxx.gameboxx.aliases.items.Items;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.options.SingleOption;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaterialO extends SingleOption<MaterialData, MaterialO> {

    private boolean blocks = false;

    public MaterialO blocks(boolean blocks) {
        this.blocks = blocks;
        return this;
    }

    @Override
    public boolean parse(CommandSender sender, String input) {
        if (input.startsWith("@")) {
            PlayerO playerOption = new PlayerO();
            playerOption.parse(sender, input.substring(1));
            if (!playerOption.hasValue()) {
                error = playerOption.getError();
                return false;
            }
            if (playerOption.getValue().getInventory().getItemInMainHand() == null) {
                value = new MaterialData(Material.AIR);
            } else {
                value = playerOption.getValue().getInventory().getItemInMainHand().getData();
            }
            return true;
        }

        ItemData item = Items.getItem(input);
        if (item == null) {
            error = Msg.getString("material.invalid", Param.P("input", input));
            return false;
        }

        if (blocks && !item.getType().isBlock()) {
            error = Msg.getString("material.block", Param.P("input", input));
            return false;
        }

        value = new MaterialData(item.getType(), (byte)item.getData());
        return true;
    }

    @Override
    public String serialize() {
        return serialize(getValue());
    }

    public static String serialize(MaterialData material) {
        return material == null ? null : material.getItemType().toString() + ":" + material.getData();
    }

    @Override
    public String getDisplayValue() {
        return display(getValue());
    }

    public static String display(MaterialData material) {
        return material == null ? null : Msg.getString("material.display", Param.P("material", Items.getName(material)), Param.P("data", material.getData()));
    }

    @Override
    public String getTypeName() {
        return "Material";
    }

    @Override
    public List<String> onComplete(CommandSender sender, String prefix, String input) {
        List<String> suggestions = new ArrayList<>();

        if (input.length() < 2) {
            return suggestions;
        }

        for (ItemData item : Items.getItems()) {
            if (blocks && !item.getType().isBlock()) {
                continue;
            }
            String name = item.getName().replace(" ", "");
            if (name.toLowerCase().startsWith(input.toLowerCase())) {
                suggestions.add(prefix + name);
            }
        }

        Collections.sort(suggestions);
        return suggestions;
    }

    @Override
    public MaterialO clone() {
        return super.cloneData(new MaterialO().blocks(blocks));
    }
}
