package de.hsos.swa.mannschaftssport.boundary.rest.team;

import de.hsos.swa.mannschaftssport.boundary.dto.TeamDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.TeamEingabeDTO;
import de.hsos.swa.mannschaftssport.boundary.rest.Kategorien;
import de.hsos.swa.mannschaftssport.entity.Team;
import de.hsos.swa.mannschaftssport.entity.TeamKatalog;
import de.hsos.swa.mannschaftssport.entity.Teamkategorie;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.List;
import java.util.Set;

@Path("/teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamResource {

    @Inject
    TeamKatalog teams;

    @GET
    public List<TeamDTO> alle(@QueryParam("name") String name) {
        List<Team> treffer = (name == null || name.isBlank()) ? teams.alle() : teams.finde(name);
        return treffer.stream().map(TeamDTO::von).toList();
    }

    @POST
    public Response anlegen(TeamEingabeDTO eingabe, @Context UriInfo uriInfo) {
        if (eingabe == null) throw new IllegalArgumentException("Eingabe darf nicht leer sein");
        Teamkategorie kategorie = Kategorien.parse(eingabe.kategorie());
        Team neu = teams.speichere(new Team(0, eingabe.name(), kategorie, null, Set.of()));
        return Response.created(uriInfo.getAbsolutePathBuilder().path(Long.toString(neu.getId())).build())
                .entity(TeamDTO.von(neu))
                .build();
    }
}