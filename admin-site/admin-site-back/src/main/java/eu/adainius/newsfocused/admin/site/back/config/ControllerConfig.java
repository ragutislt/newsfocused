package eu.adainius.newsfocused.admin.site.back.config;

import java.util.Collections;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import eu.adainius.newsfocused.admin.site.back.auth.AuthenticationController;
import lombok.extern.slf4j.Slf4j;

@Configuration
public class ControllerConfig extends ResourceConfig {

    public ControllerConfig() {
        register(AuthenticationController.class);
        setProperties(Collections.singletonMap(
                "jersey.config.server.response.setStatusOverSendError", true));
    }

}