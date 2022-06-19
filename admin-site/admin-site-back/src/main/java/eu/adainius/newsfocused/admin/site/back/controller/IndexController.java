package eu.adainius.newsfocused.admin.site.back.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import eu.adainius.newsfocused.admin.site.back.config.Roles;
import lombok.extern.slf4j.Slf4j;

@Component
@Path("api/index")
@Slf4j
public class IndexController {
    @GET
    @Produces("text/plain")
    @RolesAllowed(Roles.ROLE_ADMIN)
    public String index() throws IOException {
        InputStream resource = new ClassPathResource(
            "public/index.html").getInputStream();
          try ( BufferedReader reader = new BufferedReader(
            new InputStreamReader(resource)) ) {
              String indexHtmlFile = reader.lines()
                .collect(Collectors.joining("\n"));

            log.info(indexHtmlFile);
          }

        return "Hi";
    }
}
