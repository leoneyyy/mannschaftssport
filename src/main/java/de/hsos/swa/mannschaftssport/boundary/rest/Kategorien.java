package de.hsos.swa.mannschaftssport.boundary.rest;

import de.hsos.swa.mannschaftssport.entity.Teamkategorie;

public final class Kategorien {
    private Kategorien() {}

    public static Teamkategorie parse(String s) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException("Kategorie darf nicht leer sein");
        try {
            return Teamkategorie.valueOf(s.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unbekannte Kategorie: " + s);
        }
    }
}