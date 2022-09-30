package eu.adainius.newsfocused.admin.site.back.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import eu.adainius.newsfocused.admin.site.back.domain.Admin;

public class AdminFromPropertiesRepositoryTest {
    @Test
    void testRetrieveByUsername_retrieves_an_existing_admin() throws IOException {
        // GIVEN
        String adminUsername = "adminUsername";
        Properties properties = new Properties();
        properties.put("admin", adminUsername);

        AdminRepository adminRepository = new AdminFromPropertiesRepository(properties);

        // WHEN
        Optional<Admin> maybeAdmin = adminRepository.retrieveByUsername(adminUsername);

        // THEN
        assertTrue(maybeAdmin.isPresent());
        assertEquals(adminUsername, maybeAdmin.get().username());
    }

    @Test
    void testRetrieveByUsername_returns_empty_if_properties_do_not_contain_the_admin() throws IOException {
        // GIVEN
        String adminUsername = "adminUsernameNonExisting";

        AdminRepository adminRepository = new AdminFromPropertiesRepository(new Properties());

        // WHEN
        Optional<Admin> maybeAdmin = adminRepository.retrieveByUsername(adminUsername);

        // THEN
        assertFalse(maybeAdmin.isPresent());
    }
}
