package de.hsos.swa.mannschaftssport.boundary.dto;

import de.hsos.swa.mannschaftssport.entity.Player;
import jakarta.json.bind.annotation.JsonbNillable;

@JsonbNillable
public record PlayerDTO(long id, String vorname, String nachname, String position, String spielerId) {
    public static PlayerDTO von(Player p){
        return new PlayerDTO(p.getId(),p.getVorname(), p.getNachname(), p.getPosition(), p.getSpielerId());
    }
}
