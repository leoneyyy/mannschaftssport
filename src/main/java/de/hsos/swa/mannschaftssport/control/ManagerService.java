package de.hsos.swa.mannschaftssport.control;

import de.hsos.swa.mannschaftssport.entity.Manager;
import de.hsos.swa.mannschaftssport.entity.ManagerKatalog;
import de.hsos.swa.mannschaftssport.entity.Team;
import de.hsos.swa.mannschaftssport.entity.TeamKatalog;
import de.hsos.swa.mannschaftssport.entity.exception.ManagerNichtGefundenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ManagerService {

    @Inject
    ManagerKatalog managerKatalog;

    @Inject
    TeamKatalog teamKatalog;

    public List<Manager> alle() {
        return managerKatalog.alleManager();
    }

    public Manager finde(Long id) {
        Manager manager = managerKatalog.findeManager(id);
        if (manager == null) {
            throw new ManagerNichtGefundenException(id);
        }
        return manager;
    }

    public Manager anlegen(Manager manager) {
        return managerKatalog.speichereManager(manager);
    }

    public Manager aendern(Long id, String name) {
        Manager manager = finde(id);
        manager.setName(name);
        return managerKatalog.speichereManager(manager);
    }

    public void loeschen(Long id) {
        Manager manager = finde(id);
        for (Team team : teamKatalog.alleTeams()) {
            if (manager.equals(team.getManager())) {
                team.removeManager();
                teamKatalog.speichereTeam(team);
            }
        }
        managerKatalog.loescheManager(id);
    }
}
