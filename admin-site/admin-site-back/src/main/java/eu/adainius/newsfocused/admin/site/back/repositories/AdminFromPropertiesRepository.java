package eu.adainius.newsfocused.admin.site.back.repositories;

import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import eu.adainius.newsfocused.admin.site.back.domain.Admin;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminFromPropertiesRepository implements AdminRepository {
    private final Properties properties;
    private static final String ADMIN_PROPERTY_NAME = "admin";

    public AdminFromPropertiesRepository(Properties applicationProperties) {
        this.properties = applicationProperties;
    }

    @Override
    public Optional<Admin> retrieveByUsername(String adminUsername) {
        String adminUsernameFromProperties = this.properties.getProperty(ADMIN_PROPERTY_NAME);
        if (StringUtils.isEmpty(adminUsernameFromProperties)
                || !adminUsernameFromProperties.equals(adminUsername)) {
            log.error("Admin with username {} was not found!", adminUsername);
            return Optional.empty();
        }
        return Optional.of(Admin.builder().username(adminUsernameFromProperties).build());
    }

}
