package de.hsos.swa.mannschaftssport.gateway.memory;

import de.hsos.swa.mannschaftssport.entity.Player;
import de.hsos.swa.mannschaftssport.entity.PlayerKatalog;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;


@ApplicationScoped
public class PlayerRepository implements PlayerKatalog {

    private final Logger LOG = Logger.getLogger(PlayerRepository.class);

    private final Map<Long, Player> players = new ConcurrentSkipListMap<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Player> alle() {
        LOG.trace(alle());
        return new ArrayList<>(players.values());
    }

    @Override
    public List<Player> finde(String name) {
        LOG.tracef("finde name=%s", name);
        List<Player> ergebnis = new ArrayList<>();
        for(Player p : players.values()){
            String vollerName = p.getVorname() + " "+ p.getNachname();
            if(vollerName.toLowerCase().contains(name.toLowerCase())){
                ergebnis.add(p);
            }

        }
        return ergebnis;
    }

    @Override
    public Player finde(long playerId) {
        LOG.tracef("finde id=%d",playerId);
        return players.get(playerId);
    }

    @Override
    public Player speichere(Player player) {
        Player mitId = player;
        if(player.getId() == 0){
            mitId = new Player(idGenerator.getAndIncrement(),player.getVorname(),player.getNachname(),player.getPosition(),player.getSpielerId());
        }
        players.put(mitId.getId(),mitId);
        LOG.tracef("speichere id=%d", mitId.getId());
        return mitId;
    }

    @Override
    public boolean loesche(long playerId) {
        LOG.tracef("loesche id=%d",playerId);
        return players.remove(playerId) !=null;
    }
}
