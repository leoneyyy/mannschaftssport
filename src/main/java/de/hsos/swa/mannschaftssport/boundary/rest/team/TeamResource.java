package de.hsos.swa.mannschaftssport.boundary.rest.team;

import de.hsos.swa.mannschaftssport.boundary.dto.TeamDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.TeamEingabeDTO;
import de.hsos.swa.mannschaftssport.boundary.rest.Kategorien;
import de.hsos.swa.mannschaftssport.boundary.rest.links.TeamLinks;
import de.hsos.swa.mannschaftssport.entity.Team;
import de.hsos.swa.mannschaftssport.entity.TeamKatalog;
import de.hsos.swa.mannschaftssport.entity.Teamkategorie;
import io.quarkus.hal.HalCollectionWrapper;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Path("/teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Teams", description = "Verwaltung von Teams")
public class TeamResource {

    @Inject
    TeamKatalog teams;

    @GET
    @Operation(summary = "Alle Teams abrufen", description = "Liefert alle Teams, optional gefiltert nach Name.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Liste der Teams",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = TeamDTO.class, type = SchemaType.ARRAY)))
    })
    @Produces({ MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON })
    public HalCollectionWrapper<TeamDTO> alle(@QueryParam("name") String name, @Context UriInfo uriInfo) {
        List<Team> treffer = (name == null || name.isBlank()) ? teams.alle() : teams.finde(name);
        List<HalEntityWrapper<TeamDTO>> eintraege = new ArrayList<>();
        for (Team t : treffer) {
            eintraege.add(TeamLinks.mitLinks(t, uriInfo));
        }
        return new HalCollectionWrapper<>(eintraege, "teams",
                Link.fromUri(uriInfo.getRequestUri()).rel("self").build());
    }

    @POST
    @Operation(summary = "Team anlegen", description = "Legt ein neues Team an.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Team wurde angelegt",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = TeamDTO.class))),
            @APIResponse(responseCode = "400", description = "Ungültige Eingabe")
    })
    public Response anlegen(TeamEingabeDTO eingabe, @Context UriInfo uriInfo) {
        if (eingabe == null) throw new IllegalArgumentException("Eingabe darf nicht leer sein");
        Teamkategorie kategorie = Kategorien.parse(eingabe.kategorie());
        Team neu = teams.speichere(new Team(0, eingabe.name(), kategorie, null, Set.of()));
        return Response.created(uriInfo.getAbsolutePathBuilder().path(Long.toString(neu.getId())).build())
                .entity(TeamDTO.von(neu))
                .build();
    }
}