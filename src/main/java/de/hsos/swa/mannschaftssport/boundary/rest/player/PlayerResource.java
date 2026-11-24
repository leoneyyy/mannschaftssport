package de.hsos.swa.mannschaftssport.boundary.rest.player;

import de.hsos.swa.mannschaftssport.boundary.dto.PlayerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.PlayerEingabeDTO;
import de.hsos.swa.mannschaftssport.boundary.rest.links.PlayerLinks;
import de.hsos.swa.mannschaftssport.entity.Player;
import de.hsos.swa.mannschaftssport.entity.PlayerKatalog;
import io.quarkus.hal.HalCollectionWrapper;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Spieler", description = "Verwaltung von Spielern")
public class PlayerResource {

    @Inject
    PlayerKatalog players;

    @GET
    @Operation(summary = "Alle Spieler abrufen", description = "Liefert alle Spieler, optional gefiltert nach Name.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Liste der Spieler",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PlayerDTO.class, type = SchemaType.ARRAY)))
    })
    @Produces({ MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON })
    public HalCollectionWrapper<PlayerDTO> alle(@QueryParam("name") String name,@Context UriInfo uriInfo) {
        List<Player> treffer = (name == null || name.isBlank()) ? players.alle() : players.finde(name);
        List<HalEntityWrapper<PlayerDTO>> eintraege = new ArrayList<>();
        for (Player p : treffer) {
            eintraege.add(PlayerLinks.mitLinks(p, uriInfo));
        }
        return new HalCollectionWrapper<>(eintraege, "players",
                Link.fromUri(uriInfo.getRequestUri()).rel("self").build());
    }

    @POST
    @Operation(summary = "Spieler anlegen", description = "Legt einen neuen Spieler an.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Spieler wurde angelegt",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PlayerDTO.class))),
            @APIResponse(responseCode = "400", description = "Ungültige Eingabe")
    })
    public Response anlegen(PlayerEingabeDTO eingabe, @Context UriInfo uriInfo) {
        if (eingabe == null) throw new IllegalArgumentException("Eingabe darf nicht leer sein");
        Player neu = players.speichere(new Player(eingabe.vorname(), eingabe.nachname(), eingabe.position(), null));
        return Response.created(uriInfo.getAbsolutePathBuilder().path(Long.toString(neu.getId())).build())
                .entity(PlayerDTO.von(neu))
                .build();
    }
}