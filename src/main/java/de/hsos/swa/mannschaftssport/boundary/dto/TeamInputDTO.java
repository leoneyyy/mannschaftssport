package de.hsos.swa.mannschaftssport.boundary.dto;

import de.hsos.swa.mannschaftssport.entity.Category;
import de.hsos.swa.mannschaftssport.entity.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeamInputDTO(
        @NotBlank String name,
        @NotNull Category category) {

    public Team toEntity() {
        return new Team(name, category);
    }
}