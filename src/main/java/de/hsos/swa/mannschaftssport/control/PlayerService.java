package de.hsos.swa.mannschaftssport.control;

import de.hsos.swa.mannschaftssport.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class PlayerService {

    @Inject
    PlayerKatalog playerKatalog;

    @Inject
    TeamKatalog teamKatalog;

    @Inject
    SpielerpassService spielerpassService;

    public List<Player> alle() {
        return playerKatalog.allePlayer();
    }

    public Player finde(Long id) {
        Player player = playerKatalog.findePlayer(id);
        if (player == null) {
            throw new NichtGefundenException("Player", id);
        }
        return player;
    }

    public Player anlegen(Player player) {
        return playerKatalog.speicherePlayer(player);
    }

    public Player aendern(Long id, String name) {
        Player player = finde(id);
        player.setName(name);
        return playerKatalog.speicherePlayer(player);
    }

    public void loeschen(Long id) {
        finde(id);
        for (Team team : teamKatalog.alleTeams()) {
            team.removePlayer(id);
        }
        playerKatalog.loeschePlayer(id);
    }

    public Player spielerpassRegistrieren(Long id) {
        Player player = finde(id);
        if (player.hatSpielerpass()) {
            throw new SpielerpassBereitsRegistriertException(id);
        }
        String spielerId = spielerpassService.registriere(player);
        player.spielerIdZuweisen(spielerId);
        return playerKatalog.speicherePlayer(player);
    }
}
