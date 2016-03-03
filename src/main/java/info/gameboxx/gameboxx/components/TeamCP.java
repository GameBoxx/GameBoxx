package info.gameboxx.gameboxx.components;

import info.gameboxx.gameboxx.components.internal.GameComponent;
import info.gameboxx.gameboxx.game.Game;
import info.gameboxx.gameboxx.game.GameSession;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamCP extends GameComponent {
    private final Map<String, Set<UUID>> teams = new HashMap<>();

    public TeamCP(Game game) {
        super(game);
    }

    @Override
    public void registerOptions() {

    }

    @Override
    public TeamCP newInstance(GameSession session) {
        return (TeamCP) new TeamCP(getGame()).setSession(session);
    }

    /**
     * Get a {@link Set} with the team names.
     * @return {@link Set} with the team names.
     */
    public Set<String> getTeams() {
        return teams.keySet();
    }

    /**
     * Add a team.
     * @param team The name of the team.
     */
    public void addTeam(String team) {
        if(teams.containsKey(team)) {
            return;
        }

        teams.put(team, new HashSet<UUID>());
    }

    /**
     * Remove a team.
     * @param team The name of the team.
     */
    public void removeTeam(String team) {
        if(!teams.containsKey(team)) {
            return;
        }

        teams.remove(team);
    }

    /**
     * Remove all {@link Player}{@code s} from a team.
     * @param team The team to clear the {@link Player}{@code s} from.
     */
    public void clearTeam(String team) {
        addTeam(team);
        teams.get(team).clear();
    }

    /**
     * Get a {@link Set} with {@link Player}{@code s} from a team.
     * @param team The team to get the {@link Set} with {@link Player}{@code s} from.
     * @return {@link Set} with {@link Player}{@code s} from a team.
     */
    public Set<UUID> getPlayers(String team) {
        return teams.get(team);
    }

    /**
     * Add a {@link Player} to a team.
     * @param team The team to add the {@link Player} to.
     * @param player The {@link Player} to add to the team.
     */
    public void addPlayer(String team, UUID player) {
        addTeam(team);
        teams.get(team).add(player);
    }

    /**
     * Remove a {@link Player} from a team.
     * @param team The team to remove the {@link Player} from.
     * @param player The {@link Player} to remove from the team.
     */
    public void removePlayer(String team, UUID player) {
        addTeam(team);
        teams.get(team).remove(player);
    }
}
