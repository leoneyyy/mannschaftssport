package de.hsos.swa.mannschaftssport.boundary.rest;

import de.hsos.swa.mannschaftssport.entity.exception.NichtGefundenException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NichtGefundenMapper implements ExceptionMapper<NichtGefundenException> {
    @Override
    public Response toResponse(NichtGefundenException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new Fehler(e.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}