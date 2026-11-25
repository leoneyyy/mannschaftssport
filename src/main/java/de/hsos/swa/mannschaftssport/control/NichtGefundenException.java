package de.hsos.swa.mannschaftssport.control;

public class NichtGefundenException extends RuntimeException {
    public NichtGefundenException(String typ, Long id) {
        super(typ + " " + id + " wurde nicht gefunden.");
    }
}
