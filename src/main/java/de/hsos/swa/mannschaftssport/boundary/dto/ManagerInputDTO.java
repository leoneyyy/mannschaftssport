package de.hsos.swa.mannschaftssport.boundary.dto;

import de.hsos.swa.mannschaftssport.entity.Manager;
import jakarta.validation.constraints.NotBlank;

public record ManagerInputDTO(@NotBlank String name) {

    public Manager toEntity() {
        return new Manager(name);
    }
}