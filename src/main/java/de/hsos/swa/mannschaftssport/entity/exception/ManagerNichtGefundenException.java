package de.hsos.swa.mannschaftssport.entity.exception;

public class ManagerNichtGefundenException extends NichtGefundenException{
    public ManagerNichtGefundenException(long managerId){
        super("Manager " + managerId + "nicht gefunden");
    }
}
