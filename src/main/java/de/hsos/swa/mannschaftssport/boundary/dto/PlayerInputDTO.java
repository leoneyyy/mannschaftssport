package de.hsos.swa.mannschaftssport.boundary.dto;

import de.hsos.swa.mannschaftssport.entity.Player;
import jakarta.validation.constraints.NotBlank;

public record PlayerInputDTO(@NotBlank String name) {

    public Player toEntity() {
        return new Player(name);
    }
}