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
