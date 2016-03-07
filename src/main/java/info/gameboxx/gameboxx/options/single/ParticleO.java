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
import info.gameboxx.gameboxx.util.Parse;
import info.gameboxx.gameboxx.util.ParticleEffect;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class ParticleO extends SingleOption<ParticleEffect, ParticleO> {

    @Override
    public boolean parse(Player player, String input) {
        String[] split = input.split(":");

        Integer amount = 1;
        Double speed = 1d;
        Double offsetX = 0d;
        Double offsetY = 0d;
        Double offsetZ = 0d;
        Object data = null;

        //TODO: Get type from alias and display aliases.
        Particle particle = Particle.valueOf(split[0].toUpperCase());
        if (particle == null) {
            Msg.getString("particle.invalid-type", Param.P("input", split[0]), Param.P("types", Parse.Array(Particle.values())));
            return false;
        }

        if (split.length > 1) {
            split = split[1].split(",");

            if (split.length > 0 && !split[0].isEmpty()) {
                amount = Parse.Int(split[0]);
                if (amount == null) {
                    Msg.getString("particle.invalid-amount", Param.P("input", split[0]));
                    return false;
                }
            }

            if (split.length > 1 && !split[1].isEmpty()) {
                speed = Parse.Double(split[1]);
                if (speed == null) {
                    Msg.getString("particle.invalid-speed", Param.P("input", split[1]));
                    return false;
                }
            }

            if (split.length > 2 && !split[2].isEmpty()) {
                offsetX = Parse.Double(split[2]);
                if (offsetX == null) {
                    Msg.getString("particle.invalid-offset", Param.P("input", split[2]), Param.P("axis", "x"));
                    return false;
                }
            }

            if (split.length > 3 && !split[3].isEmpty()) {
                offsetY = Parse.Double(split[3]);
                if (offsetY == null) {
                    Msg.getString("particle.invalid-offset", Param.P("input", split[3]), Param.P("axis", "y"));
                    return false;
                }
            }

            if (split.length > 4 && !split[4].isEmpty()) {
                offsetZ = Parse.Double(split[4]);
                if (offsetZ == null) {
                    Msg.getString("particle.invalid-offset", Param.P("input", split[4]), Param.P("axis", "z"));
                    return false;
                }
            }

            if (split.length > 5 && !split[5].isEmpty()) {
                if (particle.getDataType().equals(MaterialData.class)) {
                    MaterialO materialOption = new MaterialO();
                    if (!materialOption.parse(split[5])) {
                        error = materialOption.getError();
                        return false;
                    }
                    data = materialOption.getValue();
                } else if (particle.getDataType().equals(ItemStack.class)) {
                    ItemO itemOption = new ItemO();
                    if (!itemOption.parse(split[5])) {
                        error = itemOption.getError();
                        return false;
                    }
                    data = (ItemStack)itemOption.getValue();
                }
                if (data == null) {
                    error = Msg.getString("particle.invalid-data", Param.P("input", split[5]));
                    return false;
                }
            }
        }

        value = new ParticleEffect(particle, amount, offsetX, offsetY, offsetZ, speed, data);
        return true;
    }

    @Override
    public String serialize() {
        return serialize(getValue());
    }

    public static String serialize(ParticleEffect particle) {
        String data = "";
        if (particle.hasData()) {
            if (particle.getData() instanceof MaterialData) {
                data = "," + MaterialO.serialize((MaterialData)particle.getData());
            } else if (particle.getData() instanceof ItemStack) {
                data = "," + MaterialO.serialize(((ItemStack)particle.getData()).getData());
            }
        }
        return particle == null ? null : particle.getParticle().toString() + ":" + particle.getAmount() + "," + particle.getSpeed() + "," +
                particle.getOffsetX() + "," + particle.getOffsetY() + "," + particle.getOffsetZ() + data;
    }

    @Override
    public ParticleO clone() {
        return super.cloneData(new ParticleO());
    }
}
