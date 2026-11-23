package de.hsos.swa.mannschaftssport.boundary.rest.manager;

import de.hsos.swa.mannschaftssport.boundary.dto.ManagerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.ManagerEingabe;
import de.hsos.swa.mannschaftssport.entity.Manager;
import de.hsos.swa.mannschaftssport.entity.ManagerKatalog;
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

@Path("/managers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ManagerResource {

    @Inject
    ManagerKatalog managers;

    @GET
    public List<ManagerDTO> alle(@QueryParam("name") String name) {
        List<Manager> treffer = (name == null || name.isBlank()) ? managers.alle() : managers.finde(name);
        return treffer.stream().map(ManagerDTO::von).toList();
    }

    @POST
    public Response anlegen(ManagerEingabe eingabe, @Context UriInfo uriInfo) {
        if (eingabe == null) throw new IllegalArgumentException("Eingabe darf nicht leer sein");
        Manager neu = managers.speichere(new Manager(eingabe.vorname(), eingabe.nachname()));
        return Response.created(uriInfo.getAbsolutePathBuilder().path(Long.toString(neu.getId())).build())
                .entity(ManagerDTO.von(neu))
                .build();
    }
}