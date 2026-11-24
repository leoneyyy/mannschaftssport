package de.hsos.swa.mannschaftssport.boundary.rest.player;

import de.hsos.swa.mannschaftssport.boundary.dto.PlayerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.PlayerEingabeDTO;
import de.hsos.swa.mannschaftssport.boundary.rest.links.PlayerLinks;
import de.hsos.swa.mannschaftssport.entity.Player;
import de.hsos.swa.mannschaftssport.entity.PlayerKatalog;
import de.hsos.swa.mannschaftssport.entity.exception.PlayerNichtGefundenException;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

@Path("/players/{id}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Spieler", description = "Verwaltung von Spielern")
public class PlayerIdResource {

    @Inject
    PlayerKatalog players;

    @GET
    @Operation(summary = "Spieler lesen", description = "Liefert einen einzelnen Spieler anhand seiner ID.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Spieler gefunden",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PlayerDTO.class))),
            @APIResponse(responseCode = "404", description = "Spieler nicht gefunden")
    })
    @Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
    public HalEntityWrapper<PlayerDTO> lese(@PathParam("id") long id, @Context UriInfo uriInfo) {
        Player p = players.finde(id);
        if (p == null) throw new PlayerNichtGefundenException(id);
        return PlayerLinks.mitLinks(p, uriInfo);
    }

    @PUT
    @Operation(summary = "Spieler ändern", description = "Aktualisiert Vorname, Nachname und Position eines Spielers.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Spieler wurde aktualisiert",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PlayerDTO.class))),
            @APIResponse(responseCode = "400", description = "Ungültige Eingabe"),
            @APIResponse(responseCode = "404", description = "Spieler nicht gefunden")
    })
    @Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
    public HalEntityWrapper<PlayerDTO> aendere(@PathParam("id") long id, PlayerEingabeDTO eingabe, @Context UriInfo uriInfo) {
        if (eingabe == null) throw new IllegalArgumentException("Eingabe darf nicht leer sein");
        Player p = players.finde(id);
        if (p == null) throw new PlayerNichtGefundenException(id);
        p.aendere(eingabe.vorname(), eingabe.nachname(), eingabe.position());
        return PlayerLinks.mitLinks(p,uriInfo);
    }

    @DELETE
    @Operation(summary = "Spieler löschen", description = "Löscht einen Spieler anhand seiner ID.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Spieler wurde gelöscht"),
            @APIResponse(responseCode = "404", description = "Spieler nicht gefunden")
    })
    public Response loesche(@PathParam("id") long id) {
        if (!players.loesche(id)) throw new PlayerNichtGefundenException(id);
        return Response.noContent().build();
    }
}