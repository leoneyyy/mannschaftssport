package de.hsos.swa.mannschaftssport.entity.exception;

public class PlayerNichtImTeamException extends NichtGefundenException {

    public PlayerNichtImTeamException(Long teamId, Long playerId) {
        super("player-nicht-im-team", "Player nicht im Team",
                "Player " + playerId + " gehört nicht zu Team " + teamId + ".");
    }
}
