package de.hsos.swa.mannschaftssport.entity;

public class SpielerpassBereitsRegistriertException extends RuntimeException {

    private final Long playerId;

    public SpielerpassBereitsRegistriertException(Long playerId) {
        super("Für Player " + playerId + " ist bereits ein Spielerpass registriert.");
        this.playerId = playerId;
    }

    public Long getPlayerId() {
        return playerId;
    }
}
