package de.hsos.swa.mannschaftssport.gateway.memory;

import de.hsos.swa.mannschaftssport.entity.Manager;
import de.hsos.swa.mannschaftssport.entity.ManagerKatalog;
import de.hsos.swa.mannschaftssport.entity.Player;
import de.hsos.swa.mannschaftssport.entity.PlayerKatalog;
import de.hsos.swa.mannschaftssport.entity.Team;
import de.hsos.swa.mannschaftssport.entity.TeamKatalog;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class MannschaftsRepository implements TeamKatalog, PlayerKatalog, ManagerKatalog {

    private final Map<Long, Team> teams = new ConcurrentHashMap<>();
    private final Map<Long, Player> players = new ConcurrentHashMap<>();
    private final Map<Long, Manager> managers = new ConcurrentHashMap<>();

    private final AtomicLong naechsteTeamId = new AtomicLong(1);
    private final AtomicLong naechstePlayerId = new AtomicLong(1);
    private final AtomicLong naechsteManagerId = new AtomicLong(1);


    @Override
    public Team speichereTeam(Team team) {
        if (team.getId() == null) {
            team.setId(naechsteTeamId.getAndIncrement());
        }
        teams.put(team.getId(), team);
        return team;
    }

    @Override
    public Team findeTeam(Long id) {
        return teams.get(id);
    }

    @Override
    public List<Team> alleTeams() {
        return teams.values().stream()
                .sorted(Comparator.comparing(Team::getId))
                .toList();
    }

    @Override
    public boolean loescheTeam(Long id) {
        return teams.remove(id) != null;
    }


    @Override
    public Player speicherePlayer(Player player) {
        if (player.getId() == null) {
            player.setId(naechstePlayerId.getAndIncrement());
        }
        players.put(player.getId(), player);
        return player;
    }

    @Override
    public Player findePlayer(Long id) {
        return players.get(id);
    }

    @Override
    public List<Player> allePlayer() {
        return players.values().stream()
                .sorted(Comparator.comparing(Player::getId))
                .toList();
    }

    @Override
    public boolean loeschePlayer(Long id) {
        return players.remove(id) != null;
    }

    @Override
    public Manager speichereManager(Manager manager) {
        if (manager.getId() == null) {
            manager.setId(naechsteManagerId.getAndIncrement());
        }
        managers.put(manager.getId(), manager);
        return manager;
    }

    @Override
    public Manager findeManager(Long id) {
        return managers.get(id);
    }

    @Override
    public List<Manager> alleManager() {
        return managers.values().stream()
                .sorted(Comparator.comparing(Manager::getId))
                .toList();
    }

    @Override
    public boolean loescheManager(Long id) {
        return managers.remove(id) != null;
    }
}