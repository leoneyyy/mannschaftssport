package de.hsos.swa.mannschaftssport.boundary.rest.manager;

import de.hsos.swa.mannschaftssport.boundary.dto.ManagerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.ManagerEingabe;
import de.hsos.swa.mannschaftssport.boundary.rest.links.ManagerLinks;
import de.hsos.swa.mannschaftssport.entity.Manager;
import de.hsos.swa.mannschaftssport.entity.ManagerKatalog;
import io.quarkus.hal.HalCollectionWrapper;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/managers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Manager", description = "Verwaltung von Managern")
public class ManagerResource {

    @Inject
    ManagerKatalog managers;

    @GET
    @Operation(summary = "Alle Manager abrufen", description = "Liefert alle Manager, optional gefiltert nach Name.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Liste der Manager",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ManagerDTO.class, type = SchemaType.ARRAY)))
    })
    @Produces({ MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON })
    public HalCollectionWrapper<ManagerDTO> alle(@QueryParam("name") String name, @Context UriInfo uriInfo) {
        List<Manager> treffer = (name == null || name.isBlank()) ? managers.alle() : managers.finde(name);
        List<HalEntityWrapper<ManagerDTO>> eintraege = new ArrayList<>();
        for (Manager m : treffer) {
            eintraege.add(ManagerLinks.mitLinks(m, uriInfo));
        }
        return new HalCollectionWrapper<>(eintraege, "managers",
                Link.fromUri(uriInfo.getRequestUri()).rel("self").build());
    }

    @POST
    @Operation(summary = "Manager anlegen", description = "Legt einen neuen Manager an.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Manager wurde angelegt",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ManagerDTO.class))),
            @APIResponse(responseCode = "400", description = "Ungültige Eingabe")
    })
    public Response anlegen(ManagerEingabe eingabe, @Context UriInfo uriInfo) {
        if (eingabe == null) throw new IllegalArgumentException("Eingabe darf nicht leer sein");
        Manager neu = managers.speichere(new Manager(eingabe.vorname(), eingabe.nachname()));
        return Response.created(uriInfo.getAbsolutePathBuilder().path(Long.toString(neu.getId())).build())
                .entity(ManagerDTO.von(neu))
                .build();
    }
}