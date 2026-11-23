package de.hsos.swa.mannschaftssport.entity;

public class Manager {

    private final long id;
    private String vorname, nachname;

    public Manager(long id, String vorname, String nachname){
        this.id = id;
        aendere(vorname,nachname);
    }

    public Manager(String vorname, String nachname){
        this(0, vorname,nachname);
    }

    public void aendere(String vorname, String nachname){
        if(vorname == null || vorname.isBlank()) throw new IllegalArgumentException("Vorname darf nicht leer sein");
        if(nachname == null || nachname.isBlank()) throw new IllegalArgumentException("Nachname darf nicht leer sein");
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public long getId() {
        return id;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }
}
