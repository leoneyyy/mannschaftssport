package de.hsos.swa.mannschaftssport.entity;

import java.util.List;

public interface ManagerKatalog {

    List<Manager> alle();

    List<Manager> finde(String name);

    Manager finde(long id);
    Manager speichere(Manager manager);

    boolean loesche(long id);

}
