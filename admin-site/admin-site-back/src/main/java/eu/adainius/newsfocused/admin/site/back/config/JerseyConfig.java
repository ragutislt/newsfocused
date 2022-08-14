package eu.adainius.newsfocused.admin.site.back.config;

import java.util.Map;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import eu.adainius.newsfocused.admin.site.back.controller.IndexController;
import eu.adainius.newsfocused.admin.site.back.controller.auth.AuthenticationController;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(AuthenticationController.class);
        register(IndexController.class);
        setProperties(
                Map.of(
                        "jersey.config.server.response.setStatusOverSendError", true,
                        "jersey.config.servlet.filter.forwardOn404", true));
    }

}