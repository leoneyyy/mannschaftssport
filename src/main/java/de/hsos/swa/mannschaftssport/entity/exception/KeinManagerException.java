package de.hsos.swa.mannschaftssport.entity.exception;

public class KeinManagerException extends NichtGefundenException {

    public KeinManagerException(Long teamId) {
        super("kein-manager", "Kein Manager zugeordnet",
                "Team " + teamId + " hat keinen Manager.");
    }
}
