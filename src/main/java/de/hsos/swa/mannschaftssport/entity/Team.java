package de.hsos.swa.mannschaftssport.entity;

import de.hsos.swa.mannschaftssport.entity.exception.KeineSpielerIdException;

import java.util.LinkedHashSet;
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

    public void setzeManager(Long managerId){
        this.managerId = managerId;
    }

    public void entferneManager(){
        this.managerId = null;
    }

    public boolean hatManager(){
        return this.managerId != null;
    }

    public void nimmAuf(Player player){
        if(!player.hatSpielerID()){
            throw new KeineSpielerIdException(player.getId());
        }
        playerIds.add(player.getId());
    }

    public void entferne(long playerId){
        playerIds.remove(playerId);
    }

    public boolean hatPlayer(long playerId){
        return playerIds.contains(playerId);
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
        return Set.copyOf(playerIds);
    }
}
