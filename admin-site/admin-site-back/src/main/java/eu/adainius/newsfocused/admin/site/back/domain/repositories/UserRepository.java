package eu.adainius.newsfocused.admin.site.back.domain.repositories;

import java.util.Set;

import eu.adainius.newsfocused.admin.site.back.domain.User;

public interface UserRepository {

    void save(User registeredUser);

    Set<User> retrieveAll();

}
