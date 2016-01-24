package info.gameboxx.gameboxx.game;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * A game component.
 * The game component can have have many child components and it has one parent component.
 * Except for the top level component which is the {@link Game} in this Game API which has no parent component.
 */
//TODO: Add conflicts between components so that you can't add componentX when it has componentY
//TODO: Implement dependencies and soft dependencies.
//TODO: Keep a reference of dependencies so they can be accessed easily and quickly.
public abstract class GameComponent {

    private GameBoxx gb;
    private GameComponent parent;
    private Map<Class<? extends GameComponent>, GameComponent> components = new HashMap<Class<? extends GameComponent>, GameComponent>();

    /**
     * Instantiate a new game component for the specified parent.
     * It will not automatically add the component to the provided parent.
     * <b>Example usage:</b>
     * addComponent(new SubComponent(this));
     * @param parent The parent component.
     *               This may be null for the top level component.
     */
    public GameComponent(GameComponent parent) {
        this.parent = parent;
        gb = GameBoxx.get();
    }

    /**
     * Get the {@link GameBoxx} instance.
     * @return GameBoxx instance.
     */
    public GameBoxx getAPI() {
        return gb;
    }

    /**
     * Check whether or not this component has a parent component.
     * @return True when the component has a parent.
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Get the parent component instance.
     * @return The parent component.
     */
    public GameComponent getParent() {
        return parent;
    }

    /**
     * Adds a new component to this component.
     * If this component already had a component of the provided type it will return the previous component.
     * It won't overwrite the component.
     * @param component The component instance to add.
     * @return The added component or the previous component if it already had a component of that type.
     */
    public GameComponent addComponent(GameComponent component) {
        return components.put(component.getClass(), component);
    }

    /**
     * Gets a child component from this component.
     * Will return null if this component doesn't have the provided component type.
     * @param <T> The type of component to get.
     * @return A GameComponent or {@code null} if this component has no component of the specified type.
     */
    public <T extends GameComponent> T getComponent(Class<T> component) {
        return Utils.<T>convertInstance(components.get(component), component);
    }

    /**
     * Check if this component has a child component of the specified type.
     * @param component The component type to check for.
     * @return True when this component has a child of the specified component type.
     */
    public boolean hasComponent(Class<? extends GameComponent> component) {
        return components.containsKey(component);
    }

    /**
     * Get a map with all the child components of this component.
     * @return Map with all components where the key is the Class type and the value is the instance.
     */
    public Map<Class<? extends GameComponent>, GameComponent> getComponents() {
        return components;
    }

    /**
     * Creates a deep copy/clone of the component.
     * It will copy all the children of the component.
     * Always call the deepCopy() method on the top level component you want to copy.
     * @param <T>
     * @return A deep copy of the component and all it's children.
     */
    public abstract <T extends GameComponent> T deepCopy();

    /**
     * Copies all the child components from the from GameComponent in to the to GameComponent.
     * It will use the {@link #deepCopy()} method to clone each child component.
     * This method should be called when overriding the deepCopy method.
     * @param from The component to copy the children from.
     * @param to The new cloned component to add the cloned components to.
     */
    protected GameComponent copyChildComponents(GameComponent from, GameComponent to) {
        for (GameComponent child : from.getComponents().values()) {
            to.addComponent(child.deepCopy());
        }
        return to;
    }
}
