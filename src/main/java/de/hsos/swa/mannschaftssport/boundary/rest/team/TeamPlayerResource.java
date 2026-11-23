package de.hsos.swa.mannschaftssport.boundary.rest.team;

import de.hsos.swa.mannschaftssport.boundary.dto.PlayerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.TeamDTO;
import de.hsos.swa.mannschaftssport.control.KaderVerwaltung;
import de.hsos.swa.mannschaftssport.entity.Player;
import de.hsos.swa.mannschaftssport.entity.PlayerKatalog;
import de.hsos.swa.mannschaftssport.entity.Team;
import de.hsos.swa.mannschaftssport.entity.TeamKatalog;
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

import java.util.ArrayList;
import java.util.List;

@Path("/teams/{id}/players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamPlayerResource {

    @Inject
    TeamKatalog teams;

    @Inject
    PlayerKatalog players;

    @Inject
    KaderVerwaltung kader;

    @GET
    public List<PlayerDTO> alle(@PathParam("id") long teamId) {
        Team team = teams.finde(teamId);
        if (team == null) throw new TeamNichtGefundenException(teamId);
        List<PlayerDTO> ergebnis = new ArrayList<>();
        for (long pid : team.getPlayerIds()) {
            Player p = players.finde(pid);
            if (p != null) ergebnis.add(PlayerDTO.von(p));
        }
        return ergebnis;
    }

    @PUT
    @Path("/{playerId}")
    public TeamDTO nimmAuf(@PathParam("id") long teamId, @PathParam("playerId") long playerId) {
        return TeamDTO.von(kader.nimmAuf(teamId, playerId));
    }

    @DELETE
    @Path("/{playerId}")
    public Response entferne(@PathParam("id") long teamId, @PathParam("playerId") long playerId) {
        kader.entferne(teamId, playerId);
        return Response.noContent().build();
    }
}