package eu.adainius.newsfocused.admin.site.back.repositories;

import java.util.Optional;

import eu.adainius.newsfocused.admin.site.back.domain.Admin;

public class AdminFileBasedJsonRepository implements AdminRepository {

    @Override
    public Optional<Admin> retrieveByUsername(String adminUsername) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }
    
}
