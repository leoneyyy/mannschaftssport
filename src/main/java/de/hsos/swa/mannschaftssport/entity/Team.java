package de.hsos.swa.mannschaftssport.entity;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Team {

    private final long id;
    private String name;
    private Teamkategorie teamkategorie;
    private Long managerId;
    private final Set<Long> playerIds = new LinkedHashSet<>();

    public Team(long id, String name, Teamkategorie teamkategorie, Long managerId, Set<Long> playerIds){
        this.id = id;
        this.managerId = managerId;
        this.playerIds.addAll(playerIds);
        aendere(name, teamkategorie);

    }

    public Team(String name, Teamkategorie teamkategorie, Long managerId, Set<Long> playerIds){
        this(0, name, teamkategorie,null, Set.of());
    }

    public void aendere(String name, Teamkategorie kategorie){
        if(name == null || name.isBlank()) throw new IllegalArgumentException("Name darf nicht leer sein");
        if(kategorie == null) throw new IllegalArgumentException("Kategorie darf nicht leer sein");
        this.name = name;
        this.teamkategorie = kategorie;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Teamkategorie getTeamkategorie() {
        return teamkategorie;
    }

    public Long getManagerId() {
        return managerId;
    }

    public Set<Long> getPlayerIds() {
        return playerIds;
    }
}
