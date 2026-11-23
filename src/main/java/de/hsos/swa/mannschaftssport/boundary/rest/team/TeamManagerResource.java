package de.hsos.swa.mannschaftssport.boundary.rest.team;

import de.hsos.swa.mannschaftssport.boundary.dto.ManagerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.TeamDTO;
import de.hsos.swa.mannschaftssport.control.KaderVerwaltung;
import de.hsos.swa.mannschaftssport.entity.Manager;
import de.hsos.swa.mannschaftssport.entity.ManagerKatalog;
import de.hsos.swa.mannschaftssport.entity.Team;
import de.hsos.swa.mannschaftssport.entity.TeamKatalog;
import de.hsos.swa.mannschaftssport.entity.exception.ManagerNichtGefundenException;
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

@Path("/teams/{id}/manager")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamManagerResource {

    @Inject
    TeamKatalog teams;

    @Inject
    ManagerKatalog managers;

    @Inject
    KaderVerwaltung kader;

    @GET
    public ManagerDTO lese(@PathParam("id") long teamId) {
        Team team = teams.finde(teamId);
        if (team == null) throw new TeamNichtGefundenException(teamId);
        Long managerId = team.getManagerId();
        if (managerId == null) throw new ManagerNichtGefundenException(0);
        Manager manager = managers.finde(managerId);
        if (manager == null) throw new ManagerNichtGefundenException(managerId);
        return ManagerDTO.von(manager);
    }

    @PUT
    @Path("/{managerId}")
    public TeamDTO setze(@PathParam("id") long teamId, @PathParam("managerId") long managerId) {
        return TeamDTO.von(kader.setzeManager(teamId, managerId));
    }

    @DELETE
    public Response entferne(@PathParam("id") long teamId) {
        kader.entferneManager(teamId);
        return Response.noContent().build();
    }
}