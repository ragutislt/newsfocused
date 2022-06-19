package eu.adainius.newsfocused.admin.site.back.config;

import java.util.Collections;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import eu.adainius.newsfocused.admin.site.back.controller.IndexController;
import eu.adainius.newsfocused.admin.site.back.controller.auth.AuthenticationController;

@Configuration
public class ControllerConfig extends ResourceConfig {

    public ControllerConfig() {
        register(AuthenticationController.class);
        register(IndexController.class);
        setProperties(Collections.singletonMap(
                "jersey.config.server.response.setStatusOverSendError", true));
    }

}