package de.hsos.swa.mannschaftssport.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Team {

    private Long id;
    private String name;
    private Category category;
    private Manager manager;
    private final List<Player> players = new ArrayList<>();

    public Team(String name, Category category) {
        this.name = Objects.requireNonNull(name, "name darf nicht null sein");
        this.category = Objects.requireNonNull(category, "category darf nicht null sein");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name darf nicht null sein");
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = Objects.requireNonNull(category, "category darf nicht null sein");
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = Objects.requireNonNull(manager, "manager darf nicht null sein");
    }

    public void removeManager() {
        this.manager = null;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void addPlayer(Player player) {
        Objects.requireNonNull(player, "player darf nicht null sein");
        if (!player.hatSpielerpass()) {
            throw new SpielerOhneSpielerpassException(player.getId());
        }
        if (!players.contains(player)) {
            players.add(player);
        }
    }

    public boolean removePlayer(Long playerId) {
        return players.removeIf(p -> Objects.equals(p.getId(), playerId));
    }

    public boolean hasPlayer(Long playerId) {
        return players.stream().anyMatch(p -> Objects.equals(p.getId(), playerId));
    }
}
