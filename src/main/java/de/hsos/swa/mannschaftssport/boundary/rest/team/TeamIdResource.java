package de.hsos.swa.mannschaftssport.boundary.rest.team;

import de.hsos.swa.mannschaftssport.boundary.dto.TeamDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.TeamEingabeDTO;
import de.hsos.swa.mannschaftssport.boundary.rest.Kategorien;
import de.hsos.swa.mannschaftssport.entity.Team;
import de.hsos.swa.mannschaftssport.entity.TeamKatalog;
import de.hsos.swa.mannschaftssport.entity.Teamkategorie;
import de.hsos.swa.mannschaftssport.entity.exception.TeamNichtGefundenException;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/teams/{id}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamIdResource {

    @Inject
    TeamKatalog teams;

    @GET
    public TeamDTO lese(@PathParam("id") long id) {
        Team t = teams.finde(id);
        if (t == null) throw new TeamNichtGefundenException(id);
        return TeamDTO.von(t);
    }

    @PUT
    public TeamDTO aendere(@PathParam("id") long id, TeamEingabeDTO eingabe) {
        if (eingabe == null) throw new IllegalArgumentException("Eingabe darf nicht leer sein");
        Team t = teams.finde(id);
        if (t == null) throw new TeamNichtGefundenException(id);
        Teamkategorie kategorie = Kategorien.parse(eingabe.kategorie());
        t.aendere(eingabe.name(), kategorie);
        return TeamDTO.von(teams.speichere(t));
    }

    @DELETE
    public Response loesche(@PathParam("id") long id) {
        if (!teams.loesche(id)) throw new TeamNichtGefundenException(id);
        return Response.noContent().build();
    }
}