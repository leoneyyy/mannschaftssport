package de.hsos.swa.mannschaftssport.boundary.rest.player;

import de.hsos.swa.mannschaftssport.boundary.dto.PlayerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.PlayerEingabeDTO;
import de.hsos.swa.mannschaftssport.entity.Player;
import de.hsos.swa.mannschaftssport.entity.PlayerKatalog;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.List;

@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlayerResource {

    @Inject
    PlayerKatalog players;

    @GET
    public List<PlayerDTO> alle(@QueryParam("name") String name) {
        List<Player> treffer = (name == null || name.isBlank()) ? players.alle() : players.finde(name);
        return treffer.stream().map(PlayerDTO::von).toList();
    }

    @POST
    public Response anlegen(PlayerEingabeDTO eingabe, @Context UriInfo uriInfo) {
        if (eingabe == null) throw new IllegalArgumentException("Eingabe darf nicht leer sein");
        Player neu = players.speichere(new Player(eingabe.vorname(), eingabe.nachname(), eingabe.position(), null));
        return Response.created(uriInfo.getAbsolutePathBuilder().path(Long.toString(neu.getId())).build())
                .entity(PlayerDTO.von(neu))
                .build();
    }
}