package de.hsos.swa.mannschaftssport.control;

import de.hsos.swa.mannschaftssport.entity.Player;
import de.hsos.swa.mannschaftssport.entity.PlayerKatalog;
import de.hsos.swa.mannschaftssport.entity.exception.PlayerNichtGefundenException;
import de.hsos.swa.mannschaftssport.entity.exception.SpielerIdBereitsVergebenException;
import de.hsos.swa.mannschaftssport.entity.SpielerpassRegister;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SpielerpassVergabe {

  @Inject
    PlayerKatalog players;

  @Inject
  SpielerpassRegister register;

  public Player registriere(long playerId){
    Player player = players.finde(playerId);
    if(player == null)throw new PlayerNichtGefundenException(playerId);
    if(player.hatSpielerID()) throw new SpielerIdBereitsVergebenException(playerId);

    String spielerId = register.registriere(playerId);
    player.weiseSpielerIdZu(spielerId);
    return players.speichere(player);
  }
}
