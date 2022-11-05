package eu.adainius.newsfocused.admin.site.back.infrastructure.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Path("index")
public class IndexController {
    @GET
    @Produces("text/html")
    public String index() throws IOException {
        String indexHtmlFile;

        InputStream resource = new ClassPathResource(
                "public/index.html").getInputStream();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource))) {
            indexHtmlFile = reader.lines()
                    .collect(Collectors.joining("\n"));
        }

        return indexHtmlFile;
    }
}
