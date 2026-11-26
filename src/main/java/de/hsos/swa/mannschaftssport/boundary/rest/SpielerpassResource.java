package de.hsos.swa.mannschaftssport.boundary.rest;

import de.hsos.swa.mannschaftssport.boundary.dto.PlayerDTO;
import de.hsos.swa.mannschaftssport.control.PlayerService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@ApplicationScoped
@Path("/players/{id}/spielerpass")
@Produces({MediaType.APPLICATION_JSON, "application/hal+json"})
@Tag(name = "Spielerpass", description = "Registrierung des Spielerpasses über das Modul Passwesen")
public class SpielerpassResource {

    @Inject
    PlayerService playerService;

    @POST
    @Operation(summary = "Spielerpass registrieren", description = "Vergibt über das Modul Passwesen eine Spieler-ID.")
    @APIResponse(responseCode = "200", description = "Spielerpass registriert")
    @APIResponse(responseCode = "404", description = "Spieler nicht gefunden")
    @APIResponse(responseCode = "409", description = "Spielerpass bereits registriert")
    public PlayerDTO registrieren(@PathParam("id") Long id) {
        return PlayerDTO.fromEntity(playerService.spielerpassRegistrieren(id));
    }

    public static Link link(Long playerId, UriInfo uriInfo) {
        return Link.fromUri(uriInfo.getBaseUriBuilder()
                        .path(SpielerpassResource.class)
                        .build(playerId))
                .rel("spielerpass-registrieren")
                .build();
    }
}
