package de.hsos.swa.mannschaftssport.boundary.dto;

import de.hsos.swa.mannschaftssport.entity.Category;
import de.hsos.swa.mannschaftssport.entity.Team;

public record TeamDTO(Long id, String name, Category category) {

    public static TeamDTO fromEntity(Team team) {
        return new TeamDTO(team.getId(), team.getName(), team.getCategory());
    }
}
