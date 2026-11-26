package de.hsos.swa.mannschaftssport.entity.exception;

public class ManagerNichtGefundenException extends NichtGefundenException {

    public ManagerNichtGefundenException(Long managerId) {
        super("manager-nicht-gefunden", "Manager nicht gefunden",
                "Manager " + managerId + " wurde nicht gefunden.");
    }
}
