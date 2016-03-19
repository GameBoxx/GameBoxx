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

package info.gameboxx.gameboxx.commands.api.parse;

import info.gameboxx.gameboxx.commands.api.SubCmd;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.util.Utils;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCmdO extends SingleOption<SubCmd, SubCmdO> {

    private final SubCmd[] subCmds;
    private final Map<String, List<String>> aliasMap;

    public SubCmdO(SubCmd... subCmds) {
        this.subCmds = subCmds;
        aliasMap = new HashMap<>();
        for (SubCmd sub : subCmds) {
            aliasMap.put(sub.getSubName(), sub.getAliases());
        }
    }

    public SubCmd[] getSubCmds() {
        return subCmds;
    }

    @Override
    public boolean parse(CommandSender sender, String input) {
        for (SubCmd sub : subCmds) {
            if (sub.getSubName().equalsIgnoreCase(input) || sub.getAliases().contains(input.toLowerCase())) {
                value = sub;
                return true;
            }
        }

        error = Msg.getString("subcmd.invalid", Param.P("input", input), Param.P("subcmds", Utils.getAliasesString("subcmd.entry", aliasMap)));
        return false;
    }

    @Override
    public String getTypeName() {
        return "SubCommand";
    }

    @Override
    public SubCmdO clone() {
        return new SubCmdO(subCmds);
    }
}
