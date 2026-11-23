package de.hsos.swa.mannschaftssport.entity;

public class SpielerIdBereitsVergebenException extends RuntimeException {
    public SpielerIdBereitsVergebenException(String message) {
        super(message);
    }
}
