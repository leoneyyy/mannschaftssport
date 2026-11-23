package de.hsos.swa.mannschaftssport.entity.exception;

public abstract class RegelverletzungException extends RuntimeException {
    protected RegelverletzungException(String meldung){
        super(meldung);
    }
}
