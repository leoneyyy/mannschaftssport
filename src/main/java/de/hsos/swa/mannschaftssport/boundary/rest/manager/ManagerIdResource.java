package de.hsos.swa.mannschaftssport.boundary.rest.manager;

import de.hsos.swa.mannschaftssport.boundary.dto.ManagerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.ManagerEingabe;
import de.hsos.swa.mannschaftssport.entity.Manager;
import de.hsos.swa.mannschaftssport.entity.ManagerKatalog;
import de.hsos.swa.mannschaftssport.entity.exception.ManagerNichtGefundenException;
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

@Path("/managers/{id}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ManagerIdResource {

    @Inject
    ManagerKatalog managers;

    @GET
    public ManagerDTO lese(@PathParam("id") long id) {
        Manager m = managers.finde(id);
        if (m == null) throw new ManagerNichtGefundenException(id);
        return ManagerDTO.von(m);
    }

    @PUT
    public ManagerDTO aendere(@PathParam("id") long id, ManagerEingabe eingabe) {
        if (eingabe == null) throw new IllegalArgumentException("Eingabe darf nicht leer sein");
        Manager m = managers.finde(id);
        if (m == null) throw new ManagerNichtGefundenException(id);
        m.aendere(eingabe.vorname(), eingabe.nachname());
        return ManagerDTO.von(managers.speichere(m));
    }

    @DELETE
    public Response loesche(@PathParam("id") long id) {
        if (!managers.loesche(id)) throw new ManagerNichtGefundenException(id);
        return Response.noContent().build();
    }
}