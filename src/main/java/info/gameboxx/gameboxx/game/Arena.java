package info.gameboxx.gameboxx.game;

import info.gameboxx.gameboxx.components.PlayersCP;
import info.gameboxx.gameboxx.util.Parse;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.UUID;

/**
 * Base Arena class.
 * This class is a GameComponent so you can add any of the components.
 * @see {@link com.jroossien.gameapi.components}
 */
//TODO: Don't use UUID's for arena identifiers just use the name. (Commands and such need to be able to identify the arena easily and having two arenas with the same name just makes it confusing)
//TODO: Add arena type for world/default stuff and implement this with world creation etc.
//TODO: Implement session system.
public abstract class Arena extends GameComponent {

    protected UUID uid;
    protected String name;
    protected String type;

    /**
     * Use the {@link info.gameboxx.gameboxx.game.Game#createArena(String)} method to create a new arena.
     * @param uid The unique ID for the arena.
     * @param type The type of arena which is the name of the {@link info.gameboxx.gameboxx.game.Game}.
     * @param name The name of the arena.
     */
    public Arena(UUID uid, String type, String name) {
        super(null); //This is the base component and it has no parent.
        this.uid = uid;
        this.type = type;
        this.name = name;
    }

    /**
     * Arenas from config will instantiate using this constructor.
     * @param data The {@link ConfigurationSection} with all the arena data.
     */
    public Arena(ConfigurationSection data) {
        super(null); //This is the base component and it has no parent.
        uid = Parse.UUID(data.getString("uid"));
        name = data.getString("name");
        type = data.getString("type");
    }

    /**
     * Used by the {@link info.gameboxx.gameboxx.game.Game} to save all the arena data.
     * Do not manually call this method without using {@link info.gameboxx.gameboxx.game.Game}
     * @param cfg The {@link YamlConfiguration} to save the data in.
     * @return The YamlConfiguration will be returned to Game to actually save the data and such.
     */
    public YamlConfiguration save(YamlConfiguration cfg) {
        cfg.set("uid", uid.toString());
        cfg.set("name", name);
        //TODO: When the name is changed delete the previous config file and create a new one or rename it.
        //TODO: Save the actual config file inside Game
        return cfg;
    }

    /**
     * Add all the {@link GameComponent}s to the arena.
     * This allows you to design the game like how you want it.
     * You can add as many components as you need.
     * For example if you want to allow people to spectate your game you would add a {@link info.gameboxx.gameboxx.components.SpectateGC}
     * @see {@link com.jroossien.gameapi.components}
     */
    public void addComponents() {
        addComponent(new PlayersCP(this));
    }

    /**
     * Get the unique ID of the arena.
     * @return The unique ID. {@link UUID}
     */
    public UUID getUid() {
        return uid;
    }

    /**
     * Get the name of the arena.
     * Do not use the name to identify/reference an arena!
     * Use {@link #getUid()} for that as that's unique to each arena.
     * @return The name of the arena. <b>May not be unique!</b>
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the arena.
     * @param name The new name for the arena.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the type of the arena.
     * This will be the name of the game for example Spleef.
     * @return The arena type / game name.
     */
    public String getType() {
        return type;
    }
}
