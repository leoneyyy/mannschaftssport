package de.hsos.swa.mannschaftssport.entity.exception;

public class KeineSpielerIdException extends RegelverletzungException {
    public KeineSpielerIdException(long playerId) {
        super("Player " + playerId + " hat keine Spieler-ID und kann keinem Team zugeordnet werden");
    }
}
