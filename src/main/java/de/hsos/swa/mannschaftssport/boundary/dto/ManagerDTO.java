package de.hsos.swa.mannschaftssport.boundary.dto;

import de.hsos.swa.mannschaftssport.entity.Manager;

public record ManagerDTO(long id, String vorname, String nachname) {
    public static ManagerDTO von (Manager m){
        return new ManagerDTO(m.getId(), m.getVorname(), m.getNachname());
    }
}
