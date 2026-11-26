package de.hsos.swa.mannschaftssport.entity.exception;

public class SpielerpassBereitsRegistriertException extends RegelverletzungException {

    private final Long playerId;

    public SpielerpassBereitsRegistriertException(Long playerId) {
        super("spielerpass-bereits-registriert", "Spielerpass bereits registriert",
                "Für Player " + playerId + " ist bereits ein Spielerpass registriert.");
        this.playerId = playerId;
    }

    public Long getPlayerId() {
        return playerId;
    }
}
