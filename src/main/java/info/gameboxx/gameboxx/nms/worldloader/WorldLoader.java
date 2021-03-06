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

package info.gameboxx.gameboxx.nms.worldloader;

import info.gameboxx.gameboxx.nms.annotation.NMSDependant;
import org.bukkit.World;
import org.bukkit.WorldCreator;

/**
 * This allows loading/creating worlds async.
 * It's not really async but it won't freeze the server.
 *
 * @author Friwi (https://www.spigotmc.org/resources/lib-asyncworldloader.7370/)
 */
@NMSDependant(implementationPath = "info.gameboxx.gameboxx.nms.worldloader")
public interface WorldLoader {

    /**
     * Creates a new world.
     *
     * @param creator The {@link WorldCreator} to use for creating the world.
     * @return The newly created {@link World}.
     */
    World createAsyncWorld(WorldCreator creator);
}
