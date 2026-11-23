package de.hsos.swa.mannschaftssport.entity;

import java.util.Objects;

public class Player {

    private final long id;
    private String vorname, nachname;
    private String position;
    private String spielerId;

    public Player(long id, String vorname, String nachname,  String position, String spielerId){
        this.id = id;
        aendere(vorname, nachname, position, spielerId);
    }
    public Player(String vorname, String nachname, String position, String spielerId){
        this(0,vorname,nachname,position,spielerId);
    }

    public void aendere(String vorname, String nachname, String position, String spielerId){
        if(vorname == null || vorname.isBlank()) throw new IllegalArgumentException("Vorname darf nicht leer sein");
        if(nachname == null || nachname.isBlank()) throw new IllegalArgumentException("Nachname darf nicht leer sein");
        if(position == null || position.isBlank()) throw new IllegalArgumentException("Position darf nicht leer sein");
        if(spielerId == null || spielerId.isBlank())throw new IllegalArgumentException("SpielerId darf nicht leer sein");
        this.vorname = vorname;
        this.nachname = nachname;
        this.position = position;
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
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id && Objects.equals(vorname, player.vorname) && Objects.equals(nachname, player.nachname) && Objects.equals(position, player.position) && Objects.equals(spielerId, player.spielerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vorname, nachname, position, spielerId);
    }
}
