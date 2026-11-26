package de.hsos.swa.mannschaftssport.entity;

import java.util.List;

public interface TeamKatalog {
    Team speichereTeam(Team team);
    Team findeTeam(Long id);
    Team findeTeamVon(Long playerId);
    List<Team> alleTeams();
    boolean loescheTeam(Long id);
}
