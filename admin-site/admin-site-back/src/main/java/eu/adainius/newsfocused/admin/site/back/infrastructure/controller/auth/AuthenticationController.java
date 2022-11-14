package eu.adainius.newsfocused.admin.site.back.infrastructure.controller.auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import eu.adainius.newsfocused.admin.site.back.config.Roles;

@Component
@Path("api/auth")
public class AuthenticationController {
    @POST
    @RolesAllowed(Roles.ROLE_ADMIN)
    public Response login() {
        return Response.ok().build(); // nothing to do here since spring security will have already authenticated the user
    }
}
