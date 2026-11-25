package de.hsos.swa.mannschaftssport.boundary.rest;

import de.hsos.swa.mannschaftssport.boundary.dto.ManagerDTO;
import de.hsos.swa.mannschaftssport.boundary.dto.ManagerInputDTO;
import de.hsos.swa.mannschaftssport.control.ManagerService;
import de.hsos.swa.mannschaftssport.entity.Manager;
import io.quarkus.resteasy.reactive.links.InjectRestLinks;
import io.quarkus.resteasy.reactive.links.RestLink;
import io.quarkus.resteasy.reactive.links.RestLinkType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@ApplicationScoped
@Path("/managers")
@Produces({MediaType.APPLICATION_JSON, "application/hal+json"})
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Managers", description = "Verwaltung der Manager")
public class ManagerResource {

    @Inject
    ManagerService managerService;

    @GET
    @RestLink(rel = "list", entityType = ManagerDTO.class)
    @InjectRestLinks
    @Operation(summary = "Alle Manager abrufen")
    @APIResponse(responseCode = "200", description = "Liste aller Manager")
    public List<ManagerDTO> alle() {
        return managerService.alle().stream()
                .map(ManagerDTO::fromEntity)
                .toList();
    }

    @GET
    @Path("/{id}")
    @RestLink(rel = "self", entityType = ManagerDTO.class)
    @InjectRestLinks(RestLinkType.INSTANCE)
    @Operation(summary = "Einen Manager abrufen")
    @APIResponse(responseCode = "200", description = "Manager gefunden")
    @APIResponse(responseCode = "404", description = "Manager nicht gefunden")
    public ManagerDTO finde(@PathParam("id") Long id) {
        return ManagerDTO.fromEntity(managerService.finde(id));
    }

    @POST
    @Operation(summary = "Manager anlegen")
    @APIResponse(responseCode = "201", description = "Manager angelegt")
    @APIResponse(responseCode = "400", description = "Ungültige Eingabe")
    public Response anlegen(@Valid @NotNull ManagerInputDTO input, @Context UriInfo uriInfo) {
        Manager manager = managerService.anlegen(input.toEntity());
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(manager.getId()))
                .build();
        return Response.created(location)
                .entity(ManagerDTO.fromEntity(manager))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Manager ändern")
    @APIResponse(responseCode = "200", description = "Manager geändert")
    @APIResponse(responseCode = "400", description = "Ungültige Eingabe")
    @APIResponse(responseCode = "404", description = "Manager nicht gefunden")
    public ManagerDTO aendern(@PathParam("id") Long id, @Valid @NotNull ManagerInputDTO input) {
        return ManagerDTO.fromEntity(managerService.aendern(id, input.name()));
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Manager löschen", description = "Der Manager wird auch aus allen Teams entfernt.")
    @APIResponse(responseCode = "204", description = "Manager gelöscht")
    @APIResponse(responseCode = "404", description = "Manager nicht gefunden")
    public Response loeschen(@PathParam("id") Long id) {
        managerService.loeschen(id);
        return Response.noContent().build();
    }
}
