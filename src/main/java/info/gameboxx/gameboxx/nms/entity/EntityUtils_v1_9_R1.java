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

package info.gameboxx.gameboxx.nms.entity;

import net.minecraft.server.v1_9_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class EntityUtils_V1_9_R1 implements EntityUtils {

    public void setTag(Entity entity, EntityUtils.Tag tagName, boolean state) {
        net.minecraft.server.v1_9_R1.Entity nmsEntity = ((CraftEntity)entity).getHandle();
        NBTTagCompound nbt = new NBTTagCompound();
        nmsEntity.e(nbt);
        nbt.setBoolean(tagName.getName(), state);
        nmsEntity.f(nbt);
    }

    public boolean getTag(Entity entity, EntityUtils.Tag tagName) {
        net.minecraft.server.v1_9_R1.Entity nmsEntity = ((CraftEntity)entity).getHandle();
        NBTTagCompound nbt = new NBTTagCompound();
        nmsEntity.e(nbt);
        return nbt.getBoolean(tagName.getName());
    }

}
