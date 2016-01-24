package info.gameboxx.gameboxx.game;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * A game component.
 * The game component can have have many child components and it has one parent component.
 * Except for the top level component which is the {@link Arena} in this Game API which has no parent component.
 */
//TODO: Add conflicts between components so that you can't add componentX when it has componentY
//TODO: Implement dependencies and soft dependencies.
//TODO: Keep a reference of dependencies so they can be accessed easily and quickly.
//TODO: Might have to split components up in DataComponent and SettingsComponent instead of GameComponent for sessions. (Need to design this properly)
public abstract class GameComponent {

    private GameBoxx gb;
    private GameComponent parent;
    private Map<Class<? extends GameComponent>, GameComponent> components = new HashMap<Class<? extends GameComponent>, GameComponent>();

    /**
     * Instantiate a new game component for the specified parent.
     * It will not automatically add the component to provided parent.
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
}
