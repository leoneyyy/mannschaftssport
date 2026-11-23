package de.hsos.swa.mannschaftssport.entity;

import java.util.List;

public interface PlayerKatalog {

    List<Player> alle();

    List<Player> finde(String name);

    Player finde(long playerId);

    Player speichere(Player player);

    boolean loesche(long playerId);

}
