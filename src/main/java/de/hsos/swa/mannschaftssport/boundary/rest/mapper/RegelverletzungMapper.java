package de.hsos.swa.mannschaftssport.boundary.rest.mapper;

import de.hsos.swa.mannschaftssport.entity.exception.RegelverletzungException;
import io.quarkiverse.httpproblem.ExceptionMapperBase;
import io.quarkiverse.httpproblem.HttpProblem;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.net.URI;

@Provider
public class RegelverletzungMapper extends ExceptionMapperBase<RegelverletzungException> {

    @Override
    protected HttpProblem toProblem(RegelverletzungException e) {
        return HttpProblem.builder()
                .withType(URI.create("/problems/" + e.getTyp()))
                .withTitle(e.getTitel())
                .withStatus(Response.Status.CONFLICT)
                .withDetail(e.getMessage())
                .build();
    }
}
