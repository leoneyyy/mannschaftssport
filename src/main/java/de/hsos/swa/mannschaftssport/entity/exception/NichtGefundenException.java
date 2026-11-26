package de.hsos.swa.mannschaftssport.entity.exception;

public abstract class NichtGefundenException extends RuntimeException {

    private final String typ;
    private final String titel;

    protected NichtGefundenException(String typ, String titel, String meldung) {
        super(meldung);
        this.typ = typ;
        this.titel = titel;
    }

    public String getTyp() {
        return typ;
    }

    public String getTitel() {
        return titel;
    }
}
