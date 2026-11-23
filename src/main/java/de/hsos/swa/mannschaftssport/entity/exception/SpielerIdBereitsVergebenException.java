package de.hsos.swa.mannschaftssport.entity.exception;

public class SpielerIdBereitsVergebenException extends RegelverletzungException {
    public SpielerIdBereitsVergebenException(long playerId) {
        super("Player " + playerId + " hat bereits eine Spieler-ID");
    }
}
