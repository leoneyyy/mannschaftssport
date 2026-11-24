package de.hsos.swa.mannschaftssport.boundary.rest.links;


import de.hsos.swa.mannschaftssport.boundary.dto.TeamDTO;
import de.hsos.swa.mannschaftssport.entity.Team;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

public final class TeamLinks {

    private TeamLinks(){}

    public static HalEntityWrapper<TeamDTO> mitLinks(Team t, UriInfo uriInfo){
        HalEntityWrapper<TeamDTO> wrapper = new HalEntityWrapper<>(TeamDTO.von(t));
        UriBuilder basis = uriInfo.getBaseUriBuilder().path("teams").path(Long.toString(t.getId()));

        wrapper.addLinks(Link.fromUri(basis.clone().build()).rel("self").build());
        wrapper.addLinks(Link.fromUri(basis.clone().path("players").build()).rel("players").build());
        wrapper.addLinks(Link.fromUri(basis.clone().path("manager").build()).rel("manager").build());

        return wrapper;
    }
}