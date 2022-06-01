package eu.adainius.newsfocused.admin.site.back.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import eu.adainius.newsfocused.admin.site.back.auth.AuthenticationController;

@Component
@ApplicationPath("api")
public class ControllerConfig extends ResourceConfig {

    public ControllerConfig() {
        register(AuthenticationController.class);
    }

}