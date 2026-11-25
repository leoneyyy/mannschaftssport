package de.hsos.swa.mannschaftssport.gateway.passwesen;

import de.hsos.swa.mannschaftssport.entity.Player;
import de.hsos.swa.mannschaftssport.entity.SpielerpassService;
import de.hsos.swa.passwesen.api.Passwesen;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PasswesenAdapter implements SpielerpassService {

    @Inject
    Passwesen passwesen;

    @Override
    public String registriere(Player player) {
        return passwesen.registriereSpielerpass(player.getId(), player.getName());
    }
}
