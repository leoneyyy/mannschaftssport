package de.hsos.swa.mannschaftssport.boundary.dto;

import de.hsos.swa.mannschaftssport.entity.Player;
import jakarta.json.bind.annotation.JsonbNillable;

@JsonbNillable
public record PlayerDTO(Long id, String name, String spielerId) {

    public static PlayerDTO fromEntity(Player player) {
        return new PlayerDTO(player.getId(), player.getName(), player.getSpielerId());
    }
}
