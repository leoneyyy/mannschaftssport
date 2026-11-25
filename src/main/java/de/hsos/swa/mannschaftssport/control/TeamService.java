package de.hsos.swa.mannschaftssport.control;

import de.hsos.swa.mannschaftssport.entity.Category;
import de.hsos.swa.mannschaftssport.entity.Manager;
import de.hsos.swa.mannschaftssport.entity.Player;
import de.hsos.swa.mannschaftssport.entity.Team;
import de.hsos.swa.mannschaftssport.entity.TeamKatalog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TeamService {

    @Inject
    TeamKatalog teamKatalog;

    @Inject
    PlayerService playerService;

    @Inject
    ManagerService managerService;


    public List<Team> alle() {
        return teamKatalog.alleTeams();
    }

    public Team finde(Long id) {
        Team team = teamKatalog.findeTeam(id);
        if (team == null) {
            throw new NichtGefundenException("Team", id);
        }
        return team;
    }

    public Team anlegen(Team team) {
        return teamKatalog.speichereTeam(team);
    }

    public Team aendern(Long id, String name, Category category) {
        Team team = finde(id);
        team.setName(name);
        team.setCategory(category);
        return teamKatalog.speichereTeam(team);
    }

    public void loeschen(Long id) {
        finde(id);
        teamKatalog.loescheTeam(id);
    }


    public Manager findeManager(Long teamId) {
        Team team = finde(teamId);
        if (team.getManager() == null) {
            throw new NichtGefundenException("Manager von Team", teamId);
        }
        return team.getManager();
    }

    public void setzeManager(Long teamId, Long managerId) {
        Team team = finde(teamId);
        Manager manager = managerService.finde(managerId);
        team.setManager(manager);
        teamKatalog.speichereTeam(team);
    }

    public void entferneManager(Long teamId) {
        Team team = finde(teamId);
        team.removeManager();
        teamKatalog.speichereTeam(team);
    }


    public List<Player> findeSpieler(Long teamId) {
        return finde(teamId).getPlayers();
    }

    public void fuegeSpielerHinzu(Long teamId, Long playerId) {
        Team team = finde(teamId);
        Player player = playerService.finde(playerId);
        team.addPlayer(player);
        teamKatalog.speichereTeam(team);
    }

    public void entferneSpieler(Long teamId, Long playerId) {
        Team team = finde(teamId);
        if (!team.removePlayer(playerId)) {
            throw new NichtGefundenException("Player in Team " + teamId + ":", playerId);
        }
        teamKatalog.speichereTeam(team);
    }
}