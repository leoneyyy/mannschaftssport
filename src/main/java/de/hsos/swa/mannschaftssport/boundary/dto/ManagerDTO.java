package de.hsos.swa.mannschaftssport.boundary.dto;

import de.hsos.swa.mannschaftssport.entity.Manager;

public record ManagerDTO(Long id, String name) {

    public static ManagerDTO fromEntity(Manager manager) {
        return new ManagerDTO(manager.getId(), manager.getName());
    }
}
