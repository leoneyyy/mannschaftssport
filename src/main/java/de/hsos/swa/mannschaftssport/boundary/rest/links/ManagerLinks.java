package de.hsos.swa.mannschaftssport.boundary.rest.links;


import de.hsos.swa.mannschaftssport.boundary.dto.ManagerDTO;
import de.hsos.swa.mannschaftssport.boundary.rest.manager.ManagerIdResource;
import de.hsos.swa.mannschaftssport.entity.Manager;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.UriInfo;

public final class ManagerLinks {

    private ManagerLinks(){}

    public static HalEntityWrapper<ManagerDTO> mitLinks(Manager m, UriInfo uriInfo){
        HalEntityWrapper<ManagerDTO> wrapper = new HalEntityWrapper<>(ManagerDTO.von(m));

        wrapper.addLinks(Link.fromUri(uriInfo.getBaseUriBuilder().path(ManagerIdResource.class).build(m.getId())).rel("self").build());

        return wrapper;
    }
}
