package de.hsos.swa.mannschaftssport.entity;

import de.hsos.swa.mannschaftssport.entity.exception.SpielerpassBereitsRegistriertException;

import java.util.Objects;

public class Player {

    private Long id;
    private String name;
    private String spielerId;

    public Player(String name) {
        this.name = Objects.requireNonNull(name, "name darf nicht null sein");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name darf nicht null sein");
    }

    public String getSpielerId() {
        return spielerId;
    }

    public boolean hatSpielerpass() {
        return spielerId != null;
    }

    public void spielerIdZuweisen(String spielerId) {
        Objects.requireNonNull(spielerId, "spielerId darf nicht null sein");
        if (hatSpielerpass()) {
            throw new SpielerpassBereitsRegistriertException(id);
        }
        this.spielerId = spielerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
