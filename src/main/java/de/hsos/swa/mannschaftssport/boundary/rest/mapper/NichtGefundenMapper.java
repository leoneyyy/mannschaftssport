package de.hsos.swa.mannschaftssport.boundary.rest.mapper;

import de.hsos.swa.mannschaftssport.entity.exception.NichtGefundenException;
import io.quarkiverse.httpproblem.ExceptionMapperBase;
import io.quarkiverse.httpproblem.HttpProblem;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.net.URI;

@Provider
public class NichtGefundenMapper extends ExceptionMapperBase<NichtGefundenException> {

    @Override
    protected HttpProblem toProblem(NichtGefundenException e) {
        return HttpProblem.builder()
                .withType(URI.create("/problems/" + e.getTyp()))
                .withTitle(e.getTitel())
                .withStatus(Response.Status.NOT_FOUND)
                .withDetail(e.getMessage())
                .build();
    }
}
