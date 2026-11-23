package de.hsos.swa.mannschaftssport.boundary.dto;

import de.hsos.swa.mannschaftssport.entity.Team;

import java.util.Set;

public record TeamDTO(long id, String name, String kategorie, Long managerId, Set<Long> playerIds) {
    public static TeamDTO von(Team t){
        return new TeamDTO(t.getId(),t.getName(),t.getTeamkategorie().name().toLowerCase(),t.getManagerId(),t.getPlayerIds());
    }
}
