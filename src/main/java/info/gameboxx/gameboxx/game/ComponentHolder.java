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

import info.gameboxx.gameboxx.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * A holder for game components.
 */
public class ComponentHolder {

    private Map<Class<? extends GameComponent>, GameComponent> components = new HashMap<Class<? extends GameComponent>, GameComponent>();

    /**
     * Adds a new component to the game.
     * If the game already had a component of the provided type it will return the previous component.
     * It won't overwrite the component.
     * @param component The component instance to add.
     * @return The added component or the previous component if it already had a component of that type.
     */
    public GameComponent addComponent(GameComponent component) {
        return components.put(component.getClass(), component);
    }

    /**
     * Gets a component instance that has been added to the game.
     * Will return null if the game doesn't have the provided component type.
     * @param <T> The type of component to get.
     * @return A GameComponent or {@code null} if the game has no component of the specified type.
     */
    public <T extends GameComponent> T getComponent(Class<T> component) {
        return Utils.<T>convertInstance(components.get(component), component);
    }

    /**
     * Check if the game has a component of the specified type.
     * @param component The component type to check for.
     * @return True when the game has a component of the specified type.
     */
    public boolean hasComponent(Class<? extends GameComponent> component) {
        return components.containsKey(component);
    }

    /**
     * Get a map with all the components added to the game.
     * @return Map with all components where the key is the Class type and the value is the added instance.
     */
    public Map<Class<? extends GameComponent>, GameComponent> getComponents() {
        return components;
    }
}
