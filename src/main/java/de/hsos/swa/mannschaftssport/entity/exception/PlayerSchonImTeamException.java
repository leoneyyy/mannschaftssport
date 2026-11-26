package de.hsos.swa.mannschaftssport.entity.exception;

public class PlayerSchonImTeamException extends RegelverletzungException {

    private final Long playerId;
    private final Long teamId;

    public PlayerSchonImTeamException(Long playerId, Long teamId) {
        super("player-schon-im-team", "Player schon in einem Team",
                "Player " + playerId + " ist bereits Team " + teamId + " zugeordnet.");
        this.playerId = playerId;
        this.teamId = teamId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public Long getTeamId() {
        return teamId;
    }
}
