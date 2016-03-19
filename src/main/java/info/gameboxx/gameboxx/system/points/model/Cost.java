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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public abstract class Cost implements Applicable.UserApplicable {

    private static final Map<String, Cost> costRegistration = Maps.newHashMap();
    private final BigDecimal bigDecimal;

    public Cost(String id, double amount) {
        costRegistration.put(id, this);
        bigDecimal = BigDecimal.valueOf(amount);
    }

    public Cost(String id, BigDecimal bigDecimal) {
        costRegistration.put(id, this);
        this.bigDecimal = bigDecimal;
    }

    public static Cost build(String input) {
        /*PLACEHOLDER*/
        return new Cost("", 0) {
            @Override
            public boolean canAfford(User user) {
                return false;
            }

            @Override
            public void apply(User user) {

            }
        };
        /*PLACEHOLDER*/
    }

    public double getCost() {
        return bigDecimal.doubleValue();
    }

    public abstract boolean canAfford(User user);

}
