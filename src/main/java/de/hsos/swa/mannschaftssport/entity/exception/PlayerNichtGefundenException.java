package de.hsos.swa.mannschaftssport.entity.exception;

public class PlayerNichtGefundenException extends NichtGefundenException{
    public PlayerNichtGefundenException(long playerId){
        super("Player " + playerId + "nicht gefunden");
    }
}
