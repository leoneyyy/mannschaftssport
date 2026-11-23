package de.hsos.swa.mannschaftssport.entity.exception;

public class TeamNichtGefundenException extends NichtGefundenException {
    public TeamNichtGefundenException(long teamId){
        super("Team " + teamId + " nicht gefunden");
    }
}
