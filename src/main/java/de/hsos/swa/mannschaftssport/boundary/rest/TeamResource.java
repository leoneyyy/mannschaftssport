package de.hsos.swa.mannschaftssport.boundary.rest;

import de.hsos.swa.mannschaftssport.boundary.dto.ManagerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.PlayerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.ReferenceDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.TeamDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.TeamInputDTO;
import de.hsos.swa.mannschaftssport.control.TeamService;
import de.hsos.swa.mannschaftssport.entity.Team;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@ApplicationScoped
@Path("/teams")
@Produces({MediaType.APPLICATION_JSON, "application/hal+json"})
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Teams", description = "Verwaltung der Teams mit Manager und Spielern")
public class TeamResource {

    @Inject
    TeamService teamService;

    @GET
    @RestLink(rel = "list", entityType = TeamDTO.class)
    @InjectRestLinks
    @Operation(summary = "Alle Teams abrufen")
    @APIResponse(responseCode = "200", description = "Liste aller Teams")
    public List<TeamDTO> alle() {
        return teamService.alle().stream()
                .map(TeamDTO::fromEntity)
                .toList();
    }

    @GET
    @Path("/{id}")
    @RestLink(rel = "self", entityType = TeamDTO.class)
    @InjectRestLinks(RestLinkType.INSTANCE)
    @Operation(summary = "Ein Team abrufen", description = "Enthält Links zum Manager und zu den Spielern des Teams.")
    @APIResponse(responseCode = "200", description = "Team gefunden")
    @APIResponse(responseCode = "404", description = "Team nicht gefunden")
    public TeamDTO finde(@PathParam("id") Long id) {
        return TeamDTO.fromEntity(teamService.finde(id));
    }

    @POST
    @Operation(summary = "Team anlegen")
    @APIResponse(responseCode = "201", description = "Team angelegt")
    @APIResponse(responseCode = "400", description = "Ungültige Eingabe")
    public Response anlegen(@Valid @NotNull TeamInputDTO input, @Context UriInfo uriInfo) {
        Team team = teamService.anlegen(input.toEntity());
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(team.getId()))
                .build();
        return Response.created(location)
                .entity(TeamDTO.fromEntity(team))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Team ändern")
    @APIResponse(responseCode = "200", description = "Team geändert")
    @APIResponse(responseCode = "400", description = "Ungültige Eingabe")
    @APIResponse(responseCode = "404", description = "Team nicht gefunden")
    public TeamDTO aendern(@PathParam("id") Long id, @Valid @NotNull TeamInputDTO input) {
        return TeamDTO.fromEntity(teamService.aendern(id, input.name(), input.category()));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Team löschen")
    @APIResponse(responseCode = "204", description = "Team gelöscht")
    @APIResponse(responseCode = "404", description = "Team nicht gefunden")
    public Response loeschen(@PathParam("id") Long id) {
        teamService.loeschen(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/manager")
    @RestLink(rel = "manager", entityType = TeamDTO.class)
    @InjectRestLinks(RestLinkType.INSTANCE)
    @Operation(summary = "Manager des Teams abrufen")
    @APIResponse(responseCode = "200", description = "Manager des Teams")
    @APIResponse(responseCode = "404", description = "Team nicht gefunden oder kein Manager gesetzt")
    public ManagerDTO manager(@PathParam("id") Long id) {
        return ManagerDTO.fromEntity(teamService.findeManager(id));
    }

    @PUT
    @Path("/{id}/relationships/manager")
    @Operation(summary = "Manager des Teams setzen")
    @APIResponse(responseCode = "204", description = "Manager gesetzt")
    @APIResponse(responseCode = "400", description = "Ungültige Eingabe")
    @APIResponse(responseCode = "404", description = "Team oder Manager nicht gefunden")
    public Response setzeManager(@PathParam("id") Long id, @Valid @NotNull ReferenceDTO manager) {
        teamService.setzeManager(id, manager.id());
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}/relationships/manager")
    @Operation(summary = "Manager des Teams entfernen")
    @APIResponse(responseCode = "204", description = "Manager entfernt")
    @APIResponse(responseCode = "404", description = "Team nicht gefunden")
    public Response entferneManager(@PathParam("id") Long id) {
        teamService.entferneManager(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/players")
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
    @Path("/{id}/relationships/players")
    @Operation(summary = "Spieler zum Team hinzufügen", description = "Nur Spieler mit Spieler-ID können einem Team zugeordnet werden.")
    @APIResponse(responseCode = "204", description = "Spieler hinzugefügt")
    @APIResponse(responseCode = "400", description = "Ungültige Eingabe")
    @APIResponse(responseCode = "404", description = "Team oder Spieler nicht gefunden")
    @APIResponse(responseCode = "409", description = "Spieler hat noch keinen Spielerpass")
    public Response fuegeSpielerHinzu(@PathParam("id") Long id, @Valid @NotNull ReferenceDTO player) {
        teamService.fuegeSpielerHinzu(id, player.id());
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}/relationships/players/{playerId}")
    @Operation(summary = "Spieler aus dem Team entfernen")
    @APIResponse(responseCode = "204", description = "Spieler entfernt")
    @APIResponse(responseCode = "404", description = "Team nicht gefunden oder Spieler nicht im Team")
    public Response entferneSpieler(@PathParam("id") Long id, @PathParam("playerId") Long playerId) {
        teamService.entferneSpieler(id, playerId);
        return Response.noContent().build();
    }
}
