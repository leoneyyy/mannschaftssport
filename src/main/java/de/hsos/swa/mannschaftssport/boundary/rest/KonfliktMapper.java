package de.hsos.swa.mannschaftssport.boundary.rest;

import de.hsos.swa.mannschaftssport.entity.exception.RegelverletzungException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class KonfliktMapper implements ExceptionMapper<RegelverletzungException> {
    @Override
    public Response toResponse(RegelverletzungException e) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new Fehler(e.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}