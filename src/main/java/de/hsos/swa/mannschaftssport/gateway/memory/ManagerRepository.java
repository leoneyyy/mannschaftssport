package de.hsos.swa.mannschaftssport.gateway.memory;

import de.hsos.swa.mannschaftssport.entity.Manager;
import de.hsos.swa.mannschaftssport.entity.ManagerKatalog;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class ManagerRepository implements ManagerKatalog {

    private final Logger LOG = Logger.getLogger(ManagerRepository.class);

    private final Map<Long, Manager> managers = new ConcurrentSkipListMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);


    @Override
    public List<Manager> alle() {
        LOG.trace("alle");
        return new ArrayList<>(managers.values());
    }

    @Override
    public List<Manager> finde(String name) {
        LOG.tracef("finde name=%s", name);
        List<Manager> ergebnis = new ArrayList<>();
        for(Manager m : managers.values()){
            String vollerName = m.getVorname() + " "+ m.getNachname();
            if(vollerName.toLowerCase().contains(name.toLowerCase())){
                ergebnis.add(m);
            }
        }

        ;
        return ergebnis;
    }

    @Override
    public Manager finde(long id) {
        LOG.tracef("finde id=%d", id);
        return managers.get(id);
    }

    @Override
    public Manager speichere(Manager manager) {
        Manager mitId = manager;
        if(manager.getId() == 0){
            mitId = new Manager(idGenerator.getAndIncrement(), manager.getVorname(),manager.getNachname());
        }
        managers.put(mitId.getId(), mitId);
        LOG.tracef("speichere id=%d",mitId.getId());
        return mitId;
    }

    @Override
    public boolean loesche(long id) {
        LOG.tracef("loesche id=%d",id);
        return managers.remove(id) != null;
    }
}
