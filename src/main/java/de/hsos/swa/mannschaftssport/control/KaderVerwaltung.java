package de.hsos.swa.mannschaftssport.control;

import de.hsos.swa.mannschaftssport.entity.*;
import de.hsos.swa.mannschaftssport.entity.exception.ManagerNichtGefundenException;
import de.hsos.swa.mannschaftssport.entity.exception.PlayerNichtGefundenException;
import de.hsos.swa.mannschaftssport.entity.exception.PlayerSchonImTeamException;
import de.hsos.swa.mannschaftssport.entity.exception.TeamNichtGefundenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class KaderVerwaltung {

    @Inject
    TeamKatalog teams;
    @Inject
    PlayerKatalog players;
    @Inject
    ManagerKatalog managers;
    public Team nimmAuf(long teamId, long playerId){
        Team team = holeTeam(teamId);
        Player player = players.finde(playerId);
        if(player == null) throw new PlayerNichtGefundenException(playerId);

        Team anderesTeam = teams.findeTeamvon(playerId);
        if(anderesTeam != null && anderesTeam.getId() != teamId){
            throw new PlayerSchonImTeamException(playerId, anderesTeam.getId());
        }
        team.nimmAuf(player);
        return teams.speichere(team);
    }
    public Team entferne(long teamId, long playerId) {
        Team team = holeTeam(teamId);
        team.entferne(playerId);
        return teams.speichere(team);
    }
    public Team setzeManager(long teamId, long managerId) {
        Team team = holeTeam(teamId);
        if (managers.finde(managerId) == null) throw new ManagerNichtGefundenException(managerId);
        team.setzeManager(managerId);
        return teams.speichere(team);
    }
    public Team entferneManager(long teamId) {
        Team team = holeTeam(teamId);
        team.entferneManager();
        return teams.speichere(team);
    }


    public void loeschePlayer(long playerId) {
        if (players.finde(playerId) == null) throw new PlayerNichtGefundenException(playerId);
        Team team = teams.findeTeamvon(playerId);
        if (team != null) {
            team.entferne(playerId);
            teams.speichere(team);
        }
        players.loesche(playerId);
    }

    public void loescheManager(long managerId) {
        if (managers.finde(managerId) == null) throw new ManagerNichtGefundenException(managerId);
        for (Team team : teams.findeTeamsVonManager(managerId)) {
            team.entferneManager();
            teams.speichere(team);
        }
        managers.loesche(managerId);
    }

    private Team holeTeam(long teamId) {
        Team team = teams.finde(teamId);
        if (team == null) throw new TeamNichtGefundenException(teamId);
        return team;
    }

}
