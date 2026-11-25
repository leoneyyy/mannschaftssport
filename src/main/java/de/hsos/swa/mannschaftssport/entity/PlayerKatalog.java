package de.hsos.swa.mannschaftssport.entity;

import java.util.List;

public interface PlayerKatalog {
    Player speicherePlayer(Player player);
    Player findePlayer(Long id);
    List<Player> allePlayer();
    boolean loeschePlayer(Long id);
}
