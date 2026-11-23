package de.hsos.swa.mannschaftssport.entity;

import de.hsos.swa.mannschaftssport.entity.exception.SpielerIdBereitsVergebenException;

public class Player {

    private final long id;
    private String vorname, nachname;
    private String position;
    private String spielerId;

    public Player(long id, String vorname, String nachname,  String position, String spielerId){
        this.id = id;
        this.spielerId = spielerId;
        aendere(vorname, nachname, position);
    }
    public Player(String vorname, String nachname, String position, String spielerId){
        this(0,vorname,nachname,position,null);
    }

    public void aendere(String vorname, String nachname, String position) {
        if (vorname == null || vorname.isBlank()) throw new IllegalArgumentException("Vorname darf nicht leer sein");
        if (nachname == null || nachname.isBlank()) throw new IllegalArgumentException("Nachname darf nicht leer sein");
        if (position == null || position.isBlank()) throw new IllegalArgumentException("Position darf nicht leer sein");
        this.vorname = vorname.trim();
        this.nachname = nachname.trim();
        this.position = position;
    }

    public boolean hatSpielerID(){
        return spielerId != null;
    }

    public void weiseSpielerIdZu(String spielerId){
        if(hatSpielerID()){
            throw new SpielerIdBereitsVergebenException(id);
        }
        if(spielerId == null || spielerId.isBlank()){
            throw new IllegalArgumentException("Spieler-ID darf nicht leer sein");
        }
        this.spielerId = spielerId;
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

    public String getPosition() {
        return position;
    }

    public String getSpielerId() {
        return spielerId;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Player andere && id != 0 && andere.id == id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
