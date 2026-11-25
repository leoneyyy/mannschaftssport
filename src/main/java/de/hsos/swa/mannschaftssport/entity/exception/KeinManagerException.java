package de.hsos.swa.mannschaftssport.entity.exception;

public class KeinManagerException extends NichtGefundenException {
    public KeinManagerException(long teamId) {
        super("Team " + teamId + " hat keinen Manager");
    }
}
