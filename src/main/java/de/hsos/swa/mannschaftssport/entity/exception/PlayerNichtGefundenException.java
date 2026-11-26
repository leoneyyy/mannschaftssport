package de.hsos.swa.mannschaftssport.entity.exception;

public class PlayerNichtGefundenException extends NichtGefundenException {

    public PlayerNichtGefundenException(Long playerId) {
        super("player-nicht-gefunden", "Player nicht gefunden",
                "Player " + playerId + " wurde nicht gefunden.");
    }
}
