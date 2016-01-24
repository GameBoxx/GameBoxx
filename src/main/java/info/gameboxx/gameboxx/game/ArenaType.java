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

package info.gameboxx.gameboxx.game;

/**
 * The type of arena.
 */
public enum ArenaType {
    /**
     * This arena type is linked to it's own world.
     * Each time a new {@link GameSession} is created it will create a copy of the linked template world.
     * This arena type can have multiple {@link GameSession} sessions.
     */
    WORLD,

    /**
     * This arena type doesn't have an arena/world attached to it.
     * Basically when a new {@link GameSession} is created it will generate a new world with configurable options.
     * This can be used for games like UHC and such that use a random generated vanilla world.
     */
    GENERATE_WORLD,

    /**
     * This arena type is the default and it won't have an world or anything attached.
     * It can only have ONE {@link GameSession} and by default nothing will regenerate.
     * You can attach a component to regenerate a cuboid in the arena but that's it.
     * This should used for smaller games with multiple games per world and such.
     * Just remember that there can only be one session per arena with this type.
     */
    DEFAULT;
}
