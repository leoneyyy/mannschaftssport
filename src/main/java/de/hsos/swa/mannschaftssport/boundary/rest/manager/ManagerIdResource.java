package de.hsos.swa.mannschaftssport.boundary.rest.manager;

import de.hsos.swa.mannschaftssport.boundary.dto.ManagerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.ManagerEingabe;
import de.hsos.swa.mannschaftssport.boundary.rest.links.ManagerLinks;
import de.hsos.swa.mannschaftssport.control.KaderVerwaltung;
import de.hsos.swa.mannschaftssport.entity.Manager;
import de.hsos.swa.mannschaftssport.entity.ManagerKatalog;
import de.hsos.swa.mannschaftssport.entity.exception.ManagerNichtGefundenException;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

@Path("/managers/{id}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Manager", description = "Verwaltung von Managern")
public class ManagerIdResource {

    @Inject
    ManagerKatalog managers;

    @Inject
    KaderVerwaltung kader;

    @GET
    @Operation(summary = "Manager lesen", description = "Liefert einen einzelnen Manager anhand seiner ID.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Manager gefunden",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ManagerDTO.class))),
            @APIResponse(responseCode = "404", description = "Manager nicht gefunden")
    })
    @Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
    public HalEntityWrapper<ManagerDTO> lese(@PathParam("id") long id, @Context UriInfo uriInfo) {
        Manager m = managers.finde(id);
        if (m == null) throw new ManagerNichtGefundenException(id);
        return ManagerLinks.mitLinks(m, uriInfo);
    }

    @PUT
    @Operation(summary = "Manager ändern", description = "Aktualisiert Vor- und Nachname eines Managers.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Manager wurde aktualisiert",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ManagerDTO.class))),
            @APIResponse(responseCode = "400", description = "Ungültige Eingabe"),
            @APIResponse(responseCode = "404", description = "Manager nicht gefunden")
    })
    @Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
    public HalEntityWrapper<ManagerDTO> aendere(@PathParam("id") long id, ManagerEingabe eingabe, @Context UriInfo uriInfo) {
        if (eingabe == null) throw new IllegalArgumentException("Eingabe darf nicht leer sein");
        Manager m = managers.finde(id);
        if (m == null) throw new ManagerNichtGefundenException(id);
        m.aendere(eingabe.vorname(), eingabe.nachname());
        return ManagerLinks.mitLinks(managers.speichere(m), uriInfo);
    }

    @DELETE
    @Operation(summary = "Manager löschen", description = "Löscht einen Manager anhand seiner ID.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Manager wurde gelöscht"),
            @APIResponse(responseCode = "404", description = "Manager nicht gefunden")
    })
    public Response loesche(@PathParam("id") long id) {
        kader.loescheManager(id);
        return Response.noContent().build();
    }
}