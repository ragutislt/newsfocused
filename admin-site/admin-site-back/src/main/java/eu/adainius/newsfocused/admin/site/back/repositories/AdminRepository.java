package eu.adainius.newsfocused.admin.site.back.repositories;

import java.util.Optional;

import eu.adainius.newsfocused.admin.site.back.domain.Admin;

public interface AdminRepository {

    Optional<Admin> retrieveByUsername(String adminUsername);

}
