package de.hsos.swa.mannschaftssport.boundary.rest.player;

import de.hsos.swa.mannschaftssport.boundary.dto.PlayerDTO;
import de.hsos.swa.mannschaftssport.boundary.rest.links.PlayerLinks;
import de.hsos.swa.mannschaftssport.control.SpielerpassVergabe;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

@Path("/players/{id}/spielerpass")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Spielerpass", description = "Registrierung eines Spielerpasses")
public class SpielerpassResource {

    @Inject
    SpielerpassVergabe vergabe;

    @POST
    @Operation(summary = "Spielerpass registrieren", description = "Registriert einen Spielerpass für den angegebenen Spieler.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Spielerpass wurde registriert",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PlayerDTO.class))),
            @APIResponse(responseCode = "404", description = "Spieler nicht gefunden"),
            @APIResponse(responseCode = "409", description = "Spielerpass bereits vorhanden")
    })
    @Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
    public HalEntityWrapper<PlayerDTO> registriere(@PathParam("id") long id, @Context UriInfo uriInfo) {
        return PlayerLinks.mitLinks(vergabe.registriere(id),uriInfo);
    }
}