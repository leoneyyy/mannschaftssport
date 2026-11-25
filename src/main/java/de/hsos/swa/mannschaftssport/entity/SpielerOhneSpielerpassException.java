package de.hsos.swa.mannschaftssport.entity;

public class SpielerOhneSpielerpassException extends RuntimeException {
    private final Long playerId;

    public SpielerOhneSpielerpassException(Long playerId) {
        super("Player " + playerId + " hat noch keine Spieler-ID und kann keinem Team zugeordnet werden.");
        this.playerId = playerId;
    }

    public Long getPlayerId() {
        return playerId;
    }
}
