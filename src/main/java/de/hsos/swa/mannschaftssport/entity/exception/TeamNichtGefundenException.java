package de.hsos.swa.mannschaftssport.entity.exception;

public class TeamNichtGefundenException extends NichtGefundenException {

    public TeamNichtGefundenException(Long teamId) {
        super("team-nicht-gefunden", "Team nicht gefunden",
                "Team " + teamId + " wurde nicht gefunden.");
    }
}
