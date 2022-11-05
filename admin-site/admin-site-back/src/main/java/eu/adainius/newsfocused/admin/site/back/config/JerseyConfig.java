package eu.adainius.newsfocused.admin.site.back.config;

import java.util.Map;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.Configuration;

import eu.adainius.newsfocused.admin.site.back.infrastructure.controller.ApiController;
import eu.adainius.newsfocused.admin.site.back.infrastructure.controller.IndexController;
import eu.adainius.newsfocused.admin.site.back.infrastructure.controller.auth.AuthenticationController;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(AuthenticationController.class);
        register(IndexController.class);
        register(ApiController.class);
        register(JerseyNotFoundExceptionMapper.class);
        setProperties(
                Map.of(
                        "jersey.config.server.response.setStatusOverSendError", true,
                        ServletProperties.FILTER_FORWARD_ON_404, false,
                        ServletProperties.FILTER_STATIC_CONTENT_REGEX, "/static/.*"));
    }

}