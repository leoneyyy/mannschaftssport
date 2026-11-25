package de.hsos.swa.mannschaftssport.entity;

import java.util.List;

public interface ManagerKatalog {
    Manager speichereManager(Manager manager);
    Manager findeManager(Long id);
    List<Manager> alleManager();
    boolean loescheManager(Long id);
}
