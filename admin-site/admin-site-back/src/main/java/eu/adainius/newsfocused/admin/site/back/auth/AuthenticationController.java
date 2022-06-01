package eu.adainius.newsfocused.admin.site.back.auth;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

@Component
@Path("auth")
public class AuthenticationController {
    @GET
    public String login() {
        return "Hello";
    }
}
