package eu.adainius.newsfocused.admin.site.back.auth;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

@Component
@Path("/auth")
public class AuthenticationController {
    @GET
    @Produces("text/plain")
    public String login() {
        return "Hello";
    }
}
