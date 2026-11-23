package de.hsos.swa.mannschaftssport.entity;

import java.util.List;

public interface TeamKatalog {

    List<Team> alle();

    List<Team> finde(String name);

    Team finde(long id);

    Team speichere(Team team);

    Team findeTeamvon(long playerId);

    boolean loesche(long id);
}
