package de.hsos.swa.mannschaftssport.boundary.rest.team;

import de.hsos.swa.mannschaftssport.boundary.dto.PlayerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.TeamDTO;
import de.hsos.swa.mannschaftssport.boundary.rest.links.PlayerLinks;
import de.hsos.swa.mannschaftssport.boundary.rest.links.TeamLinks;
import de.hsos.swa.mannschaftssport.control.KaderVerwaltung;
import de.hsos.swa.mannschaftssport.entity.Player;
import de.hsos.swa.mannschaftssport.entity.PlayerKatalog;
import de.hsos.swa.mannschaftssport.entity.Team;
import de.hsos.swa.mannschaftssport.entity.TeamKatalog;
import de.hsos.swa.mannschaftssport.entity.exception.TeamNichtGefundenException;
import io.quarkus.hal.HalCollectionWrapper;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Path("/teams/{id}/players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Team-Spieler", description = "Verwaltung des Kaders eines Teams")
public class TeamPlayerResource {

    @Inject
    TeamKatalog teams;

    @Inject
    PlayerKatalog players;

    @Inject
    KaderVerwaltung kader;

    @GET
    @Operation(summary = "Kader eines Teams abrufen", description = "Liefert alle Spieler eines Teams.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Liste der Spieler im Team",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PlayerDTO.class, type = SchemaType.ARRAY))),
            @APIResponse(responseCode = "404", description = "Team nicht gefunden")
    })
    @Produces({ MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON })
    public HalCollectionWrapper<PlayerDTO> alle(@PathParam("id") long teamId, @Context UriInfo uriInfo) {
        Team team = teams.finde(teamId);
        if (team == null) throw new TeamNichtGefundenException(teamId);
        List<HalEntityWrapper<PlayerDTO>> eintraege = new ArrayList<>();
        for (long pid : team.getPlayerIds()) {
            Player p = players.finde(pid);
            if (p != null) eintraege.add(PlayerLinks.mitLinks(p,uriInfo));
        }
        URI teamUri = uriInfo.getBaseUriBuilder().path(TeamIdResource.class).build(teamId);
        return new HalCollectionWrapper<>(eintraege, "players", Link.fromUri(uriInfo.getRequestUri()).rel("self").build(),Link.fromUri(teamUri).rel("team").build());
    }

    @PUT
    @Path("/{playerId}")
    @Operation(summary = "Spieler in Team aufnehmen", description = "Fügt einen Spieler zum Kader eines Teams hinzu.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Spieler aufgenommen",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = TeamDTO.class))),
            @APIResponse(responseCode = "404", description = "Team oder Spieler nicht gefunden"),
            @APIResponse(responseCode = "409", description = "Aufnahme nicht möglich (z.B. Regelverletzung)")
    })
    @Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
    public HalEntityWrapper<TeamDTO> nimmAuf(@PathParam("id") long teamId, @PathParam("playerId") long playerId, @Context UriInfo uriInfo) {
        return TeamLinks.mitLinks(kader.nimmAuf(teamId, playerId), uriInfo);
    }

    @DELETE
    @Path("/{playerId}")
    @Operation(summary = "Spieler aus Team entfernen", description = "Entfernt einen Spieler aus dem Kader eines Teams.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Spieler entfernt"),
            @APIResponse(responseCode = "404", description = "Team oder Spieler nicht gefunden")
    })
    public Response entferne(@PathParam("id") long teamId, @PathParam("playerId") long playerId) {
        kader.entferne(teamId, playerId);
        return Response.noContent().build();
    }
}