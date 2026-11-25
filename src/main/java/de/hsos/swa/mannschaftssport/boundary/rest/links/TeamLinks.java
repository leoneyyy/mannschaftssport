package de.hsos.swa.mannschaftssport.boundary.rest.links;


import de.hsos.swa.mannschaftssport.boundary.dto.TeamDTO;
import de.hsos.swa.mannschaftssport.boundary.rest.team.TeamIdResource;
import de.hsos.swa.mannschaftssport.boundary.rest.team.TeamManagerResource;
import de.hsos.swa.mannschaftssport.boundary.rest.team.TeamPlayerResource;
import de.hsos.swa.mannschaftssport.entity.Team;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.UriInfo;

public final class TeamLinks {

    private TeamLinks(){}

    public static HalEntityWrapper<TeamDTO> mitLinks(Team t, UriInfo uriInfo){
        HalEntityWrapper<TeamDTO> wrapper = new HalEntityWrapper<>(TeamDTO.von(t));
        long id = t.getId();

        wrapper.addLinks(Link.fromUri(uriInfo.getBaseUriBuilder().path(TeamIdResource.class).build(id)).rel("self").build());
        wrapper.addLinks(Link.fromUri(uriInfo.getBaseUriBuilder().path(TeamPlayerResource.class).build(id)).rel("players").build());

        // nur anbieten, wenn es auch einen Manager gibt, sonst fuehrt der Link auf eine 404
        if (t.hatManager()) {
            wrapper.addLinks(Link.fromUri(uriInfo.getBaseUriBuilder().path(TeamManagerResource.class).build(id)).rel("manager").build());
        }

        return wrapper;
    }
}
