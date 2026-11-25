package de.hsos.swa.mannschaftssport.boundary.rest;

import de.hsos.swa.mannschaftssport.boundary.dto.PlayerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.PlayerInputDTO;
import de.hsos.swa.mannschaftssport.control.PlayerService;
import de.hsos.swa.mannschaftssport.entity.Player;
import io.quarkus.hal.HalEntityWrapper;
import io.quarkus.hal.HalService;
import io.quarkus.resteasy.reactive.links.InjectRestLinks;
import io.quarkus.resteasy.reactive.links.RestLink;
import io.quarkus.resteasy.reactive.links.RestLinkType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@ApplicationScoped
@Path("/players")
@Produces({MediaType.APPLICATION_JSON, "application/hal+json"})
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Players", description = "Verwaltung der Spieler und ihrer Spielerpässe")
public class PlayerResource {

    @Inject
    PlayerService playerService;

    @Inject
    HalService halService;

    @GET
    @RestLink(rel = "list", entityType = PlayerDTO.class)
    @InjectRestLinks
    @Operation(summary = "Alle Spieler abrufen")
    @APIResponse(responseCode = "200", description = "Liste aller Spieler")
    public List<PlayerDTO> alle() {
        return playerService.alle().stream()
                .map(PlayerDTO::fromEntity)
                .toList();
    }

    @GET
    @Path("/{id}")
    @RestLink(rel = "self", entityType = PlayerDTO.class)
    @InjectRestLinks(RestLinkType.INSTANCE)
    @Operation(summary = "Einen Spieler abrufen",
            description = "Hat der Spieler noch keine Spieler-ID, enthält die Antwort den Link spielerpass-registrieren.")
    @APIResponse(responseCode = "200", description = "Spieler gefunden")
    @APIResponse(responseCode = "404", description = "Spieler nicht gefunden")
    public Response finde(@PathParam("id") Long id, @Context UriInfo uriInfo) {
        PlayerDTO dto = PlayerDTO.fromEntity(playerService.finde(id));
        HalEntityWrapper<PlayerDTO> hal = halService.toHalWrapper(dto);
        Response.ResponseBuilder antwort = Response.ok(hal);

        if (dto.spielerId() == null) {
            Link link = spielerpassLink(id, uriInfo);
            hal.addLinks(link);
            antwort.links(link);
        }
        return antwort.build();
    }

    @POST
    @Operation(summary = "Spieler anlegen")
    @APIResponse(responseCode = "201", description = "Spieler angelegt")
    @APIResponse(responseCode = "400", description = "Ungültige Eingabe")
    public Response anlegen(@Valid @NotNull PlayerInputDTO input, @Context UriInfo uriInfo) {
        Player player = playerService.anlegen(input.toEntity());
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(player.getId()))
                .build();
        return Response.created(location)
                .entity(PlayerDTO.fromEntity(player))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Spieler ändern")
    @APIResponse(responseCode = "200", description = "Spieler geändert")
    @APIResponse(responseCode = "400", description = "Ungültige Eingabe")
    @APIResponse(responseCode = "404", description = "Spieler nicht gefunden")
    public PlayerDTO aendern(@PathParam("id") Long id, @Valid @NotNull PlayerInputDTO input) {
        return PlayerDTO.fromEntity(playerService.aendern(id, input.name()));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Spieler löschen", description = "Der Spieler wird auch aus allen Teams entfernt.")
    @APIResponse(responseCode = "204", description = "Spieler gelöscht")
    @APIResponse(responseCode = "404", description = "Spieler nicht gefunden")
    public Response loeschen(@PathParam("id") Long id) {
        playerService.loeschen(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/spielerpass")
    @Operation(summary = "Spielerpass registrieren", description = "Vergibt über das Modul Passwesen eine Spieler-ID.")
    @APIResponse(responseCode = "200", description = "Spielerpass registriert")
    @APIResponse(responseCode = "404", description = "Spieler nicht gefunden")
    @APIResponse(responseCode = "409", description = "Spielerpass bereits registriert")
    public PlayerDTO spielerpassRegistrieren(@PathParam("id") Long id) {
        return PlayerDTO.fromEntity(playerService.spielerpassRegistrieren(id));
    }

    private Link spielerpassLink(Long id, UriInfo uriInfo) {
        URI uri = uriInfo.getBaseUriBuilder()
                .path(PlayerResource.class)
                .path(String.valueOf(id))
                .path("spielerpass")
                .build();
        return Link.fromUri(uri).rel("spielerpass-registrieren").build();
    }
}
