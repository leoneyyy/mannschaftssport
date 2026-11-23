package de.hsos.swa.mannschaftssport.entity.exception;

public abstract class NichtGefundenException extends RuntimeException {
    protected NichtGefundenException(String meldung){
        super(meldung);
    }
}
