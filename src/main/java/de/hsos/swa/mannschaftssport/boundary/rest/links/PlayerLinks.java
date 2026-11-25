package de.hsos.swa.mannschaftssport.boundary.rest.links;


import de.hsos.swa.mannschaftssport.boundary.dto.PlayerDTO;
import de.hsos.swa.mannschaftssport.boundary.rest.player.PlayerIdResource;
import de.hsos.swa.mannschaftssport.boundary.rest.player.SpielerpassResource;
import de.hsos.swa.mannschaftssport.entity.Player;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.UriInfo;

public final class PlayerLinks {

    private PlayerLinks(){}

    public static HalEntityWrapper<PlayerDTO> mitLinks(Player p, UriInfo uriInfo){
        HalEntityWrapper<PlayerDTO> wrapper = new HalEntityWrapper<>(PlayerDTO.von(p));
        long id = p.getId();

        wrapper.addLinks(Link.fromUri(uriInfo.getBaseUriBuilder().path(PlayerIdResource.class).build(id)).rel("self").build());

        // Zustandsabhaengig: Registrierung nur anbieten, solange noch kein Spielerpass existiert
        if(!p.hatSpielerID()){
            wrapper.addLinks(Link.fromUri(uriInfo.getBaseUriBuilder().path(SpielerpassResource.class).build(id)).rel("spielerpass-registrieren").build());
        }
        return wrapper;
    }
}
