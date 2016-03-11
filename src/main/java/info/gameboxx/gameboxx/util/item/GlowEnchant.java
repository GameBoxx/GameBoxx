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

package info.gameboxx.gameboxx.util.item;

import info.gameboxx.gameboxx.util.Enchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Map;

public class GlowEnchant extends EnchantmentWrapper {

    private static Enchantment glow;

    public GlowEnchant() {
        super(321);
    }

    public static void unregister() {
        try {
            Field byId = Enchantment.class.getDeclaredField("byId");
            byId.setAccessible(true);
            ((Map<Integer, Enchant>)byId.get(null)).remove(glow.getId());

            Field byName = Enchantment.class.getDeclaredField("byName");
            byName.setAccessible(true);
            ((Map<String, Enchant>)byName.get(null)).remove(glow.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        glow = null;
    }

    public static Enchantment get() {
        if (glow != null) {
            return glow;
        }

        if (!isAcceptingRegistrations()) {
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        glow = new GlowEnchant();
        Enchantment.registerEnchantment(glow);
        return glow;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getName() {
        return "GLOW";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }
}
