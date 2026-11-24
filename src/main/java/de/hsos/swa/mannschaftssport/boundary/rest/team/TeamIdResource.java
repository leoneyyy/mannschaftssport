package de.hsos.swa.mannschaftssport.boundary.rest.team;

import de.hsos.swa.mannschaftssport.boundary.dto.TeamDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.TeamEingabeDTO;
import de.hsos.swa.mannschaftssport.boundary.rest.Kategorien;
import de.hsos.swa.mannschaftssport.boundary.rest.links.TeamLinks;
import de.hsos.swa.mannschaftssport.entity.Team;
import de.hsos.swa.mannschaftssport.entity.TeamKatalog;
import de.hsos.swa.mannschaftssport.entity.Teamkategorie;
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

@Path("/teams/{id}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Teams", description = "Verwaltung von Teams")
public class TeamIdResource {

    @Inject
    TeamKatalog teams;

    @GET
    @Operation(summary = "Team lesen", description = "Liefert ein einzelnes Team anhand seiner ID.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Team gefunden",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = TeamDTO.class))),
            @APIResponse(responseCode = "404", description = "Team nicht gefunden")
    })
    @Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
    public HalEntityWrapper<TeamDTO> lese(@PathParam("id") long id, @Context UriInfo uriInfo) {
        Team t = teams.finde(id);
        if (t == null) throw new TeamNichtGefundenException(id);
        return TeamLinks.mitLinks(t, uriInfo);
    }

    @PUT
    @Operation(summary = "Team ändern", description = "Aktualisiert Name und Kategorie eines Teams.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Team wurde aktualisiert",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = TeamDTO.class))),
            @APIResponse(responseCode = "400", description = "Ungültige Eingabe"),
            @APIResponse(responseCode = "404", description = "Team nicht gefunden")
    })
    @Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
    public HalEntityWrapper<TeamDTO> aendere(@PathParam("id") long id, TeamEingabeDTO eingabe, @Context UriInfo uriInfo) {
        if (eingabe == null) throw new IllegalArgumentException("Eingabe darf nicht leer sein");
        Team t = teams.finde(id);
        if (t == null) throw new TeamNichtGefundenException(id);
        Teamkategorie kategorie = Kategorien.parse(eingabe.kategorie());
        t.aendere(eingabe.name(), kategorie);
        return TeamLinks.mitLinks(teams.speichere(t), uriInfo);
    }

    @DELETE
    @Operation(summary = "Team löschen", description = "Löscht ein Team anhand seiner ID.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Team wurde gelöscht"),
            @APIResponse(responseCode = "404", description = "Team nicht gefunden")
    })
    public Response loesche(@PathParam("id") long id) {
        if (!teams.loesche(id)) throw new TeamNichtGefundenException(id);
        return Response.noContent().build();
    }
}