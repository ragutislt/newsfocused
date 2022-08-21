package eu.adainius.newsfocused.admin.site.back.config;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Provider
@Configuration
@Slf4j
public class JerseyNotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException exception) {
        log.info("Treating exception {}", exception.getMessage());
        // TODO implement custom error page ? Is it worth it for the API? Still better than default one from container
        return Response.status(Status.NOT_FOUND).build();
    }
}
