package de.hsos.swa.mannschaftssport.entity.exception;

public class PlayerSchonImTeamException extends RegelverletzungException{
    public PlayerSchonImTeamException(long playerId, long teamId){
        super("Player " + playerId + " ist bereits Team "+ teamId + " zugeordnet");
    }
}
