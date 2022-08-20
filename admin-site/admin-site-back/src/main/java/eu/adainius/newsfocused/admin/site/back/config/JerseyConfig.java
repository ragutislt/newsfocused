package eu.adainius.newsfocused.admin.site.back.config;

import java.util.Map;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.Configuration;

import eu.adainius.newsfocused.admin.site.back.controller.IndexController;
import eu.adainius.newsfocused.admin.site.back.controller.auth.AuthenticationController;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(AuthenticationController.class);
        register(IndexController.class);
        // register(GenericExceptionMapper.class);
        setProperties(
                Map.of(
                        "jersey.config.server.response.setStatusOverSendError", true,
                        ServletProperties.FILTER_FORWARD_ON_404, true));
    }

}