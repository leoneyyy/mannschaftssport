package de.hsos.swa.mannschaftssport.gateway.memory;

import de.hsos.swa.mannschaftssport.entity.Team;
import de.hsos.swa.mannschaftssport.entity.TeamKatalog;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;


@ApplicationScoped
public class TeamRepository implements TeamKatalog {

    private final Logger LOG = Logger.getLogger(TeamRepository.class);

    private final Map<Long, Team> teams = new ConcurrentSkipListMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Team> alle() {
        LOG.trace("alle");
        return new ArrayList<>(teams.values());
    }

    @Override
    public List<Team> finde(String name) {
        LOG.tracef("finde name=%s", name);
        List<Team> ergebnis = new ArrayList<>();
        for (Team t : teams.values()) {
            if (t.getName().toLowerCase().contains(name.toLowerCase())) {
                ergebnis.add(t);
            }
        }
        return ergebnis;
    }

    @Override
    public Team finde(long id) {
        LOG.tracef("finde id=%d", id);
        return teams.get(id);
    }

    @Override
    public Team speichere(Team team) {
        Team mitId = team;
        if (team.getId() == 0) {
            mitId = new Team(idGenerator.getAndIncrement(), team.getName(), team.getTeamkategorie(), team.getManagerId(), team.getPlayerIds());
        }
        teams.put(mitId.getId(), mitId);
        LOG.tracef("speichere id=%d", mitId.getId());
        return mitId;
    }

    @Override
    public Team findeTeamvon(long playerId) {
        LOG.tracef("findeTeamvon playerId=%d", playerId);
        for (Team t : teams.values()) {
            if (t.hatPlayer(playerId)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public List<Team> findeTeamsVonManager(long managerId) {
        LOG.tracef("findeTeamsVonManager managerId=%d", managerId);
        List<Team> ergebnis = new ArrayList<>();
        for (Team t : teams.values()) {
            if (t.getManagerId() != null && t.getManagerId() == managerId) {
                ergebnis.add(t);
            }
        }
        return ergebnis;
    }

    @Override
    public boolean loesche(long id) {
        LOG.tracef("loesche id=%d", id);
        return teams.remove(id) != null;
    }
}