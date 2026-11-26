package de.hsos.swa.mannschaftssport.boundary.rest;

import de.hsos.swa.mannschaftssport.boundary.dto.ManagerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.ReferenceDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.TeamDTO;
import de.hsos.swa.mannschaftssport.control.TeamService;
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
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@ApplicationScoped
@Path("/teams/{id}")
@Produces({MediaType.APPLICATION_JSON, "application/hal+json"})
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Team-Manager", description = "Manager einem Team zuordnen und entfernen")
public class TeamManagerResource {

    @Inject
    TeamService teamService;

    @GET
    @Path("/manager")
    @RestLink(rel = "manager", entityType = TeamDTO.class)
    @InjectRestLinks(RestLinkType.INSTANCE)
    @Operation(summary = "Manager des Teams abrufen")
    @APIResponse(responseCode = "200", description = "Manager des Teams")
    @APIResponse(responseCode = "404", description = "Team nicht gefunden oder kein Manager gesetzt")
    public ManagerDTO manager(@PathParam("id") Long id) {
        return ManagerDTO.fromEntity(teamService.findeManager(id));
    }

    @PUT
    @Path("/relationships/manager")
    @Operation(summary = "Manager des Teams setzen")
    @APIResponse(responseCode = "204", description = "Manager gesetzt")
    @APIResponse(responseCode = "400", description = "Ungültige Eingabe")
    @APIResponse(responseCode = "404", description = "Team oder Manager nicht gefunden")
    public Response setzeManager(@PathParam("id") Long id, @Valid @NotNull ReferenceDTO manager) {
        teamService.setzeManager(id, manager.id());
        return Response.noContent().build();
    }

    @DELETE
    @Path("/relationships/manager")
    @Operation(summary = "Manager des Teams entfernen")
    @APIResponse(responseCode = "204", description = "Manager entfernt")
    @APIResponse(responseCode = "404", description = "Team nicht gefunden")
    public Response entferneManager(@PathParam("id") Long id) {
        teamService.entferneManager(id);
        return Response.noContent().build();
    }
}
