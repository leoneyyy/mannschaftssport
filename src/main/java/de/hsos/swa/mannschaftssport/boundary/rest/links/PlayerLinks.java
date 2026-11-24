package de.hsos.swa.mannschaftssport.boundary.rest.links;


import de.hsos.swa.mannschaftssport.boundary.dto.PlayerDTO;
import de.hsos.swa.mannschaftssport.entity.Player;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

public final class PlayerLinks {

    private PlayerLinks(){}

    public static HalEntityWrapper<PlayerDTO> mitLinks(Player p, UriInfo uriInfo){
        HalEntityWrapper<PlayerDTO> wrapper = new HalEntityWrapper<>(PlayerDTO.von(p));
        UriBuilder basis = uriInfo.getBaseUriBuilder().path("players").path(Long.toString(p.getId()));

        wrapper.addLinks(Link.fromUri(basis.clone().build()).rel("self").build());

        if(!p.hatSpielerID()){
            wrapper.addLinks(Link.fromUri(basis.clone().path("spielerpass").build()).rel("spierlpass-registrieren").build());
        }
        return wrapper;
    }
}
