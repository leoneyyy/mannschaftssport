package de.hsos.swa.mannschaftssport.entity;

import java.util.Objects;

public class Manager {

    private Long id;
    private String name;

    public Manager(String name) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Manager other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
