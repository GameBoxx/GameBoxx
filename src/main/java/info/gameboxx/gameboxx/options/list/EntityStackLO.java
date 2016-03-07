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

package info.gameboxx.gameboxx.options.list;

import info.gameboxx.gameboxx.options.ListOption;
import info.gameboxx.gameboxx.options.single.EntityStackO;
import info.gameboxx.gameboxx.util.entity.EntityStack;

public class EntityStackLO extends ListOption<EntityStack, EntityStackLO, EntityStackO> {

    private Integer maxEntities = null;
    private boolean allowStacked = true;

    public EntityStackLO max(Integer maxEntities) {
        this.maxEntities = maxEntities;
        return this;
    }

    public EntityStackLO allowStacked(boolean allowStacked) {
        this.allowStacked = allowStacked;
        return this;
    }

    @Override
    public EntityStackO getSingleOption() {
        return new EntityStackO().max(maxEntities).allowStacked(allowStacked);
    }

    @Override
    public EntityStackLO clone() {
        return new EntityStackLO().max(maxEntities).allowStacked(allowStacked);
    }
}
