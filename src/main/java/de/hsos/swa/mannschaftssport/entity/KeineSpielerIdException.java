package de.hsos.swa.mannschaftssport.entity;

public class KeineSpielerIdException extends RuntimeException {
    public KeineSpielerIdException(long playerId) {
        super("Player" + playerId + "hat keine Spieler-ID und kann keinem Team zugeordent werden");
    }
}
