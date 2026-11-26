package de.hsos.swa.mannschaftssport.boundary.rest;

import de.hsos.swa.mannschaftssport.boundary.dto.PlayerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.ReferenceDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.TeamDTO;
import de.hsos.swa.mannschaftssport.control.TeamService;
import io.quarkus.resteasy.reactive.links.InjectRestLinks;
import io.quarkus.resteasy.reactive.links.RestLink;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@ApplicationScoped
@Path("/teams/{id}")
@Produces({MediaType.APPLICATION_JSON, "application/hal+json"})
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Kader", description = "Spieler in ein Team aufnehmen und aus einem Team entfernen")
public class KaderResource {

    @Inject
    TeamService teamService;

    @GET
    @Path("/players")
    @RestLink(rel = "players", entityType = TeamDTO.class)
    @InjectRestLinks
    @Operation(summary = "Spieler des Teams abrufen")
    @APIResponse(responseCode = "200", description = "Liste der Spieler des Teams")
    @APIResponse(responseCode = "404", description = "Team nicht gefunden")
    public List<PlayerDTO> spieler(@PathParam("id") Long id) {
        return teamService.findeSpieler(id).stream()
                .map(PlayerDTO::fromEntity)
                .toList();
    }

    @POST
    @Path("/relationships/players")
    @Operation(summary = "Spieler zum Team hinzufügen",
            description = "Nur Spieler mit Spieler-ID können einem Team zugeordnet werden. Ein Spieler kann nur in einem Team sein.")
    @APIResponse(responseCode = "204", description = "Spieler hinzugefügt")
    @APIResponse(responseCode = "400", description = "Ungültige Eingabe")
    @APIResponse(responseCode = "404", description = "Team oder Spieler nicht gefunden")
    @APIResponse(responseCode = "409", description = "Spieler hat noch keinen Spielerpass oder ist bereits in einem anderen Team")
    public Response fuegeSpielerHinzu(@PathParam("id") Long id, @Valid @NotNull ReferenceDTO player) {
        teamService.fuegeSpielerHinzu(id, player.id());
        return Response.noContent().build();
    }

    @DELETE
    @Path("/relationships/players/{playerId}")
    @Operation(summary = "Spieler aus dem Team entfernen")
    @APIResponse(responseCode = "204", description = "Spieler entfernt")
    @APIResponse(responseCode = "404", description = "Team nicht gefunden oder Spieler nicht im Team")
    public Response entferneSpieler(@PathParam("id") Long id, @PathParam("playerId") Long playerId) {
        teamService.entferneSpieler(id, playerId);
        return Response.noContent().build();
    }
}
