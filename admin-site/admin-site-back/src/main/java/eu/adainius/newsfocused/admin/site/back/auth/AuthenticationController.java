package eu.adainius.newsfocused.admin.site.back.auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import eu.adainius.newsfocused.admin.site.back.config.Roles;

@Component
@Path("api/auth")
public class AuthenticationController {
    @GET
    @Produces("text/plain")
    @RolesAllowed(Roles.ROLE_ADMIN)
    public String login() {
        return "Hello";
    }
}
