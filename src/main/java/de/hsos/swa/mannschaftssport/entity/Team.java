package de.hsos.swa.mannschaftssport.entity;

import java.util.List;

public class Team {

    private final long id;
    private String name;
    private Teamkategorie teamkategorie;
    private Long managerId;
    private List<Long> playerIds;

    public Team(long id, String name, Teamkategorie teamkategorie, long managerId, List<Long> playerIds){
        this.id = id;
        aendere(name, teamkategorie);

    }

    public Team(String name, Teamkategorie teamkategorie, long managerId, List<Long> playerIds){
        this(0, name, teamkategorie,managerId,playerIds);
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

    public List<Long> getPlayerIds() {
        return playerIds;
    }
}
