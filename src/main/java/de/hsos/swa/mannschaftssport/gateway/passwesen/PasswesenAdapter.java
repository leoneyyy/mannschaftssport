package de.hsos.swa.mannschaftssport.gateway.passwesen;

import de.hsos.swa.mannschaftssport.entity.SpielerpassRegister;
import de.hsos.swa.passwesen.api.Passwesen;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
@ApplicationScoped
public class PasswesenAdapter implements SpielerpassRegister {

    @Inject
    Passwesen passwesen;
    @Override
    public String registriere(long playerId) {
        return passwesen.registriereSpielerpass(playerId);
    }
}
