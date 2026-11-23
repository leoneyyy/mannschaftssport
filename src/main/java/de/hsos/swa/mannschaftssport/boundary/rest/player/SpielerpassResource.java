package de.hsos.swa.mannschaftssport.boundary.rest.player;

import de.hsos.swa.mannschaftssport.boundary.dto.PlayerDTO;
import de.hsos.swa.mannschaftssport.control.SpielerpassVergabe;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/players/{id}/spielerpass")
@Produces(MediaType.APPLICATION_JSON)
public class SpielerpassResource {

    @Inject
    SpielerpassVergabe vergabe;

    @POST
    public PlayerDTO registriere(@PathParam("id") long id) {
        return PlayerDTO.von(vergabe.registriere(id));
    }
}