package de.hsos.swa.mannschaftssport.boundary.rest.team;

import de.hsos.swa.mannschaftssport.boundary.dto.ManagerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.TeamDTO;
import de.hsos.swa.mannschaftssport.boundary.rest.links.ManagerLinks;
import de.hsos.swa.mannschaftssport.boundary.rest.links.TeamLinks;
import de.hsos.swa.mannschaftssport.control.KaderVerwaltung;
import de.hsos.swa.mannschaftssport.entity.Manager;
import de.hsos.swa.mannschaftssport.entity.ManagerKatalog;
import de.hsos.swa.mannschaftssport.entity.Team;
import de.hsos.swa.mannschaftssport.entity.TeamKatalog;
import de.hsos.swa.mannschaftssport.entity.exception.KeinManagerException;
import de.hsos.swa.mannschaftssport.entity.exception.ManagerNichtGefundenException;
import de.hsos.swa.mannschaftssport.entity.exception.TeamNichtGefundenException;
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

@Path("/teams/{id}/manager")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Team-Manager", description = "Verwaltung des Managers eines Teams")
public class TeamManagerResource {

    @Inject
    TeamKatalog teams;

    @Inject
    ManagerKatalog managers;

    @Inject
    KaderVerwaltung kader;

    @GET
    @Operation(summary = "Manager eines Teams abrufen", description = "Liefert den Manager des angegebenen Teams.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Manager gefunden",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ManagerDTO.class))),
            @APIResponse(responseCode = "404", description = "Team oder Manager nicht gefunden")
    })
    @Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
    public HalEntityWrapper<ManagerDTO> lese(@PathParam("id") long teamId, @Context UriInfo uriInfo) {
        Team team = teams.finde(teamId);
        if (team == null) throw new TeamNichtGefundenException(teamId);
        Long managerId = team.getManagerId();
        if (managerId == null) throw new KeinManagerException(teamId);
        Manager manager = managers.finde(managerId);
        if (manager == null) throw new ManagerNichtGefundenException(managerId);
        return ManagerLinks.mitLinks(manager, uriInfo);
    }

    @PUT
    @Path("/{managerId}")
    @Operation(summary = "Manager eines Teams setzen", description = "Setzt oder ersetzt den Manager eines Teams.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Manager gesetzt",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = TeamDTO.class))),
            @APIResponse(responseCode = "404", description = "Team oder Manager nicht gefunden")
    })
    @Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
    public HalEntityWrapper<TeamDTO> setze(@PathParam("id") long teamId, @PathParam("managerId") long managerId, @Context UriInfo uriInfo) {
        return TeamLinks.mitLinks(kader.setzeManager(teamId, managerId), uriInfo);
    }

    @DELETE
    @Operation(summary = "Manager eines Teams entfernen", description = "Entfernt den Manager des Teams.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Manager entfernt"),
            @APIResponse(responseCode = "404", description = "Team nicht gefunden")
    })
    public Response entferne(@PathParam("id") long teamId) {
        kader.entferneManager(teamId);
        return Response.noContent().build();
    }
}