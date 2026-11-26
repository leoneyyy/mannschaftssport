package de.hsos.swa.mannschaftssport.entity.exception;

public class SpielerOhneSpielerpassException extends RegelverletzungException {

    private final Long playerId;

    public SpielerOhneSpielerpassException(Long playerId) {
        super("spieler-ohne-spielerpass", "Spieler ohne Spielerpass",
                "Player " + playerId + " hat noch keine Spieler-ID und kann keinem Team zugeordnet werden.");
        this.playerId = playerId;
    }

    public Long getPlayerId() {
        return playerId;
    }
}
