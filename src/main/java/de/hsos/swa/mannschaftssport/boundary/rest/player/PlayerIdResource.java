package de.hsos.swa.mannschaftssport.boundary.rest.player;

import de.hsos.swa.mannschaftssport.boundary.dto.PlayerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.PlayerEingabeDTO;
import de.hsos.swa.mannschaftssport.entity.Player;
import de.hsos.swa.mannschaftssport.entity.PlayerKatalog;
import de.hsos.swa.mannschaftssport.entity.exception.PlayerNichtGefundenException;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/players/{id}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlayerIdResource {

    @Inject
    PlayerKatalog players;

    @GET
    public PlayerDTO lese(@PathParam("id") long id) {
        Player p = players.finde(id);
        if (p == null) throw new PlayerNichtGefundenException(id);
        return PlayerDTO.von(p);
    }

    @PUT
    public PlayerDTO aendere(@PathParam("id") long id, PlayerEingabeDTO eingabe) {
        if (eingabe == null) throw new IllegalArgumentException("Eingabe darf nicht leer sein");
        Player p = players.finde(id);
        if (p == null) throw new PlayerNichtGefundenException(id);
        p.aendere(eingabe.vorname(), eingabe.nachname(), eingabe.position());
        return PlayerDTO.von(players.speichere(p));
    }

    @DELETE
    public Response loesche(@PathParam("id") long id) {
        if (!players.loesche(id)) throw new PlayerNichtGefundenException(id);
        return Response.noContent().build();
    }
}