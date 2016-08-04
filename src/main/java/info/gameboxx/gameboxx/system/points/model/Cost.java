/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2016 GameBoxx <http://gameboxx.info>
 *  Copyright (c) 2016 contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package info.gameboxx.gameboxx.system.points.model;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import info.gameboxx.gameboxx.user.User;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public abstract class Cost implements Applicable.UserApplicable {

    private static final Map<String, Cost> costRegistration = Maps.newHashMap();
    private BigDecimal bigDecimal;

    public Cost(String id) {
        costRegistration.put(id, this);
        bigDecimal = BigDecimal.valueOf(0);
    }

    public Cost(String id, double amount) {
        costRegistration.put(id, this);
        bigDecimal = BigDecimal.valueOf(amount);
    }

    //economy:10 points:[gems:5, dollars:10]
    public static Cost[] build(String input) {
        Validate.notNull(input);
        String[] split = input.split(" ");
        Cost[] res = new Cost[split.length];
        int i = 0;
        for (String part : input.split(" ")) {
            String[] partSplit = part.split(":", 2);
            Cost cost = costRegistration.get(partSplit[0]);
            Validate.notNull(cost);
            if (!cost.isMultiOptional()) {
                cost.setCost(Double.parseDouble(partSplit[1]));
            } else {
                if (partSplit[1].startsWith("[")) {
                    partSplit[1] = partSplit[1].substring(1);
                    String[] optionSplit = partSplit[1].split(", ");
                    MultiOptionalCost multiOptionalCost = (MultiOptionalCost) cost;
                    int j = 0;
                    for (Object object : multiOptionalCost.getOptions()) {
                        if (object.toString().toLowerCase().equals(optionSplit[j].split(":")[0])) {

                        }
                    }
                }
            }
            res[i++] = cost;
        }
        return res;
    }

    public double getCost() {
        return bigDecimal.doubleValue();
    }

    public void setCost(double cost) {
        bigDecimal = BigDecimal.valueOf(cost);
    }

    public abstract boolean canAfford(User user);

    public boolean isMultiOptional() {
        return false;
    }

}
